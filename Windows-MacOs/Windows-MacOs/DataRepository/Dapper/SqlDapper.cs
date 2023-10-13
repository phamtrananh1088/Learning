using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using WinMacOs.DataRepository.DBManager;
using WinMacOs.Utility.Enums;
using WinMacOs.Utility.Utils;
using Dapper;
using System.Linq.Expressions;
using WinMacOs.Utility.Extensions;

namespace WinMacOs.DataRepository.Dapper
{
    public class SqlDapper : ISqlDapper
    {
        private string _connectionString;
        private int? commandTimeout = AppSetting.CommandTimeout;
        private DbCurrentType _dbCurrentType;
        public SqlDapper()
        {
            _connectionString = DBServerProvider.GetConnectionString();
        }
        public SqlDapper(string connKeyName, DbCurrentType dbCurrentType)
        {
            _dbCurrentType = dbCurrentType;
            _connectionString = DBServerProvider.GetConnectionString(connKeyName);
        }
        public SqlDapper(string connKeyName)
        {
            _connectionString = DBServerProvider.GetConnectionString(connKeyName);
        }

        private bool _transaction { get; set; }

        private IDbConnection _transactionConnection = null;

        /// <summary>
        /// タイムアウト時間(秒)
        /// </summary>
        /// <param name="timeout"></param>
        /// <returns></returns>
        public ISqlDapper SetTimout(int timeout)
        {
            this.commandTimeout = timeout;
            return this;
        }

        private T Execute<T>(Func<IDbConnection, IDbTransaction, T> func, bool beginTransaction = false)
        {
            if (_transaction)
            {
                return func(_transactionConnection, dbTransaction);
            }
            if (beginTransaction)
            {
                return ExecuteTransaction(func);
            }
            using (var connection = DBServerProvider.GetDbConnection(_connectionString, _dbCurrentType))
            {
                return func(connection, dbTransaction);
            }
        }

        private T ExecuteTransaction<T>(Func<IDbConnection, IDbTransaction, T> func)
        {
            using (_transactionConnection = DBServerProvider.GetDbConnection(_connectionString, _dbCurrentType))
            {
                try
                {
                    _transactionConnection.Open();
                    dbTransaction = _transactionConnection.BeginTransaction();
                    T reslutT = func(_transactionConnection, dbTransaction);
                    dbTransaction.Commit();
                    return reslutT;
                }
                catch (Exception ex)
                {
                    dbTransaction?.Rollback();
                    throw ex;
                }
                finally
                {
                    dbTransaction?.Dispose();
                }
            }
        }

        private async Task<T> ExecuteAsync<T>(Func<IDbConnection, IDbTransaction, Task<T>> funcAsync, bool beginTransaction = false)
        {
            if (_transaction)
            {
                return await funcAsync(_transactionConnection, dbTransaction);
            }
            if (beginTransaction)
            {
                return await ExecuteTransactionAsync(funcAsync);
            }
            using (var connection = new SqlConnection(DBServerProvider.GetConnectionString(_connectionString)))
            {
                T reslutT = await funcAsync(connection, dbTransaction);
                if (!_transaction && dbTransaction != null)
                {
                    dbTransaction.Commit();
                }
                return reslutT;
            }
        }

        private async Task<T> ExecuteTransactionAsync<T>(Func<IDbConnection, IDbTransaction, Task<T>> funcAsync)
        {
            using (var connection = new SqlConnection(DBServerProvider.GetConnectionString(_connectionString)))
            {
                try
                {
                    connection.Open();
                    dbTransaction = connection.BeginTransaction();
                    T reslutT = await funcAsync(connection, dbTransaction);
                    if (!_transaction && dbTransaction != null)
                    {
                        dbTransaction.Commit();
                    }
                    return reslutT;
                }
                catch (Exception ex)
                {
                    dbTransaction?.Rollback();
                    throw ex;
                }
            }
        }

        /// <summary>
        /// <param name="action"></param>
        /// <param name="error"></param>
        //public void BeginTransaction(Func<ISqlDapper, bool> action, Action<Exception> error)
        public string BeginTransaction(Func<ISqlDapper, bool> action)
        {
            _transaction = true;
            using (var connection = DBServerProvider.GetDbConnection(_connectionString, _dbCurrentType))
            {
                try
                {
                    _transactionConnection = connection;
                    _transactionConnection.Open();
                    dbTransaction = _transactionConnection.BeginTransaction();
                    bool result = action(this);
                    if (result)
                    {
                        dbTransaction?.Commit();
                        return "";
                    }
                    else
                    {
                        dbTransaction?.Rollback();
                        return "更新に失敗しました";
                    }
                }
                catch (Exception ex)
                {
                    dbTransaction?.Rollback();
                    return ex.Message;
                    //error(ex);
                }
                finally
                {
                    _transaction = false;
                    dbTransaction?.Dispose();
                }
            }
        }

        /// <summary>
        /// <param name="action"></param>
        public async Task<string> BeginTransactionAsync(Func<ISqlDapper, Task<string>> action)
        {
            _transaction = true;
            using (var connection = DBServerProvider.GetDbConnection(_connectionString, _dbCurrentType))
            {
                try
                {
                    _transactionConnection = connection;
                    _transactionConnection.Open();
                    dbTransaction = _transactionConnection.BeginTransaction();
                    string result = await action(this);
                    if (String.IsNullOrEmpty(result))
                    {
                        dbTransaction?.Commit();
                        return "";
                    }
                    else
                    {
                        dbTransaction?.Rollback();
                        return result;
                    }
                }
                catch (Exception ex)
                {
                    dbTransaction?.Rollback();
                    return ex.Message;
                }
                finally
                {
                    _transaction = false;
                    dbTransaction?.Dispose();
                }
            }
        }

        /// <summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="cmd"></param>
        /// <param name="param"></param>
        /// <param name="commandType"></param>
        /// <returns></returns>
        public List<T> QueryList<T>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                return conn.Query<T>(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout).ToList();
            }, beginTransaction);
        }
        public async Task<IEnumerable<T>> QueryListAsync<T>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                return await conn.QueryAsync<T>(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public async Task<DataTable> QueryDataTable(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                var dt = new DataTable();
                var reader = await conn.ExecuteReaderAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
                dt.Load(reader);
                return dt;
            }, beginTransaction);
        }

        public async Task<T> QueryFirstAsync<T>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false) where T : class
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                return await conn.QueryFirstOrDefaultAsync<T>(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public T QueryFirst<T>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false) where T : class
        {
            return Execute((conn, dbTransaction) =>
            {
                return conn.QueryFirstOrDefault<T>(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public async Task<dynamic> QueryDynamicFirstAsync(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                return await conn.QueryFirstOrDefaultAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public dynamic QueryDynamicFirst(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                return conn.QueryFirstOrDefault(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public async Task<dynamic> QueryDynamicListAsync(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                return await conn.QueryAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public List<dynamic> QueryDynamicList(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                return conn.Query(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout).ToList();
            }, beginTransaction);
        }

        public async Task<object> ExecuteScalarAsync(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                return await conn.ExecuteScalarAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public object ExecuteScalar(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                return conn.ExecuteScalar(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public async Task<int> ExcuteNonQueryAsync(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                return await conn.ExecuteAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public int ExcuteNonQuery(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute<int>((conn, dbTransaction) =>
            {
                return conn.Execute(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }
        public IDataReader ExecuteReader(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute<IDataReader>((conn, dbTransaction) =>
            {
                return conn.ExecuteReader(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public SqlMapper.GridReader QueryMultiple(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                return conn.QueryMultiple(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout);
            }, beginTransaction);
        }

        public async Task<(IEnumerable<T1>, IEnumerable<T2>)> QueryMultipleAsync<T1, T2>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = await conn.QueryMultipleAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (await reader.ReadAsync<T1>(), await reader.ReadAsync<T2>());
                }
            }, beginTransaction);
        }

        /// <summary>
        /// 戻す値を取得
        /// param.Get<int>("@b");
        /// </summary>
        /// <typeparam name="T1"></typeparam>
        /// <param name="cmd"></param>
        /// <param name="param"></param>
        /// <param name="commandType"></param>
        /// <param name="dbTransaction"></param>
        /// <returns></returns>
        public (List<T1>, List<T2>) QueryMultiple<T1, T2>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = conn.QueryMultiple(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (reader.Read<T1>().ToList(), reader.Read<T2>().ToList());
                }
            }, beginTransaction);
        }

        public async Task<(IEnumerable<dynamic>, IEnumerable<dynamic>)> QueryDynamicMultipleAsync(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = await conn.QueryMultipleAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (await reader.ReadAsync(), await reader.ReadAsync());
                }
            }, beginTransaction);
        }

        public (List<dynamic>, List<dynamic>) QueryDynamicMultiple(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = conn.QueryMultiple(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (reader.Read().ToList(), reader.Read().ToList());
                }
            }, beginTransaction);
        }

        public async Task<(IEnumerable<T1>, IEnumerable<T2>, IEnumerable<T3>)> QueryMultipleAsync<T1, T2, T3>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = await conn.QueryMultipleAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (await reader.ReadAsync<T1>(), await reader.ReadAsync<T2>(), await reader.ReadAsync<T3>());
                }
            }, beginTransaction);
        }

        public (List<T1>, List<T2>, List<T3>) QueryMultiple<T1, T2, T3>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = conn.QueryMultiple(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (reader.Read<T1>().ToList(), reader.Read<T2>().ToList(), reader.Read<T3>().ToList());
                }
            }, beginTransaction);
        }

        public async Task<(IEnumerable<dynamic>, IEnumerable<dynamic>)> QueryDynamicMultipleAsync2(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = await conn.QueryMultipleAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (
                    await reader.ReadAsync<dynamic>(),
                    await reader.ReadAsync<dynamic>()
                    );
                }
            }, beginTransaction);
        }

        public (List<dynamic>, List<dynamic>) QueryDynamicMultiple2(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = conn.QueryMultiple(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (
                        reader.Read<dynamic>().ToList(),
                        reader.Read<dynamic>().ToList()
                    );
                }
            }, beginTransaction);
        }

        public async Task<(IEnumerable<dynamic>, IEnumerable<dynamic>, IEnumerable<dynamic>)> QueryDynamicMultipleAsync3(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = await conn.QueryMultipleAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (
                    await reader.ReadAsync<dynamic>(),
                    await reader.ReadAsync<dynamic>(),
                    await reader.ReadAsync<dynamic>()
                    );
                }
            }, beginTransaction);
        }

        public (List<dynamic>, List<dynamic>, List<dynamic>) QueryDynamicMultiple3(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = conn.QueryMultiple(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (reader.Read<dynamic>().ToList(),
                    reader.Read<dynamic>().ToList(),
                    reader.Read<dynamic>().ToList()
                    );
                }
            }, beginTransaction);
        }

        public async Task<(IEnumerable<dynamic>, IEnumerable<dynamic>, IEnumerable<dynamic>, IEnumerable<dynamic>, IEnumerable<dynamic>)> QueryDynamicMultipleAsync5(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = await conn.QueryMultipleAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (
                    await reader.ReadAsync<dynamic>(),
                    await reader.ReadAsync<dynamic>(),
                    await reader.ReadAsync<dynamic>(),
                    await reader.ReadAsync<dynamic>(),
                    await reader.ReadAsync<dynamic>()
                    );
                }
            }, beginTransaction);
        }

        public (List<dynamic>, List<dynamic>, List<dynamic>, List<dynamic>, List<dynamic>) QueryDynamicMultiple5(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = conn.QueryMultiple(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (reader.Read<dynamic>().ToList(),
                    reader.Read<dynamic>().ToList(),
                    reader.Read<dynamic>().ToList(),
                    reader.Read<dynamic>().ToList(),
                    reader.Read<dynamic>().ToList()
                    );
                }
            }, beginTransaction);
        }

        public async Task<(IEnumerable<T1>, IEnumerable<T2>, IEnumerable<T3>, IEnumerable<T4>)> QueryMultipleAsync<T1, T2, T3, T4>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = await conn.QueryMultipleAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (await reader.ReadAsync<T1>(),
                    await reader.ReadAsync<T2>(),
                    await reader.ReadAsync<T3>(),
                    await reader.ReadAsync<T4>()
                    );
                }
            }, beginTransaction);
        }

        public (List<T1>, List<T2>, List<T3>, List<T4>) QueryMultiple<T1, T2, T3, T4>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = conn.QueryMultiple(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (reader.Read<T1>().ToList(),
                    reader.Read<T2>().ToList(),
                    reader.Read<T3>().ToList(),
                    reader.Read<T4>().ToList()
                    );
                }
            }, beginTransaction);
        }

        public async Task<(IEnumerable<T1>, IEnumerable<T2>, IEnumerable<T3>, IEnumerable<T4>, IEnumerable<T5>)> QueryMultipleAsync<T1, T2, T3, T4, T5>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return await ExecuteAsync(async (conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = await conn.QueryMultipleAsync(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (await reader.ReadAsync<T1>(),
                    await reader.ReadAsync<T2>(),
                    await reader.ReadAsync<T3>(),
                    await reader.ReadAsync<T4>(),
                    await reader.ReadAsync<T5>()
                    );
                }
            }, beginTransaction);
        }

        public (List<T1>, List<T2>, List<T3>, List<T4>, List<T5>) QueryMultiple<T1, T2, T3, T4, T5>(string cmd, object param, CommandType? commandType = null, bool beginTransaction = false)
        {
            return Execute((conn, dbTransaction) =>
            {
                using (SqlMapper.GridReader reader = conn.QueryMultiple(cmd, param, dbTransaction, commandType: commandType ?? CommandType.Text, commandTimeout: commandTimeout))
                {
                    return (reader.Read<T1>().ToList(),
                    reader.Read<T2>().ToList(),
                    reader.Read<T3>().ToList(),
                    reader.Read<T4>().ToList(),
                    reader.Read<T5>().ToList()
                    );
                }
            }, beginTransaction);
        }
        IDbTransaction dbTransaction = null;

        /// <summary>
        /// 
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="entity"></param>
        /// <param name="addFileds">更新項目を指定</param>
        /// <param name="beginTransaction"></param>
        /// <returns></returns>
        public int Add<T>(T entity, Expression<Func<T, object>> addFileds = null, bool beginTransaction = false)
        {
            return AddRange<T>(new T[] { entity }, addFileds, beginTransaction);
        }
        /// <summary>
        /// 
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="entities"></param>
        /// <param name="addFileds">更新項目を指定</param>
        /// <param name="beginTransaction"></param>
        /// <returns></returns>
        public int AddRange<T>(IEnumerable<T> entities, Expression<Func<T, object>> addFileds = null, bool beginTransaction = true)
        {
            Type entityType = typeof(T);
            var key = entityType.GetKeyProperty();
            if (key == null)
            {
                throw new Exception("実体にプライベートキー情報が含む場合のみ更新できます。");
            }
            string[] columns;

            //指定插入的字段
            if (addFileds != null)
            {
                columns = addFileds.GetExpressionToArray();
            }
            else
            {
                var properties = entityType.GetGenericProperties();
                if (key.PropertyType != typeof(Guid))
                {
                    properties = properties.Where(x => x.Name != key.Name).ToArray();
                }
                columns = properties.Select(x => x.Name).ToArray();
            }
            string sql = null;
            if (DBType.Name == DbCurrentType.Default.ToString())
            {
                sql = $"insert into {entityType.GetEntityTableName()}({string.Join(",", columns)})" +
                 $"select {string.Join(",", columns)}  from  {EntityToSqlTempName.TempInsert};";
                sql = entities.GetEntitySql(key.PropertyType == typeof(Guid), sql, null, addFileds, null);
            }

            return Execute<int>((conn, dbTransaction) =>
            {
                return conn.Execute(sql, (DBType.Name == DbCurrentType.Default.ToString()) ? entities.ToList() : null, dbTransaction);
            }, beginTransaction);
        }

        /// <summary>
        /// sqlserver使用的临时表参数化批量更新
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="entity">实体必须带主键</param>
        /// <param name="updateFileds">指定更新的字段x=new {x.a,x.b}</param>
        /// <param name="beginTransaction">是否开启事务</param>
        /// <returns></returns>
        public int Update<T>(T entity, Expression<Func<T, object>> updateFileds = null, bool beginTransaction = true)
        {
            return UpdateRange<T>(new T[] { entity }, updateFileds, beginTransaction);
        }

        /// <summary>
        ///(根据主键批量更新实体) sqlserver使用的临时表参数化批量更新
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="entities">实体必须带主键</param>
        /// <param name="updateFileds">批定更新字段</param>
        /// <param name="beginTransaction"></param>
        /// <returns></returns>
        public int UpdateRange<T>(IEnumerable<T> entities, Expression<Func<T, object>> updateFileds = null, bool beginTransaction = true)
        {
            Type entityType = typeof(T);
            var key = entityType.GetKeyProperty();
            if (key == null)
            {
                throw new Exception("実体にプライベートキー情報が含む場合のみ更新できます。");
            }

            var properties = entityType.GetGenericProperties()
            .Where(x => x.Name != key.Name);
            if (updateFileds != null)
            {
                properties = properties.Where(x => updateFileds.GetExpressionToArray().Contains(x.Name));
            }

            if (DBType.Name == DbCurrentType.Default.ToString())
            {
                List<string> paramsList = new List<string>();
                foreach (var item in properties)
                {
                    paramsList.Add(item.Name + "=@" + item.Name);
                }
                string sqltext = $@"UPDATE { entityType.GetEntityTableName()} SET {string.Join(",", paramsList)} WHERE {entityType.GetKeyName()} = @{entityType.GetKeyName()} ;";

                return ExcuteNonQuery(sqltext, entities, CommandType.Text, true);
            }
            string fileds = string.Join(",", properties.Select(x => $" a.{x.Name}=b.{x.Name}").ToArray());
            string sql = $"update  a  set {fileds} from  {entityType.GetEntityTableName()} as a inner join {EntityToSqlTempName.TempInsert.ToString()} as b on a.{key.Name}=b.{key.Name}";
            sql = entities.ToList().GetEntitySql(true, sql, null, updateFileds, null);
            return ExcuteNonQuery(sql, null, CommandType.Text, true);
        }

        public int DelWithKey<T>(bool beginTransaction = false, params object[] keys)
        {
            Type entityType = typeof(T);
            var keyProperty = entityType.GetKeyProperty();
            if (keyProperty == null || keys == null || keys.Length == 0) return 0;

            IEnumerable<(bool, string, object)> validation = keyProperty.ValidationValueForDbType(keys);
            if (validation.Any(x => !x.Item1))
            {
                throw new Exception($"プライベートキータイプ「{validation.Where(x => !x.Item1).Select(s => s.Item3).FirstOrDefault()}」は間違ってます。");
            }
            string tKey = entityType.GetKeyProperty().Name;
            FieldType fieldType = entityType.GetFieldType();
            string joinKeys = (fieldType == FieldType.Int || fieldType == FieldType.BigInt)
                 ? string.Join(",", keys)
                 : $"'{string.Join("','", keys)}'";
            string sql;

            sql = $"DELETE FROM {entityType.GetEntityTableName() } where {tKey} in ({joinKeys});";

            return ExcuteNonQuery(sql, null);
        }

        /// <summary>
        /// 使用key批量删除
        /// </summary>
        /// <typeparam name="T"></typeparam>
        /// <param name="keys"></param>
        /// <returns></returns>
        public int DelWithKey<T>(params object[] keys)
        {
            return DelWithKey<T>(false, keys);
        }

        /// <summary>
        /// Bulk INSERT
        /// </summary>
        /// <param name="table"></param>
        /// <param name="tableName"></param>
        /// <param name="sqlBulkCopyOptions"></param>
        /// <param name="dbKeyName"></param>
        /// <returns></returns>
        private int MSSqlBulkInsert(DataTable table, string tableName, SqlBulkCopyOptions sqlBulkCopyOptions = SqlBulkCopyOptions.UseInternalTransaction, string dbKeyName = null)
        {
            using (var Connection = DBServerProvider.GetDbConnection(_connectionString, _dbCurrentType))
            {
                if (!string.IsNullOrEmpty(dbKeyName))
                {
                    Connection.ConnectionString = DBServerProvider.GetConnectionString(dbKeyName);
                }
                using (SqlBulkCopy sqlBulkCopy = new SqlBulkCopy(Connection.ConnectionString, sqlBulkCopyOptions))
                {
                    sqlBulkCopy.DestinationTableName = tableName;
                    sqlBulkCopy.BatchSize = table.Rows.Count;
                    for (int i = 0; i < table.Columns.Count; i++)
                    {
                        sqlBulkCopy.ColumnMappings.Add(table.Columns[i].ColumnName, table.Columns[i].ColumnName);
                    }
                    sqlBulkCopy.WriteToServer(table);
                    return table.Rows.Count;
                }
            }
        }
        public int BulkInsert<T>(List<T> entities, string tableName = null,
            Expression<Func<T, object>> columns = null,
            SqlBulkCopyOptions? sqlBulkCopyOptions = null)
        {
            DataTable table = entities.ToDataTable(columns, false);
            return BulkInsert(table, tableName ?? typeof(T).GetEntityTableName(), sqlBulkCopyOptions);
        }
        public int BulkInsert(DataTable table, string tableName, SqlBulkCopyOptions? sqlBulkCopyOptions = null, string fileName = null, string tmpPath = null)
        {
            if (!string.IsNullOrEmpty(tmpPath))
            {
                tmpPath = tmpPath.ReplacePath();
            }

            return MSSqlBulkInsert(table, tableName, sqlBulkCopyOptions ?? SqlBulkCopyOptions.KeepIdentity);
        }

    }
}
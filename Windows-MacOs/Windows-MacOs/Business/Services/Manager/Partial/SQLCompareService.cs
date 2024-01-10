using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Business.Services;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.Dapper;
using WinMacOs.DataRepository.DBManager;
using WinMacOs.DataRepository.EFDbContext;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Repositories;
using WinMacOs.Models;
using WinMacOs.Models.Enums;
using WinMacOs.Models.Manager.SQLCompare;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;
using System.Linq;
using System.Data.SqlClient;

namespace WinMacOs.Business.Services.Manager
{
    public partial class SQLCompareService
    {

        public async Task<SQLCompareModel> GetSQLCompareModel()
        {
            var tablesSource = await GetAllTables(dapperSource);
            var tablesTarget = await GetAllTables(dapperTarger);
            var data = new SQLCompareModel()
            {
                SQLTables = tablesSource,
                Source = ((SqlConnectionStringBuilder)connectionStringBuilderSource).DataSource,
                Target = ((SqlConnectionStringBuilder)connectionStringBuilderTarget).DataSource
            };
            return data;
        }

        public async Task<SQLCompareModel> GetSQLScript(string schemaName, string tableName)
        {
            var tableSource = await GetTable(dapperSource, schemaName, tableName);
            var tableTarget = await GetTable(dapperTarger, schemaName, tableName);
            var columnssSource = await GetAllColumns(dapperSource, schemaName, tableName);
            var columnssTarget = await GetAllColumns(dapperTarger, schemaName, tableName);
            string SQLScript;
            if (tableTarget == null)
            {
                tableSource.Status = Status.Add;
                SQLScript = tableSource.GetSQL();
            }
            else if (tableSource == null)
            {
                tableTarget.Status = Status.Delete;
                SQLScript = tableTarget.GetSQL();
            }
            else
            {
                tableSource.Status = Status.Update;
                SQLScript = tableSource.GetSQL();
            }
            var data = new SQLCompareModel()
            {
                Source = ((SqlConnectionStringBuilder)connectionStringBuilderSource).DataSource,
                Target = ((SqlConnectionStringBuilder)connectionStringBuilderTarget).DataSource,

            };
            return data;
        }

        private async Task<List<SQLTableModel>> GetAllTables(ISqlDapper dapper)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT  ");
                sqlbuilder.AppendLine("       ob.name AS TableName ");
                sqlbuilder.AppendLine("     , sc.name AS SchemaName ");
                sqlbuilder.AppendLine("  FROM sys.all_objects ob");
                sqlbuilder.AppendLine("  JOIN sys.schemas sc ON ob.schema_id = sc.schema_id");
                sqlbuilder.AppendLine(" WHERE ob.[type] = 'u' ORDER BY ob.name");

                var data = await dapper.QueryListAsync<SQLTableModel>(sqlbuilder.ToString(), new { });
                return data.ToList();
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }

        private async Task<SQLTableModel> GetTable(ISqlDapper dapper, string schemaName, string tableName)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT  ");
                sqlbuilder.AppendLine("       ob.name AS TableName ");
                sqlbuilder.AppendLine("     , sc.name AS SchemaName ");
                sqlbuilder.AppendLine("  FROM sys.all_objects ob ");
                sqlbuilder.AppendLine("  JOIN sys.schemas sc ON ob.schema_id = sc.schema_id ");
                sqlbuilder.AppendLine(" WHERE ob.[type] = 'u' ");
                sqlbuilder.AppendLine("   AND ob.name = @tableName ");
                sqlbuilder.AppendLine("   AND sc.name = @schemaName ");

                var data = await dapper.QueryFirstAsync<SQLTableModel>(sqlbuilder.ToString(), new { schemaName, tableName });
                return data;
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }

        private async Task<List<SQLColumnModel>> GetAllColumns(ISqlDapper dapper, string schemaName, string tableName)
        {
            try
            {
                SQLTypeModel c = new SQLTypeModel { Name = "varchar", MaxLength= 10, IsNull = false, Precision = 0, Scale = 0 };
                var cs = System.Text.Json.JsonSerializer.Serialize(c);
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT ");
                sqlbuilder.AppendLine("       co.name AS ColumnName ");
                sqlbuilder.AppendLine("       ty.name + co.max_length + co.precision + co.scale + co.is_nullable AS SQLTypeModelRaw ");
                sqlbuilder.AppendLine("  FROM sys.all_columns co ");
                sqlbuilder.AppendLine("  JOIN sys.types ty ON co.system_type_id = ty.system_type_id ");
                sqlbuilder.AppendLine(" WHERE co.object_id = OBJECT_ID(QUOTENAME(@schemaName) + '.' + QUOTENAME(@tableName),'u') ");

                var data = await dapper.QueryListAsync<SQLColumnModel>(sqlbuilder.ToString(), new { schemaName, tableName });
                return data.ToList();
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }

        private async Task<object> GetAllDefaultConstraints(ISqlDapper dapper, string schemaName, string tableName)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT * from sys.default_constraints WHERE object_id = OBJECT_ID(QUOTENAME(@schemaName) + '.' + QUOTENAME(@tableName),'u') ");

                var data = await dapper.QueryListAsync<object>(sqlbuilder.ToString(), new { schemaName, tableName });
                return data;
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }
    }
}

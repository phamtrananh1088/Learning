﻿using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using System.Linq;
using System.Linq.Expressions;
using System.Diagnostics.CodeAnalysis;
using System.Reflection;
using WinMacOs.Utility.SystemModels;
using System.Data.Entity;
using WinMacOs.DataRepository.Dapper;
using WinMacOs.DataRepository.EFDbContext;
using WinMacOs.DataRepository.DBManager;
using WinMacOs.Utility.Enums;
using WinMacOs.Utility.Extensions;
using System.Data.Entity.Infrastructure;
using WinMacOs.DataRepository.Utilities;
using System.Data.SqlClient;
using WinMacOs.Utility.Utils;

namespace WinMacOs.DataRepository.BaseProvider
{

    public abstract class RepositoryBase<TEntity> where TEntity : BaseEntity
    {
        public RepositoryBase()
        {
        }
        public RepositoryBase(WinMacDbContext dbContext)
        {
            this.DefaultDbContext = dbContext ?? throw new Exception("dbContextはインスタンス化されません。");
        }

        private WinMacDbContext DefaultDbContext { get; set; }
        private WinMacDbContext EFContext
        {
            get
            {
                return DefaultDbContext;
            }
        }

        public virtual WinMacDbContext DbContext
        {
            get { return DefaultDbContext; }
        }
        private DbSet<TEntity> DBSet
        {
            get { return EFContext.Set<TEntity>(); }
        }
        public ISqlDapper DapperContext
        {
            get { return DBServerProvider.GetSqlDapper(); }
        }
        ///// <summary>
        ///// トランザクション処理を行う
        ///// </summary>
        ///// <param name="action">falseの場合はRollback</param>
        ///// <returns></returns>
        //public virtual WebResponseContent DbContextTransaction(Func<WebResponseContent> action)
        //{
        //    WebResponseContent webResponse = new WebResponseContent();
        //    using (IDbContextTransaction transaction = DefaultDbContext.Database.BeginTransaction())
        //    {
        //        try
        //        {
        //            webResponse = action();
        //            if (webResponse.Status)
        //            {
        //                transaction.Commit();
        //            }
        //            else
        //            {
        //                transaction.Rollback();
        //            }

        //            return webResponse;
        //        }
        //        catch (Exception ex)
        //        {
        //            transaction.Rollback();
        //            return new WebResponseContent().Error(ex.Message);
        //        }
        //    }
        //}

        public virtual bool Exists<TExists>(Expression<Func<TExists, bool>> predicate) where TExists : class
        {
            return EFContext.Set<TExists>().Any(predicate);
        }

        public virtual Task<bool> ExistsAsync<TExists>(Expression<Func<TExists, bool>> predicate) where TExists : class
        {
            return EFContext.Set<TExists>().AnyAsync(predicate);
        }

        public virtual bool Exists(Expression<Func<TEntity, bool>> predicate)
        {
            return DBSet.Any(predicate);
        }

        public virtual Task<bool> ExistsAsync(Expression<Func<TEntity, bool>> predicate)
        {
            return DBSet.AnyAsync(predicate);
        }
        public virtual List<TFind> Find<TFind>(Expression<Func<TFind, bool>> predicate) where TFind : class
        {
            return EFContext.Set<TFind>().Where(predicate).ToList();
        }

        public virtual Task<TFind> FindAsyncFirst<TFind>(Expression<Func<TFind, bool>> predicate) where TFind : class
        {
            return FindAsIQueryable<TFind>(predicate).FirstOrDefaultAsync();
        }

        public virtual Task<TEntity> FindAsyncFirst(Expression<Func<TEntity, bool>> predicate)
        {
            return FindAsIQueryable<TEntity>(predicate).FirstOrDefaultAsync();
        }

        public virtual Task<List<TFind>> FindAsync<TFind>(Expression<Func<TFind, bool>> predicate) where TFind : class
        {
            return FindAsIQueryable<TFind>(predicate).ToListAsync();
        }

        public virtual Task<List<TEntity>> FindAsync(Expression<Func<TEntity, bool>> predicate)
        {
            return FindAsIQueryable(predicate).ToListAsync();
        }

        public virtual Task<TEntity> FindFirstAsync(Expression<Func<TEntity, bool>> predicate)
        {
            return FindAsIQueryable(predicate).FirstOrDefaultAsync();
        }

        public virtual Task<List<T>> FindAsync<T>(Expression<Func<TEntity, bool>> predicate, Expression<Func<TEntity, T>> selector)
        {
            return FindAsIQueryable(predicate).Select(selector).ToListAsync();
        }

        public virtual Task<List<T>> FindAsync<T>(
            Expression<Func<TEntity, bool>> predicate,
            Expression<Func<TEntity, T>> selector,
            Expression<Func<TEntity, Dictionary<object, QueryOrderBy>>> orderBy = null
            )
        {
            return FindAsIQueryable(predicate, orderBy).Select(selector).ToListAsync();
        }

        public virtual Task<T> FindFirstAsync<T>(Expression<Func<TEntity, bool>> predicate, Expression<Func<TEntity, T>> selector)
        {
            var query = FindAsIQueryable(predicate).Select(selector);
            var data = query.FirstOrDefaultAsync();
            return data;
        }

        public virtual Task<T> FindFirstAsync<T>(Expression<Func<TEntity, bool>> predicate,
            Expression<Func<TEntity, T>> selector,
            Expression<Func<TEntity, Dictionary<object, QueryOrderBy>>> orderBy = null)
        {
            var query = FindAsIQueryable(predicate, orderBy).Select(selector);
            var data = query.FirstOrDefaultAsync();
            return data;
        }

        

        #region FindAsIQueryable

        public virtual IQueryable<TFind> FindAsIQueryable<TFind>(Expression<Func<TFind, bool>> predicate) where TFind : class
        {
            return EFContext.Set<TFind>().Where(predicate);
        }

        public IQueryable<TEntity> FindAsIQueryable(Expression<Func<TEntity, bool>> predicate, Expression<Func<TEntity, Dictionary<object, QueryOrderBy>>> orderBy = null)
        {
            if (orderBy != null)
                return DbContext.Set<TEntity>().Where(predicate).GetIQueryableOrderBy(orderBy.GetExpressionToDic());
            var res = DbContext.Set<TEntity>().Where(predicate);
            return res;
        }

        /// <summary>
        /// 多条件查询
        /// </summary>
        /// <typeparam name="Source"></typeparam>
        /// <param name="sources"></param>
        /// <param name="predicate"></param>
        /// <returns></returns>
        public virtual IQueryable<TEntity> FindAsIQueryable<Source>(IEnumerable<Source> sources,
            Func<Source, Expression<Func<TEntity, bool>>> predicate)
            where Source : class
        {
            // EFContext.ChangeTracker.QueryTrackingBehavior = QueryTrackingBehavior.TrackAll;
            Expression<Func<TEntity, bool>> resultPredicate = x => 1 == 2;
            foreach (Source source in sources)
            {
                Expression<Func<TEntity, bool>> expression = predicate(source);
                resultPredicate = (resultPredicate).Or<TEntity>((expression));
            }
            return EFContext.Set<TEntity>().Where(resultPredicate);
        }
        #endregion
        public virtual List<TEntity> Find<Source>(IEnumerable<Source> sources,
            Func<Source, Expression<Func<TEntity, bool>>> predicate)
            where Source : class
        {
            return FindAsIQueryable(sources, predicate).ToList();
        }
        public virtual List<TResult> Find<Source, TResult>(IEnumerable<Source> sources,
              Func<Source, Expression<Func<TEntity, bool>>> predicate,
              Expression<Func<TEntity, TResult>> selector)
              where Source : class
        {
            return FindAsIQueryable(sources, predicate).Select(selector).ToList();
        }

        public virtual List<T> Find<T>(Expression<Func<TEntity, bool>> predicate, Expression<Func<TEntity, T>> selector)
        {
            return DBSet.Where(predicate).Select(selector).ToList();
        }
        /// <summary>
        /// Get All
        /// </summary>
        /// <param name="predicate"></param>
        /// <returns></returns>
        public virtual DbSet<TEntity> GetAll()
        {
            return DBSet;
        }

        /// <summary>
        /// 单表查询
        /// </summary>
        /// <param name="predicate"></param>
        /// <returns></returns>
        public virtual List<TEntity> Find(Expression<Func<TEntity, bool>> predicate)
        {
            return FindAsIQueryable(predicate).ToList();
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="predicate"></param>
        /// <param name=""></param>
        /// <param name="orderBy">排序字段</param>
        /// <returns></returns>
        public virtual TEntity FindFirst(Expression<Func<TEntity, bool>> predicate, Expression<Func<TEntity, Dictionary<object, QueryOrderBy>>> orderBy = null)
        {
            return FindAsIQueryable(predicate, orderBy).FirstOrDefault();
        }

        public IQueryable<TEntity> Include<TProperty>(Expression<Func<TEntity, TProperty>> incluedProperty)
        {
            return DbContext.Set<TEntity>().Include(incluedProperty);
        }

        /// <summary>
        /// 通过条件查询返回指定列的数据(将TEntity映射到匿名或实体T)
        ///var result = Sys_UserRepository.GetInstance.Find(x => x.UserName == loginInfo.userName, p => new { uname = p.UserName });
        /// <summary>
        /// 
        /// </summary>
        /// <typeparam name="TKey"></typeparam>
        /// <param name="pageIndex"></param>
        /// <param name="pagesize"></param>
        /// <param name="rowcount"></param>
        /// <param name="predicate">查询条件</param>
        /// <param name="orderBySelector">多个排序字段key为字段，value为升序/降序</param>
        /// <returns></returns>
        public virtual IQueryable<TFind> IQueryablePage<TFind>(int pageIndex, int pagesize, out int rowcount, Expression<Func<TFind, bool>> predicate, Expression<Func<TEntity, Dictionary<object, QueryOrderBy>>> orderBy, bool returnRowCount = true) where TFind : class
        {
            pageIndex = pageIndex <= 0 ? 1 : pageIndex;
            pagesize = pagesize <= 0 ? 10 : pagesize;
            if (predicate == null)
            {
                predicate = x => 1 == 1;
            }
            var _db = DbContext.Set<TFind>();
            rowcount = returnRowCount ? _db.Count(predicate) : 0;
            return DbContext.Set<TFind>().Where(predicate)
                .GetIQueryableOrderBy(orderBy.GetExpressionToDic())
                .Skip((pageIndex - 1) * pagesize)
                .Take(pagesize);
        }

        /// <summary>
        /// 分页排序
        /// </summary>
        /// <param name="queryable"></param>
        /// <param name="pageIndex"></param>
        /// <param name="pagesize"></param>
        /// <param name="rowcount"></param>
        /// <param name="orderBy"></param>
        /// <returns></returns>
        public virtual IQueryable<TEntity> IQueryablePage(IQueryable<TEntity> queryable, int pageIndex, int pagesize, out int rowcount, Dictionary<string, QueryOrderBy> orderBy, bool returnRowCount = true)
        {
            pageIndex = pageIndex <= 0 ? 1 : pageIndex;
            pagesize = pagesize <= 0 ? 10 : pagesize;
            rowcount = returnRowCount ? queryable.Count() : 0;
            return queryable.GetIQueryableOrderBy<TEntity>(orderBy)
                .Skip((pageIndex - 1) * pagesize)
                .Take(pagesize);
        }

        public virtual List<TResult> QueryByPage<TResult>(int pageIndex, int pagesize, out int rowcount, Expression<Func<TEntity, bool>> predicate, Expression<Func<TEntity, Dictionary<object, QueryOrderBy>>> orderBy, Expression<Func<TEntity, TResult>> selectorResult, bool returnRowCount = true)
        {
            return IQueryablePage<TEntity>(pageIndex, pagesize, out rowcount, predicate, orderBy, returnRowCount).Select(selectorResult).ToList();
        }

        public List<TEntity> QueryByPage(int pageIndex, int pagesize, out int rowcount, Expression<Func<TEntity, bool>> predicate, Expression<Func<TEntity, Dictionary<object, QueryOrderBy>>> orderBy, bool returnRowCount = true)
        {
            return IQueryablePage<TEntity>(pageIndex, pagesize, out rowcount, predicate, orderBy, returnRowCount).ToList();
        }

        public virtual List<TResult> QueryByPage<TResult>(int pageIndex, int pagesize, Expression<Func<TEntity, bool>> predicate, Expression<Func<TEntity, Dictionary<object, QueryOrderBy>>> orderBy, Expression<Func<TEntity, TResult>> selectorResult = null)
        {
            return IQueryablePage<TEntity>(pageIndex, pagesize, out _, predicate, orderBy).Select(selectorResult).ToList();
        }

        /// <summary>
        /// 更新表数据
        /// </summary>
        /// <param name="entity"></param>
        /// <param name="saveChanges">是否保存</param>
        /// <param name="properties">格式 Expression<Func<entityt, object>> expTree = x => new { x.字段1, x.字段2 };</param>
        //public virtual int Update(TEntity entity, Expression<Func<TEntity, object>> properties, bool saveChanges = false)
        //{
        //    entity = SetCommonParam(entity, true);
        //    return Update(entity, properties, saveChanges);
        //}

        public virtual int Update(TEntity entity, Expression<Func<TEntity, object>> properties, bool saveChanges = false)
        {
            return UpdateRange(new List<TEntity>
            {
                entity
            }, properties, saveChanges);
        }

        public virtual int Update(TEntity entity, string[] properties, bool saveChanges = false)
        {
            return UpdateRange(new List<TEntity>() { entity }, properties, saveChanges);
        }
        public virtual int Update(TEntity entity, bool saveChanges = false)
        {
            return UpdateRange(new List<TEntity>() { entity }, new string[0], saveChanges);
        }
        public virtual int UpdateRange(IEnumerable<TEntity> entities, Expression<Func<TEntity, object>> properties, bool saveChanges = false)
        {
            return UpdateRange(entities, properties?.GetExpressionProperty(), saveChanges);
        }
        public virtual int UpdateRange(IEnumerable<TEntity> entities, bool saveChanges = false)
        {
            return UpdateRange(entities, new string[0], saveChanges);
        }

        /// <summary>
        /// 更新表数据
        /// </summary>
        /// <param name="models"></param>
        /// <param name="properties">格式 Expression<Func<entityt, object>> expTree = x => new { x.字段1, x.字段2 };</param>
        public int UpdateRange(IEnumerable<TEntity> entities, string[] properties, bool saveChanges = false)
        {
            entities = SetCommonParam(entities);
            if (properties != null && entities.First() is BaseEntity)
            {
                List<string> lstProperties = properties.ToList();
                lstProperties.Add("UPDATE_TIME");
                lstProperties.Add("UPDATE_PG");
                lstProperties.Add("UPDATE_HOST");
                lstProperties.Add("UPDATE_ID");
                properties = lstProperties.ToArray();
            }
            if (properties != null && properties.Length > 0)
            {
                PropertyInfo[] entityProperty = typeof(TEntity).GetProperties();
                string keyName = entityProperty.GetKeyName();
                if (properties.Contains(keyName))
                {
                    properties = properties.Where(x => x != keyName).ToArray();
                }
                properties = properties.Where(x => entityProperty.Select(s => s.Name).Contains(x)).ToArray();
            }
            int iUpState = 0;
            foreach (TEntity item in entities)
            {
                if (properties == null || properties.Length == 0)
                {
                    iUpState++;
                    DbContext.Entry<TEntity>(item).State = EntityState.Modified;
                    continue;
                }
                var entry = DbContext.Entry(item);
                properties.ToList().ForEach(x =>
                {
                    iUpState++;
                    entry.Property(x).IsModified = true;
                });
            }
            if (!saveChanges) return iUpState;

            //2020.04.24增加更新时并行重试处理
            try
            {
                // Attempt to save changes to the database
                return DbContext.SaveChanges();
            }
            catch (DbUpdateConcurrencyException ex)
            {
                int affectedRows = 0;
                foreach (var entry in ex.Entries)
                {
                    var proposedValues = entry.CurrentValues;

                    var databaseValues = entry.GetDatabaseValues();
                    //databaseValues == null说明数据已被删除
                    if (databaseValues != null)
                    {
                        foreach (var property in properties == null
                            || properties.Length == 0 ? proposedValues.PropertyNames
                            : proposedValues.PropertyNames.Where(x => properties.Contains(x)))
                        {
                            var proposedValue = proposedValues[property];
                            var databaseValue = databaseValues[property];
                        }
                        affectedRows++;
                        entry.OriginalValues.SetValues(databaseValues);
                    }
                }
                if (affectedRows == 0) return 0;

                return DbContext.SaveChanges();
            }
        }

        /// <summary>
        ///
        /// </summary>
        /// <param name="entity"></param>
        /// <param name="updateDetail">是否修改明细</param>
        /// <param name="delNotExist">是否删除明细不存在的数据</param>
        /// <param name="updateMainFields">主表指定修改字段</param>
        /// <param name="updateDetailFields">明细指定修改字段</param>
        /// <param name="saveChange">是否保存</param>
        /// <returns></returns>
        //public virtual WebResponseContent UpdateRange<Detail>(TEntity entity,
        //    bool updateDetail = false,
        //    bool delNotExist = false,
        //    Expression<Func<TEntity, object>> updateMainFields = null,
        //    Expression<Func<Detail, object>> updateDetailFields = null,
        //    bool saveChange = false) where Detail : class
        //{
        //    WebResponseContent webResponse = new WebResponseContent();
        //    Update(entity, updateMainFields);
        //    string message = "";
        //    if (updateDetail)
        //    {
        //        PropertyInfo[] properties = typeof(TEntity).GetProperties();
        //        PropertyInfo detail = properties.Where(x => x.PropertyType.Name == "List`1").ToList().FirstOrDefault();
        //        if (detail != null)
        //        {
        //            PropertyInfo key = properties.GetKeyProperty();
        //            object obj = detail.GetValue(entity);
        //            Type detailType = typeof(TEntity).GetCustomAttribute<EntityAttribute>().DetailTable[0];
        //            message = UpdateDetail<Detail>(obj as List<Detail>, key.Name, key.GetValue(entity), updateDetailFields, delNotExist);
        //        }
        //    }
        //    if (!saveChange) return webResponse.OK();

        //    DbContext.SaveChanges();
        //    return webResponse.OK("修改成功,明细" + message, entity);
        //}
        //private string UpdateDetail<TDetail>(List<TDetail> list,
        //    string keyName,
        //    object keyValue,
        //    Expression<Func<TDetail, object>> updateDetailFields = null,
        //    bool delNotExist = false) where TDetail : class
        //{
        //    if (list == null) return "";
        //    PropertyInfo property = typeof(TDetail).GetKeyProperty();
        //    string detailKeyName = property.Name;
        //    DbSet<TDetail> details = DbContext.Set<TDetail>();
        //    Expression<Func<TDetail, object>> selectExpression = detailKeyName.GetExpression<TDetail, object>();
        //    Expression<Func<TDetail, bool>> whereExpression = keyName.CreateExpression<TDetail>(keyValue, LinqExpressionType.Equal);

        //    List<object> detailKeys = details.Where(whereExpression).Select(selectExpression).ToList();

        //    //获取主键默认值
        //    string keyDefaultVal = property.PropertyType
        //        .Assembly
        //        .CreateInstance(property.PropertyType.FullName).ToString();
        //    int addCount = 0;
        //    int editCount = 0;
        //    int delCount = 0;
        //    PropertyInfo mainKeyProperty = typeof(TDetail).GetProperty(keyName);
        //    List<object> keys = new List<object>();
        //    list.ForEach(x =>
        //    {
        //        var set = DbContext.Set<TDetail>();
        //        object val = property.GetValue(x);
        //        //主键是默认值的为新增的数据
        //        if (val.ToString() == keyDefaultVal)
        //        {
        //            x.SetCreateDefaultVal();
        //            //设置主表的值，也可以不设置
        //            mainKeyProperty.SetValue(x, keyValue);
        //            details.Add(x);
        //            addCount++;
        //        }
        //        else//修改的数据
        //        {
        //            //获取所有修改的key,如果从数据库查来的key,不在修改中的key，则为删除的数据
        //            keys.Add(val);
        //            x.SetModifyDefaultVal();
        //            Update<TDetail>(x, updateDetailFields);
        //            //  repository.DbContext.Entry<TDetail>(x).State = EntityState.Modified;
        //            editCount++;
        //        }
        //    });
        //    //删除
        //    if (delNotExist)
        //    {
        //        detailKeys.Where(x => !keys.Contains(x)).ToList().ForEach(d =>
        //        {
        //            delCount++;
        //            TDetail detail = Activator.CreateInstance<TDetail>();
        //            property.SetValue(detail, d);
        //            DbContext.Entry<TDetail>(detail).State = EntityState.Deleted;
        //            for (int i = 0; i < list.Count(); i++)
        //            {
        //                if (property.GetValue(list[i]) == d)
        //                {
        //                    list.RemoveAt(i);
        //                }
        //            }
        //        });
        //    }
        //    return $"修改[{editCount}]条,新增[{addCount}]条,删除[{delCount}]条";
        //}

        public virtual void Delete(TEntity model, bool saveChanges)
        {
            DBSet.Remove(model);
            if (saveChanges)
            {
                DbContext.SaveChanges();
            }
        }
        /// <summary>
        /// 通过主键批量删除
        /// </summary>
        /// <param name="keys">主键key</param>
        /// <param name="delList">是否连明细一起删除</param>
        /// <returns></returns>
        public virtual int DeleteWithKeys(object[] keys, bool delList = false)
        {
            Type entityType = typeof(TEntity);
            string tKey = entityType.GetKeyProperty().Name;
            FieldType fieldType = entityType.GetFieldType();
            string joinKeys = (fieldType == FieldType.Int || fieldType == FieldType.BigInt)
                 ? string.Join(",", keys)
                 : $"'{string.Join("','", keys)}'";

            string sql = $"DELETE FROM {entityType.GetEntityTableName() } where {tKey} in ({joinKeys});";
            if (delList)
            {
                Type detailType = entityType.GetCustomAttribute<EntityAttribute>().DetailTable?[0];
                if (detailType != null)
                    sql += $"DELETE FROM {detailType.GetEntityTableName()} where {tKey} in ({joinKeys});";
            }
            return ExecuteSqlCommand(sql);
        }

        private TEntity SetCommonParam(TEntity entitie, bool isInsert = false)
        {
            if (entitie is BaseEntity baseEntity)
            {
                if (isInsert)
                {
                    baseEntity.INSERT_HOST = UserContext.Current.ClientHost;
                    baseEntity.INSERT_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss");
                    baseEntity.INSERT_ID = UserContext.Current.社員ID ?? UserContext.Current.ログインID;
                    if (String.IsNullOrEmpty(baseEntity.INSERT_PG))
                        baseEntity.INSERT_PG = DefaultDbContext.INSERT_UPDATE_PG;

                    baseEntity.UPDATE_HOST = String.Empty;
                    baseEntity.UPDATE_TIME = String.Empty;
                    baseEntity.UPDATE_ID = String.Empty;
                    baseEntity.UPDATE_PG = String.Empty;
                }
                else
                {
                    baseEntity.UPDATE_HOST = !String.IsNullOrEmpty(baseEntity.UPDATE_HOST) ? baseEntity.UPDATE_HOST : UserContext.Current.ClientHost;
                    baseEntity.UPDATE_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss");
                    baseEntity.UPDATE_ID = UserContext.Current.社員ID ?? UserContext.Current.ログインID ?? baseEntity.UPDATE_ID;
                    if (String.IsNullOrEmpty(baseEntity.UPDATE_PG))
                        baseEntity.UPDATE_PG = DefaultDbContext.INSERT_UPDATE_PG;
                }
                return (TEntity)baseEntity;
            }
            else
            {
                return entitie;
            }
        }

        private IEnumerable<TEntity> SetCommonParam(IEnumerable<TEntity> entities, bool isInsert = false)
        {
            if (entities is IList<TEntity> listEntity)
            {
                for (int i = 0; i < listEntity.Count; i++)
                {
                    listEntity[i] = SetCommonParam(listEntity[i], isInsert);
                }
                entities = listEntity;
            }
            return entities;
        }

        public virtual Task AddAsync(TEntity entities)
        {
            entities = SetCommonParam(entities, true);
            return DBSet.AddRangeAsync(entities);
            
        }

        public virtual Task AddRangeAsync(IEnumerable<TEntity> entities)
        {
            entities = SetCommonParam(entities, true);
            return DBSet.AddRangeAsync(entities);
        }

        public virtual void Add(TEntity entities, bool saveChanges = false)
        {
            entities = SetCommonParam(entities, true);
            AddRange(new List<TEntity>() { entities }, saveChanges);
        }
        public virtual void AddRange(IEnumerable<TEntity> entities, bool saveChanges = false)
        {
            entities = SetCommonParam(entities, true);
            DBSet.AddRange(entities);
            if (saveChanges) DbContext.SaveChanges();
        }

        public virtual Task AddAsync(TEntity entities, Expression<Func<TEntity, object>> properties)
        {
            return AddRangeAsync(new List<TEntity>() { entities }, properties?.GetExpressionProperty());
        }

        public Task AddRangeAsync(IEnumerable<TEntity> entities, string[] properties)
        {
            entities = SetCommonParam(entities, true);
            if (properties != null && entities.First() is BaseEntity)
            {
                List<string> lstProperties = properties.ToList();
                lstProperties.Add("INSERT_TIME");
                lstProperties.Add("INSERT_PG");
                lstProperties.Add("INSERT_HOST");
                lstProperties.Add("INSERT_ID");
                lstProperties.Add("UPDATE_TIME");
                lstProperties.Add("UPDATE_PG");
                lstProperties.Add("UPDATE_HOST");
                lstProperties.Add("UPDATE_ID");
                properties = lstProperties.ToArray();
            }
            if (properties != null && properties.Length > 0)
            {
                PropertyInfo[] entityProperty = typeof(TEntity).GetProperties();
                string keyName = entityProperty.GetKeyName();
                if (properties.Contains(keyName))
                {
                    properties = properties.Where(x => x != keyName).ToArray();
                }
                properties = properties.Where(x => entityProperty.Select(s => s.Name).Contains(x)).ToArray();
            }
            foreach (TEntity item in entities)
            {
                //if (properties == null || properties.Length == 0)
                //{
                //    DbContext.Entry<TEntity>(item).State = EntityState.Modified;
                //    continue;
                //}
                //var entry = DbContext.Entry(item);
                //properties.ToList().ForEach(x =>
                //{
                //    try
                //    {
                //        entry.Property(x).IsModified = true;
                //    } catch { }
                //});
            }

            return DBSet.AddRangeAsync(entities);
        }

        //public virtual void AddRange<T>(IEnumerable<T> entities, bool saveChanges = false)
        //    where T : class
        //{
        //    DbContext.Set<T>().AddRange(entities);
        //    if (saveChanges) DbContext.SaveChanges();
        //}

        public virtual int SaveChanges()
        {
            return EFContext.SaveChanges();
        }

        public virtual Task<int> SaveChangesAsync()
        {
            return EFContext.SaveChangesAsync();
        }

        public virtual int ExecuteSqlCommand(string sql, params SqlParameter[] sqlParameters)
        {
            return DbContext.Database.ExecuteSqlCommand(sql, sqlParameters);
        }

        public virtual List<TEntity> FromSql(string sql, params SqlParameter[] sqlParameters)
        {
            return DBSet.SqlQuery(sql, sqlParameters).ToList();
        }

        ///// <summary>
        ///// 执行sql
        ///// 使用方式 FormattableString sql=$"select * from xx where name ={xx} and pwd={xx1} "，
        ///// FromSqlInterpolated内部处理sql注入的问题，直接在{xx}写对应的值即可
        ///// 注意：sql必须 select * 返回所有TEntity字段，
        ///// </summary>
        ///// <param name="formattableString"></param>
        ///// <returns></returns>
        //public virtual IQueryable<TEntity> FromSqlInterpolated([NotNull] FormattableString sql)
        //{
        //    //DBSet.FromSqlInterpolated(sql).Select(x => new { x,xxx}).ToList();
        //    return DBSet.FromSqlInterpolated(sql);
        //}

        /// <summary>
        /// 取消上下文跟踪
        /// </summary>
        /// <param name="entity"></param>
        public virtual void Detached(TEntity entity)
        {
            DbContext.Entry(entity).State = EntityState.Detached;
        }
        public virtual void DetachedRange(IEnumerable<TEntity> entities)
        {
            foreach (var entity in entities)
            {
                DbContext.Entry(entity).State = EntityState.Detached;
            }
        }
    }

    public abstract class RepositoryBase
    {
        public RepositoryBase()
        {
        }
        public RepositoryBase(WinMacDbContext dbContext)
        {
            this.DefaultDbContext = dbContext ?? throw new Exception("dbContextはインスタンス化されません。");
        }

        private WinMacDbContext DefaultDbContext { get; set; }

        public virtual WinMacDbContext DbContext
        {
            get { return DefaultDbContext; }
        }

        public ISqlDapper DapperContext
        {
            get { return DBServerProvider.GetSqlDapper(); }
        }
        ///// <summary>
        ///// トランザクション処理を行う
        ///// </summary>
        ///// <param name="action">falseの場合はRollback</param>
        ///// <returns></returns>
        //public virtual WebResponseContent DbContextBeginTransaction(Func<WebResponseContent> action)
        //{
        //    WebResponseContent webResponse = new WebResponseContent();
        //    using (IDbContextTransaction transaction = DefaultDbContext.Database.BeginTransaction())
        //    {
        //        try
        //        {
        //            webResponse = action();
        //            if (webResponse.Status)
        //            {
        //                transaction.Commit();
        //            }
        //            else
        //            {
        //                transaction.Rollback();
        //            }

        //            return webResponse;
        //        }
        //        catch (Exception ex)
        //        {
        //            transaction.Rollback();
        //            return new WebResponseContent().Error(ex.Message);
        //        }
        //    }
        //}

        public virtual int ExecuteSqlCommand(string sql, params SqlParameter[] sqlParameters)
        {
            return DbContext.Database.ExecuteSqlCommand(sql, sqlParameters);
        }
    }
}

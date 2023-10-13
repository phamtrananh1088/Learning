using System;
using System.Collections.Generic;
using System.Text;
using System.Data;
using System.Data.SqlClient;
using WinMacOs.Utility.Enums;
using WinMacOs.Utility.Utils;
using WinMacOs.Utility.Extensions;
using WinMacOs.DataRepository.EFDbContext;
using WinMacOs.DataRepository.Dapper;
using System.Web.Mvc;

namespace WinMacOs.DataRepository.DBManager
{

    public class DBServerProvider
    {
        private static Dictionary<string, string> ConnectionPool = new Dictionary<string, string>(StringComparer.OrdinalIgnoreCase);

        private static readonly string DefaultConnName = "DefaultConnection";

        static DBServerProvider()
        {
            SetConnection(DefaultConnName, AppSetting.DbConnectionString);
        }
        public static void SetConnection(string key, string val)
        {
            if (ConnectionPool.ContainsKey(key))
            {
                ConnectionPool[key] = val;
                return;
            }
            ConnectionPool.Add(key, val);
        }
        /// <summary>
        /// デフォルトデータベース連携
        /// </summary>
        /// <param name="val"></param>
        public static void SetDefaultConnection(string val)
        {
            SetConnection(DefaultConnName, val);
        }
        public static string GetConnectionString(string key)
        {
            key = key ?? DefaultConnName;
            if (ConnectionPool.ContainsKey(key))
            {
                return ConnectionPool[key];
            }
            return key;
        }
        /// <summary>
        /// デフォルトデータベース連携を取得
        /// </summary>
        /// <returns></returns>
        public static string GetConnectionString()
        {
            return GetConnectionString(DefaultConnName);
        }
        public static IDbConnection GetDbConnection(string connString = null)
        {
            if (connString == null)
            {
                connString = ConnectionPool[DefaultConnName];
            }

            //ほかのデータベースをここに判断条件を追加してください

            return new SqlConnection(connString);
        }
        /// <summary>
        /// データベースDbConnectionを取得、デフォルトはDBTypeのデータベース
        /// </summary>
        /// <param name="connString">connStringはnullの場合、多重定義GetDbConnection(string connString = null)</param>
        /// <param name="dapperType">データベース：SQLserver</param>
        /// <returns></returns>
        public static IDbConnection GetDbConnection(string connString = null, DbCurrentType dbCurrentType = DbCurrentType.Default)
        {
            //DbConnectionを取得
            if (connString.IsNullOrEmpty() || DbCurrentType.Default == dbCurrentType)
            {
                return GetDbConnection(connString);
            }

            //ほかのデータベースをここに判断条件を追加してください

            return new SqlConnection(connString);

        }

        #region Entity Framework (EF) Core
        public static WinMacDbContext DbContext
        {
            get { return GetEFDbContext(); }
        }
        public static WinMacDbContext GetEFDbContext()
        {
            return GetEFDbContext(null);
        }
        public static WinMacDbContext GetEFDbContext(string dbName)
        {
            WinMacDbContext beefContext = DependencyResolver.Current.GetService(typeof(WinMacDbContext)) as WinMacDbContext;
            //WinMacDbContext beefContext = _01.Reafs_W_NetCore.Utility.Utilities.HttpContext.Current.RequestServices.GetService(typeof(WinMacDbContext)) as WinMacDbContext;
            if (dbName != null)
            {
                if (!ConnectionPool.ContainsKey(dbName))
                {
                    throw new Exception("データベース連携名称は間違っています。");
                }
                beefContext.Database.Connection.ConnectionString = ConnectionPool[dbName];
            }
            return beefContext;
        }
        public static void SetDbContextConnection(WinMacDbContext beefContext, string dbName)
        {
            if (!ConnectionPool.ContainsKey(dbName))
            {
                throw new Exception("データベース連携名称は間違っています。");
            }
            beefContext.Database.Connection.ConnectionString = ConnectionPool[dbName];
        }
        /// <summary>
        /// データベースアクセスを取得
        /// </summary>
        /// <typeparam name="TEntity"></typeparam>
        /// <param name="defaultDbContext"></param>
        /// <returns></returns>
        public static void GetDbContextConnection<TEntity>(WinMacDbContext defaultDbContext)
        {
            //string connstr = defaultDbContext.Database.GetDbConnection().ConnectionString;
            //if (connstr != ConnectionPool[DefaultConnName])
            //{
            //    defaultDbContext.Database.GetDbConnection().ConnectionString = ConnectionPool[DefaultConnName];
            //};
        }
        #endregion

        #region Dapper
        public static ISqlDapper SqlDapper
        {
            get
            {
                return new SqlDapper(DefaultConnName);
            }
        }
        public static ISqlDapper GetSqlDapper(string dbName = null)
        {
            return new SqlDapper(dbName ?? DefaultConnName);
        }
        /// <summary>
        /// 
        /// </summary>
        /// <param name="dbCurrentType">指定数据库类型：MySql/MsSql/PgSql</param>
        /// <param name="dbName">指定数据连串名称</param>
        /// <returns></returns>
        public static ISqlDapper GetSqlDapper(DbCurrentType dbCurrentType, string dbName = null)
        {
            if (dbName.IsNullOrEmpty())
            {
                return new SqlDapper(dbName ?? DefaultConnName);
            }
            return new SqlDapper(dbName, dbCurrentType);
        }
        #endregion

    }
}

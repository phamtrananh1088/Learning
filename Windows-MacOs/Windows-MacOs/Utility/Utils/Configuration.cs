using Microsoft.AspNet.Identity;
using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Configuration;
using System.Web.Hosting;
using WinMacOs.Models;
using WinMacOs.Utility.Extensions;

namespace WinMacOs.Utility.Utils
{
    /// <summary>
    /// 静の変数.
    /// </summary>
    public class AppSetting
    {
        private static Connection _connection
        {
            get
            {
                string strcon = ConfigurationManager.ConnectionStrings["WinMacDbContext"].ConnectionString;
                var connectionString = new System.Data.SqlClient.SqlConnectionStringBuilder(strcon);
                return new Connection { DBType = "SqlServer", CommandTimeout = connectionString.ConnectTimeout, DbConnectionString = connectionString.ConnectionString };
            }
        }
        public static string DbConnectionString
        {
            get { return _connection.DbConnectionString; }
        }

        public static int CommandTimeout
        {
            get { return _connection.CommandTimeout; }
        }
        /// <summary>
        /// FileUploadPath.
        /// </summary>
        public static string FileUploadPath
        {
            get
            {
                string sFileUploadPath = ConfigurationManager.AppSettings["FileUploadPath"];
                return sFileUploadPath ?? "";
            }
        }

        /// <summary>
        /// NotesPath.
        /// </summary>
        public static string NotesPath
        {
            get
            {
                string sNotesPath = ConfigurationManager.AppSettings["NotesPath"];
                return sNotesPath ?? "";
            }
        }

        /// <summary>
        /// JWT有效期(分钟=默认120)
        /// </summary>
        public static int ExpMinutes { get; private set; } = 120;

        public static string ShareName
        {
            get
            {
                string sVar = ConfigurationManager.AppSettings["ShareName"];
                return sVar ?? "";
            }
        }

        public static string TokenHeaderName = "Authorization";

        public static int TokenCount { 
         get
            {
                return (ConfigurationManager.AppSettings["TokenCount"] ?? "5").GetInt();
            }
        }

        public static string CurrentPath {
            get
            {
                return HostingEnvironment.ApplicationPhysicalPath;
            }
        }
        public static string DownLoadPath { get { return CurrentPath + "\\Download\\"; } }


        public static Secret Secret
        {
            get
            {
                NameValueCollection section = (NameValueCollection)ConfigurationManager.GetSection("secret");
                return new Secret() { JWT = section["JWT"], Audience = section["Audience"], Issuer = section["Issuer"] };
            }
        }

        public static Credentials Credentials
        {
            get
            {
                NameValueCollection section = (NameValueCollection)ConfigurationManager.GetSection("credentials");
                return new Credentials() { userId = section["userId"], password = section["password"] };
            }
        }

        /// <summary>
        /// オンラインユーザリスト.
        /// </summary>
        public static List<LoginUser> LoginUsers { get; set; }

        public static int PrintAPIRecallDelayTime
        {
            get
            {
                string sVar = ConfigurationManager.AppSettings["PrintAPIRecallDelayTime"];
                return (sVar ?? "0").GetInt();
            }
        } // タイマー時間：30秒⇒30　、　1分⇒60
    }

    public class Connection
    {
        public string DBType { get; set; }
        public string DbConnectionString { get; set; }
        public int CommandTimeout { get; set; }
    }
}
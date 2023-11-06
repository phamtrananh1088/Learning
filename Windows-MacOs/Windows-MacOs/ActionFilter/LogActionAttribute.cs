using Newtonsoft.Json;
using System;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Web.Mvc;
using System.Web.Routing;
using WinMacOs.DataRepository.DBManager;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.Utils;

namespace WinMacOs.ActionFilter
{
    public class LogActionAttribute : ActionFilterAttribute
    {
        private void WriteLog(ActionExecutingContext context, string parameters)
        {
            UserContext userContext = UserContext.Current;
            string 社員ID = userContext.社員ID ?? userContext.ログインID;
            string host = userContext.ClientHost;
            Task.Factory.StartNew(() =>
            {
                //ResultExecutedContext resultContext = (ResultExecutedContext)context;
                ActionExecutingContext resultContext = (ActionExecutingContext)context;

                RouteData routerData = context.RouteData;

                string controller = (routerData.Values["controller"]?.ToString()) ?? "";
                string action = (routerData.Values["action"]?.ToString()) ?? "";

                string sシステム名 = AppSetting.SystemName;

                //if (action.Equals("Login"))
                //{
                //    JsonResult result = (JsonResult)resultContext.Result;
                //    UserInfo userInfo = JwtHelper.SerializeJwt(result.Value.GetValue("Data").GetValue("token").ToString());
                //    社員ID = userInfo.ログインID;
                //}
                //if (string.IsNullOrEmpty(社員ID)) return;

                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.AppendLine("INSERT INTO S019_Web操作ログファイル (");
                sqlBuilder.AppendLine("       処理日");
                sqlBuilder.AppendLine("     , 処理時間");
                sqlBuilder.AppendLine("     , システム名");
                sqlBuilder.AppendLine("     , プログラム名");
                sqlBuilder.AppendLine("     , 処理コメント");
                sqlBuilder.AppendLine("     , パラメータ");
                sqlBuilder.AppendLine("     , INSERT_TIME");
                sqlBuilder.AppendLine("     , INSERT_HOST");
                sqlBuilder.AppendLine("     , INSERT_ID");
                sqlBuilder.AppendLine(")");
                sqlBuilder.AppendLine("VALUES (");
                sqlBuilder.AppendLine("       @処理日");
                sqlBuilder.AppendLine("     , @処理時間");
                sqlBuilder.AppendLine("     , @システム名");
                sqlBuilder.AppendLine("     , @プログラム名");
                sqlBuilder.AppendLine("     , @処理コメント");
                sqlBuilder.AppendLine("     , @パラメータ");
                sqlBuilder.AppendLine("     , @INSERT_TIME");
                sqlBuilder.AppendLine("     , @INSERT_HOST");
                sqlBuilder.AppendLine("     , @INSERT_ID");
                sqlBuilder.AppendLine(")");

                DBServerProvider.SqlDapper.ExcuteNonQueryAsync(sqlBuilder.ToString(), new
                {
                    処理日 = DateTime.Now.ToString("yyyy/MM/dd"),
                    処理時間 = DateTime.Now.ToString("HH:mm:ss"),
                    システム名 = sシステム名,
                    プログラム名 = controller,
                    処理コメント = action,
                    INSERT_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss"),
                    INSERT_HOST = host,
                    INSERT_ID = 社員ID,
                    パラメータ = parameters
                });
            });
        }

        public static void WriteError(string message, string parameters, string pInsertPG = "")// ハノイ側修正2023/10/03　STEP2_W　課題管理表№400：「ログ出力処理の機能拡張依頼」
        {
            UserContext userContext = UserContext.Current;
            string 社員ID = userContext.社員ID ?? userContext.ログインID;
            string host = userContext.ClientHost;
            Task.Factory.StartNew(() =>
            {
                string sシステム名 = AppSetting.SystemName;
                
                StringBuilder sqlBuilder = new StringBuilder();
                sqlBuilder.AppendLine("INSERT INTO S020_Webエラーログファイル (");
                sqlBuilder.AppendLine("       処理日");
                sqlBuilder.AppendLine("     , 処理時間");
                sqlBuilder.AppendLine("     , システム名");
                sqlBuilder.AppendLine("     , クエリ内容");
                sqlBuilder.AppendLine("     , パラメータ");
                sqlBuilder.AppendLine("     , 処理コメント");
                sqlBuilder.AppendLine("     , メソッド名");
                sqlBuilder.AppendLine("     , INSERT_TIME");
                // ハノイ側修正2023/10/03　STEP2_W　課題管理表№400：「ログ出力処理の機能拡張依頼」START
                if (!string.IsNullOrEmpty(pInsertPG))
                {
                    sqlBuilder.AppendLine("     , INSERT_PG");
                }
                // ハノイ側修正2023/10/03　STEP2_W　課題管理表№400：「ログ出力処理の機能拡張依頼」END
                sqlBuilder.AppendLine("     , INSERT_HOST");
                sqlBuilder.AppendLine("     , INSERT_ID");
                sqlBuilder.AppendLine(")");
                sqlBuilder.AppendLine("VALUES (");
                sqlBuilder.AppendLine("       @処理日");
                sqlBuilder.AppendLine("     , @処理時間");
                sqlBuilder.AppendLine("     , @システム名");
                sqlBuilder.AppendLine("     , @クエリ内容");
                sqlBuilder.AppendLine("     , @パラメータ");
                sqlBuilder.AppendLine("     , @処理コメント");
                sqlBuilder.AppendLine("     , @メソッド名");
                sqlBuilder.AppendLine("     , @INSERT_TIME");
                // ハノイ側修正2023/10/03　STEP2_W　課題管理表№400：「ログ出力処理の機能拡張依頼」START
                if (!string.IsNullOrEmpty(pInsertPG))
                {
                    sqlBuilder.AppendLine("     , @INSERT_PG");
                }
                // ハノイ側修正2023/10/03　STEP2_W　課題管理表№400：「ログ出力処理の機能拡張依頼」END
                sqlBuilder.AppendLine("     , @INSERT_HOST");
                sqlBuilder.AppendLine("     , @INSERT_ID");
                sqlBuilder.AppendLine(")");

                var lstStrace = message.Split('\n').ToList().Where(x => x.Trim().Substring(0, 2).Equals("at")).ToList();
                if (lstStrace.Count > 0) lstStrace = lstStrace.Where(x => x.Contains(":line")).ToList();
                if (lstStrace.Count > 0) lstStrace.Reverse();
                string method = "";
                foreach (string strace in lstStrace)
                {
                    var temp = strace.Split('(')[0];
                    var arrTemp = temp.Split('.');
                    temp = arrTemp[arrTemp.Length - 1];
                    if (!string.IsNullOrEmpty(method)) method += " > ";
                    method += temp;
                }

                DBServerProvider.SqlDapper.ExcuteNonQueryAsync(sqlBuilder.ToString(), new
                {
                    処理日 = DateTime.Now.ToString("yyyy/MM/dd"),
                    処理時間 = DateTime.Now.ToString("HH:mm:ss"),
                    システム名 = sシステム名,
                    クエリ内容 = "通常エラーログ",
                    パラメータ = parameters,
                    処理コメント = message,
                    メソッド名 = method,
                    INSERT_TIME = DateTime.Now.ToString("yyyy/MM/dd HH:mm:ss"),
                    INSERT_PG = pInsertPG,// ハノイ側修正2023/10/03　STEP2_W　課題管理表№400：「ログ出力処理の機能拡張依頼」
                    INSERT_HOST = host,
                    INSERT_ID = 社員ID
                });
            });
        }

        string parameters = String.Empty;

        public override void OnActionExecuting(ActionExecutingContext filterContext)
        {
            parameters = JsonConvert.SerializeObject(filterContext.ActionParameters);
            WriteLog(filterContext, parameters);
            base.OnActionExecuting(filterContext);
        }

        /// <inheritdoc/>
        public override void OnActionExecuted(ActionExecutedContext filterContext)
        {
            base.OnActionExecuted(filterContext);
        }

        /// <inheritdoc/>
        public override void OnResultExecuting(ResultExecutingContext filterContext)
        {
            base.OnResultExecuting(filterContext);
        }

        /// <inheritdoc/>
        public override void OnResultExecuted(ResultExecutedContext filterContext)
        {
            if (filterContext.Result is JsonResult)
            {
                JsonResult result = (JsonResult)filterContext.Result;
                if ((bool)result.Data.GetValue("Status") == false)
                {
                    if (result.Data.GetValue("Code")?.ToString() == "500")
                    {
                        string message = result.Data.GetValue("Message")?.ToString();
                        WriteError(message, parameters);
                    }
                }
            }

            base.OnResultExecuted(filterContext);
        }
    }
}

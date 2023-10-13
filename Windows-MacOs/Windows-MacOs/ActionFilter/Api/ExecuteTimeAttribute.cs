using System;
using System.Diagnostics;
using System.Web.Mvc;
using System.Web.Routing;

namespace WinMacOs.ActionFilter.Api
{
    /// <summary>
    /// 実行時間取得フィルター.
    /// </summary>
    public class ExecuteTimeAttribute : ActionFilterAttribute
    {
        private Stopwatch stopwatch;

        /// <summary>ログ.</summary>
        /// <inheritdoc/>
        public override void OnActionExecuting(ActionExecutingContext filterContext)
        {
            // コントローラーは実行時間記録必要ではないとき
            if (filterContext.ActionDescriptor.ControllerDescriptor.GetCustomAttributes(typeof(SkipExecuteTimeAttribute), true).Length > 0)
            {
                return;
            }

            // アクションは実行時間記録必要ではないとき
            if (filterContext.ActionDescriptor.GetCustomAttributes(typeof(SkipExecuteTimeAttribute), true).Length > 0)
            {
                return;
            }

            // ログ書き込み
            NLogger.Default.InfoLog("実行開始");

            // 経過時間開始
            stopwatch = new Stopwatch();
            stopwatch.Start();
            base.OnActionExecuting(filterContext);
        }

        /// <inheritdoc/>
        public override void OnActionExecuted(ActionExecutedContext filterContext)
        {

            // コントローラーは実行時間記録必要ではないとき
            if (filterContext.ActionDescriptor.ControllerDescriptor.GetCustomAttributes(typeof(SkipExecuteTimeAttribute), true).Length > 0)
            {
                return;
            }

            // アクションは実行時間記録必要ではないとき
            if (filterContext.ActionDescriptor.GetCustomAttributes(typeof(SkipExecuteTimeAttribute), true).Length > 0)
            {
                return;
            }

            // 経過時間終了
            stopwatch.Stop();
            NLogger.Default.InfoLog(string.Format("実行終了。実行時間: {0} ms", stopwatch.ElapsedMilliseconds));
            stopwatch = null;
            base.OnActionExecuted(filterContext);
        }
    }
}
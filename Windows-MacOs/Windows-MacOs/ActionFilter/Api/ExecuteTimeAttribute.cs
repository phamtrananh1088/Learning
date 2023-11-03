using NLog;
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
            GetTimer(filterContext, "action").Start();
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
            var actionTimer = GetTimer(filterContext, "action");
            actionTimer.Stop();
            NLogger.Default.InfoLog(string.Format("実行終了。実行時間: {0} ms", actionTimer.ElapsedMilliseconds));
            base.OnActionExecuted(filterContext);
        }

        private Stopwatch GetTimer(ControllerContext context, string name)
        {
            string key = "__actionExecuteTimer__" + name;
            if (context.HttpContext.Items.Contains(key))
            {
                return (Stopwatch)context.HttpContext.Items[key];
            }

            var result = new Stopwatch();
            context.HttpContext.Items[key] = result;
            return result;
        }
    }
}
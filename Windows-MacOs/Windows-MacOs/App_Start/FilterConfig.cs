using System.Web;
using System.Web.Mvc;
using WinMacOs.ActionFilter;
using WinMacOs.ActionFilter.Api;

namespace WinMacOs
{
    public class FilterConfig
    {
        public static void RegisterGlobalFilters(GlobalFilterCollection filters)
        {
            filters.Add(new HandleErrorAttribute());
            // 実行時間
            filters.Add(new ExecuteTimeAttribute());
            filters.Add(new LogActionAttribute());
        }
    }
}

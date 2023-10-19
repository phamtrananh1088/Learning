using System;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;
using WinMacOs.Controllers;
using WinMacOs.DataRepository.AutofacManager;

namespace WinMacOs
{
    public class MvcApplication : HttpApplication
    {
        protected void Application_Start()
        {
            AreaRegistration.RegisterAllAreas();
            //UnityConfig.RegisterComponents();
            GlobalConfiguration.Configure(WebApiConfig.Register);
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);

            Bootstrapper.Initialise();
        }

        protected void Application_Error(object sender, EventArgs e)
        {
            NLogger.Default.ErrorLog(Server.GetLastError());
            //Response.Redirect("/home/error");
        }
    }
}

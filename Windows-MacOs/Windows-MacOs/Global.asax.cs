using Newtonsoft.Json.Linq;
using System;
using System.Web;
using System.Web.Http;
using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;
using WinMacOs.Controllers;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.Utility.Utils.Binder;

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

            //ModelBinders.Binders.Add(typeof(JToken), new JTokenModelBinder());
            ModelBinderProviders.BinderProviders.Add(new JTokenModelBinderProviders());
        }

        protected void Application_Error(object sender, EventArgs e)
        {
            NLogger.Default.ErrorLog(Server.GetLastError());
            //Response.Redirect("/home/error");
        }
    }
}

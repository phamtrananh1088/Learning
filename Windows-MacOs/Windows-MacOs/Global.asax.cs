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
            return;
            var exception = Server.GetLastError();

            var httpException = exception as HttpException;
            var routeData = new RouteData();

            routeData.Values.Add("controller", "Error");

            if (httpException == null)
                routeData.Values.Add("action", "Index");
            else //It's an Http Exception, Let's handle it.
                switch (httpException.GetHttpCode())
                {
                    case 404:
                        // Page not found.
                        routeData.Values.Add("action", "HttpError404");
                        break;
                    case 500:
                        // Server error.
                        routeData.Values.Add("action", "HttpError500");
                        break;

                    // Here you can handle Views to other error codes.
                    // I choose a General error template  
                    default:
                        routeData.Values.Add("action", "General");
                        break;
                }

            // Pass exception details to the target error View.
            routeData.Values.Add("error", exception);

            var request = Request;
            // Pass request details to the target request View.
            routeData.Values.Add("request", request);

            // Clear the error on server.
            Server.ClearError();

            // Avoid IIS7 getting in the middle
            Response.TrySkipIisCustomErrors = true;

            // Call target Controller and pass the routeData.
            IController errorController = AutofacContainerModule.GetService<Reafs_TController>();
            errorController.Execute(new RequestContext(
                new HttpContextWrapper(Context), routeData));
        }
    }
}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;

namespace WinMacOs
{
    public class RouteConfig
    {
        public static void RegisterRoutes(RouteCollection routes)
        {
            routes.IgnoreRoute("{resource}.axd/{*pathInfo}");

            routes.MapRoute(
                name: "Reafs_T",
                url: "Reafs_T/{path}",
                defaults: new { controller = "Reafs_T", action = "Index" }
            );

            routes.MapRoute(
                name: "Reafs_W",
                url: "Reafs_W/{path}",
                defaults: new { controller = "Reafs_W", action = "Index" }
            );

            routes.MapRoute(
                name: "Default",
                url: "{controller}/{action}/{id}",
                defaults: new { controller = "Home", action = "Index", id = UrlParameter.Optional }
            );
        }
    }
}

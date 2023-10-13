using System;
using System.Collections.Generic;
using System.Text;
using System.Web.Mvc;

namespace WinMacOs.Utility.Extensions
{
    public static class ServiceProviderManagerExtension
    {
        public static object GetService(this Type serviceType)
        {
            return DependencyResolver.Current.GetService(serviceType);
            //return Utilities.HttpContext.Current.RequestServices.GetService(serviceType);
        }

    }
}

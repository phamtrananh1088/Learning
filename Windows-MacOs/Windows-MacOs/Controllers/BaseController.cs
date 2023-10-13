using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;
using WinMacOs.ActionFilter;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Controllers
{
    [LargeJsonNetFilter]
    public abstract class BaseController<TModel> : Controller
        where TModel : new()
    {
        protected override void Initialize(RequestContext requestContext)
        {
            base.Initialize(requestContext);
            
        }
    }
}
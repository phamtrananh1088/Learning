using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;
using WinMacOs.ActionFilter;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Controllers
{
    [LargeJsonNetFilter]
    public abstract class BaseVueController<IServiceBase> : Controller
    {
        protected IServiceBase Service;
        protected override void Initialize(RequestContext requestContext)
        {
            base.Initialize(requestContext);
        }

        public BaseVueController()
        {

        }
        public BaseVueController(IServiceBase service)
        {
            Service = service;
        }

        
    }
}
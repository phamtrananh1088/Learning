using System.Web.Mvc;
using Unity;
using Unity.Mvc5;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Repositories;
using WinMacOs.Utility.CacheManager;

using Reafs_TIServices = WinMacOs.Business.IServices.Reafs_T;
using Reafs_WIServices = WinMacOs.Business.IServices.Reafs_W;
using ManagerIServices = WinMacOs.Business.IServices.Manager;
using WinMacOs.Controllers;
using Reafs_TServices = WinMacOs.Business.Services.Reafs_T;
using Reafs_WServices = WinMacOs.Business.Services.Reafs_W;
using ManagerServices = WinMacOs.Business.Services.Manager;
using WinMacOs.DataRepository.AutofacManager;
using System.Reflection;
using Unity.Lifetime;
using Microsoft.Extensions.Caching.Distributed;
using WinMacOs.Utility.CacheManager.Sevice;
using WinMacOs.Business.IServices.Dual;
using WinMacOs.Business.Services.Dual;

namespace WinMacOs
{
    public static class UnityConfig
    {
        public static void RegisterComponents()
        {
			var container = new UnityContainer();

            // register all your components with the container here
            // it is NOT necessary to register your controllers

            // e.g. container.RegisterType<ITestService, TestService>();
            container.RegisterType<Reafs_TIServices.ICommonService, Reafs_TServices.CommonService>();
            container.RegisterType<Reafs_TIServices.ILoginService, Reafs_TServices.LoginService>();
            container.RegisterType<Reafs_TIServices.ITD00001Service, Reafs_TServices.TD00001Service>();
            container.RegisterType<Reafs_WIServices.ICommonService, Reafs_WServices.CommonService>();
            container.RegisterType<ManagerIServices.ICanvasService, ManagerServices.CanvasService>();
            container.RegisterType<ManagerIServices.ISQLCompareService, ManagerServices.SQLCompareService>();
            container.RegisterType<IDualService, DualService>();
            container.RegisterType<IEnDualService, EnDualService>();
            container.RegisterType<IUnitOfWork, UnitOfWork>();

            container.RegisterType<ICacheService, DistributedCacheService>(new SingletonLifetimeManager());
            container.RegisterType<IDistributedCache, RedisCache>();
            //container.AddModule();

            DependencyResolver.SetResolver(new UnityDependencyResolver(container));
        }
    }
}
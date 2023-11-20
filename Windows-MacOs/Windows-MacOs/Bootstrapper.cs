using System.Web.Mvc;
using Microsoft.Practices.Unity;
using Unity.Mvc3;
using Reafs_TIServices = WinMacOs.Business.IServices.Reafs_T;
using Reafs_WIServices = WinMacOs.Business.IServices.Reafs_W;
using WinMacOs.Controllers;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Repositories;
using Reafs_TServices = WinMacOs.Business.Services.Reafs_T;
using Reafs_WServices = WinMacOs.Business.Services.Reafs_W;

namespace WinMacOs
{
    public static class Bootstrapper
    {
        public static void Initialise()
        {
            var container = BuildUnityContainer();

            DependencyResolver.SetResolver(new UnityDependencyResolver(container));
        }

        private static IUnityContainer BuildUnityContainer()
        {
            var container = new UnityContainer();

            // register all your components with the container here
            // it is NOT necessary to register your controllers

            // e.g. container.RegisterType<ITestService, TestService>();
            container.RegisterType<Reafs_TIServices.ICommonService, Reafs_TServices.CommonService>();
            container.RegisterType<Reafs_TIServices.ILoginService, Reafs_TServices.LoginService>();
            container.RegisterType<Reafs_WIServices.ICommonService, Reafs_WServices.CommonService>();
            container.RegisterType<IUnitOfWork, UnitOfWork>();
            return container;
        }
    }
}
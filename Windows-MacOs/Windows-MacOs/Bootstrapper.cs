using System.Web.Mvc;
using Microsoft.Practices.Unity;
using Unity.Mvc3;
using WinMacOs.Controllers;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Repositories;

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
            container.RegisterType<IUnitOfWork, UnitOfWork>();

            return container;
        }
    }
}
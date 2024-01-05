using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using Microsoft.Extensions.DependencyModel;
using Unity;
using WinMacOs.Utility.Extensions;

namespace WinMacOs.DataRepository.AutofacManager
{
    public static class AutofacContainerModuleExtension
    {
        public static void AddModule(this UnityContainer container)
        {
            //services.AddMemoryCache();

            //配置ファイルを初期化
            //AppSetting.Init(services, configuration);

            Type baseType = typeof(IDependency);
            var compilationLibrary = DependencyContext.Default
                                                      .RuntimeLibraries
                                                      .Where(x => !x.Serviceable && x.Type == "project")
                                                      .ToList();
            List<Assembly> assemblyList = new List<Assembly>();

            foreach (var _compilation in compilationLibrary)
            {
                try
                {
                    assemblyList.Add(Assembly.Load(new AssemblyName(_compilation.Name)));
                }
                catch (Exception ex)
                {
                    Console.WriteLine(_compilation.Name + ex.Message);
                }
            }
            foreach (var assembly in assemblyList)
            {
                Type type = assembly.GetType();
                container.RegisterType(type, type);
            }

            //builder.RegisterAssemblyTypes(assemblyList.ToArray())
            //        .Where(type => baseType.IsAssignableFrom(type) && !type.IsAbstract)
            //        .AsSelf().AsImplementedInterfaces()
            //        .InstancePerLifetimeScope();
            //builder.RegisterType<UserContext>().InstancePerLifetimeScope();
            //builder.RegisterType<ActionObserver>().InstancePerLifetimeScope();

            //string connectionString = DBServerProvider.GetConnectionString(null);
            //services.AddDbContextPool<Context>(optionsBuilder => { optionsBuilder.UseSqlServer(connectionString); }, 64);

            ////キャシュ配置
            //builder.RegisterType<MemoryCacheService>().As<ICacheService>().SingleInstance();

            //return services;
        }

    }
}

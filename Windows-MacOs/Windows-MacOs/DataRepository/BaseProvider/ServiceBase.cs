using System;
using System.Reflection;
using System.Web;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;
using WinMacOs.Utility.CacheManager;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.DataRepository.BaseProvider
{

    public abstract class ServiceBase<T, TRepository> : ServiceFunFilter<T>
         where T : BaseEntity
         where TRepository : IRepository<T>
    {
        public ICacheService CacheContext
        {
            get
            {
                return AutofacContainerModule.GetService<ICacheService>();
            }
        }
        public HttpContext Context
        {
            get
            {
                return HttpContext.Current;
            }
        }
        private WebResponseContent Response { get; set; }

        protected IRepository<T> repository;

        private PropertyInfo[] _propertyInfo { get; set; } = null;
        private PropertyInfo[] TProperties
        {
            get
            {
                if (_propertyInfo != null)
                {
                    return _propertyInfo;
                }
                _propertyInfo = typeof(T).GetProperties();
                return _propertyInfo;
            }
        }

        public ServiceBase()
        {
        }

        public ServiceBase(TRepository repository)
        {
            Response = new WebResponseContent(true);
            this.repository = repository;
        }

        protected virtual void Init(IRepository<T> repository)
        {

        }

        protected virtual Type GetRealDetailType()
        {
            return typeof(T).GetCustomAttribute<EntityAttribute>()?.DetailTable?[0];
        }

    }

    public class ServiceFunFilter<T> where T : BaseEntity
    {
    }

    public abstract class ServiceBase<TRepository> : ServiceFunFilter
         where TRepository : IRepository
    {
        public ICacheService CacheContext
        {
            get
            {
                return AutofacContainerModule.GetService<ICacheService>();
            }
        }
        public HttpContext Context
        {
            get
            {
                return HttpContext.Current;
            }
        }
        private WebResponseContent Response { get; set; }

        protected IRepository repositorySimple;
        protected IUnitOfWork repository;

        private PropertyInfo[] _propertyInfo { get; set; } = null;

        public ServiceBase()
        {
        }
        
        public ServiceBase(TRepository repository)
        {
            Response = new WebResponseContent(true);
            this.repositorySimple = repository;
        }
        public ServiceBase(IUnitOfWork repository)
        {
            Response = new WebResponseContent(true);
            this.repository = repository;
        }

        protected virtual void Init(IRepository repository)
        {
           
        }

    }

    public class ServiceFunFilter
    {
        protected ParamCommon GetParamCommon()
        {
            ParamCommon paramCommon = new ParamCommon
            {
                HOST = RouteUtils.GetClientIP()
            };

            return paramCommon;
        }
    }
}

using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Web.Services.Description;
using WinMacOs.Business.IServices.Reafs_T;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.SystemModels;

namespace WinMacOs.Business.Services.Reafs_T
{
    public partial class LoginService : ServiceBase<IUnitOfWork>, ILoginService
    {
        public LoginService(IUnitOfWork repository)
             : base(repository)
        {
            Init(repository);
        }
        public static ILoginService Instance
        {
            get { return AutofacContainerModule.GetService<ILoginService>(); }
        }
    }
}

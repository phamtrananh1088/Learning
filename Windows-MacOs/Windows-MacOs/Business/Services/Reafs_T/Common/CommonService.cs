using _03.Reafs_W_NetCore.Business.IServices.Reafs_T;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Web.Services.Description;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.SystemModels;

namespace WinMacOs.Business.Services.Reafs_T
{
    public partial class CommonService : ServiceBase<IUnitOfWork>, ICommonService
    {
        public CommonService(IUnitOfWork repository)
             : base(repository)
        {
            Init(repository);
        }
        public static ICommonService Instance
        {
            get { return AutofacContainerModule.GetService<ICommonService>(); }
        }
    }
}

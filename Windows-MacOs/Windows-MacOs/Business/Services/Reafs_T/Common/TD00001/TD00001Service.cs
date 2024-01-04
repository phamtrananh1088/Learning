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
    public partial class TD00001Service : ServiceBase<IUnitOfWork>, ITD00001Service, IDependency
    {
        public TD00001Service(IUnitOfWork repository)
             : base(repository)
        {
            Init(repository);
        }
        public static ITD00001Service Instance
        {
            get { return AutofacContainerModule.GetService<ITD00001Service>(); }
        }
    }
}

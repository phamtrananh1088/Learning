using System;
using System.Collections.Generic;
using System.Text;
using WinMacOs.Business.IServices.Reafs_W;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.IRepositories;

namespace WinMacOs.Business.Services.Reafs_W
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

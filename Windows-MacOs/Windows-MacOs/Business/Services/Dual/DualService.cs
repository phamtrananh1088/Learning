using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Data.SqlClient;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Business.IServices.Dual;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Services.Dual
{
    public partial class DualService : ServiceBase<IUnitOfWork>, IDualService
    {
        public DualService(IUnitOfWork repository)
             : base(repository)
        {
            Init(repository);
        }
        public static IDualService Instance
        {
            get { return AutofacContainerModule.GetService<IDualService>(); }
        }

    }
}
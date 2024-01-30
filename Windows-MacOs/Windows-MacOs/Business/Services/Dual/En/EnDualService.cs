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
    public partial class EnDualService : DualService, IEnDualService
    {
        public EnDualService(IUnitOfWork repository)
             : base(repository)
        {
            Init(repository);
        }
        public static new IEnDualService Instance
        {
            get { return AutofacContainerModule.GetService<IEnDualService>(); }
        }

    }
}
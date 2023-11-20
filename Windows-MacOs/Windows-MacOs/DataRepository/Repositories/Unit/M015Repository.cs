using System;
using System.Collections.Generic;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using System.Text;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.EFDbContext;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Utility.TableModels;

namespace WinMacOs.DataRepository.Repositories
{
    public partial class M015Repository : RepositoryBase<M015_業者ユーザマスタ>, IM015Repository
    {
        public M015Repository(WinMacDbContext dbContext)
        : base(dbContext)
        {

        }
        public static IM015Repository Instance
        {
            get { return AutofacContainerModule.GetService<IM015Repository>(); }
        }
    }
}

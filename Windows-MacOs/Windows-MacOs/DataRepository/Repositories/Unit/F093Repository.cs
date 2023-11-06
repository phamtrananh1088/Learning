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
    public partial class F093Repository : RepositoryBase<F093_一時添付ファイル>, IF093Repository
    {
        public F093Repository(WinMacDbContext dbContext)
        : base(dbContext)
        {

        }
        public static IF093Repository Instance
        {
            get { return AutofacContainerModule.GetService<IF093Repository>(); }
        }
    }
}

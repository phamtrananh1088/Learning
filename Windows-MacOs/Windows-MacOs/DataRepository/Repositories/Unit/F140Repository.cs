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
    public partial class F140Repository : RepositoryBase<F140_ログイン認証ファイル>, IF140Repository
    {
        public F140Repository(WinMacDbContext dbContext)
        : base(dbContext)
        {

        }
        public static IF140Repository Instance
        {
            get { return AutofacContainerModule.GetService<IF140Repository>(); }
        }
    }
}

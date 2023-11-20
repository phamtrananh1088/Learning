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
    public partial class S016Repository : RepositoryBase<S016_メッセージマスタ>, IS016Repository
    {
        public S016Repository(WinMacDbContext dbContext)
        : base(dbContext)
        {

        }
        public static IS016Repository Instance
        {
            get { return AutofacContainerModule.GetService<IS016Repository>(); }
        }
    }
}

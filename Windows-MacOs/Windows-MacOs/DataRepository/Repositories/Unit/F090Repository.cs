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
    public partial class F090Repository : RepositoryBase<F090_ドキュメント管理ファイル>, IF090Repository
    {
        public F090Repository(WinMacDbContext dbContext)
        : base(dbContext)
        {

        }
        public static IF090Repository Instance
        {
            get { return AutofacContainerModule.GetService<IF090Repository>(); }
        }
    }
}

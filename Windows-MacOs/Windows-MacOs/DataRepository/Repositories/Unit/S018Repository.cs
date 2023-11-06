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
    public partial class S018Repository : RepositoryBase<S018_ドキュメント定義>, IS018Repository
    {
        public S018Repository(WinMacDbContext dbContext)
        : base(dbContext)
        {

        }
        public static IS018Repository Instance
        {
            get { return AutofacContainerModule.GetService<IS018Repository>(); }
        }
    }
}

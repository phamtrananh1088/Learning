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
    public partial class SrtRepository : RepositoryBase<Srt>, ISrtRepository
    {
        public SrtRepository(WinMacDbContext dbContext)
        : base(dbContext)
        {

        }
        public static ISrtRepository Instance
        {
            get { return AutofacContainerModule.GetService<ISrtRepository>(); }
        }

        public IQueryable<Srt> FromSqlInterpolated([NotNull] FormattableString sql)
        {
            throw new NotImplementedException();
        }

    }
}

using System;
using System.Threading.Tasks;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.EFDbContext;
using WinMacOs.Utility.Extensions;
using System.Data.Entity;

namespace WinMacOs.DataRepository.Repositories
{
    public class UnitOfWork : RepositoryBase, IUnitOfWork, IDisposable
    {

        private string _INSERT_UPDATE_PG = String.Empty;
        public string INSERT_UPDATE_PG { get
            {
                return _INSERT_UPDATE_PG; 
            }
            set
            {
                DbContext.INSERT_UPDATE_PG = value;
                _INSERT_UPDATE_PG = value;
            }
        }

        #region Per Unit Repository
        private ISrtRepository _SrtRepo;
        public ISrtRepository SrtRepo
        {
            get
            {
                if (this._SrtRepo == null)
                {
                    this._SrtRepo = new SrtRepository(DbContext);
                }
                return this._SrtRepo;
            }
        }
        #endregion

        DbContextTransaction dbContextTransaction = null;
        public async Task BeginTransactionAsync()
        {
            dbContextTransaction = await DbContext.Database.BeginTransactionAsync();
        }

        public async Task SaveChangesAsync()
        {
            await DbContext.SaveChangesAsync();
            if (dbContextTransaction != null)
            {
                dbContextTransaction.Commit();
                dbContextTransaction.Dispose();
            }
        }

        public void Dispose()
        {
            DbContext.Dispose();
            if (dbContextTransaction != null)
            {
                dbContextTransaction.Dispose();
            }
        }
    }
}

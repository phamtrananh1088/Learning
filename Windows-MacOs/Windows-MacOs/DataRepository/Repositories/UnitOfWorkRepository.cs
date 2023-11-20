using System;
using System.Threading.Tasks;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.EFDbContext;
using WinMacOs.Utility.Extensions;
using System.Data.Entity;
using Microsoft.Owin.Logging;

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

        //#region Per Unit Repository
        //private ISrtRepository _SrtRepo;
        //public ISrtRepository SrtRepo
        //{
        //    get
        //    {
        //        if (this._SrtRepo == null)
        //        {
        //            this._SrtRepo = new SrtRepository(DbContext);
        //        }
        //        return this._SrtRepo;
        //    }
        //}
        //#endregion

        #region F090_ドキュメント管理ファイル
        private IF090Repository _F090_ドキュメント管理ファイル;
        public IF090Repository F090_ドキュメント管理ファイル
        {
            get
            {
                if (this._F090_ドキュメント管理ファイル == null)
                {
                    this._F090_ドキュメント管理ファイル = new F090Repository(DbContext);
                }
                return this._F090_ドキュメント管理ファイル;
            }
        }
        #endregion

        #region F093_一時添付ファイル
        private IF093Repository _F093_一時添付ファイル;
        public IF093Repository F093_一時添付ファイル
        {
            get
            {
                if (this._F093_一時添付ファイル == null)
                {
                    this._F093_一時添付ファイル = new F093Repository(DbContext);
                }
                return this._F093_一時添付ファイル;
            }
        }
        #endregion

        #region S018_ドキュメント定義
        private IS018Repository _S018_ドキュメント定義;
        public IS018Repository S018_ドキュメント定義
        {
            get
            {
                if (this._S018_ドキュメント定義 == null)
                {
                    this._S018_ドキュメント定義 = new S018Repository(DbContext);
                }
                return this._S018_ドキュメント定義;
            }
        }
        #endregion

        #region M015_業者ユーザマスタ
        private IM015Repository _M015_業者ユーザマスタ;
        public IM015Repository M015_業者ユーザマスタ
        {
            get
            {
                if (this._M015_業者ユーザマスタ == null)
                {
                    this._M015_業者ユーザマスタ = new M015Repository(DbContext);
                }
                return this._M015_業者ユーザマスタ;
            }
        }
        #endregion

        #region S016_メッセージマスタ
        private IS016Repository _S016_メッセージマスタ;
        public IS016Repository S016_メッセージマスタ
        {
            get
            {
                if (this._S016_メッセージマスタ == null)
                {
                    this._S016_メッセージマスタ = new S016Repository(DbContext);
                }
                return this._S016_メッセージマスタ;
            }
        }
        #endregion

        #region F140_ログイン認証ファイル
        private IF140Repository _F140_ログイン認証ファイル;
        public IF140Repository F140_ログイン認証ファイル
        {
            get
            {
                if (this._F140_ログイン認証ファイル == null)
                {
                    this._F140_ログイン認証ファイル = new F140Repository(DbContext);
                }
                return this._F140_ログイン認証ファイル;
            }
        }
        #endregion

        public UnitOfWork(WinMacDbContext context): base(context)
        {
        }

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
            dbContextTransaction?.Dispose();
        }
    }
}

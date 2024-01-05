using System;
using System.Collections.Generic;
using System.Text;
using System.Data.Entity;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.SystemModels;

namespace WinMacOs.DataRepository.EFDbContext
{
    public class WinMacDbContext : DbContext
    {
        /// <summary>
        /// データベースアクセス名称 
        /// </summary>
        public string INSERT_UPDATE_PG = String.Empty;
        public WinMacDbContext()
                : base("WinMacDbContext")
        {
        }

        public override int SaveChanges()
        {
            try
            {
                return base.SaveChanges();
            }
            catch (Exception ex)//DbUpdateException 
            {
                throw (ex.InnerException as Exception ?? ex);
            }
        }
        public override DbSet<TEntity> Set<TEntity>()
        {
            return base.Set<TEntity>();
        }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Entity<Srt>()
             .HasKey(c => new { c.JI_NO });
            modelBuilder.Entity<M003_社員マスタ>()
             .HasKey(c => new { c.社員ID });
            modelBuilder.Entity<M015_業者ユーザマスタ>()
            .HasKey(c => new { c.業者コード, c.業者コード枝番, c.ユーザーＩＤ });
            modelBuilder.Entity<S016_メッセージマスタ>()
                .HasKey(c => new { c.メッセージコード });
            modelBuilder.Entity<F140_ログイン認証ファイル>()
           .HasKey(c => new { c.連番, c.ログインID });
            modelBuilder.Entity<F090_ドキュメント管理ファイル>()
            .HasKey(c => new { c.ドキュメントNO });
            modelBuilder.Entity<F093_一時添付ファイル>()
           .HasKey(c => new { c.添付NO, c.枝番 });
            modelBuilder.Entity<S018_ドキュメント定義>()
           .HasKey(c => new { c.帳票種類 });
        }
    }
}

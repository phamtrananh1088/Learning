using System;
using System.Collections.Generic;
using System.Text;
using System.Data.Entity;
using WinMacOs.Utility.TableModels;

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
            modelBuilder.Entity<M015_業者ユーザマスタ>()
            .HasKey(c => new { c.業者コード, c.業者コード枝番, c.ユーザーＩＤ });
        }
    }
}

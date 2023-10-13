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
        public string DataBaseName = null;
        public string INSERT_UPDATE_PG = String.Empty;
        public WinMacDbContext()
                : base()
        {
        }
        public WinMacDbContext(string connection)
            : base()
        {
            DataBaseName = connection;
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
            modelBuilder.Entity<SrtModel>()
             .HasKey(c => new { c.JI_NO });
        }
    }
}

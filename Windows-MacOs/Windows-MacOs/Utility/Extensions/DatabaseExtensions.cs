using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace WinMacOs.Utility.Extensions
{
    public static class DatabaseExtensions
    {
        public static Task<DbContextTransaction> BeginTransactionAsync(this Database database)
        {
            return Task.Factory.StartNew(() => database.BeginTransaction());
        }
    }
}
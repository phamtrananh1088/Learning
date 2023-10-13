using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Diagnostics.CodeAnalysis;
using System.Linq;
using System.Threading;
using System.Threading.Tasks;
using System.Web;

namespace WinMacOs.Utility.Extensions
{
    public static class DbSetExtensions
    {
        //
        // Summary:
        //     Begins tracking the given entities, and any other reachable entities that are
        //     not already being tracked, in the Microsoft.EntityFrameworkCore.EntityState.Added
        //     state such that they will be inserted into the database when Microsoft.EntityFrameworkCore.DbContext.SaveChanges
        //     is called.
        //     This method is async only to allow special value generators, such as the one
        //     used by 'Microsoft.EntityFrameworkCore.Metadata.SqlServerValueGenerationStrategy.SequenceHiLo',
        //     to access the database asynchronously. For all other cases the non async method
        //     should be used.
        //
        // Parameters:
        //   entities:
        //     The entities to add.
        //
        // Returns:
        //     A task that represents the asynchronous operation.
        public static Task AddRangeAsync<TEntity>(this DbSet<TEntity> dbSet, [NotNullAttribute] params TEntity[] entities) where TEntity : class
        {
           return Task.Factory.StartNew(() => dbSet.AddRange(entities));
        }

        //
        // Summary:
        //     Begins tracking the given entities, and any other reachable entities that are
        //     not already being tracked, in the Microsoft.EntityFrameworkCore.EntityState.Added
        //     state such that they will be inserted into the database when Microsoft.EntityFrameworkCore.DbContext.SaveChanges
        //     is called.
        //     This method is async only to allow special value generators, such as the one
        //     used by 'Microsoft.EntityFrameworkCore.Metadata.SqlServerValueGenerationStrategy.SequenceHiLo',
        //     to access the database asynchronously. For all other cases the non async method
        //     should be used.
        //
        // Parameters:
        //   entities:
        //     The entities to add.
        //
        //   cancellationToken:
        //     A System.Threading.CancellationToken to observe while waiting for the task to
        //     complete.
        //
        // Returns:
        //     A task that represents the asynchronous operation.
        public static Task AddRangeAsync<TEntity>(this DbSet<TEntity> dbSet, [NotNullAttribute] IEnumerable<TEntity> entities, CancellationToken cancellationToken = default) where TEntity : class
        {
            return Task.Factory.StartNew(() => dbSet.AddRange(entities));
        }
    }
}
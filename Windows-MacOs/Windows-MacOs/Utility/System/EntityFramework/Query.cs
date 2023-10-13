using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace Microsoft.EntityFramework.Query
{
    //
    // Summary:
    //     Supports queryable Include/ThenInclude chaining operators.
    //
    // Type parameters:
    //   TEntity:
    //     The entity type.
    //
    //   TProperty:
    //     The property type.
    public interface IIncludableQueryable<out TEntity, out TProperty> : IQueryable<TEntity>, IEnumerable<TEntity>, IEnumerable, IQueryable
    {
    }
}
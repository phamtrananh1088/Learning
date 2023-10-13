using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace System.Diagnostics.CodeAnalysis
{
    //
    // Summary:
    //     Specifies that an output is not null even if the corresponding type allows it.
    [AttributeUsage(AttributeTargets.Property | AttributeTargets.Field | AttributeTargets.Parameter | AttributeTargets.ReturnValue, Inherited = false)]
    public sealed class NotNullAttribute : Attribute
    {
        //
        // Summary:
        //     Initializes a new instance of the System.Diagnostics.CodeAnalysis.NotNullAttribute
        //     class.
        public NotNullAttribute()
        {

        }
    }
}
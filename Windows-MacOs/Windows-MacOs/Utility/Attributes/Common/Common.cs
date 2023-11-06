using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WinMacOs.Utility.Attributes.Common
{
    //
    // Summary:
    //     Signals that custom LINQ operator parameter should not be parameterized during
    //     query compilation.
    [AttributeUsage(AttributeTargets.Parameter)]
    public sealed class NotParameterizedAttribute : Attribute
    {
    }
}
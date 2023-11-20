using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Web;

namespace WinMacOs.Utility.Security.Claims
{
    //
    // Summary:
    //     Claims related extensions for System.Security.Claims.ClaimsPrincipal.
    public static class PrincipalExtensions
    {
        //
        // Summary:
        //     Returns the value for the first claim of the specified type otherwise null the
        //     claim is not present.
        //
        // Parameters:
        //   principal:
        //     The System.Security.Claims.ClaimsPrincipal instance this method extends.
        //
        //   claimType:
        //     The claim type whose first value should be returned.
        //
        // Returns:
        //     The value of the first instance of the specified claim type, or null if the claim
        //     is not present.
        public static string FindFirstValue(this ClaimsPrincipal principal, string claimType)
        {
            return principal.Claims.FirstOrDefault((m) => m.Type == claimType)?.Value;
        }
    }
}
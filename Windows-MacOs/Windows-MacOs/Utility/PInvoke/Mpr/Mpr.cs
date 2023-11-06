using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.InteropServices;
using System.Web;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.PInvoke.Mpr
{
    /// <summary>
    /// Mpr.dll is a Windows DLL file that is responsible for managing network connections in Windows. 
    /// It provides support for various network protocols, such as TCP/IP, IPX/SPX, and NetBEUI, 
    /// allowing your computer to communicate with other devices on a network 12. 
    /// If you encounter an error message related to mpr.dll, it could indicate a registry problem, 
    /// a virus or malware issue, or even a hardware failure 2. You can try to fix the error by using System Restore, 
    /// running a virus/malware scan of your entire system, or reinstalling the program that uses the mpr.dll file 2. 
    /// For more information on Multiple Provider Router (MPR), you can read this Microsoft article.
    /// </summary>
    [System.Diagnostics.CodeAnalysis.SuppressMessage("Style", "IDE1006:Naming Styles", Justification = "<Pending>")]
    public static class Mpr
    {
        #region param declaration
        [StructLayout(LayoutKind.Sequential)]
        public class NETRESOURCE
        {
            public int dwScope { get; set; }
            public int dwType { get; set; }
            public int dwDisplayType { get; set; }
            public int dwUsage { get; set; }
            public string lpLocalName { get; set; }
            public string lpRemoteName { get; set; }
            public string lpComment { get; set; }
            public string lpProvider { get; set; }
        }

        #endregion

        #region dll
        [DllImport("mpr.dll")]
        private static extern int WNetAddConnection2(NETRESOURCE lpNetResource, string lpPassword, string lpUsername, int dwFlags);

        [DllImport("mpr.dll")]
        private static extern int WNetCancelConnection2(string name, int flags,
            bool force);
        #endregion

        #region PInvoke
        public static int NetResourceConnect()
        {
            int ret = 0;
            string shareName = AppSetting.ShareName;
            string userId = AppSetting.Credentials.userId;
            string password = AppSetting.Credentials.password;

            if (!string.IsNullOrEmpty(shareName))
            {
                var netResource = new NETRESOURCE();
                netResource.dwScope = 0;
                netResource.dwType = 1;
                netResource.dwDisplayType = 0;
                netResource.dwUsage = 0;
                netResource.lpLocalName = "";
                netResource.lpRemoteName = shareName;
                netResource.lpComment = "";
                netResource.lpProvider = "";
                ret = WNetAddConnection2(netResource, password, userId, 0);
            }

            return ret;
        }

        public static int NetResourceCancelConnect()
        {
            int ret = 0;
            string shareName = AppSetting.ShareName;

            if (!string.IsNullOrEmpty(shareName))
            {
                ret = WNetCancelConnection2(shareName, 0, true);
            }

            return ret;
        }
        #endregion
    }
}
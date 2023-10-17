using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace WinMacOs.Utility.Utils
{
    public static class Common
    {
        public static object GetValue(this object obj, string propertyName)
        {
            if (obj == null) return null;
            if (obj is IDictionary<string, object>)
            {
                var data = (IDictionary<string, object>)obj;
                object value = data[propertyName];
                return value;
            }
            else
            {
                System.Reflection.PropertyInfo pi = obj.GetType().GetProperty(propertyName);
                var res = pi?.GetValue(obj, null);
                return res;
            };
        }


        public static TEntity CopyFrom<TEntity>(this TEntity copyToEntity, TEntity copyFromEntity)
        {
            if (copyFromEntity?.GetType()?.Name?.IndexOf("List") >= 0)
            {
                throw new Exception("Wrong type.");
            }
            if (copyToEntity is IDictionary<string, object> && copyFromEntity is IDictionary<string, object>)
            {
                var dataCopyTo = (IDictionary<string, object>)copyToEntity;
                var dataCopyFrom = (IDictionary<string, object>)copyFromEntity;
                foreach (var from in dataCopyFrom)
                {
                    dataCopyTo[from.Key] = from.Value;
                }
                copyToEntity = (TEntity)dataCopyTo;
            }
            else if (copyToEntity is IDictionary<string, object> &&
                copyFromEntity?.GetType()?.Name?.IndexOf("<>f__AnonymousType") >= 0)
            {
                var dataCopyTo = (IDictionary<string, object>)copyToEntity;
                PropertyInfo[] oProperties = copyFromEntity.GetType().GetProperties();

                foreach (PropertyInfo CurrentProperty in oProperties)
                {
                    dataCopyTo[CurrentProperty.Name] = CurrentProperty.GetValue(copyFromEntity, null);
                }

                copyToEntity = (TEntity)dataCopyTo;
            }
            else if (copyToEntity?.GetType()?.Name.IndexOf("<>f__AnonymousType") >= 0 &&
                copyFromEntity?.GetType()?.Name.IndexOf("<>f__AnonymousType") >= 0)
            {
                PropertyInfo[] oProperties = copyFromEntity.GetType().GetProperties();

                foreach (PropertyInfo CurrentProperty in oProperties.Where(p => p.CanWrite))
                {
                    if (CurrentProperty.GetValue(copyFromEntity, null) != null)
                    {
                        CurrentProperty.SetValue(copyToEntity, CurrentProperty.GetValue(copyFromEntity, null), null);
                    }
                }
            }
            return copyToEntity;
        }

        public static TEntity SetValue<TEntity>(this TEntity obj, object value, string propertyName)
        {
            if (obj == null) return obj;
            if (obj is IDictionary<string, object>)
            {
                var data = (IDictionary<string, object>)obj;
                data[propertyName] = value;
                return (TEntity)data;
            }
            else
            {
                System.Reflection.PropertyInfo pi = obj.GetType().GetProperty(propertyName);
                pi?.SetValue(obj, value);
                return obj;
            };
        }

        /// <summary>
        /// コンテンツタイプリストを取得する.
        /// </summary>
        /// <returns>コンテンツタイプリスト.</returns>
        public static string GetContentType(this string fileName)
        {
            if (string.IsNullOrEmpty(fileName))
            {
                return "application/octet-stream";
            }

            System.IO.FileInfo fileInfo = new System.IO.FileInfo(fileName);
            if (fileInfo == null) return "application/octet-stream";

            string fileNameExtension = fileInfo.Extension.ToLower();
            Dictionary<string, string> contentDict = new Dictionary<string, string>();

            contentDict.Add(".doc", "application/msword");
            contentDict.Add(".exe", "application/octet-stream");
            contentDict.Add(".pdf", "application/pdf");
            contentDict.Add(".xls", "application/vnd.ms-excel");
            contentDict.Add(".xlsx", "application/vnd.ms-excel");
            contentDict.Add(".ppt", "application/vnd.ms-powerpoint");
            contentDict.Add(".lzh", "application/x-lha");
            contentDict.Add(".tar", "application/x-tar");
            contentDict.Add(".tgz", "application/x-tar");
            contentDict.Add(".taz", "application/x-tar");
            contentDict.Add(".zip", "application/zip");
            contentDict.Add(".mp3", "audio/mp3");
            contentDict.Add(".mp4", "audio/mp4");
            contentDict.Add(".css", "text/css");
            contentDict.Add(".csv", "text/csv");
            contentDict.Add(".html", "text/html");
            contentDict.Add(".text", "text/plain");
            contentDict.Add(".txt", "text/plain");
            contentDict.Add(".js", "text/javascript");
            contentDict.Add(".bmp", "image/bmp");
            contentDict.Add(".gif", "image/gif");
            contentDict.Add(".jpeg", "image/jpeg");
            contentDict.Add(".jpg", "image/jpeg");
            contentDict.Add(".png", "image/png");
            contentDict.Add(".avi", "video/avi");
            contentDict.Add(".mpg", "video/mpg");
            contentDict.Add(".mpeg", "video/mpg");

            if (string.IsNullOrEmpty(fileNameExtension))
            {
                return "application/octet-stream";
            }

            // コンテンツタイプリストからコンテンツタイプを取得する。
            if (contentDict.ContainsKey(fileNameExtension))
            {
                return contentDict[fileNameExtension];
            }

            return "application/octet-stream";
        }

        public static async Task<byte[]> DownloadFile(string path)
        {
            try
            {
                byte[] imageArray = new byte[] { };
                if (!string.IsNullOrEmpty(path))
                {
                    if (System.IO.File.Exists(path))
                    {
                        imageArray = await Task.Factory.StartNew(() => System.IO.File.ReadAllBytes(path));
                    }
                }

                return imageArray;
            }
            catch (Exception ex)
            {
                Debug.WriteLine(ex.Message);
                return null;
            }
        }

        public static string GetGuid()
        {
            return Guid.NewGuid().ToString("N");
        }

        public static decimal decimalParser(this object obj)
        {
            if (obj == null)
            {
                return 0;
            }

            decimal result = 0;
            decimal.TryParse(obj?.ToString(), out result);
            return result;
        }

        private struct NETRESOURCE
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

        [DllImport("mpr.dll")]
        private static extern int WNetAddConnection2(NETRESOURCE lpNetResource, string lpPassword, string lpUsername, int dwFlags);

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
    }
}

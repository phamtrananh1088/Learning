using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace WinMacOs.Utility.Extensions
{
    public static class FileExtensions
    {
        public static Task<byte[]> ReadAllBytesAsync (string path)
        {
            return Task.Factory.StartNew(() =>
            {
                return File.ReadAllBytes(path);
            });
        }
    }
}
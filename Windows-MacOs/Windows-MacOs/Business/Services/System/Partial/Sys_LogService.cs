using System;
using System.Linq;
using System.Collections.Concurrent;
using System.Threading;
using System.Threading.Tasks;
using System.Data;
using System.Data.SqlClient;
using System.IO;
using System.Text;
using WinMacOs.Utility.Extensions;
using WinMacOs.DataRepository.DBManager;
using System.Web;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Services
{
    /// <summary>
    /// クエリでログを非同期登録
    /// </summary>
    public static class Logger
    {
        private static string _loggerPath = AppSetting.DownLoadPath + "Logger\\Queue\\";
        static Logger()
        {
            Task.Run(() => { Start(); });
        }

        private static void Start()
        {

        }

        public static void Error(string message = null)
        {
           
        }


        private static void WriteText(string message)
        {
            try
            {
                WriteFile(_loggerPath + "WriteError\\", $"{DateTime.Now:yyyyMMdd}.txt", message + "\r\n");
            }
            catch (Exception ex)
            {
                Console.WriteLine($"ログファイル作成エラー：{ex.Message}");
            }
        }

        /// <summary>
        /// ファイル作成
        /// </summary>
        /// <param name="path">パース</param>
        /// <param name="fileName">ファイル名所</param>
        /// <param name="content">内容</param>
        public static void WriteFile(string path, string fileName, string content, bool appendToLast = false)
        {
            path = path.ReplacePath();
            fileName = fileName.ReplacePath();
            if (!Directory.Exists(path))
                Directory.CreateDirectory(path);

            using (FileStream stream = File.Open(path + fileName, FileMode.OpenOrCreate, FileAccess.Write))
            {
                byte[] by = Encoding.Default.GetBytes(content);
                stream.SetLength(0);

                stream.Write(by, 0, by.Length);
            }
        }

        /// <summary>
        /// フォルダ削除
        /// </summary>
        /// <param name="dir"></param>  
        /// <returns></returns>
        public static void DeleteFolder(string dir)
        {
            dir = dir.ReplacePath();
            if (Directory.Exists(dir))
            {
                foreach (string d in Directory.GetFileSystemEntries(dir))
                {
                    if (File.Exists(d))
                    {
                        File.Delete(d);
                    }
                    else
                    {
                        DeleteFolder(d);
                    }
                }
                Directory.Delete(dir, true);
            }
        }
    }
}

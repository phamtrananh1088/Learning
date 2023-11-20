using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Models;

namespace WinMacOs.Business.IServices.Reafs_W
{
    public partial interface ICommonService
    {
        Task<byte[]> GetImage(string path, string type);
        Task<byte[]> GetImage2(string path);// ハノイ側修正2022/10/21　課題管理表№186：設計書「修正履歴」シートの「2022/10/20仕様変更分」「工事区分のハテナボックス押下時の処理追記」
        Task<byte[]> DownloadFile(string path);
        Task<Stream> getImageTempF093(int 添付NO);
        Task<WebResponseContent> insertF093(JObject data);
        Task<WebResponseContent> insertF090(JObject data);
        WebResponseContent saveFile(string SAVE_DIR, string SAVE_FILE_NAME, byte[] FILE_DATA);
        WebResponseContent saveFileByF093(string SAVE_DIR, string SAVE_FILE_NAME, int 添付NO);
        Task<WebResponseContent> insertF090WithSaveFile(JObject data, string 添付NO);
        Task<WebResponseContent> insertF090WithSaveFile(JObject data, byte[] FILE_DATA);
        WebResponseContent getFileImage(string PATH);
        WebResponseContent deleteF090(JObject data);
        Task<WebResponseContent> GetControlActiveButton(string menuId, string subMenuId);
    }
}

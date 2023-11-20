using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Models;

namespace WinMacOs.Business.IServices.Reafs_T
{
    public partial interface ICommonService
    {
        Task<WebResponseContent> GetMsg(string key);

        Task<WebResponseContent> GetControlActiveButton(string menuId, string subMenuId);
        Task<byte[]> DownloadFile(string path);

        Task<WebResponseContent> GetCurrentMenuActionList(string ユーザー種別);
        Task<WebResponseContent> GetMenuNameList();
        Task<WebResponseContent> Fnc_InsertF090(JArray data);
        Task<WebResponseContent> GetAllMenuActionList();
    }
}

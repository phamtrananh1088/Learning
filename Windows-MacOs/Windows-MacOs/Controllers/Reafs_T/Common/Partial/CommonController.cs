using _03.Reafs_W_NetCore.Business.IServices.Reafs_T;
using Newtonsoft.Json;
using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using WinMacOs.ActionFilter;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Models;
using WinMacOs.Models.Enums;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.PInvoke.CommonModels;
using WinMacOs.Utility.PInvoke.Mpr;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Controllers.Reafs_T
{
    [Route("api/Reafs_T/Common")]
    public partial class CommonController
    {
        public CommonController(
               ICommonService commonService
              )
          : base(commonService)
        {
        }

        [HttpGet, HttpPost, Route("getMsg"), AllowAnonymous]
        public async Task<ActionResult> GetMsg(CommonInfo commonInfo)
        {
            return Json(await Service.GetMsg(commonInfo.メッセージコード));
        }

        [HttpPost, HttpGet, Route("getControlActiveButton"), AllowAnonymous]
        public async Task<ActionResult> GetControlActiveButton(MenuInfo menuInfo)
        {
            return Json(await Service.GetControlActiveButton(menuInfo.MenuId, menuInfo.SubMenuId));
        }

        [HttpPost, Route("DownloadFile"), AllowAnonymous]
        public async Task<ActionResult> DownloadFile(FileDownloadEntity FileDownloadInfo)
        {
            return File(await Service.DownloadFile(FileDownloadInfo.FilePath), FileDownloadInfo.FileName.GetContentType());
        }

        // サイドメニューの取得
        [HttpGet, HttpPost, Route("getTreeMenu"), AllowAnonymous]
        public async Task<ActionResult> GetTreeMenu()
        {
            return Json(await Service.GetCurrentMenuActionList("0"));
        }
        [HttpGet, HttpPost, Route("getParentTreeMenu"), AllowAnonymous]
        public async Task<ActionResult> GetParentTreeMenu()
        {
            return Json(await Service.GetCurrentMenuActionList("1"));
        }
        [HttpGet, HttpPost, Route("getChildTreeMenu"), AllowAnonymous]
        public async Task<ActionResult> GetChildTreeMenu()
        {
            return Json(await Service.GetCurrentMenuActionList("2"));
        }

        [HttpGet, HttpPost, Route("getMenuName"), AllowAnonymous]
        public async Task<ActionResult> GetMenuName()
        {
            return Json(await Service.GetMenuNameList());
        }

        [HttpPost, Route("fnc_InsertF090"), AllowAnonymous]
        public async Task<ActionResult> Fnc_InsertF090(JArray data)
        {
            return Json(await Service.Fnc_InsertF090(data));
        }

        [HttpGet, HttpPost, Route("getAllMenu"), AllowAnonymous]
        public async Task<ActionResult> GetAllMenu()
        {
            return Json(await Service.GetAllMenuActionList());
        }
    }
}

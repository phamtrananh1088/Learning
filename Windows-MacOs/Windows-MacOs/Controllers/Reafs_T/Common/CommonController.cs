using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Web.Services.Description;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.SystemModels;

namespace WinMacOs.Controllers.Reafs_T
{
    //[Authorize]
    [RoutePrefix("api/Reafs_T/Common")]
    public partial class CommonController : BaseVueController<IUnitOfWork>
    {
        public CommonController(IUnitOfWork repository)
             : base(repository)
        {
            
        }

        [HttpGet, HttpPost, Route("getAllMenu"), AllowAnonymous]
        public async Task<ActionResult> GetAllMenu()
        {
            return Json(await GetAllMenuActionList());
        }

        [HttpGet, HttpPost, Route("getMsg"), AllowAnonymous]
        public async Task<ActionResult> GetMsg(CommonInfo commonInfo)
        {
            return Json(await GetMsg(commonInfo.メッセージコード));
        }

        [HttpPost, HttpGet, Route("getControlActiveButton"), AllowAnonymous]
        public async Task<ActionResult> GetControlActiveButton(MenuInfo menuInfo)
        {
            return Json(await GetControlActiveButton(menuInfo.MenuId, menuInfo.SubMenuId));
        }

        [HttpPost, Route("DownloadFile"), AllowAnonymous]
        public async Task<ActionResult> DownloadFile(FileDownloadEntity FileDownloadInfo)
        {
            return File(await DownloadFile(FileDownloadInfo.FilePath), FileDownloadInfo.FileName.GetContentType());
        }

        // サイドメニューの取得
        [HttpGet, HttpPost, Route("getTreeMenu"), AllowAnonymous]
        public async Task<ActionResult> GetTreeMenu()
        {
            return Json(await GetCurrentMenuActionList("0"));
        }
        [HttpGet, HttpPost, Route("getParentTreeMenu"), AllowAnonymous]
        public async Task<ActionResult> GetParentTreeMenu()
        {
            return Json(await GetCurrentMenuActionList("1"));
        }
        [HttpGet, HttpPost, Route("getChildTreeMenu"), AllowAnonymous]
        public async Task<ActionResult> GetChildTreeMenu()
        {
            return Json(await GetCurrentMenuActionList("2"));
        }

        [HttpGet, HttpPost, Route("getMenuName"), AllowAnonymous]
        public async Task<ActionResult> GetMenuName()
        {
            return Json(await GetMenuNameList());
        }

        [HttpPost, Route("fnc_InsertF090"), AllowAnonymous]
        public async Task<ActionResult> InsertF090(JArray data)
        {
            return Json(await Fnc_InsertF090(data));
        }
    }
}

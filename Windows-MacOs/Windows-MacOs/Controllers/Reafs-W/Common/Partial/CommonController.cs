using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web.Mvc;
using Newtonsoft.Json.Linq;
using WinMacOs.Business.IServices.Reafs_W;
using WinMacOs.Models;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.SystemModels;

namespace WinMacOs.Controllers.Reafs_W
{
    [RoutePrefix("api/Reafs_W/Common")]
    public partial class CommonController
    {
        public CommonController(
          ICommonService commonService
         )
        : base(commonService)
        {
        }

        [HttpGet, Route("GetImage"), AllowAnonymous]
        public async Task<ActionResult> GetImage(string path, string type)
        {
            return File(await Service.GetImage(path, type), path.GetContentType());
        }

        // ハノイ側修正2022/10/21　課題管理表№186：設計書「修正履歴」シートの「2022/10/20仕様変更分」「工事区分のハテナボックス押下時の処理追記」START
        [HttpGet, Route("GetImage2"), AllowAnonymous]
        public async Task<ActionResult> GetImage2(string path)
        {
            return File(await Service.GetImage2(path), path.GetContentType());
        }
        // ハノイ側修正2022/10/21　課題管理表№186：設計書「修正履歴」シートの「2022/10/20仕様変更分」「工事区分のハテナボックス押下時の処理追記」END

        [HttpPost, Route("DownloadFile"), AllowAnonymous]
        public async Task<ActionResult> DownloadFile(FileDownloadEntity FileDownloadInfo)
        {
            return File(await Service.DownloadFile(FileDownloadInfo.FilePath), FileDownloadInfo.FileName.GetContentType());
        }

        [HttpPost, HttpGet, Route("getImageTempF093"), AllowAnonymous]
        public async Task<ActionResult> getImageTempF093(int 添付NO)
        {
            return File(await Service.getImageTempF093(添付NO), "image/jpeg");
        }

        [HttpPost, Route("exInsertF093"), AllowAnonymous]
        public async Task<ActionResult> exInsertF093(JObject data)
        {
            return Json(await Service.insertF093(data));
        }

        [HttpPost, Route("exInsertF090"), AllowAnonymous]
        public async Task<ActionResult> exInsertF090(JObject data)
        {
            return Json(await Service.insertF090(data));
        }

        [HttpPost, Route("exSaveFile"), AllowAnonymous]
        public ActionResult exSaveFile(string SAVE_DIR, string SAVE_FILE_NAME, byte[] FILE_DATA)
        {
            return Json(Service.saveFile(SAVE_DIR, SAVE_FILE_NAME, FILE_DATA));
        }

        [HttpPost, Route("exSaveFileByF093"), AllowAnonymous]
        public ActionResult exSaveFileByF093(string SAVE_DIR, string SAVE_FILE_NAME, int 添付NO)
        {
            return Json(Service.saveFileByF093(SAVE_DIR, SAVE_FILE_NAME, 添付NO));
        }

        [HttpPost, Route("exInsertF090WithSaveFile"), AllowAnonymous]
        public async Task<ActionResult> exInsertF090WithSaveFile(JObject data)
        {
            var 添付NO = data["添付NO"]?.ToString();
            var FILE_DATA = data["FILE_DATA"]?.ToObject<byte[]>();
            if (!string.IsNullOrEmpty(添付NO))
            {
                return Json(await Service.insertF090WithSaveFile(data, 添付NO));
            }
            else
            {
                return Json(await Service.insertF090WithSaveFile(data, FILE_DATA));
            }
            
        }

        [HttpGet, Route("exGetFileImage"), AllowAnonymous]
        public ActionResult exGetFileImage(string PATH)
        {
            return Json( Service.getFileImage(PATH));
        }

        [HttpPost, Route("exDeleteF090"), AllowAnonymous]
        public ActionResult exDeleteF090(JObject data)
        {
            return Json(Service.deleteF090(data));
        }

        [HttpPost, Route("getControlActiveButton"), AllowAnonymous]
        public async Task<ActionResult> GetControlActiveButton(MenuInfo menuInfo)
        {
            return Json(await Service.GetControlActiveButton(menuInfo.MenuId, menuInfo.SubMenuId));
        }

        [HttpPost, Route("getControlActiveButton2"), AllowAnonymous]
        public async Task<ActionResult> GetControlActiveButton2(JObject menuInfo)
        {
            return Json(await  (new WebResponseContent()).OKAsync("OK", menuInfo));
        }

        [HttpPost, Route("getControlActiveButton3"), AllowAnonymous]
        public async Task<ActionResult> GetControlActiveButton3(JArray menuInfo)
        {
            return Json(await (new WebResponseContent()).OKAsync("OK", menuInfo));
        }
    }
}

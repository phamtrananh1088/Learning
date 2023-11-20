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
using WinMacOs.Business.IServices.Reafs_T;
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
    [RoutePrefix("api/Reafs_T/User")]
    public partial class LoginController
    {

        public LoginController(
               ILoginService loginService
              )
          : base(loginService)
        {
        }

        #region ログイン
        [HttpPost, Route("login"), AllowAnonymous]
        public async Task<ActionResult> Login(LoginInfo loginInfo)
        {
            return Json(await Service.Login(loginInfo));
        }
        #endregion

        #region Token更新
        [HttpPost, Route("replaceToken")]
        public async Task<ActionResult> ReplaceToken()
        {
            return Json(await Service.ReplaceToken());
        }
        #endregion

        #region 戻り値が「-1」を5回繰り返した場合はパスワードを失効扱いにする
        [HttpPost, Route("updM015"), AllowAnonymous]
        public async Task<ActionResult> UpdM015(LoginInfo loginInfo)
        {
            return Json(await Service.UpdM015(loginInfo));
        }
        #endregion
    }
}

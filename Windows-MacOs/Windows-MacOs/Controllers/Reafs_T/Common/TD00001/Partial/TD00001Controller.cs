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
    [RoutePrefix("api/Reafs_T/TD00001")]
    public partial class TD00001Controller
    {
        public TD00001Controller(
               ITD00001Service menuService
              )
          : base(menuService)
        {
        }

        [HttpPost, Route("getNews")]
        public ActionResult GetNews()
        {
            return Json(Service.GetNewsList());
        }

        [HttpPost, Route("getCnt")]
        public async Task<ActionResult> GetCnt(LoginInfo LoginInfo)
        {
            return Json(await Service.GetCnt(LoginInfo));
        }
    }
}

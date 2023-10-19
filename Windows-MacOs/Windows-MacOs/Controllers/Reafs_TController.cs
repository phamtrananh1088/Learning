﻿using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;

namespace WinMacOs.Controllers
{
    //[Authorize]
    public class Reafs_TController : BaseVueController<IUnitOfWork>
    {
        public Reafs_TController(IUnitOfWork repository)
             : base(repository)
        {
            
        }
  
        public ActionResult Index(String path, int? id)
        {
            return File("~/Reafs_T/index.html", "text/html");
        }

        //[HttpGet, HttpPost, Route("getAllMenu"), AllowAnonymous]
        //public async Task<ActionResult> GetAllMenu()
        //{
        //    WebResponseContent responseContent = new WebResponseContent();
        //    try
        //    {

        //        string sql =
        //                    $"   SELECT                                          " +
        //                    $"          S002.メニューID     AS MenuId            " +
        //                    $"         ,S002.メニュー名     AS MenuName          " +
        //                    $"         ,S003.サブメニューID AS SubMenuId         " +
        //                    $"         ,S003.サブメニュー名 AS SubMenuName       " +
        //                    $"         ,S003.フォームID     AS Path              " +
        //                    $"     FROM ST002_メニューマスタ     S002            " +
        //                    $"LEFT JOIN ST003_サブメニューマスタ S003            " +
        //                    $"       ON S002.メニューID = S003.メニューID        " +
        //                    $"    WHERE S003.削除区分 = 0                        " +
        //                    $"    ORDER BY S003.表示順                         ";

        //        responseContent.Data = await repository.DapperContext.QueryListAsync<object>(sql, null);

        //        return Json(responseContent);
        //    }
        //    catch (Exception ex)
        //    {
        //        NLogger.Default.ErrorLog(ex);
        //        return Json(responseContent);
        //    }

        //}
    }
}

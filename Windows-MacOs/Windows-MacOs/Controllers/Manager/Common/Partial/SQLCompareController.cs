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
using WinMacOs.Business.IServices.Manager;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Models;
using WinMacOs.Models.Enums;
using WinMacOs.Models.Manager.SQLCompare;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.PInvoke.CommonModels;
using WinMacOs.Utility.PInvoke.Mpr;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Controllers.Manager
 {
    [RoutePrefix("manager/SQLCompare")]
    public partial class SQLCompareController
    {
        public SQLCompareController(
               ISQLCompareService loginService
              )
          : base(loginService)
        {
        }

        [Route("")]
        [Route("index")]
        public async Task<ActionResult> Index()
        {
            ViewBag.Title = "SQL Compare";
            SQLCompareModel model = await Service.GetSQLCompareModel();
            return View(model);
        }


        /// <summary>
        /// GetSQLScript.
        /// </summary>
        /// <param name="tableName">tableName.</param>
        /// <returns>ActionResult.</returns>
        [Route("GetSQLScript")]
        public async Task<ActionResult> GetSQLScript(string schemaName, string tableName)
        {
            SQLCompareModel model = await Service.GetSQLScript(schemaName, tableName);
            return PartialView("EditorTemplates//SQLScriptView", model);
        }

        /// <summary>
        /// GetSQLCreateObject.
        /// </summary>
        /// <param name="type">type.</param>
        /// <param name="schemaName">schemaName.</param>
        /// <param name="name">name.</param>
        /// <returns>ActionResult.</returns>
        [Route("GetSQLCreateObject")]
        [HttpGet]
        public async Task<ActionResult> GetSQLCreateObject(string type, string schemaName, string name)
        {
            string model = await Service.GetSQLCreateObject(type, schemaName, name);
           
            return File(Encoding.UTF8.GetBytes(model), "text/plain", $"[{schemaName}].[{name}].sql");
        }

    }
}

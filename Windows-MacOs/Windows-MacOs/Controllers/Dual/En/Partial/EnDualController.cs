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
using WinMacOs.Business.IServices.Dual;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Utilities;
using WinMacOs.Models;
using WinMacOs.Models.Dual.En;
using WinMacOs.Models.Enums;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.PInvoke.CommonModels;
using WinMacOs.Utility.PInvoke.Mpr;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Controllers.Dual
{
    [RoutePrefix("dual/en")]
    public partial class EnDualController
    {
        protected IEnDualService myService;
        public EnDualController(
               IEnDualService service
              )
          : base(service)
        {
            myService = service;
        }

        [Route("")]
        [Route("index")]
        [HttpGet]
        public new async Task<ActionResult> Index()
        {
            EnDualModel model = await myService.GetData();
            return PartialView("~/Views/Dual/EditorTemplates/En.cshtml", model);
        }
    }
}

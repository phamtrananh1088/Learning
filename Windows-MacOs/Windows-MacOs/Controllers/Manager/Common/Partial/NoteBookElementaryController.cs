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
using WinMacOs.Models.Manager.Canvas;
using WinMacOs.Models.Manager.NoteBookElementary;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.PInvoke.CommonModels;
using WinMacOs.Utility.PInvoke.Mpr;
using WinMacOs.Utility.SystemModels;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Controllers.Manager
 {
    [RoutePrefix("manager/NoteBookElementary")]
    public partial class NoteBookElementaryController
    {
        public NoteBookElementaryController(
               INoteBookElementaryService service
              )
          : base(service)
        {
        }

        [Route("")]
        [Route("index")]
        public async Task<ActionResult> Index()
        {
            NoteBookElementaryModel model = await Service.GetNoteBookElementaryModel();
            return View(model);
        }

        [HttpPost, Route("DrawLine"), AllowAnonymous]
        public async Task<ActionResult> DrawLine()
        {
            return Json(await Service.DrawLine());
        }
    }
}

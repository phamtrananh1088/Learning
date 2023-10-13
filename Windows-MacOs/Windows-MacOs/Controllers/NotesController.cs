using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using WinMacOs.Models;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Controllers
{
    //[Authorize]
    public class NotesController : BaseController<NoteModel>
    {
        public ActionResult Index()
        {
            NoteModel note = new NoteModel() { IsCreateNew = true };
            return View("Edit", note);
        }

        /// <summary>
        /// InsertNote.
        /// </summary>
        /// <param name="NoteModel">NoteModel.</param>
        /// <returns>ActionResult.</returns>
        [HttpPost]
        public ActionResult InsertNote(NoteModel model)
        {
            try
            {
                if (model.IsCreateNew)
                {
                    model.CreateDate = DateTime.Now;
                    model.FileName = model.CreateDate.ToString("yyyy-MM-dd HH-mm-ss-fffffff") + ".txt";
                }
                string orgFileName = model.FileName;
                var path = AppSetting.NotesPath;
                DirectoryInfo di = Directory.CreateDirectory(HttpContext.Server.MapPath(path));
                System.IO.File.WriteAllText(HttpContext.Server.MapPath(path) + orgFileName, System.Uri.UnescapeDataString(model.Data));
                return Json(new JsonMessage { success = true, message = "", data = Newtonsoft.Json.JsonConvert.SerializeObject(model) });
            }
            catch (Exception ex)
            {
                NLogger.Default.ErrorLog(ex);
                return Json(new JsonMessage { success = false, message = "" });
            }
        }

        /// <summary>
        /// InsertNote.
        /// </summary>
        /// <param name="NoteModel">NoteModel.</param>
        /// <returns>ActionResult.</returns>
        [HttpPost]
        public ActionResult DeleteNote(NoteModel model)
        {
            try
            {
                string orgFileName = model.FileName;
                
                var path = AppSetting.NotesPath;
                System.IO.File.Delete(HttpContext.Server.MapPath(path) + orgFileName);
                return Json(new JsonMessage { success = true, message = "" });
            }
            catch (Exception ex)
            {
                NLogger.Default.ErrorLog(ex);
                return Json(new JsonMessage { success = false, message = "" });
            }
        }
    }
}

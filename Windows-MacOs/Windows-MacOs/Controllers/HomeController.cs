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
    public class HomeController : BaseController<UploadModel>
    {
        public ActionResult Index()
        {
            return View();
        }

        /// <summary>
        /// UploadAttachedFile.
        /// </summary>
        /// <param name="base64File">sBase64Data.</param>
        /// <param name="orgFileName">orgFileName</param>
        /// <returns>ActionResult.</returns>
        public ActionResult UploadAttachedFile(UploadModel formData)
        {
            try
            {
                byte[] bytes = new byte[(int)formData.File.InputStream.Length];
                using (var re = new BinaryReader(formData.File.InputStream)) {
                    re.Read(bytes, 0, (int)formData.File.InputStream.Length);
                }
                string orgFileName = formData.File.FileName;
                //Random rnd = new Random();
                //string myRandomNo = rnd.Next(0, 100000).ToString("D6");
                //var fileName = decimal.Parse(DateTime.Now.ToString("yyyyMMddHHmmss") + myRandomNo);
                var path = AppSetting.FileUploadPath;
                DirectoryInfo di = Directory.CreateDirectory(HttpContext.Server.MapPath(path));
                using (var fs = new FileStream(HttpContext.Server.MapPath(path) + orgFileName, FileMode.Create, FileAccess.Write))
                {
                    fs.Write(bytes, 0, bytes.Length);
                }
                System.Threading.Thread.Sleep(1000);
                return  Json(new { message="Upload successed." });
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
    }
}

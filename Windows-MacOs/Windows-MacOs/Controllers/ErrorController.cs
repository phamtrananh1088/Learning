using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Web.Routing;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;

namespace WinMacOs.Controllers
{
    //[Authorize]
    public class ErrorController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }

        public ActionResult NotFound()
        {
            string path = Request.Url.Query;
            string spa = path.Contains("/Reafs_W/") ? "Reafs_W" : path.Contains("/Reafs_R_Web/") ? "Reafs_R_Web": path.Contains("/Reafs_T/") ? "Reafs_T" : "Reafs_T";
            return File(string.Format("~/{0}/index.html", spa), "text/html");
        }
    }
}

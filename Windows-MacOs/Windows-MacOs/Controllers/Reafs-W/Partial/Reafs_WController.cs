using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;

namespace WinMacOs.Controllers.Reafs_W
{
    //[Authorize]
    public partial class Reafs_WController
    {
        public ActionResult Index(String path)
        {
            return File("~/Reafs_W/index.html", "text/html");
        }
    }
}

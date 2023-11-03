using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Web.Services.Description;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;

namespace WinMacOs.Controllers.Reafs_T
{
    //[Authorize]
    public partial class Reafs_TController : BaseVueController<IUnitOfWork>
    {
        public Reafs_TController(IUnitOfWork repository)
             : base(repository)
        {
            
        }

        public ActionResult Index()
        {
            return File("~/Reafs_T/index.html", "text/html");
        }
    }
}

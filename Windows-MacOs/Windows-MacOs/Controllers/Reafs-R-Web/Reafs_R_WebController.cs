using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using WinMacOs.Business.IServices.Reafs_R_Web;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;

namespace WinMacOs.Controllers.Reafs_R_Web
{
    //[Authorize]
    public partial class Reafs_R_WebController : BaseVueController<ICommonService>
    {
        public Reafs_R_WebController()
             : base()
        {

        }
    }
}

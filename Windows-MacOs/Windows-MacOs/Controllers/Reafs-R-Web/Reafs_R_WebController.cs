using _03.Reafs_W_NetCore.Business.IServices.Reafs_R_Web;
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

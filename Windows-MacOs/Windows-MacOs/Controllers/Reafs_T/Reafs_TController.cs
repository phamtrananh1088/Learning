using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Web.Services.Description;
using WinMacOs.Business.IServices.Reafs_T;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;

namespace WinMacOs.Controllers.Reafs_T
{
    //[Authorize]
    public partial class Reafs_TController : BaseVueController<ICommonService>
    {
        public Reafs_TController()
             : base()
        {
            
        }
    }
}

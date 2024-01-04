using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;
using System.Web.Services.Description;
using WinMacOs.Business.Business.Filters;
using WinMacOs.Business.IServices.Reafs_T;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.SystemModels;

namespace WinMacOs.Controllers.Reafs_T
{
    [JWTAuthorize()]
    public partial class TD00001Controller : BaseVueController<ITD00001Service>
    {
        public TD00001Controller() : base()
        {
        }
    }
}

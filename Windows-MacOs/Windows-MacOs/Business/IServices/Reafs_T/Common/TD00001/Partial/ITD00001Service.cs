using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Models;
using WinMacOs.Utility.DomainModels;

namespace WinMacOs.Business.IServices.Reafs_T
{
    public partial interface ITD00001Service
    {
        WebResponseContent GetNewsList();

        Task<WebResponseContent> GetCnt(LoginInfo LoginInfo);
    }
}

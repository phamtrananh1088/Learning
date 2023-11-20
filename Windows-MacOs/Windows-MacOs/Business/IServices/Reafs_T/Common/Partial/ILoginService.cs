using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Models;
using WinMacOs.Utility.DomainModels;

namespace WinMacOs.Business.IServices.Reafs_T
{
    public partial interface ILoginService
    {
        Task<WebResponseContent> Login(LoginInfo loginInfo);
        Task<WebResponseContent> ReplaceToken();
        //WebResponseContent GetDepartment(string userName);
        Task<WebResponseContent> UpdM015(LoginInfo loginInfo);
    }
}

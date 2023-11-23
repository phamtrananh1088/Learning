using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Business.Services;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.Models;
using WinMacOs.Models.Enums;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Services.Manager
{
    public partial class CanvasService
    {
        /// <summary>
        /// 担当者ログイン処理
        /// </summary>
        /// <param name="loginInfo"></param>
        /// <returns></returns>
        public async Task<WebResponseContent> DrawLine()
        {
            string msg = string.Empty;
            WebResponseContent responseContent = new WebResponseContent();

            try
            {
                return await responseContent.OKAsync("OK");
            }
            catch (Exception ex)
            {
                msg = ex.Message + ex.StackTrace;
                Logger.Error(msg);
                return responseContent.Error(ResponseType.ServerError, msg);
            }
        }
    }
}

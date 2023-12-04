using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Business.Services;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.Models;
using WinMacOs.Models.Enums;
using WinMacOs.Models.Manager.Canvas;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Services.Manager
{
    public partial class CanvasService
    {

        public Task<CanvasModel> GetCanvasModel()
        {
            string json = "{\"CanvasMenus\":[{\"MenuNo\":\"1\",\"MenuName\":\"File\",\"ShortCut\":null,\"ParentMenuNo\":null,\"SubMenus\":[{\"MenuNo\":\"100\",\"MenuName\":\"New\",\"ShortCut\":[\"Ctrl\",\"N\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"105\",\"MenuName\":\"New from Template...\",\"ShortCut\":[\"Ctrl\",\"Alt\",\"N\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"110\",\"MenuName\":\"Open...\",\"ShortCut\":[\"Ctrl\",\"O\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"115\",\"MenuName\":\"Open Recent\",\"ShortCut\":[\"Ctrl\",\"O\"],\"ParentMenuNo\":\"1\",\"SubMenus\":[{\"MenuNo\":null,\"MenuName\":null,\"ShortCut\":null,\"ParentMenuNo\":null,\"SubMenus\":null,\"MenuType\":\"\"}],\"MenuType\":\"\"},{\"MenuNo\":\"119\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"120\",\"MenuName\":\"Revert\",\"ShortCut\":[\"Ctrl\",\"O\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"125\",\"MenuName\":\"Save\",\"ShortCut\":[\"Ctrl\",\"S\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"130\",\"MenuName\":\"Save As...\",\"ShortCut\":[\"Shift\",\"Ctrl\",\"S\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"135\",\"MenuName\":\"Save a Copy...\",\"ShortCut\":[\"Shift\",\"Ctrl\",\"Alt\",\"S\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"140\",\"MenuName\":\"Save Template...\",\"ShortCut\":null,\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"144\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"145\",\"MenuName\":\"Import...\",\"ShortCut\":[\"Ctrl\",\"I\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"150\",\"MenuName\":\"Import Web Image...\",\"ShortCut\":null,\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"155\",\"MenuName\":\"Export...\",\"ShortCut\":[\"Shift\",\"Ctrl\",\"E\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"159\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"160\",\"MenuName\":\"Print...\",\"ShortCut\":[\"Ctrl\",\"P\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"164\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"165\",\"MenuName\":\"Clean Up Document\",\"ShortCut\":null,\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"170\",\"MenuName\":\"Document Resources\",\"ShortCut\":null,\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"174\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"175\",\"MenuName\":\"Document Properties...\",\"ShortCut\":[\"Shift\",\"Ctrl\",\"D\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"179\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"180\",\"MenuName\":\"Close\",\"ShortCut\":[\"Ctrl\",\"W\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"185\",\"MenuName\":\"Quit\",\"ShortCut\":[\"Ctrl\",\"Q\"],\"ParentMenuNo\":\"1\",\"SubMenus\":null,\"MenuType\":\"\"}],\"MenuType\":\"\"},{\"MenuNo\":\"2\",\"MenuName\":\"Edit\",\"ShortCut\":null,\"ParentMenuNo\":null,\"SubMenus\":[{\"MenuNo\":\"200\",\"MenuName\":\"Undo\",\"ShortCut\":[\"Ctrl\",\"Z\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"205\",\"MenuName\":\"Redo\",\"ShortCut\":[\"Ctrl\",\"Y\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"210\",\"MenuName\":\"Undo History...\",\"ShortCut\":[\"Shift\",\"Ctrl\",\"H\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"214\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"215\",\"MenuName\":\"Cut\",\"ShortCut\":[\"Ctrl\",\"X\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"220\",\"MenuName\":\"Copy\",\"ShortCut\":[\"Ctrl\",\"C\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"225\",\"MenuName\":\"Paste\",\"ShortCut\":[\"Ctrl\",\"V\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"230\",\"MenuName\":\"Paste...\",\"ShortCut\":null,\"ParentMenuNo\":\"2\",\"SubMenus\":[{\"MenuNo\":\"230001\",\"MenuName\":\"In Place\",\"ShortCut\":[\"Ctrl\",\"Alt\",\"V\"],\"ParentMenuNo\":\"230\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"230005\",\"MenuName\":\"On Page\",\"ShortCut\":null,\"ParentMenuNo\":\"230\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"23010\",\"MenuName\":\"Style\",\"ShortCut\":[\"Shift\",\"Ctrl\",\"V\"],\"ParentMenuNo\":\"230\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"23015\",\"MenuName\":\"Size\",\"ShortCut\":null,\"ParentMenuNo\":\"230\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"23020\",\"MenuName\":\"Width\",\"ShortCut\":null,\"ParentMenuNo\":\"230\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"23025\",\"MenuName\":\"Height\",\"ShortCut\":null,\"ParentMenuNo\":\"230\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"23030\",\"MenuName\":\"Size Separately\",\"ShortCut\":null,\"ParentMenuNo\":\"230\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"23035\",\"MenuName\":\"Width Separately\",\"ShortCut\":null,\"ParentMenuNo\":\"230\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"23040\",\"MenuName\":\"Height Separately\",\"ShortCut\":null,\"ParentMenuNo\":\"230\",\"SubMenus\":null,\"MenuType\":\"\"}],\"MenuType\":\"\"},{\"MenuNo\":\"234\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"235\",\"MenuName\":\"Find/Replace...\",\"ShortCut\":[\"Ctrl\",\"F\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"239\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"240\",\"MenuName\":\"Duplicate\",\"ShortCut\":[\"Ctrl\",\"D\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"245\",\"MenuName\":\"Clone\",\"ShortCut\":null,\"ParentMenuNo\":\"2\",\"SubMenus\":[],\"MenuType\":\"\"},{\"MenuNo\":\"250\",\"MenuName\":\"Make a Bitmap Copy\",\"ShortCut\":[\"Alt\",\"B\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"254\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"255\",\"MenuName\":\"Delete\",\"ShortCut\":[\"Delete\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"259\",\"MenuName\":\"\",\"ShortCut\":null,\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"Line\"},{\"MenuNo\":\"260\",\"MenuName\":\"Select All\",\"ShortCut\":[\"Ctrl\",\"A\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"},{\"MenuNo\":\"265\",\"MenuName\":\"Select All\",\"ShortCut\":[\"Ctrl\",\"A\"],\"ParentMenuNo\":\"2\",\"SubMenus\":null,\"MenuType\":\"\"}],\"MenuType\":\"\"}]}";
            return Task.Factory.StartNew(() => System.Text.Json.JsonSerializer.Deserialize<CanvasModel>(json));
        }

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

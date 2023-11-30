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
            var m = new CanvasModel
            {
                Menus = new List<Models.Manager.Canvas.MenuModel>()
                {
                    new Models.Manager.Canvas.MenuModel()
                    {
                        Name = "File",
                        MenuItems = new List<MenuItemModel>()
                        {
                            new MenuItemModel()
                            {
                                MenuItemNo="100",
                                MenuItemName="New",
                                ShortCut = new string[] {"Ctrl", "N"},
                                
                            },
                            new MenuItemModel()
                            {
                                MenuItemNo="101",
                                MenuItemName="Open",
                                ShortCut = new string[] {"Ctrl", "O"},

                            },
                            new MenuItemModel()
                            {
                                MenuItemNo="102",
                                MenuItemName="Open",
                                ShortCut = new string[] {"Ctrl", "O"},

                            },
                        }
                    },
                    new Models.Manager.Canvas.MenuModel()
                    {
                        Name = "Edit",
                        MenuItems = new List<MenuItemModel>()
                        {
                            new MenuItemModel()
                            {
                                MenuItemNo="200",
                                MenuItemName="Copy",
                                ShortCut = new string[] {"Ctrl", "C"},

                            },
                            new MenuItemModel()
                            {
                                MenuItemNo="201",
                                MenuItemName="Paste",
                                ShortCut = new string[] {"Ctrl", "V"},

                            },
                            new MenuItemModel()
                            {
                                MenuItemNo="202",
                                MenuItemName="Paste...",
                                MenuItems=new List<MenuItemModel>()
                                {
                                    new MenuItemModel()
                                    {
                                        MenuItemNo = "2021",
                                        MenuItemName = "In Place",
                                        ShortCut = new string[] {"Ctrl", "Alt", "V"},
                                        ParentMenuItemNo="202",
                                    },
                                    new MenuItemModel()
                                    {
                                        MenuItemNo = "2022",
                                        MenuItemName = "On Page",
                                        ParentMenuItemNo="202",
                                    }
                                }

                            },
                        }
                    }
                }
            };
            string json = System.Text.Json.JsonSerializer.Serialize(m);
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

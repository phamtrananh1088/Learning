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
                Menus = new List<CanvasMenuModel>()
                {
                    new CanvasMenuModel()
                    {
                        MenuName = "File",
                        SubMenus = new List<CanvasMenuModel>()
                        {
                            new CanvasMenuModel()
                            {
                                MenuNo="100",
                                MenuName="New",
                                ShortCut = new string[] {"Ctrl", "N"},

                            },
                            new CanvasMenuModel
                            {
                                MenuNo = "105",
                                MenuName="New from Template...",
                                ShortCut = new string[] {"Ctrl", "Alt", "N"}
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="110",
                                MenuName="Open...",
                                ShortCut = new string[] {"Ctrl", "O"},

                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="115",
                                MenuName="Open Recent",
                                ShortCut = new string[] {"Ctrl", "O"},
                                SubMenus = new List<CanvasMenuModel> {
                                    new CanvasMenuModel() { }
                                }
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="119",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="120",
                                MenuName="Revert",
                                ShortCut = new string[] {"Ctrl", "O"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="125",
                                MenuName="Save",
                                ShortCut = new string[] {"Ctrl", "S"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="130",
                                MenuName="Save As...",
                                ShortCut = new string[] {"Ctrl", "Shift", "S"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="135",
                                MenuName="Save a Copy...",
                                ShortCut = new string[] { "Shift", "Ctrl", "Alt", "S" },
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="140",
                                MenuName="Save Template...",
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="144",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="145",
                                MenuName="Import...",
                                ShortCut = new string[] {"Ctrl", "I" },
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="150",
                                MenuName="Import Web Image...",
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="155",
                                MenuName="Export...",
                                ShortCut = new string[] {"Ctrl", "Shift", "E" },
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="159",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="160",
                                MenuName="Print...",
                                ShortCut = new string[] {"Ctrl", "P" },
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="164",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="165",
                                MenuName="Clean Up Document",
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="170",
                                MenuName="Document Resources",
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="174",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="175",
                                MenuName="Document Properties...",
                                ShortCut = new string[] {"Shift", "Ctrl", "D"}
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="179",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="180",
                                MenuName="Close",
                                ShortCut = new string[] { "Ctrl", "W"}
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="185",
                                MenuName="Quit",
                                ShortCut = new string[] {"Ctrl", "Q"}
                            }
                        }
                    },
                    new CanvasMenuModel()
                    {
                        MenuName = "Edit",
                        SubMenus = new List<CanvasMenuModel>()
                        {
                            new CanvasMenuModel()
                            {
                                MenuNo="200",
                                MenuName="Undo",
                                ShortCut = new string[] {"Ctrl", "Z"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="200",
                                MenuName="Redo",
                                ShortCut = new string[] {"Ctrl", "Y"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="200",
                                MenuName="Undo History...",
                                ShortCut = new string[] {"Shift", "Ctrl", "H"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="179",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="200",
                                MenuName="Cut",
                                ShortCut = new string[] {"Ctrl", "X"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="200",
                                MenuName="Copy",
                                ShortCut = new string[] {"Ctrl", "C"},

                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="201",
                                MenuName="Paste",
                                ShortCut = new string[] {"Ctrl", "V"},

                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="202",
                                MenuName="Paste...",
                                SubMenus=new List<CanvasMenuModel>()
                                {
                                    new CanvasMenuModel()
                                    {
                                        MenuNo = "2021",
                                        MenuName = "In Place",
                                        ShortCut = new string[] {"Ctrl", "Alt", "V"},
                                        ParentMenuNo="202",
                                    },
                                    new CanvasMenuModel()
                                    {
                                        MenuNo = "2022",
                                        MenuName = "On Page",
                                        ParentMenuNo="202",
                                    }
                                }
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="179",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="201",
                                MenuName="Find/Replace...",
                                ShortCut = new string[] {"Ctrl", "F"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="179",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="201",
                                MenuName="Duplicate",
                                ShortCut = new string[] {"Ctrl", "D"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="201",
                                MenuName="Clone",
                                SubMenus = new List<CanvasMenuModel>
                                {

                                }
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="201",
                                MenuName="Make a Bitmap Copy",
                                ShortCut = new string[] {"Alt", "B"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="179",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="201",
                                MenuName="Delete",
                                ShortCut = new string[] {"Delete"},
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="179",
                                MenuName="",
                                MenuType="Line"
                            },
                            new CanvasMenuModel()
                            {
                                MenuNo="201",
                                MenuName="Select All",
                                ShortCut = new string[] {"Ctrl", "A"},
                            }
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

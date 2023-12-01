﻿using Newtonsoft.Json.Linq;
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
                        MenuName = "File",
                        SubMenus = new List<Models.Manager.Canvas.MenuModel>()
                        {
                            new Models.Manager.Canvas.MenuModel()
                            {
                                MenuNo="100",
                                MenuName="New",
                                ShortCut = new string[] {"Ctrl", "N"},

                            },
                            new Models.Manager.Canvas.MenuModel()
                            {
                                MenuNo="101",
                                MenuName="Open",
                                ShortCut = new string[] {"Ctrl", "O"},

                            },
                            new Models.Manager.Canvas.MenuModel()
                            {
                                MenuNo="102",
                                MenuName="Open",
                                ShortCut = new string[] {"Ctrl", "O"},

                            },
                        }
                    },
                    new Models.Manager.Canvas.MenuModel()
                    {
                        MenuName = "Edit",
                        SubMenus = new List<Models.Manager.Canvas.MenuModel>()
                        {
                            new Models.Manager.Canvas.MenuModel()
                            {
                                MenuNo="200",
                                MenuName="Copy",
                                ShortCut = new string[] {"Ctrl", "C"},

                            },
                            new Models.Manager.Canvas.MenuModel()
                            {
                                MenuNo="201",
                                MenuName="Paste",
                                ShortCut = new string[] {"Ctrl", "V"},

                            },
                            new Models.Manager.Canvas.MenuModel()
                            {
                                MenuNo="202",
                                MenuName="Paste...",
                                SubMenus=new List<Models.Manager.Canvas.MenuModel>()
                                {
                                    new Models.Manager.Canvas.MenuModel()
                                    {
                                        MenuNo = "2021",
                                        MenuName = "In Place",
                                        ShortCut = new string[] {"Ctrl", "Alt", "V"},
                                        ParentMenuNo="202",
                                    },
                                    new Models.Manager.Canvas.MenuModel()
                                    {
                                        MenuNo = "2022",
                                        MenuName = "On Page",
                                        ParentMenuNo="202",
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

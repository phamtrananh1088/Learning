using Newtonsoft.Json.Linq;
using System;
using System.Collections;
using System.Collections.Generic;
using System.ComponentModel;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Web;
using System.Web.Mvc;

namespace WinMacOs.Utility.Utils.Binder
{
    /// <summary>
    /// 数値コントロールデータ変換.
    /// </summary>
    public class JTokenModelBinderProviders : IModelBinderProvider
    {
        public IModelBinder GetBinder(Type modelType)
        {
            return modelType == typeof(JObject) || modelType == typeof(JArray) ?  new JTokenModelBinder() : null;
        }
    }
}
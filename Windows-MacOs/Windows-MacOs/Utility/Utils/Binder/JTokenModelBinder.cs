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
    /// JTokenModelBinder.
    /// </summary>
    public class JTokenModelBinder : DefaultModelBinder
    {

        public override object BindModel(ControllerContext controllerContext, ModelBindingContext bindingContext)
        {
            try
            {
                Type modelType = bindingContext.ModelType;
                if (modelType == typeof(Newtonsoft.Json.Linq.JObject) && bindingContext.ModelMetadata.IsComplexType == true)
                {
                    IEnumerableValueProvider enumerableValueProvider = bindingContext.ValueProvider as IEnumerableValueProvider;

                    JObject j = new JObject();
                    if (enumerableValueProvider != null)
                    {
                        if (enumerableValueProvider.GetType() == typeof(ValueProviderCollection))
                        {
                            ValueProviderCollection col = enumerableValueProvider as ValueProviderCollection;
                            DictionaryValueProvider<object> dic = col.FirstOrDefault((m) => m.GetType() == typeof(DictionaryValueProvider<object>)) as DictionaryValueProvider<object>;
                            if (dic.GetKeysFromPrefix("").Count == 1)
                            {
                                foreach (var kv in dic.GetKeysFromPrefix(""))
                                {
                                    //received a JArray
                                    if (kv.Key == "" && kv.Value == "")
                                        return j;
                                    else
                                        break;
                                }
                            }
                            foreach (var kv in dic.GetKeysFromPrefix(""))
                            {
                                j.Add(kv.Key, JToken.FromObject(dic.GetValue($"{kv.Key}").RawValue));
                            }
                        }
                    }
                    return j;
                } else if (modelType == typeof(Newtonsoft.Json.Linq.JArray) && bindingContext.ModelMetadata.IsComplexType == true)
                {
                    IEnumerableValueProvider enumerableValueProvider = bindingContext.ValueProvider as IEnumerableValueProvider;

                    JArray j = new JArray();
                    if (enumerableValueProvider != null)
                    {
                        if (enumerableValueProvider.GetType() == typeof(ValueProviderCollection))
                        {
                            ValueProviderCollection col = enumerableValueProvider as ValueProviderCollection;
                            DictionaryValueProvider<object> dic = col.FirstOrDefault((m) => m.GetType() == typeof(DictionaryValueProvider<object>)) as DictionaryValueProvider<object>;
                            int index = 0;
                            IDictionary<string, string> idic = dic.GetKeysFromPrefix($"[{index}]");
                            while (idic.Count > 0)
                            {
                                JObject jo = new JObject();
                                foreach (var kv in idic)
                                {
                                    jo.Add(kv.Key, JToken.FromObject(dic.GetValue($"[{index}].{kv.Key}").RawValue));
                                }
                                j.Add(jo);
                                idic = dic.GetKeysFromPrefix($"[{++index}]");
                            }
                        }
                    }
                    return j;
                }
                else
                {
                    return base.BindModel(controllerContext, bindingContext);
                }
            }
            catch
            {
                return base.BindModel(controllerContext, bindingContext);
            }
        }
    }
}
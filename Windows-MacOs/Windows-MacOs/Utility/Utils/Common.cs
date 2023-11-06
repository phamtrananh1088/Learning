using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace WinMacOs.Utility.Utils
{
    public static class Common
    {
        public static object GetValue(this object obj, string propertyName)
        {
            if (obj == null) return null;
            if (obj is IDictionary<string, object> data)
            {
                object value = data[propertyName];
                return value;
            }
            else
            {
                System.Reflection.PropertyInfo pi = obj.GetType().GetProperty(propertyName);
                var res = pi?.GetValue(obj, null);
                return res;
            };
        }


        public static TEntity CopyFrom<TEntity>(this TEntity copyToEntity, TEntity copyFromEntity)
        {
            if (copyFromEntity?.GetType()?.Name?.IndexOf("List") >= 0)
            {
                throw new Exception("Wrong type.");
            }
            if (copyToEntity is IDictionary<string, object> dictionary && copyFromEntity is IDictionary<string, object> dictionary1)
            {
                var dataCopyTo = dictionary;
                var dataCopyFrom = dictionary1;
                foreach (var from in dataCopyFrom)
                {
                    dataCopyTo[from.Key] = from.Value;
                }
                copyToEntity = (TEntity)dataCopyTo;
            }
            else if (copyToEntity is IDictionary<string, object> dataCopyTo &&
                copyFromEntity?.GetType()?.Name?.IndexOf("<>f__AnonymousType") >= 0)
            {
                PropertyInfo[] oProperties = copyFromEntity.GetType().GetProperties();

                foreach (PropertyInfo CurrentProperty in oProperties)
                {
                    dataCopyTo[CurrentProperty.Name] = CurrentProperty.GetValue(copyFromEntity, null);
                }

                copyToEntity = (TEntity)dataCopyTo;
            }
            else if (copyToEntity?.GetType()?.Name.IndexOf("<>f__AnonymousType") >= 0 &&
                copyFromEntity?.GetType()?.Name.IndexOf("<>f__AnonymousType") >= 0)
            {
                PropertyInfo[] oProperties = copyFromEntity.GetType().GetProperties();

                foreach (PropertyInfo CurrentProperty in oProperties.Where(p => p.CanWrite))
                {
                    if (CurrentProperty.GetValue(copyFromEntity, null) != null)
                    {
                        CurrentProperty.SetValue(copyToEntity, CurrentProperty.GetValue(copyFromEntity, null), null);
                    }
                }
            }
            return copyToEntity;
        }

        public static TEntity SetValue<TEntity>(this TEntity obj, object value, string propertyName)
        {
            if (obj == null) return obj;
            if (obj is IDictionary<string, object> data)
            {
                data[propertyName] = value;
                return (TEntity)data;
            }
            else
            {
                System.Reflection.PropertyInfo pi = obj.GetType().GetProperty(propertyName);
                pi?.SetValue(obj, value);
                return obj;
            };
        }

        public static async Task<byte[]> DownloadFile(string path)
        {
            try
            {
                byte[] imageArray = new byte[] { };
                if (!string.IsNullOrEmpty(path))
                {
                    if (System.IO.File.Exists(path))
                    {
                        imageArray = await Task.Factory.StartNew(() => System.IO.File.ReadAllBytes(path));
                    }
                }

                return imageArray;
            }
            catch (Exception ex)
            {
                Debug.WriteLine(ex.Message);
                return null;
            }
        }

        public static string GetGuid()
        {
            return Guid.NewGuid().ToString("N");
        }

        public static decimal DecimalParser(this object obj)
        {
            if (obj == null)
            {
                return 0;
            }

            decimal.TryParse(obj?.ToString(), out decimal result);
            return result;
        }
    }
}

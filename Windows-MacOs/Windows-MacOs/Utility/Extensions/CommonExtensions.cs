using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Web;

namespace WinMacOs.Utility.Extensions
{
    public static class CommonExtensions
    {
        //public static object GetValue(this object obj, string propertyName)
        //{
        //    if (obj == null) return null;
        //    if (obj is IDictionary<string, object>)
        //    {
        //        var data = (IDictionary<string, object>)obj;
        //        object value = data[propertyName];
        //        return value;
        //    }
        //    else
        //    {
        //        System.Reflection.PropertyInfo pi = obj.GetType().GetProperty(propertyName);
        //        var res = pi?.GetValue(obj, null);
        //        return res;
        //    };
        //}


        //public static TEntity CopyFrom<TEntity>(this TEntity copyToEntity, TEntity copyFromEntity)
        //{
        //    if (copyFromEntity?.GetType()?.Name?.IndexOf("List") >= 0)
        //    {
        //        throw new Exception("Wrong type.");
        //    }
        //    if (copyToEntity is IDictionary<string, object> && copyFromEntity is IDictionary<string, object>)
        //    {
        //        var dataCopyTo = (IDictionary<string, object>)copyToEntity;
        //        var dataCopyFrom = (IDictionary<string, object>)copyFromEntity;
        //        foreach (var from in dataCopyFrom)
        //        {
        //            dataCopyTo[from.Key] = from.Value;
        //        }
        //        copyToEntity = (TEntity)dataCopyTo;
        //    }
        //    else if (copyToEntity is IDictionary<string, object> &&
        //        copyFromEntity?.GetType()?.Name?.IndexOf("<>f__AnonymousType") >= 0)
        //    {
        //        var dataCopyTo = (IDictionary<string, object>)copyToEntity;
        //        PropertyInfo[] oProperties = copyFromEntity.GetType().GetProperties();

        //        foreach (PropertyInfo CurrentProperty in oProperties)
        //        {
        //            dataCopyTo[CurrentProperty.Name] = CurrentProperty.GetValue(copyFromEntity, null);
        //        }

        //        copyToEntity = (TEntity)dataCopyTo;
        //    }
        //    else if (copyToEntity?.GetType()?.Name.IndexOf("<>f__AnonymousType") >= 0 &&
        //        copyFromEntity?.GetType()?.Name.IndexOf("<>f__AnonymousType") >= 0)
        //    {
        //        PropertyInfo[] oProperties = copyFromEntity.GetType().GetProperties();

        //        foreach (PropertyInfo CurrentProperty in oProperties.Where(p => p.CanWrite))
        //        {
        //            if (CurrentProperty.GetValue(copyFromEntity, null) != null)
        //            {
        //                CurrentProperty.SetValue(copyToEntity, CurrentProperty.GetValue(copyFromEntity, null), null);
        //            }
        //        }
        //    }
        //    return copyToEntity;
        //}

        //public static TEntity SetValue<TEntity>(this TEntity obj, object value, string propertyName)
        //{
        //    if (obj == null) return obj;
        //    if (obj is IDictionary<string, object>)
        //    {
        //        var data = (IDictionary<string, object>)obj;
        //        data[propertyName] = value;
        //        return (TEntity)data;
        //    }
        //    else
        //    {
        //        System.Reflection.PropertyInfo pi = obj.GetType().GetProperty(propertyName);
        //        pi?.SetValue(obj, value);
        //        return obj;
        //    };
        //}

    }
}
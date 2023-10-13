using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WinMacOs.Utility.Extensions
{
    public static class CollectionExtensions
    {
        //
        // Parameters:
        //   dictionary:
        //
        //   key:
        //
        // Type parameters:
        //   TKey:
        //
        //   TValue: [return: MaybeNull]
        public static TValue GetValueOrDefault<TKey, TValue>(this IReadOnlyDictionary<TKey, TValue> dictionary, TKey key)
        {
            dictionary.TryGetValue(key, out TValue v);
            return v;
        }
        ////
        //// Parameters:
        ////   dictionary:
        ////
        ////   key:
        ////
        ////   defaultValue:
        ////
        //// Type parameters:
        ////   TKey:
        ////
        ////   TValue:
        //[return: MaybeNull]
        //public static TValue GetValueOrDefault<TKey, TValue>(this IReadOnlyDictionary<TKey, TValue> dictionary, TKey key, [AllowNull] TValue defaultValue) where TKey : notnull;
        ////
        //// Parameters:
        ////   dictionary:
        ////
        ////   key:
        ////
        ////   value:
        ////
        //// Type parameters:
        ////   TKey:
        ////
        ////   TValue:
        //public static bool Remove<TKey, TValue>(this IDictionary<TKey, TValue> dictionary, TKey key, [MaybeNullWhen(false)] out TValue value) where TKey : notnull;
        ////
        //// Parameters:
        ////   dictionary:
        ////
        ////   key:
        ////
        ////   value:
        ////
        //// Type parameters:
        ////   TKey:
        ////
        ////   TValue:
        //public static bool TryAdd<TKey, TValue>(this IDictionary<TKey, TValue> dictionary, TKey key, TValue value) where TKey : notnull;
    }
}
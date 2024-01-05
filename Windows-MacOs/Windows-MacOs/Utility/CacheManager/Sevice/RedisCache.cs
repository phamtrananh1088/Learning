using Dapper;
using Microsoft.Extensions.Caching.Distributed;
using Microsoft.IdentityModel.Tokens;
using System;
using System.Collections;
using System.Collections.Generic;
using System.IdentityModel.Tokens.Jwt;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Web;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.Extensions;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.CacheManager.Sevice
{
    public class RedisCache : IDistributedCache
    {
        private readonly Hashtable h = new Hashtable();
        public class V
        {
            public byte[] Value { get; set; }
            public DistributedCacheEntryOptions Options { get; set; }
        }

        private static class VHelper
        {
            public static byte[] JoinByteArray(byte[] byte1, byte[] byte2)
            {
                byte[] result = new byte[byte1.Length + byte2.Length];

                Array.Copy(byte1, 0,result,0,byte1.Length);
                Array.Copy(byte2, 0, result, byte1.Length, byte2.Length);

                return result;
            }

            public static V SetV(byte[] value, DistributedCacheEntryOptions options)
            {
                return new V { Value = value, Options = options };
            }
            public static V GetV(object o)
            {
                return o as V;
            }
            public static DistributedCacheEntryOptions GetOptions(object o)
            {
                V va = GetV(o);
                return va?.Options;
            }
            public static byte[] GetValue(object o)
            {
                V va = GetV(o);
                return va?.Value;
            }
        }

        public byte[] Get(string key)
        {
            return VHelper.GetValue(h[key]);
        }

        public Task<byte[]> GetAsync(string key, CancellationToken token = default)
        {
            return Task.Factory.StartNew(() =>  Get(key), token);
        }

        public void Refresh(string key)
        {
            var v = VHelper.GetV(h[key]);
            var options = VHelper.GetOptions(h[key]);
            if (options.SlidingExpiration != null)
            {
                options.SetSlidingExpiration(options.SlidingExpiration.Value);
            } else if (options.AbsoluteExpirationRelativeToNow != null)
            {
                options.SetAbsoluteExpiration(options.AbsoluteExpirationRelativeToNow.Value);
            }
            h[key] = v;
        }

        public Task RefreshAsync(string key, CancellationToken token = default)
        {
            return Task.Factory.StartNew(() => Refresh(key), token);
        }

        public void Remove(string key)
        {
            h.Remove(key);
        }

        public Task RemoveAsync(string key, CancellationToken token = default)
        {
            return Task.Factory.StartNew(() => Remove(key), token);
        }

        public void Set(string key, byte[] value, DistributedCacheEntryOptions options)
        {
            h.Add(key, VHelper.SetV(value, options));
        }

        public Task SetAsync(string key, byte[] value, DistributedCacheEntryOptions options, CancellationToken token = default)
        {
            return Task.Factory.StartNew(() => Set(key, value, options), token);
        }
    }
}
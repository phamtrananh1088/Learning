
using Microsoft.Extensions.Caching.Memory;
using System;
using System.Collections.Generic;
using System.Linq;

namespace WinMacOs.Utility.CacheManager
{
    public class MemoryCacheService : ICacheService
    {
        protected IMemoryCache _cache;
        public MemoryCacheService(IMemoryCache cache)
        {
            _cache = cache;

        }
        /// <summary>
        /// キャシュ存在チェック
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <returns></returns>
        public bool Exists(string key)
        {
            if (key == null)
            {
                throw new ArgumentNullException(nameof(key));
            }
            return _cache.Get(key) != null;
        }

        #region キャシュ追加
        /// <summary>
        /// キャシュ追加
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <param name="value">キャシュ　値</param>
        /// <returns></returns>
        public bool Add(string key, object value)
        {
            if (key == null)
            {
                throw new ArgumentNullException(nameof(key));
            }
            if (value == null)
            {
                throw new ArgumentNullException(nameof(value));
            }
            _cache.Set(key, value);
            return Exists(key);
        }
        public bool Add(string key, string value, int expireSeconds = -1, bool isSliding = false)
        {
            return AddObject(key, value, expireSeconds, isSliding);
        }
        /// <summary>
        /// キャシュ追加
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <param name="value">キャシュ　値</param>
        /// <param name="expiresSliding">キャシュ　時限リフレッシュフラグ（時限内に操作があれば、時限リフレッシュ）</param>
        /// <param name="expiressAbsoulte">時限</param>
        /// <returns></returns>
        public bool Add(string key, object value, TimeSpan expiresSliding, TimeSpan expiressAbsoulte)
        {
            _cache.Set(key, value,
                    new MemoryCacheEntryOptions()
                    .SetSlidingExpiration(expiresSliding)
                    .SetAbsoluteExpiration(expiressAbsoulte)
                    );

            return Exists(key);
        }
        /// <summary>
        /// キャシュ追加
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <param name="value">キャシュ　値</param>
        /// <param name="expiresIn">缓存时长</param>
        /// <param name="isSliding">キャシュ　時限リフレッシュフラグ（時限内に操作があれば、時限リフレッシュ）</param>
        /// <returns></returns>
        public bool Add(string key, object value, TimeSpan expiresIn, bool isSliding = false)
        {
            if (isSliding)
                _cache.Set(key, value,
                    new MemoryCacheEntryOptions()
                    .SetSlidingExpiration(expiresIn)
                    );
            else
                _cache.Set(key, value,
                new MemoryCacheEntryOptions()
                .SetAbsoluteExpiration(expiresIn)
                );

            return Exists(key);
        }
        public bool AddObject(string key, object value, int expireSeconds = -1, bool isSliding = false)
        {
            if (expireSeconds != -1)
            {
                _cache.Set(key,
                    value,
                    new MemoryCacheEntryOptions()
                    .SetSlidingExpiration(new TimeSpan(0, 0, expireSeconds))
                    );
            }
            else
            {
                _cache.Set(key, value);
            }

            return true;
        }
        public void LPush(string key, string val)
        {
        }
        public void RPush(string key, string val)
        {
        }
        #endregion

        public T ListDequeue<T>(string key) where T : class
        {
            return null;
        }
        public object ListDequeue(string key)
        {
            return null;
        }
        public void ListRemove(string key, int keepIndex)
        {
        }
        /// <summary>
        /// キャシュ削除
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <returns></returns>
        public bool Remove(string key)
        {
            if (key == null)
            {
                throw new ArgumentNullException(nameof(key));
            }
            _cache.Remove(key);

            return !Exists(key);
        }
        /// <summary>
        /// キャシュ　バッチ削除
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <returns></returns>
        public void RemoveAll(IEnumerable<string> keys)
        {
            if (keys == null)
            {
                throw new ArgumentNullException(nameof(keys));
            }

            keys.ToList().ForEach(item => _cache.Remove(item));
        }
        public string Get(string key)
        {
            return _cache.Get(key)?.ToString();
        }
        /// <summary>
        /// キャシュ取得
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <returns></returns>
        public T Get<T>(string key) where T : class
        {
            if (key == null)
            {
                throw new ArgumentNullException(nameof(key));
            }
            return _cache.Get(key) as T;
        }

        public void Dispose()
        {
            if (_cache != null)
                _cache.Dispose();
            GC.SuppressFinalize(this);
        }


    }
}

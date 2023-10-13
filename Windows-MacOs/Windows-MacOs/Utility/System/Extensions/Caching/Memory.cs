using Microsoft.Extensions.Primitives;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace Microsoft.Extensions.Caching.Memory
{
    public static class MemoryCacheEntryExtensions
    {
        //
        // Summary:
        //     Expire the cache entry if the given Microsoft.Extensions.Primitives.IChangeToken
        //     expires.
        //
        // Parameters:
        //   options:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions.
        //
        //   expirationToken:
        //     The Microsoft.Extensions.Primitives.IChangeToken that causes the cache entry
        //     to expire.
        //
        // Returns:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions so that additional
        //     calls can be chained.
        public static MemoryCacheEntryOptions AddExpirationToken(this MemoryCacheEntryOptions options, IChangeToken expirationToken)
        {
            //TODO
            return options;
        }
        //
        // Summary:
        //     The given callback will be fired after the cache entry is evicted from the cache.
        //
        // Parameters:
        //   options:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions.
        //
        //   callback:
        //     The callback to register for calling after an entry is evicted.
        //
        // Returns:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions so that additional
        //     calls can be chained.
        public static MemoryCacheEntryOptions RegisterPostEvictionCallback(this MemoryCacheEntryOptions options, PostEvictionDelegate callback)
        {
            //TODO
            return options;
        }
        //
        // Summary:
        //     The given callback will be fired after the cache entry is evicted from the cache.
        //
        // Parameters:
        //   options:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions.
        //
        //   callback:
        //     The callback to register for calling after an entry is evicted.
        //
        //   state:
        //     The state to pass to the callback.
        //
        // Returns:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions so that additional
        //     calls can be chained.
        public static MemoryCacheEntryOptions RegisterPostEvictionCallback(this MemoryCacheEntryOptions options, PostEvictionDelegate callback, object state)
        {
            //TODO
            return options;
        }
        //
        // Summary:
        //     Sets an absolute expiration date for the cache entry.
        //
        // Parameters:
        //   options:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions.
        //
        //   absolute:
        //     The expiration time, in absolute terms.
        //
        // Returns:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions so that additional
        //     calls can be chained.
        public static MemoryCacheEntryOptions SetAbsoluteExpiration(this MemoryCacheEntryOptions options, DateTimeOffset absolute)
        {
            //TODO
            return options;
        }
        //
        // Summary:
        //     Sets an absolute expiration time, relative to now.
        //
        // Parameters:
        //   options:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions.
        //
        //   relative:
        //     The expiration time, relative to now.
        //
        // Returns:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions so that additional
        //     calls can be chained.
        public static MemoryCacheEntryOptions SetAbsoluteExpiration(this MemoryCacheEntryOptions options, TimeSpan relative)
        {
            //TODO
            return options;
        }
        //
        // Summary:
        //     Sets the priority for keeping the cache entry in the cache during a memory pressure
        //     tokened cleanup.
        //
        // Parameters:
        //   options:
        //     The option on which to set the priority.
        //
        //   priority:
        //     The Microsoft.Extensions.Caching.Memory.CacheItemPriority to set on the option.
        //
        // Returns:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions so that additional
        //     calls can be chained.
        public static MemoryCacheEntryOptions SetPriority(this MemoryCacheEntryOptions options, CacheItemPriority priority)
        {
            //TODO
            return options;
        }
        //
        // Summary:
        //     Sets the size of the cache entry value.
        //
        // Parameters:
        //   options:
        //     The options to set the entry size on.
        //
        //   size:
        //     The size to set on the Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions.
        //
        // Returns:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions so that additional
        //     calls can be chained.
        public static MemoryCacheEntryOptions SetSize(this MemoryCacheEntryOptions options, long size)
        {
            //TODO
            return options;
        }
        //
        // Summary:
        //     Sets how long the cache entry can be inactive (e.g. not accessed) before it will
        //     be removed. This will not extend the entry lifetime beyond the absolute expiration
        //     (if set).
        //
        // Parameters:
        //   options:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions.
        //
        //   offset:
        //     The sliding expiration time.
        //
        // Returns:
        //     The Microsoft.Extensions.Caching.Memory.MemoryCacheEntryOptions so that additional
        //     calls can be chained.
        public static MemoryCacheEntryOptions SetSlidingExpiration(this MemoryCacheEntryOptions options, TimeSpan offset)
        {
            //TODO
            return options;
        }
    }

    public static class CacheExtensions
    {
        public static object Get(this IMemoryCache cache, object key)
        {
            object value;
            cache.TryGetValue(key, out value);
            return value;
        }
        public static TItem Get<TItem>(this IMemoryCache cache, object key)
        {
            return (TItem)cache.Get(key);
        }
        public static TItem GetOrCreate<TItem>(this IMemoryCache cache, object key, Func<ICacheEntry, TItem> factory)
        {
            object value;
            if (cache.TryGetValue(key, out value) == false)
            {
                var en = cache.CreateEntry(key);
                en.Value = default(TItem);
                return factory(en);
            }
            else
            {
                return cache.Get<TItem>(key);
            }
        }
        [DebuggerStepThrough]
        public static Task<TItem> GetOrCreateAsync<TItem>(this IMemoryCache cache, object key, Func<ICacheEntry, Task<TItem>> factory)
        {
            object value;
            if (cache.TryGetValue(key, out value) == false)
            {
                var en = cache.CreateEntry(key);
                en.Value = default(TItem);
                return factory(en);
            }
            else
            {
                return Task.Factory.StartNew(() => cache.Get<TItem>(key));
            }
        }
        public static TItem Set<TItem>(this IMemoryCache cache, object key, TItem value)
        {
            cache.CreateEntry(key).Value = value;
            return cache.Get<TItem>(key);
        }
        public static TItem Set<TItem>(this IMemoryCache cache, object key, TItem value, MemoryCacheEntryOptions options)
        {
            var en = cache.CreateEntry(key);
            en.AbsoluteExpiration = options.AbsoluteExpiration;
            en.AbsoluteExpirationRelativeToNow = options.AbsoluteExpirationRelativeToNow;
            en.Priority = options.Priority;
            en.Size = options.Size;
            en.SlidingExpiration = options.SlidingExpiration;
            en.Value = value;
            return cache.Get<TItem>(key);
        }
        public static TItem Set<TItem>(this IMemoryCache cache, object key, TItem value, IChangeToken expirationToken)
        {
            var en = cache.CreateEntry(key);
            en.ExpirationTokens.Add(expirationToken);
            en.Value = value;
            return cache.Get<TItem>(key);
        }
        public static TItem Set<TItem>(this IMemoryCache cache, object key, TItem value, DateTimeOffset absoluteExpiration)
        {
            var en = cache.CreateEntry(key);
            en.AbsoluteExpiration = absoluteExpiration;
            en.Value = value;
            return cache.Get<TItem>(key);
        }
        public static TItem Set<TItem>(this IMemoryCache cache, object key, TItem value, TimeSpan absoluteExpirationRelativeToNow)
        {
            var en = cache.CreateEntry(key);
            en.AbsoluteExpirationRelativeToNow = absoluteExpirationRelativeToNow;
            en.Value = value;
            return cache.Get<TItem>(key);
        }
        public static bool TryGetValue<TItem>(this IMemoryCache cache, object key, out TItem value)
        {
            return cache.TryGetValue(key, out value) == false;
        }
    }

    //
    // Summary:
    //     Represents the cache options applied to an entry of the Microsoft.Extensions.Caching.Memory.IMemoryCache
    //     instance.
    public class MemoryCacheEntryOptions
    {
        public MemoryCacheEntryOptions()
        {

        }

        //
        // Summary:
        //     Gets or sets an absolute expiration date for the cache entry.
        public DateTimeOffset? AbsoluteExpiration { get; set; }
        //
        // Summary:
        //     Gets or sets an absolute expiration time, relative to now.
        public TimeSpan? AbsoluteExpirationRelativeToNow { get; set; }
        //
        // Summary:
        //     Gets the Microsoft.Extensions.Primitives.IChangeToken instances which cause the
        //     cache entry to expire.
        public IList<IChangeToken> ExpirationTokens { get; }
        //
        // Summary:
        //     Gets or sets the callbacks will be fired after the cache entry is evicted from
        //     the cache.
        public IList<PostEvictionCallbackRegistration> PostEvictionCallbacks { get; }
        //
        // Summary:
        //     Gets or sets the priority for keeping the cache entry in the cache during a memory
        //     pressure triggered cleanup. The default is Microsoft.Extensions.Caching.Memory.CacheItemPriority.Normal.
        public CacheItemPriority Priority { get; set; }
        //
        // Summary:
        //     Gets or sets the size of the cache entry value.
        public long? Size { get; set; }
        //
        // Summary:
        //     Gets or sets how long a cache entry can be inactive (e.g. not accessed) before
        //     it will be removed. This will not extend the entry lifetime beyond the absolute
        //     expiration (if set).
        public TimeSpan? SlidingExpiration { get; set; }
    }
    //
    // Summary:
    //     Specifies how items are prioritized for preservation during a memory pressure
    //     triggered cleanup.
    public enum CacheItemPriority
    {
        Low = 0,
        Normal = 1,
        High = 2,
        NeverRemove = 3
    }

    public enum EvictionReason
    {
        None = 0,
        //
        // Summary:
        //     Manually
        Removed = 1,
        //
        // Summary:
        //     Overwritten
        Replaced = 2,
        //
        // Summary:
        //     Timed out
        Expired = 3,
        //
        // Summary:
        //     Event
        TokenExpired = 4,
        //
        // Summary:
        //     Overflow
        Capacity = 5
    }

    //
    // Summary:
    //     Signature of the callback which gets called when a cache entry expires.
    //
    // Parameters:
    //   key:
    //     The key of the entry being evicted.
    //
    //   value:
    //     The value of the entry being evicted.
    //
    //   reason:
    //     The Microsoft.Extensions.Caching.Memory.EvictionReason.
    //
    //   state:
    //     The information that was passed when registering the callback.
    public delegate void PostEvictionDelegate(object key, object value, EvictionReason reason, object state);

    public class PostEvictionCallbackRegistration
    {
        public PostEvictionCallbackRegistration()
        {

        }

        public PostEvictionDelegate EvictionCallback { get; set; }
        public object State { get; set; }
    }

    //
    // Summary:
    //     Represents an entry in the Microsoft.Extensions.Caching.Memory.IMemoryCache implementation.
    public interface ICacheEntry : IDisposable
    {
        //
        // Summary:
        //     Gets or sets an absolute expiration date for the cache entry.
        DateTimeOffset? AbsoluteExpiration { get; set; }
        //
        // Summary:
        //     Gets or sets an absolute expiration time, relative to now.
        TimeSpan? AbsoluteExpirationRelativeToNow { get; set; }
        //
        // Summary:
        //     Gets the Microsoft.Extensions.Primitives.IChangeToken instances which cause the
        //     cache entry to expire.
        IList<IChangeToken> ExpirationTokens { get; }
        //
        // Summary:
        //     Gets the key of the cache entry.
        object Key { get; }
        //
        // Summary:
        //     Gets or sets the callbacks will be fired after the cache entry is evicted from
        //     the cache.
        IList<PostEvictionCallbackRegistration> PostEvictionCallbacks { get; }
        //
        // Summary:
        //     Gets or sets the priority for keeping the cache entry in the cache during a cleanup.
        //     The default is Microsoft.Extensions.Caching.Memory.CacheItemPriority.Normal.
        CacheItemPriority Priority { get; set; }
        //
        // Summary:
        //     Gets or set the size of the cache entry value.
        long? Size { get; set; }
        //
        // Summary:
        //     Gets or sets how long a cache entry can be inactive (e.g. not accessed) before
        //     it will be removed. This will not extend the entry lifetime beyond the absolute
        //     expiration (if set).
        TimeSpan? SlidingExpiration { get; set; }
        //
        // Summary:
        //     Gets or set the value of the cache entry.
        object Value { get; set; }
    }

    //
    // Summary:
    //     Represents a local in-memory cache whose values are not serialized.
    public interface IMemoryCache : IDisposable
    {
        //
        // Summary:
        //     Create or overwrite an entry in the cache.
        //
        // Parameters:
        //   key:
        //     An object identifying the entry.
        //
        // Returns:
        //     The newly created Microsoft.Extensions.Caching.Memory.ICacheEntry instance.
        ICacheEntry CreateEntry(object key);
        //
        // Summary:
        //     Removes the object associated with the given key.
        //
        // Parameters:
        //   key:
        //     An object identifying the entry.
        void Remove(object key);
        //
        // Summary:
        //     Gets the item associated with this key if present.
        //
        // Parameters:
        //   key:
        //     An object identifying the requested entry.
        //
        //   value:
        //     The located value or null.
        //
        // Returns:
        //     True if the key was found.
        bool TryGetValue(object key, out object value);
    }
}

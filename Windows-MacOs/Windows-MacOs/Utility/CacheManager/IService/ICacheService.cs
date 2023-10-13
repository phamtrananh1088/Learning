using System;
using System.Collections.Generic;
using System.Text;

namespace WinMacOs.Utility.CacheManager
{

    public interface ICacheService : IDisposable
    {
        /// <summary>
        /// キャシュ存在チェック
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <returns></returns>
        bool Exists(string key);

        /// <summary>
        /// ListをheadにPush
        /// </summary>
        /// <param name="key"></param>
        /// <param name="val"></param>
        void LPush(string key, string val);

        void RPush(string key, string val);

        /// <summary>
        /// Listデキュー lpop
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        object ListDequeue(string key);

        /// <summary>
        /// Listデキュー lpop
        /// </summary>
        /// <param name="key"></param>
        /// <returns></returns>
        T ListDequeue<T>(string key) where T : class;

        /// <summary>
        /// listのデータを削除，keepIndexは保留する項目の最後までの位置　例：list は1.2.3.....100
        /// 前の三つ項目を削除欲しいなら、keepindexは4
        /// </summary>
        /// <param name="key"></param>
        /// <param name="keepIndex"></param>
        void ListRemove(string key, int keepIndex);

        /// <summary>
        /// キャシュ追加
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <param name="value">キャシュ　値</param>
        /// <param name="expiresIn">キャシュ　時限</param>
        /// <param name="isSliding">キャシュ　時限リフレッシュフラグ（時限内に操作があれば、時限リフレッシュ） //new TimeSpan(0, 60, 0);</param>
        /// <returns></returns>
        bool AddObject(string key, object value, int expireSeconds = -1, bool isSliding = false);

        bool Add(string key, string value, int expireSeconds = -1, bool isSliding = false);

        /// <summary>
        /// キャシュ削除
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <returns></returns>
        bool Remove(string key);

        /// <summary>
        /// キャシュ　バッチ削除
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <returns></returns>
        void RemoveAll(IEnumerable<string> keys);

        /// <summary>
        /// キャシュ取得
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <returns></returns>
        T Get<T>(string key) where T : class;

        /// <summary>
        /// キャシュ取得
        /// </summary>
        /// <param name="key">キャシュ　キー</param>
        /// <returns></returns>
        string Get(string key);
    }
}

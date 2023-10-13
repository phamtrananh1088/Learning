namespace WinMacOs.Utility.Utils
{
    /// <summary>
    /// クラスのシングルトン実例化.
    /// </summary>
    /// <typeparam name="T">クラス名.</typeparam>
    public class SingletonProvider<T>
        where T : new()
    {
        private static readonly object SyncObject = new object();

        private static T singleton;

        private SingletonProvider()
        {
        }

        /// <summary>
        /// 実例化.
        /// </summary>
        public static T Instance
        {
            get
            {
                if (singleton == null)
                {
                    lock (SyncObject)
                    {
                        singleton = new T();
                    }
                }

                return singleton;
            }
        }
    }
}

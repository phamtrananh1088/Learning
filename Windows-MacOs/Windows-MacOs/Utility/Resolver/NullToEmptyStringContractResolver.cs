namespace WinMacOs.Utils.Resolver
{
    /// <summary>
    /// JSONに変換する時、Nullを空白に変換する.
    /// </summary>
    public class NullToEmptyStringContractResolver : LowerCaseContractResolver
    {
        /// <summary>
        /// コンストラクタ.
        /// </summary>
        /// <param name="bWriteNullToEmpty">Nullを空白に変換する.</param>
        public NullToEmptyStringContractResolver(bool bWriteNullToEmpty = false)
            : base(bWriteNullToEmpty)
        {
        }

        /// <inheritdoc/>
        protected override string ResolvePropertyName(string sPropertyName)
        {
            return sPropertyName;
        }
    }
}

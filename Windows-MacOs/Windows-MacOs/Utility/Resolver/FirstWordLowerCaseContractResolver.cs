namespace WinMacOs.Utils.Resolver
{
    /// <summary>
    /// JSONに変換する時、プロパティ名の第一文字を小文字に変換する.
    /// </summary>
    public class FirstWordLowerCaseContractResolver : LowerCaseContractResolver
    {
        /// <summary>
        /// コンストラクタ.
        /// </summary>
        /// <param name="bWriteNullToEmpty">Nullを空白に変換する.</param>
        public FirstWordLowerCaseContractResolver(bool bWriteNullToEmpty = false)
            : base(bWriteNullToEmpty)
        {
        }

        /// <inheritdoc/>
        protected override string ResolvePropertyName(string sPropertyName)
        {
            return sPropertyName.Substring(0, 1).ToLower() + sPropertyName.Substring(1);
        }
    }
}

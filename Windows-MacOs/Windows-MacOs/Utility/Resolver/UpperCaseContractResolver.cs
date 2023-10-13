using Newtonsoft.Json.Serialization;

namespace WinMacOs.Utils.Resolver
{
    /// <summary>
    /// JSONに変換する時、プロパティ名を大文字に変換する.
    /// </summary>
    public class UpperCaseContractResolver : DefaultContractResolver
    {
        /// <inheritdoc/>
        protected override string ResolvePropertyName(string propertyName)
        {
            return propertyName.ToUpper();
        }
    }
}

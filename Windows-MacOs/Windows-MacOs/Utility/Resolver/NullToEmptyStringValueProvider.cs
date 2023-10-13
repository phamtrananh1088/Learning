using System.Reflection;
using Newtonsoft.Json.Serialization;

namespace WinMacOs.Utils.Resolver
{
    /// <summary>
    /// Nullを空白に変換Provider.
    /// </summary>
    public class NullToEmptyStringValueProvider : IValueProvider
    {
        /// <summary>
        /// コンストラクタ.
        /// </summary>
        /// <param name="memberInfo">属性.</param>
        public NullToEmptyStringValueProvider(PropertyInfo memberInfo)
        {
            MemberInfo = memberInfo;
        }

        /// <summary>
        /// 属性.
        /// </summary>
        private PropertyInfo MemberInfo { get; set; }

        /// <inheritdoc/>
        public object GetValue(object target)
        {
            object result = MemberInfo.GetValue(target);
            if (MemberInfo.PropertyType == typeof(string) && result == null)
                result = string.Empty;
            return result;
        }

        /// <inheritdoc/>
        public void SetValue(object target, object value)
        {
            MemberInfo.SetValue(target, value);
        }
    }
}

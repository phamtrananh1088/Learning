using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Web.Configuration;
using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;

namespace WinMacOs.Utils.Resolver
{
    /// <summary>
    /// JSONに変換する時、プロパティ名を小文字に変換する.
    /// </summary>
    public class NotesSinseiContractResolver : DefaultContractResolver
    {
        /// <summary>
        /// コンストラクタ.
        /// </summary>
        /// <param name="bWriteNullToEmpty">Nullを空白に変換する.</param>
        public NotesSinseiContractResolver(bool bWriteNullToEmpty = true)
            : base()
        {
            this.WriteNullToEmpty = bWriteNullToEmpty;
        }

        /// <summary>
        /// Nullを空白に変換フラグ.
        /// </summary>
        private bool WriteNullToEmpty { get; set; }

        /// <inheritdoc/>
        protected override IList<JsonProperty> CreateProperties(Type type, MemberSerialization memberSerialization)
        {
            if (this.WriteNullToEmpty)
            {
                return type.GetProperties()
                        .Select(p =>
                        {
                            JsonProperty jp = this.CreateProperty(p, memberSerialization);
                            jp.ValueProvider = new NullToEmptyStringValueProvider(p);
                            return jp;
                        }).ToList();
            }
            else
            {
                return base.CreateProperties(type,  memberSerialization);
            }
        }
    }
}

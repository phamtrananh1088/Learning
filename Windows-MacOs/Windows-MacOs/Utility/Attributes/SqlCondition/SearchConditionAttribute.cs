using System;
using WinMacOs.Utility.Enums;

namespace WinMacOs.Utility.Attributes
{
    /// <summary>
    /// 検索条件属性.
    /// </summary>
    public class SearchConditionAttribute : Attribute
    {
        /// <summary>
        /// コンストラクタ.
        /// </summary>
        public SearchConditionAttribute()
        {
            this.FilterCompareType = CompareType.Equals;
            this.DateTimeFormat = StaticConst.SHORT_DATE;
            this.EmptyIgnore = true;
        }

        /// <summary>
        /// 設定されない時、条件を無視するか.
        /// </summary>
        public bool EmptyIgnore { get; set; }

        /// <summary>
        /// 設定されないのデファクト値.
        /// </summary>
        public object EmptyDefaultValue { get; set; }

        /// <summary>
        /// フィルターキー.
        /// </summary>
        public string FilterKey { get; set; }

        /// <summary>
        /// フィルター方法.
        /// </summary>
        public CompareType FilterCompareType { get; set; }

        /// <summary>
        /// 日時フォーマット.
        /// </summary>
        public string DateTimeFormat { get; set; }
    }
}
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Web.Mvc;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Utility.Attributes
{
    /// <summary>
    /// 最大桁数チェック.
    /// </summary>
    public class MaxLengthExAttribute : ValidationAttribute, IClientValidatable
    {
        /// <summary>
        /// インスタンスを初期化.
        /// </summary>
        /// <param name="iLength">最大桁数.</param>
        /// <param name="isNumber">Is Number Type.</param>
        public MaxLengthExAttribute(int iLength, bool isNumber = false)
        {
            this.Length = iLength;
            this.isNumber = isNumber;
        }

        /// <summary>
        /// 許容チェック桁数.
        /// </summary>
        public int Length { get; }

        public bool isNumber { get; }
        /// <summary>
        /// データ桁数チェック.
        /// </summary>
        /// <param name="value">値.</param>
        /// <returns>チェック結果.</returns>
        public override bool IsValid(object value)
        {
            return value == null || string.IsNullOrEmpty(value.ToString()) || StringUtils.GetLengthOfVarchar2(value.ToString(), isNumber: isNumber) <= this.Length;
        }

        /// <summary>
        /// メッセージフォマード.
        /// </summary>
        /// <param name="sName">項目名.</param>
        /// <returns>エラーメッセージ.</returns>
        public override string FormatErrorMessage(string sName)
        {
            if (string.IsNullOrEmpty(ErrorMessageResourceName) && string.IsNullOrEmpty(ErrorMessage))
                ErrorMessage = Resources.Messages.KYE000004;
            return string.Format(ErrorMessageString, sName, this.Length);
        }

        /// <summary>
        /// クライアント端のコントロールのチェック属性を指定する.
        /// </summary>
        /// <param name="metadata">データ モデルの共通のメタデータ.</param>
        /// <param name="context">コントローラーコンテクスト.</param>
        /// <returns>ルール.</returns>
        public IEnumerable<ModelClientValidationRule> GetClientValidationRules(ModelMetadata metadata, ControllerContext context)
        {
            // チェックツール作成
            return new[]
           {
                new ModelClientValidationRangeDateRule(
                     FormatErrorMessage(metadata.GetDisplayName()), this.Length)
           };
        }

        /// <summary>
        /// チェックルールクラス.
        /// </summary>
        public class ModelClientValidationRangeDateRule : ModelClientValidationRule
        {
            /// <summary>
            /// コンストラクタ.
            /// </summary>
            /// <param name="sErrorMessage">エラーメッセージ.</param>
            /// <param name="iLength">チェック桁数.</param>
            public ModelClientValidationRangeDateRule(string sErrorMessage, int iLength)
            {
                // エラーメッセージ指定
                ErrorMessage = sErrorMessage;
                // チェックルール指定
                ValidationType = "maxlengthex";
                // チェック必要パラメータ指定
                ValidationParameters["maxlength"] = iLength;
            }
        }
    }
}
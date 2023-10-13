using System;
using System.Collections.Generic;
using System.ComponentModel;
using WinMacOs.Utility.Attributes;

namespace WinMacOs.Models
{
    /// <summary>
    /// ログインユーザー. 20221206_DHK_TODO
    /// </summary>
    [Serializable]
    public class LoginUser
    {
        /// <summary>
        /// ユーザーコード.
        /// </summary>
        [DisplayName("ユーザーコード")]
        public virtual string USER_CD { get; set; }

        /// <summary>
        /// ユーザーコード.
        /// </summary>
        [DisplayName("ユーザーコード")]
        public virtual string PASSWORD { get; set; }

        /// <summary>
        /// パスワード設定日.
        /// </summary>
        [DisplayName("パスワード設定日")]
        [MaxLengthEx(10)]
        public virtual string PASSWORD_SETTEI_YMD { get; set; }

        /// <summary>
        /// メニュー構成コード.
        /// </summary>
        [DisplayName("メニュー構成コード")]
        public virtual string MENU_KOSEI_CD { get; set; }

        /// <summary>
        /// 社員番号.
        /// </summary>
        [DisplayName("社員番号")]
        [MaxLengthEx(6)]
        public virtual string SHAIN_NO { get; set; }

        /// <summary>
        /// 社員名.
        /// </summary>
        [DisplayName("社員名")]
        [MaxLengthEx(40)]
        public virtual string SHAIN_NM { get; set; }

        /// <summary>
        /// 社員かな名.
        /// </summary>
        [DisplayName("社員かな名")]
        [MaxLengthEx(40)]
        public virtual string SHAIN_KN { get; set; }

        /// <summary>
        /// 役職名.
        /// </summary>
        [DisplayName("役職名")]
        [MaxLengthEx(40)]
        public virtual string YAKUSHOKU_NM { get; set; }

        /// <summary>
        /// 部門コード.
        /// </summary>
        [DisplayName("部門コード")]
        [MaxLengthEx(8)]
        public virtual string BUMON_CD { get; set; }

        /// <summary>
        /// 営業区分.
        /// </summary>
        [DisplayName("営業区分")]
        [MaxLengthEx(1)]
        public virtual string EIGYO_KBN { get; set; }

        /// <summary>
        /// 整備区分.
        /// </summary>
        [DisplayName("整備区分")]
        [MaxLengthEx(1)]
        public virtual string SEIBI_KBN { get; set; }

        /// <summary>
        /// 部門名.
        /// </summary>
        [DisplayName("部門名")]
        [MaxLengthEx(70)]
        public virtual string BUMON_NM { get; set; }

        /// <summary>
        /// 部門略称.
        /// </summary>
        [DisplayName("部門略称")]
        public virtual string BUMON_RK { get; set; }

        /// <summary>
        /// ユーザ権限.
        /// </summary>
        [Description("ユーザ権限")]
        public virtual List<MenuModel> Kengen { get; set; }

        /// <summary>
        /// 最後ログイン時刻.
        /// </summary>
        [Description("最後ログイン時刻")]
        public virtual DateTime? LAST_LOGIN_DATE { get; set; }

        /// <summary>
        /// WorkID.
        /// </summary>
        public virtual string WORK_ID { get; set; }

        /// <summary>
        /// localStorageID.
        /// </summary>
        public virtual string LOCAL_STORAGE_WORK_ID { get; set; }
    }
}

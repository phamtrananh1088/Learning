using System;
using System.Collections.Generic;
using System.ComponentModel;

namespace WinMacOs.Models
{
    /// <summary>
    /// メニューモデル. 20221206_DHK_TODO
    /// </summary>
    [Serializable]
    public class MenuModel : ICloneable
    {
        /// <summary>メニュー区分.</summary>
        [Description("メニュー区分")]
        public string MENU_KBN { get; set; }

        /// <summary>メニューコード.</summary>
        [Description("メニューコード")]
        public string MENU_CD { get; set; }

        /// <summary>メニュー名.</summary>
        [Description("メニュー名")]
        public string MENU_NM { get; set; }

        /// <summary>親メニューコード.</summary>
        [Description("親メニューコード")]
        public string PARENT_CD { get; set; }

        /// <summary>階層.</summary>
        [Description("階層")]
        public string HIERARCHY { get; set; }

        /// <summary>アクセス経路.</summary>
        [Description("アクセス経路")]
        public string ROUTE { get; set; }

        /// <summary>URL.</summary>
        [Description("URL")]
        public string URL { get; set; }

        /// <summary>GUIDANCE_URL.</summary>
        [Description("GUIDANCE_URL")]
        public string GUIDANCE_URL { get; set; }

        /// <summary>アイコン.</summary>
        [Description("アイコン")]
        public string ICON { get; set; }

        /// <summary>サブメニューリスト.</summary>
        [Description("サブメニューリスト")]
        public List<MenuModel> SUBMENUS { get; set; }

        /// <summary>参照権.</summary>
        [Description("参照権")]
        public string SANSYOUKEN { get; set; }

        /// <summary>更新権.</summary>
        [Description("更新権")]
        public string KOUSINKEN { get; set; }

        /// <summary>表示区分.</summary>
        [Description("表示区分")]
        public string HYOUZI_K { get; set; }

        /// <summary>表示順.</summary>
        [Description("表示順")]
        public decimal? HYOJI_JUN { get; set; }

        /// <summary>処理説明.</summary>
        [Description("処理説明")]
        public string MENU_DESC { get; set; }

        /// <inheritdoc/>
        object ICloneable.Clone()
        {
            return this.Clone();
        }

        /// <summary>Clone.</summary>
        /// <returns>MenuModel.</returns>
        public MenuModel Clone()
        {
            return (MenuModel)this.MemberwiseClone();
        }
    }
}

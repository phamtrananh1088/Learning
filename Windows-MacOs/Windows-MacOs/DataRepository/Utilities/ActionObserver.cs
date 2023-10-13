using System;
using System.Web;

namespace WinMacOs.DataRepository.Utilities
{

    public class ActionObserver
    {
        /// <summary>
        /// Action行う開始時間
        /// </summary>
        public DateTime RequestDate { get; set; }

        /// <summary>
        /// 当リクエストのLog記録フラグ
        /// </summary>
        public bool IsWrite { get; set; }

        public HttpContext HttpContext { get; }
    }
}

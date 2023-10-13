using System;
using System.Collections.Generic;
using System.Text;

namespace WinMacOs.Utility.DomainModels
{
    public class UserInfo
    {
        public string ログインID { get; set; }
        public string 社員名 { get; set; }
        public string 社員ID { get; set; }
        public List<MultiToken> Tokens { get; set; }
        public string 所属部署コード { get; set; }

    }

    public class MultiToken
    {
        public int 連番 { get; set; }
        public string Token { get; set; }
    }

}

using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;

namespace WinMacOs.Utility.Utils
{
    public class PassWordHelper
    {
        public static string GenerateSaltedPass(int length)
        {
            byte[] salt = new byte[length];
            using (var rngCsp = new RNGCryptoServiceProvider())
            {
                rngCsp.GetNonZeroBytes(salt);
            }
            return Convert.ToBase64String(salt);
        }

        public static string GenerateHashPass(string salt, string pass)
        {
            // 文字列をバイト型配列に変換する
            byte[] data = Encoding.UTF8.GetBytes(salt + pass);

            // MD5ハッシュアルゴリズム生成
            var algorithm = new MD5CryptoServiceProvider();

            // ハッシュ値を計算する
            byte[] bs = algorithm.ComputeHash(data);

            // リソースを解放する
            algorithm.Clear();

            // バイト型配列を16進数文字列に変換
            var result = new StringBuilder();

            foreach (byte b in bs)
            {
                result.Append(b.ToString("X2"));
            }

            System.Diagnostics.Debug.WriteLine(result.ToString());
            return result.ToString();
        }

        public static string GenerateRanPass(int length = 8)
        {
            if (length < 8) length = 8;
            //pattern {1,2}num + {1,2}sym + {1,4}alphabet + {1,4}upper-alphabet
            var result = new StringBuilder();
            char sAlphabet = 'a';

            char sUpperAlphabet = 'A';
            char sNumber = '0';
            string sSymbol = "`˜!@#$%^&*()_+-={}[]￥，\\|:;\"'<>,.?/";
            var rand = new Random();
            var cNum = rand.Next(1, 3);//1,2
            var cSym = rand.Next(1, 3);//1,2
            var cUpperAlphabet = rand.Next(1, 4);//1,2,3
            var cAlphabet = length - cNum - cSym - cUpperAlphabet;
            for (int ctr = 1; ctr <= cAlphabet; ctr++)
                result.Append((char)(sAlphabet + rand.Next(0, 26)));
            for (int ctr = 1; ctr <= cUpperAlphabet; ctr++)
                result.Append((char)(sUpperAlphabet + rand.Next(0, 26)));
            for (int ctr = 1; ctr <= cNum; ctr++)
                result.Append((char)(sNumber + rand.Next(0, 11)));
            for (int ctr = 1; ctr <= cSym; ctr++)
                result.Append(sSymbol[rand.Next(0, sSymbol.Length)]);
            var res = result.ToString().ToCharArray().ToList();
            result.Clear();
            while (res.Count > 0)
            {
                var randShuffle = rand.Next(0, res.Count);
                var item = res[randShuffle];
                result.Append(item);
                res.RemoveAt(randShuffle);
            }
            return result.ToString();
        }

        public static bool ValidatePassword(string passWord, int lengthRequired, string typeRequired)
        {
            string errMess = String.Empty;
            if (String.IsNullOrEmpty(passWord) || passWord.Length <= lengthRequired)
            {
                return false;
            }
            int validConditions = 0;
            foreach (char c in passWord)
            {
                if (c >= 'a' && c <= 'z')
                {
                    validConditions++;
                    break;
                }
            }
            foreach (char c in passWord)
            {
                if (c >= 'A' && c <= 'Z')
                {
                    validConditions++;
                    break;
                }
            }
            if (validConditions == 0) return false;
            foreach (char c in passWord)
            {
                if (c >= '0' && c <= '9')
                {
                    validConditions++;
                    break;
                }
            }
            if (validConditions == 1) return false;
            if (validConditions == 2)
            {
                char[] special = { '@', '#', '$', '%', '^', '&', '+', '=' }; // or whatever    
                if (passWord.IndexOfAny(special) == -1) return false;
            }
            return true;
        }
    }
}

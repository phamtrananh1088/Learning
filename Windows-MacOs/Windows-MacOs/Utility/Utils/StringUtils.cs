using System;
using System.Drawing;
using System.IO;
using System.Reflection;
using System.Text;
using System.Text.RegularExpressions;
using WinMacOs.Utility.Attributes;
using WinMacOs.Utility.Enums;

namespace WinMacOs.Utility.Utils
{
    /// <summary>
    /// テキストツール.
    /// </summary>
    public class StringUtils
    {
        /// <summary>
        /// SQL検索条件文フォーマット.
        /// </summary>
        private const string EQUAL_FORMAT = "{0}='{1}' ";
        private const string LIKE_FORMAT = "{0} LIKE '{1}' ";

        /// <summary>
        /// テキスト桁数(Varchar2)取得する.
        /// </summary>
        /// <param name="sData">テキスト.</param>
        /// <param name="isNumber">Is Number Type.</param>
        /// <returns>テキスト桁数(Varchar2).</returns>
        public static int GetLengthOfVarchar2(string sData, bool isNumber = false)
        {
            int iLen = 0;
            byte[] b;

            for (int i = 0; i < sData.Length; i++)
            {
                b = Encoding.Default.GetBytes(sData.Substring(i, 1));
                if (b.Length > 1)
                    iLen += 2;
                else
                    iLen++;
            }

            if (isNumber && sData.StartsWith("-"))
                iLen -= 1;
            return iLen;
        }

        /// <summary>
        /// テキストの桁数(byte)取得する.
        /// </summary>
        /// <param name="sData">テキスト.</param>
        /// <returns>テキストの桁数(byte).</returns>
        public static int GetBytesLength(string sData)
        {
            int iLen = 0;
            byte[] b;

            for (int i = 0; i < sData.Length; i++)
            {
                b = Encoding.Default.GetBytes(sData.Substring(i, 1));
                if (b.Length > 1)
                    iLen += 2;
                else
                    iLen++;
            }

            return iLen;
        }

        /// <summary>
        /// クラスのプロパティよりSQL文作成.
        /// </summary>
        /// <param name="where">検索値。例）new {aaa = bbb}.</param>
        /// <returns>SQL文.</returns>
        public static string InjectToString(object where)
        {
            // パラメータはnullの時、戻る
            if (where == null)
            {
                return string.Empty;
            }

            if (where.GetType() == typeof(string))
            {
                return (string)where;
            }

            // プロパティを取得する。
            PropertyInfo[] whereProps = where.GetType().GetProperties();
            string s = string.Empty;
            // ループ
            for (int i = 0; i < whereProps.Length; i++)
            {
                PropertyInfo prop = whereProps[i];

                if (prop.GetValue(where) == null)
                {
                    continue;
                }

                // 検索条件に％または＿が存在するとき、あいまい検索
                if (prop.GetValue(where).ToString().IndexOf("%") > -1 ||
                    prop.GetValue(where).ToString().IndexOf("_") > -1)
                {
                    s += (s.Equals(string.Empty) ? string.Empty : " AND ") + string.Format(LIKE_FORMAT, prop.Name, prop.GetValue(where));
                }
                else
                {
                    s += (s.Equals(string.Empty) ? string.Empty : " AND ") + string.Format(EQUAL_FORMAT, prop.Name, prop.GetValue(where));
                }
            }

            return s;
        }

        /// <summary>
        /// クラスの検索プロパティよりSQL文作成.
        /// </summary>
        /// <param name="where">検索クラス.</param>
        /// <returns>SQL文.</returns>
        public static string InjectFromSearchCond(object where)
        {
            // パラメータはnullの時、戻る
            if (where == null)
            {
                return string.Empty;
            }

            if (where.GetType() == typeof(string))
            {
                return (string)where;
            }

            string sResult = string.Empty;

            // プロパティを取得する。
            PropertyInfo[] whereProps = where.GetType().GetProperties();

            // ループ
            foreach (PropertyInfo pi in whereProps)
            {
                // 属性を取得する。
                SearchConditionAttribute attr = (SearchConditionAttribute)pi.GetCustomAttribute(typeof(SearchConditionAttribute));

                // 属性がないとき
                if (attr == null)
                {
                    continue;
                }

                // データがない、データを無視する時
                if ((pi.GetValue(where) == null || string.IsNullOrEmpty(pi.GetValue(where).ToString().Trim())) && attr.EmptyIgnore)
                {
                    continue;
                }

                object data = pi.GetValue(where);

                // データがない、データを無視しない時、デフォルト値を使用する
                if (data == null && attr.EmptyDefaultValue != null)
                {
                    data = attr.EmptyDefaultValue;
                }

                string sCheckKey = pi.Name;
                if (!string.IsNullOrEmpty(attr.FilterKey))
                {
                    sCheckKey = attr.FilterKey;
                }

                sResult += sResult.Equals(string.Empty) ? string.Empty : " AND ";

                string sCompareStr = string.Empty;
                string sValueSign = string.Empty;

                switch (attr.FilterCompareType)
                {
                    case CompareType.Equals:
                        sCompareStr = " = ";
                        break;
                    case CompareType.Gt:
                        sCompareStr = " > ";
                        break;
                    case CompareType.Gte:
                        sCompareStr = " >= ";
                        break;
                    case CompareType.Lt:
                        sCompareStr = " < ";
                        break;
                    case CompareType.Lte:
                        sCompareStr = " <= ";
                        break;
                    case CompareType.LeftLike:
                    case CompareType.Like:
                    case CompareType.RightLike:
                        sCompareStr = " LIKE ";
                        break;
                    case CompareType.In:
                        sCompareStr = " IN ";
                        break;
                }

                if (pi.PropertyType == typeof(string))
                {
                    sValueSign = "'";
                }

                if (pi.PropertyType == typeof(DateTime))
                {
                    sValueSign = "'";
                }

                if (pi.PropertyType == typeof(DateTime?))
                {
                    sValueSign = "'";
                }

                string sDataString = string.Empty;

                if (pi.PropertyType == typeof(DateTime))
                {
                    if (data == null)
                    {
                        sDataString = string.Empty;
                    }
                    else
                    {
                        sDataString = ((DateTime)data).ToString(attr.DateTimeFormat);
                    }
                }
                else if (pi.PropertyType == typeof(DateTime?))
                {
                    if (data == null)
                    {
                        sDataString = string.Empty;
                    }
                    else
                    {
                        sDataString = ((DateTime?)data).Value.ToString(attr.DateTimeFormat);
                    }
                }

                // else if (pi.PropertyType == typeof(decimal) || pi.PropertyType == typeof(int) || pi.PropertyType == typeof(long))
                // {
                //    if (data == null)
                //    {
                //        datastring = "0";
                //    }
                //    else
                //    {
                //        datastring = data.ToString()
                //    }
                // }
                else
                {
                    if (data == null)
                    {
                        sDataString = string.Empty;
                    }
                    else
                    {
                        sDataString = data.ToString();
                    }

                    if (attr.FilterCompareType == CompareType.In)
                    {
                        sDataString = sValueSign + sDataString.Replace(",", sValueSign + "," + sValueSign) + sValueSign;
                    }
                }

                switch (attr.FilterCompareType)
                {
                    case CompareType.Equals:
                    case CompareType.Gt:
                    case CompareType.Gte:
                    case CompareType.Lt:
                    case CompareType.Lte:
                        sResult += sCheckKey + sCompareStr + sValueSign + sDataString + sValueSign;
                        break;
                    case CompareType.LeftLike:
                        sResult += sCheckKey + sCompareStr + sValueSign + sDataString + "%" + sValueSign;
                        break;
                    case CompareType.Like:
                        sResult += sCheckKey + sCompareStr + sValueSign + "%" + sDataString + "%" + sValueSign;
                        break;
                    case CompareType.RightLike:
                        sResult += sCheckKey + sCompareStr + sValueSign + "%" + sDataString + sValueSign;
                        break;
                    case CompareType.In:
                        sResult += sCheckKey + sCompareStr + "(" + sDataString + ")";
                        break;
                }
            }

            return sResult;
        }

        /// <summary>
        /// NULLの場合、string.Emptyに変換する.
        /// </summary>
        /// <param name="obj">オブジェクト.</param>
        /// <returns>テキスト.</returns>
        public static string NullToEmpty(object obj)
        {
            if (obj == null)
            {
                return string.Empty;
            }

            switch (obj)
            {
                case DateTime obj1:
                    return ((DateTime)obj).ToString(StaticConst.FULL_DATE);
                default:
                    return obj.ToString();
            }
        }

        /// <summary>
        /// NULLの場合、空白に変換する.
        /// </summary>
        /// <param name="obj">オブジェクト.</param>
        /// <returns>テキスト.</returns>
        public static string NullEmptyToSpace(object obj)
        {
            if (obj == null)
            {
                return " ";
            }

            switch (obj)
            {
                case DateTime obj1:
                    return ((DateTime)obj).ToString(StaticConst.FULL_DATE);
                default:
                    return obj.ToString().PadLeft(1, ' ');
            }
        }

        /// <summary>
        /// キーより検索条件を作成する.
        /// </summary>
        /// <param name="sKeys">キーリスト.</param>
        /// <param name="where">検索条件モデル.</param>
        /// <returns>検索条件SQL.</returns>
        public static string InjectFromKeysModel(string sKeys, object where)
        {
            if (where != null && where.GetType() == typeof(string))
            {
                return (string)where;
            }

            string sResult = string.Empty;

            foreach (string sKey in sKeys.Split(','))
            {
                if (sKey.IndexOf("=") > -1 || sKey.IndexOf(">") > -1 || sKey.IndexOf("<") > -1 || sKey.IndexOf("'") > -1)
                {
                    sResult += sKey;
                    continue;
                }

                // パラメータはnullの時、戻る
                if (where == null)
                {
                    continue;
                }

                PropertyInfo pi = where.GetType().GetProperty(sKey);
                if (pi == null)
                {
                    continue;
                }

                object data = pi.GetValue(where);

                string sValueSign = string.Empty;
                string sCompareStr = " = ";
                string sDataString = string.Empty;

                if (pi.PropertyType == typeof(string))
                {
                    sValueSign = "'";
                }

                if (pi.PropertyType == typeof(DateTime))
                {
                    sValueSign = "'";
                }

                if (pi.PropertyType == typeof(DateTime?))
                {
                    sValueSign = "'";
                }

                if (pi.PropertyType == typeof(DateTime))
                {
                    if (data == null)
                    {
                        sDataString = string.Empty;
                    }
                    else
                    {
                        sDataString = ((DateTime)data).ToString(StaticConst.LONG_DATE_TIME);
                    }
                }
                else if (pi.PropertyType == typeof(DateTime?))
                {
                    if (data == null)
                    {
                        sDataString = string.Empty;
                    }
                    else
                    {
                        sDataString = ((DateTime?)data).Value.ToString(StaticConst.LONG_DATE_TIME);
                    }
                }

                // else if (pi.PropertyType == typeof(decimal) || pi.PropertyType == typeof(int) || pi.PropertyType == typeof(long))
                // {
                //    if (data == null)
                //    {
                //        datastring = "0";
                //    }
                //    else
                //    {
                //        datastring = data.ToString()
                //    }
                // }
                else
                {
                    if (data == null)
                    {
                        sDataString = string.Empty;
                    }
                    else
                    {
                        sDataString = data.ToString();
                    }
                }

                sResult += (string.IsNullOrEmpty(sResult) ? string.Empty : " AND ") + " TRIM(" + sKey + ") " + sCompareStr + sValueSign + sDataString + sValueSign;
            }

            return sResult;
        }

        /// <summary>
        /// イメージをBase64 の数字でエンコードされた等価の文字列形式に変換します.
        /// </summary>
        /// <param name="bmp">イメージ.</param>
        /// <returns>Base64文字.</returns>
        public static string ConvertImageToBase64(Bitmap bmp)
        {
            try
            {
                using (MemoryStream ms = new MemoryStream())
                {
                    bmp.Save(ms, System.Drawing.Imaging.ImageFormat.Png);
                    byte[] arr = new byte[ms.Length];
                    ms.Position = 0;
                    ms.Read(arr, 0, (int)ms.Length);
                    ms.Close();
                    return Convert.ToBase64String(arr);
                }
            }
            catch
            {
                return null;
            }
        }

        /// <summary>
        /// 数値チェック.
        /// </summary>
        /// <param name="value">value.</param>
        /// <returns>結果.</returns>
        public static bool IsNumeric(string value)
        {
            return Regex.IsMatch(value, @"^[+-]?\d*[.]?\d*$");
        }
    }
}
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text.RegularExpressions;
using System.Web;
using System.Web.Optimization;

namespace WinMacOs.Utils.Optimization
{
    /// <summary>
    /// 自定義ＣＳＳＵＲＬ変換.
    /// </summary>
    public class CustomCssRewriteUrlTransform : IItemTransform
    {
        /// <summary>
        /// コンストラクタ.
        /// </summary>
        static CustomCssRewriteUrlTransform()
        {
            Pattern = new Regex(@"url\((.+?)\)", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        }

        /// <summary>
        /// コンストラクタ.
        /// </summary>
        public CustomCssRewriteUrlTransform()
        {
        }

        private static Regex Pattern { get; set; }

        /// <summary>
        /// 処理.
        /// </summary>
        /// <param name="sIncludedVirtualPath">The included virtual path.</param>
        /// <param name="sInput">The input.</param>
        /// <returns>The transformation of content in the bundle response object.</returns>
        public string Process(string sIncludedVirtualPath, string sInput)
        {
            HttpContext httpContext = HttpContext.Current;

            string sPhysicalIncludedPath = httpContext.Server.MapPath(sIncludedVirtualPath);
            string sPhysicalApplicationPath = httpContext.Request.PhysicalApplicationPath;
            string sApplicationPath = httpContext.Request.ApplicationPath;

            if (!sApplicationPath.EndsWith("/"))
            {
                sApplicationPath += "/";
            }

            string sResult = Pattern.Replace(sInput, match =>
            {
                string sData = match.Groups[1].Value;
                // 最初と最後の「"」「'」を取り除く
                if (sData.StartsWith("\"") || sData.StartsWith("'"))
                {
                    sData = sData.Substring(1, sData.Length - 1);
                }

                if (sData.EndsWith("\"") || sData.EndsWith("'"))
                {
                    sData = sData.Substring(0, sData.Length - 1);
                }

                if (sData.StartsWith("data:"))
                {
                    // インラインイメージの場合はそのまま返す。
                    return match.Value;
                }
                else
                {
                    string sRelativeVirtualPath = null;

                    if (sData.StartsWith("/"))
                    {
                        // 絶対パスが指定されている場合は、先頭の区切り文字を除去して、相対パスにする
                        sRelativeVirtualPath = sData.Substring(1, sData.Length - 1);
                    }
                    else
                    {
                        // 相対パスが指定されている場合は、includePathを元に求めた絶対パスを使用し、
                        // 物理アプリケーションパスからの相対パスを求める。
                        Uri uri = new Uri(new Uri(sPhysicalIncludedPath), sData);
                        Uri baseUri = new Uri(sPhysicalApplicationPath);
                        Uri relatedUri = baseUri.MakeRelativeUri(uri);
                        sRelativeVirtualPath = Uri.UnescapeDataString(relatedUri.OriginalString);
                    }

                    // アプリケーションパスに、求めた相対パスを結合して絶対パスにする
                    return $"url({sApplicationPath}{sRelativeVirtualPath})";
                }
            });

            return sResult;
        }
    }
}
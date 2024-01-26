﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualBasic;
using Microsoft.VisualBasic.CompilerServices;
using System.Collections;
using System.Globalization;
using System.Runtime.InteropServices;
using System.Text.RegularExpressions;

namespace WinMacOs.Utility.Utils
{
    public enum Mode
    {
        FullWidthKana = 0,
        HalfWidthKana = 1,
    }

    public static class JapaneseConverter
    {
        public static string KanjiToKana(string source, Mode mode)
        {
            if (string.IsNullOrWhiteSpace(source))
            {
                return source;
            }

            GlobalInterface converter = new GlobalInterface();

            if (mode == Mode.FullWidthKana)
            {
                return converter.GetConversion(
                    "MsIme.Japan",
                    GlobalInterface.FELANG_REQUEST.FELANG_REQ_REV,
                    GlobalInterface.FELANG_CMODE.FELANG_CMODE_KATAKANAOUT,
                    source);
            }
            else if (mode == Mode.HalfWidthKana)
            {
                string fullwidth = converter.GetConversion(
                    "MsIme.Japan",
                    GlobalInterface.FELANG_REQUEST.FELANG_REQ_REV,
                    GlobalInterface.FELANG_CMODE.FELANG_CMODE_KATAKANAOUT,
                    source);
                string sConverter = converter.GetHalfWidthKana(fullwidth);
                string sResult = string.Empty;
                for (int i = 0; i < sConverter.Length; i++)
                {
                    if (Regex.IsMatch(sConverter[i].ToString(), "[A-Za-zｧ-ﾝﾞﾟ0-9 ]"))
                    {
                        sResult += sConverter[i];
                    }
                }
                return sResult;
            }
            else
            {
                throw new InvalidOperationException("Not support mode!");
            }
        }

        public static string HiraganaToKatakana(string input, Mode mode)
        {
            GlobalInterface converter = new GlobalInterface();
            byte[] unicodes = Encoding.GetEncoding("Unicode").GetBytes(input);
            int i;
            for (i = 0; i < unicodes.Length; i += 2)                                     //Each 16 bits.
            {
                int _word = (unicodes[i + 1] << 8) | (unicodes[i] & 0xFF);              //Two byte make a word.
                if (_word >= 0x3041 && _word <= 0x30A0)                                 //In hiragana area
                {
                    _word += 0x60;                                                      //Add difference
                    unicodes[i + 1] = (byte)(_word >> 8);                               //Write back high byte.
                    unicodes[i] = (byte)(_word & 0xFF);                                 //Write back low byte.
                }
            }
            if (mode == Mode.HalfWidthKana)
                return converter.GetHalfWidthKana(Encoding.GetEncoding("Unicode").GetString(unicodes));
            else
                return Encoding.GetEncoding("Unicode").GetString(unicodes);
        }
    }

    #region GlobalInterface
    public class GlobalInterface
    {
        private const string IID_IFELANGUAGE = "019F7152-E6DB-11d0-83C3-00C04FDDB82E";
        private static CultureInfo jpCulture = new CultureInfo("ja-JP");

        [DllImport("ole32.dll", CharSet = CharSet.Unicode, ExactSpelling = true, PreserveSig = false)]
        public static extern void CLSIDFromString(string lpsz, ref Guid id);
        [return: MarshalAs(UnmanagedType.Interface)]
        [DllImport("ole32.dll", ExactSpelling = true, PreserveSig = false)]
        public static extern object CoCreateInstance([MarshalAs(UnmanagedType.LPStruct)] Guid rclsid, [MarshalAs(UnmanagedType.IUnknown)] object pUnkOuter, CLSCTX dwClsContext, [MarshalAs(UnmanagedType.LPStruct)] Guid riid);

        private IFELanguage CreateIFELan(string imeMode)
        {
            IFELanguage language2 = null;
            Guid id = Guid.NewGuid();
            try
            {
                CLSIDFromString(imeMode, ref id);
                Guid riid = new Guid("019F7152-E6DB-11d0-83C3-00C04FDDB82E");

                language2 = (IFELanguage)CoCreateInstance(id, null, CLSCTX.CLSCTX_SERVER, riid);
            }
            catch (Exception ex)
            {
                NLogger.Default.ErrorLog(ex);
            }
            return language2;
        }

        public string GetConversion(string imeMode, FELANG_REQUEST request, FELANG_CMODE mode, string input)
        {
            string result = null;
            int length = 50;
            int inputLength = input.Length;
            int startIndex = 0;
            try
            {
                IFELanguage language = this.CreateIFELan(imeMode);
                if (language != null)
                {
                    IntPtr zero = IntPtr.Zero;
                    IEnumerator enumerator = null;
                    language.Open();
                    List<String> list = new List<String>();
                    while (startIndex < inputLength)
                    {
                        if ((startIndex + length) > inputLength)
                        {
                            length = inputLength - startIndex;
                        }
                        list.Add(input.Substring(startIndex, length));
                        startIndex += length;
                    }
                    StringBuilder builder = new StringBuilder();
                   
                    foreach (var s in list)
                    {
                        int num = language.GetJMorphResult((int)request, (int)mode, s.Length, Marshal.StringToBSTR(s), IntPtr.Zero, ref zero);
                        MORRSLT morrslt = (MORRSLT)Marshal.PtrToStructure(zero, typeof(MORRSLT));
                        //Change target platform to x86 if get memory read, write exception
                        result = Marshal.PtrToStringAuto(morrslt.pwchOutput).Substring(0, morrslt.cchOutput);
                        builder.Append(result);
                    }
                   
                    result = builder.ToString();
                    result = Strings.StrConv(result, VbStrConv.Wide, jpCulture.LCID);
                    Marshal.FreeCoTaskMem(zero);
                    zero = IntPtr.Zero;
                    language.Close();
                }
            }
            catch (Exception ex)
            {
                NLogger.Default.ErrorLog(ex);
            }
            if (result == null)
            {
                result = string.Empty;
            }
            return result;
        }

        public ArrayList GetConversionList(string imeMode, FELANG_REQUEST request, FELANG_CMODE mode, string strInput)
        {
            ArrayList list2 = new ArrayList();
            IFELanguage language = this.CreateIFELan(imeMode);
            if (language != null)
            {
                IntPtr zero = IntPtr.Zero;
                language.Open();
                for (int i = language.GetJMorphResult((int)request, (int)mode, strInput.Length, Marshal.StringToBSTR(strInput), IntPtr.Zero, ref zero); i == 0; i = language.GetJMorphResult((int)request, (int)mode, 0, IntPtr.Zero, IntPtr.Zero, ref zero))
                {
                    MORRSLT morrslt = (MORRSLT)Marshal.PtrToStructure(zero, typeof(MORRSLT));
                    list2.Add(Marshal.PtrToStringAuto(morrslt.pwchOutput).Substring(0, morrslt.cchOutput));
                    Marshal.FreeCoTaskMem(zero);
                    zero = IntPtr.Zero;
                }
                language.Close();
            }
            return list2;
        }

        public int GetConversionModeCaps(string imeMode)
        {
            IFELanguage language = this.CreateIFELan(imeMode);
            int caps = -1;
            if (language != null)
            {
                language.Open();
                language.GetConversionModeCaps(ref caps);
                language.Close();
            }
            return caps;
        }

        public string GetHalfWidthKana(string kanaInput)
        {
            return Strings.StrConv(kanaInput, VbStrConv.Narrow, jpCulture.LCID);
        }

        [Flags]
        public enum CLSCTX
        {
            CLSCTX_ALL = 0x17,
            CLSCTX_DISABLE_AAA = 0x8000,
            CLSCTX_ENABLE_AAA = 0x10000,
            CLSCTX_ENABLE_CODE_DOWNLOAD = 0x2000,
            CLSCTX_FROM_DEFAULT_CONTEXT = 0x20000,
            CLSCTX_INPROC = 3,
            CLSCTX_INPROC_HANDLER = 2,
            CLSCTX_INPROC_HANDLER16 = 0x20,
            CLSCTX_INPROC_SERVER = 1,
            CLSCTX_INPROC_SERVER16 = 8,
            CLSCTX_LOCAL_SERVER = 4,
            CLSCTX_NO_CODE_DOWNLOAD = 0x400,
            CLSCTX_NO_CUSTOM_MARSHAL = 0x1000,
            CLSCTX_NO_FAILURE_LOG = 0x4000,
            CLSCTX_REMOTE_SERVER = 0x10,
            CLSCTX_RESERVED1 = 0x40,
            CLSCTX_RESERVED2 = 0x80,
            CLSCTX_RESERVED3 = 0x100,
            CLSCTX_RESERVED4 = 0x200,
            CLSCTX_RESERVED5 = 0x800,
            CLSCTX_SERVER = 0x15
        }

        [Flags]
        public enum FELANG_CMODE : long
        {
            FELANG_CMODE_AUTOMATIC = 0x8000000L,
            FELANG_CMODE_BESTFIRST = 0x4000L,
            FELANG_CMODE_BOPOMOFO = 0x40L,
            FELANG_CMODE_CONVERSATION = 0x20000000L,
            FELANG_CMODE_FULLWIDTHOUT = 0x20L,
            FELANG_CMODE_HALFWIDTHOUT = 0x10L,
            FELANG_CMODE_HANGUL = 0x80L,
            FELANG_CMODE_HIRAGANAOUT = 0L,
            FELANG_CMODE_KATAKANAOUT = 8L,
            FELANG_CMODE_MERGECAND = 0x1000L,
            FELANG_CMODE_MONORUBY = 2L,
            FELANG_CMODE_NAME = 0x10000000L,
            FELANG_CMODE_NOINVISIBLECHAR = 0x40000000L,
            FELANG_CMODE_NONE = 0x1000000L,
            FELANG_CMODE_NOPRUNING = 4L,
            FELANG_CMODE_PHRASEPREDICT = 0x10000000L,
            FELANG_CMODE_PINYIN = 0x100L,
            FELANG_CMODE_PLAURALCLAUSE = 0x2000000L,
            FELANG_CMODE_PRECONV = 0x200L,
            FELANG_CMODE_RADICAL = 0x400L,
            FELANG_CMODE_ROMAN = 0x2000L,
            FELANG_CMODE_SINGLECONVERT = 0x4000000L,
            FELANG_CMODE_UNKNOWNREADING = 0x800L,
            FELANG_CMODE_USENOREVWORDS = 0x8000L
        }

        public enum FELANG_REQUEST
        {
            FELANG_REQ_CONV = 0x10000,
            FELANG_REQ_RECONV = 0x20000,
            FELANG_REQ_REV = 0x30000
        }

        [ComImport, InterfaceType(ComInterfaceType.InterfaceIsIUnknown), Guid("019F7152-E6DB-11d0-83C3-00C04FDDB82E")]
        private interface IFELanguage
        {
            int Open();
            int Close();
            [PreserveSig]
            int GetJMorphResult(int dwRequest, int dwCMode, int cwchInput, IntPtr pwchInput, IntPtr pfCInfo, ref IntPtr ppResult);
            int GetConversionModeCaps(ref int caps);
            object GetPhonetic(string str, int start, int length, ref string phonetic);
            object GetConversion(string str, int start, int length, ref string phonetic);
        }

        [StructLayout(LayoutKind.Sequential)]
        private struct MORRSLT
        {
            public int dwSize;
            public IntPtr pwchOutput;
            public short cchOutput;
            public IntPtr pUnionReadingString;
            public IntPtr pUnionReadingStringLength;
            public IntPtr pchInputPos;
            public IntPtr pchOutputIdxWDD;
            public IntPtr pUnionReadingIndex;
            public IntPtr paMonoRubyPos;
            public IntPtr pWDD;
            public int cWDD;
            public IntPtr pPrivate;
            public IntPtr BLKBuff;
        }
    }
    #endregion
}

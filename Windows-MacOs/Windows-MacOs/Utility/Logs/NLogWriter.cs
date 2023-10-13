using System;
using System.Diagnostics;
using NLog;

/// <summary>
/// ログ出力用クラス
/// </summary>
/// <remarks></remarks>
public partial class NLogger
{
    private NLog.Logger nlogger;
    /// <summary>
    /// デファクトログオブジェクト.
    /// </summary>
    public static NLogger Default { get; private set; }


    static NLogger()
    {
        Default = new NLogger(NLog.LogManager.GetCurrentClassLogger());
    }

    private NLogger()
    {
        this.nlogger = LogManager.GetCurrentClassLogger();
    }

    private NLogger(NLog.Logger logger)
    {
        this.nlogger = logger;
    }

    /// <summary>
    /// コンストラクタ.
    /// </summary>
    /// <param name="sName">名前.</param>
    public NLogger(string sName)
        : this(NLog.LogManager.GetLogger(sName))
    {
    }
    /// <summary>
    /// Exception Log 出力
    /// </summary>
    /// <param name="ex">Exception</param>
    /// <remarks></remarks>
    public void ErrorLog(Exception ex)
    {
        nlogger.Error(ex, ex.Message);
    }

    public void ErrorLog(string str)
    {
        nlogger.Error(str);
    }

    /// <summary>
    /// Warning Log 出力
    /// </summary>
    /// <param name="strMsg"></param>
    /// <remarks></remarks>
    public void WarningLog(string strMsg)
    {
        nlogger.Warn(strMsg);
    }

    /// <summary>
    /// Infomation Log 出力
    /// </summary>
    /// <param name="strMsg"></param>
    /// <remarks></remarks>
    public void InfoLog(string strMsg)
    {
        nlogger.Info(strMsg);
    }

    /// <summary>
    /// SQL Log 出力（Level:Infomation） ⇒Traceに変更
    /// </summary>
    /// <param name="strMsg"></param>
    /// <remarks></remarks>
    public void SqlLog(string strMsg, LogLevel level)
    {
        nlogger.Trace(strMsg);
    }

}
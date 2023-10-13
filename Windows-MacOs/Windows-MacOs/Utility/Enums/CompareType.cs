namespace WinMacOs.Utility.Enums
{
    /// <summary>
    /// 比較方法.
    /// </summary>
    public enum CompareType
    {
        /// <summary> = .</summary>
        Equals = 0,

        /// <summary> LIKE 'XX%'</summary>
        LeftLike,

        /// <summary> LIKE '%XX%'</summary>
        Like,

        /// <summary> LIKE '%XX'</summary>
        RightLike,

        /// <summary> > </summary>
        Gt,

        /// <summary> >= </summary>
        Gte,

        /// <summary> < </summary>
        Lt,

        /// <summary> <= </summary>
        Lte,

        /// <summary> IN </summary>
        In
    }

}
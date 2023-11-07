using System;
using System.Collections.Generic;
using System.Text;
using System.Web;
using WinMacOs.Utility.SystemModels;

namespace WinMacOs.DataRepository.BaseProvider
{
    public interface IService<T> where T : BaseEntity
    {
        HttpContext Context { get; }

    }
    public interface IService
    {
        HttpContext Context { get; }

    }
}

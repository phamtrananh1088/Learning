using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.Dapper;
using WinMacOs.Models.Manager.Canvas;

namespace WinMacOs.Business.IServices.Manager
{
    public partial interface INoteBookElementaryService<T,T1> : IService where T : ISqlDapper where T1 : ISqlDapper
    {
        
    }
}

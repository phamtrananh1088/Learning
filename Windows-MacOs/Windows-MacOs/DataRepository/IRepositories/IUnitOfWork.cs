using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.DataRepository.BaseProvider;

namespace WinMacOs.DataRepository.IRepositories
{
    public interface IUnitOfWork : IRepository
    {
        Task BeginTransactionAsync();
        Task SaveChangesAsync();
        void Dispose();
        string INSERT_UPDATE_PG { get; set; }
        ISrtRepository SrtRepo { get; }
        
    }
}

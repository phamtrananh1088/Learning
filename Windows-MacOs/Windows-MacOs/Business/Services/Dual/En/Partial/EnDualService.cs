using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Business.Services;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.Dapper;
using WinMacOs.DataRepository.DBManager;
using WinMacOs.DataRepository.EFDbContext;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.DataRepository.Repositories;
using WinMacOs.Models;
using WinMacOs.Models.Enums;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;
using System.Linq;
using System.Data.SqlClient;
using WinMacOs.Models.Dual.En;

namespace WinMacOs.Business.Services.Dual
{
    public partial class EnDualService
    {
        public async Task<EnDualModel> GetData()
        {
            return new EnDualModel { Name = "English" };
        }
    }
}

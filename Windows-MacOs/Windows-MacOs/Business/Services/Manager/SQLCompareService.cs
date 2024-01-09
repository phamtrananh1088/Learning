using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Business.IServices.Manager;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.Dapper;
using WinMacOs.DataRepository.DBManager;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models.Manager.SQLCompare;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Services.Manager
{
    public partial class SQLCompareService : ServiceBase<IUnitOfWork>, ISQLCompareService
    {
        ISqlDapper dapperSource;
        ISqlDapper dapperTarger;
        public SQLCompareService(IUnitOfWork repository)
             : base(repository)
        {
            Init(repository);
            DBServerProvider.SetConnection("Compare_Source", AppSetting.GetDbConnectionString("Compare_Source"));
            DBServerProvider.SetConnection("Compare_Target", AppSetting.GetDbConnectionString("Compare_Target"));
            dapperSource = DBServerProvider.GetSqlDapper("Compare_Source");
            dapperTarger = DBServerProvider.GetSqlDapper("Compare_Target");
        }
        public static ICanvasService Instance
        {
            get { return AutofacContainerModule.GetService<ICanvasService>(); }
        }

    }
}
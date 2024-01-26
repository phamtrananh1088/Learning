using System;
using System.Collections.Generic;
using System.Data.Common;
using System.Data.SqlClient;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Business.IServices.Viewer;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.Dapper;
using WinMacOs.DataRepository.DBManager;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models.Manager.SQLCompare;
using WinMacOs.Utility.Utils;

namespace WinMacOs.Business.Services.Manager
{
    public partial class ViewerService : ServiceBase<IUnitOfWork>, IViewerService
    {

    }
}
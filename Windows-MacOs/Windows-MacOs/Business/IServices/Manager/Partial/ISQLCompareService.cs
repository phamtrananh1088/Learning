using Newtonsoft.Json.Linq;
using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Models;
using WinMacOs.Models.Manager.SQLCompare;
using WinMacOs.Utility.DomainModels;

namespace WinMacOs.Business.IServices.Manager
{
    public partial interface ISQLCompareService
    {
       Task<SQLCompareModel> GetSQLCompareModel();
       Task<SQLCompareModel> GetSQLScript(string schemaName, string tableName);
       Task<List<SQLDependencyModel>> GetSQLDependencies(List<SQLObjectModel> SQLObjects);
        Task<string> GetSQLCreateObject(string type, string schemaName, string name);
    }
}

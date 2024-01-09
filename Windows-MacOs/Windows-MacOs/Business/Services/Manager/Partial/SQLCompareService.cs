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
using WinMacOs.Models.Manager.SQLCompare;
using WinMacOs.Utility.DomainModels;
using WinMacOs.Utility.TableModels;
using WinMacOs.Utility.Utils;
using System.Linq;

namespace WinMacOs.Business.Services.Manager
{
    public partial class SQLCompareService
    {

        public async Task<SQLCompareModel> GetSQLCompareModel()
        {
            var tableSource = await GetAllTables(dapperSource);
            var tableTarget = await GetAllTables(dapperTarger);
            var data = new SQLCompareModel() { SQLTables = tableSource };
            return data;
        }

        private async Task<List<SQLTableModel>> GetAllTables(ISqlDapper dapper)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT  ");
                sqlbuilder.AppendLine("       ob.name AS TableName ");
                sqlbuilder.AppendLine("     , sc.name AS SchemaName ");
                sqlbuilder.AppendLine("  FROM sys.all_objects ob");
                sqlbuilder.AppendLine("  JOIN sys.schemas sc ON ob.schema_id = sc.schema_id");
                sqlbuilder.AppendLine(" WHERE ob.[type] = 'u' ORDER BY ob.name");

                var data = await dapper.QueryListAsync<SQLTableModel>(sqlbuilder.ToString(), new {});
                return data.ToList();
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }

        private async Task<object> GetAllColumns(ISqlDapper dapper, string object_id)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT * FROM sys.all_columns WHERE object_id = @object_id");

                var data = await dapper.QueryListAsync<object>(sqlbuilder.ToString(), new { object_id });
                return data;
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }
        
            private async Task<object> GetAllDefaultConstraints(ISqlDapper dapper, string object_id)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT * from sys.default_constraints WHERE object_id = @object_id");

                var data = await dapper.QueryListAsync<object>(sqlbuilder.ToString(), new { object_id });
                return data;
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }
    }
}

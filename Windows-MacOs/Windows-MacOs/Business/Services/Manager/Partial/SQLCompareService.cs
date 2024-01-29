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
using System.Data.SqlClient;

namespace WinMacOs.Business.Services.Manager
{
    public partial class SQLCompareService
    {

        public async Task<SQLCompareModel> GetSQLCompareModel()
        {
            var tablesSource = await GetAllTables(dapperSource);
            var tablesTarget = await GetAllTables(dapperTarger);
            var dependenciesSource = await GetDependencies(dapperSource);
            var data = new SQLCompareModel()
            {
                SQLTables = tablesSource,
                SQLDependencies = dependenciesSource,
                Source = ((SqlConnectionStringBuilder)connectionStringBuilderSource).DataSource,
                DBNameSource = ((SqlConnectionStringBuilder)connectionStringBuilderSource).InitialCatalog,
                Target = ((SqlConnectionStringBuilder)connectionStringBuilderTarget).DataSource,
                DBNameTarget = ((SqlConnectionStringBuilder)connectionStringBuilderTarget).InitialCatalog,
            };
            return data;
        }

        public async Task<SQLCompareModel> GetSQLScript(string schemaName, string tableName)
        {
            var tableSource = await GetTable(dapperSource, schemaName, tableName);
            if (tableSource != null)
            {
                var columnsSource = await GetAllColumns(dapperSource, schemaName, tableName);
                var keysSource = await GetAllKeys(dapperSource, schemaName, tableName);
                var indexsSource = await GetAllIndexs(dapperSource, schemaName, tableName);
                var defaultConstraintsSource = await GetAllDefaultConstraints(dapperSource, schemaName, tableName);
                tableSource.SQLColumns = columnsSource;
                tableSource.Keys = keysSource;
                tableSource.SQLIndexs = indexsSource;
                tableSource.SQLDefaultConstraint = defaultConstraintsSource;
            }

            var tableTarget = await GetTable(dapperTarger, schemaName, tableName);
            if (tableTarget != null)
            {
                var columnsTarget = await GetAllColumns(dapperTarger, schemaName, tableName);
                var keysTarget = await GetAllKeys(dapperTarger, schemaName, tableName);
                var indexsTarget = await GetAllIndexs(dapperTarger, schemaName, tableName);
                var defaultConstraintsTarget = await GetAllDefaultConstraints(dapperTarger, schemaName, tableName);
                tableTarget.SQLColumns = columnsTarget;
                tableTarget.Keys = keysTarget;
                tableTarget.SQLIndexs = indexsTarget;
                tableTarget.SQLDefaultConstraint = defaultConstraintsTarget;
            }

            string sQLScriptSource = "";
            string sQLScriptTarget = "";
            if (tableTarget == null)
            {
                sQLScriptSource = tableSource.GetSQL();
                tableSource.Status = Status.Add;
            }
            else if (tableSource == null)
            {
                sQLScriptTarget = tableTarget.GetSQL();
                tableTarget.Status = Status.Delete;
            }
            else
            {
                sQLScriptSource = tableSource.GetSQL();
                sQLScriptTarget = tableTarget.GetSQL();
                tableSource.Status = Status.Update;
                tableTarget.Status = Status.Update;
            }
            var data = new SQLCompareModel()
            {
                Source = ((SqlConnectionStringBuilder)connectionStringBuilderSource).DataSource,
                DBNameSource = ((SqlConnectionStringBuilder)connectionStringBuilderSource).InitialCatalog,
                Target = ((SqlConnectionStringBuilder)connectionStringBuilderTarget).DataSource,
                DBNameTarget = ((SqlConnectionStringBuilder)connectionStringBuilderTarget).InitialCatalog,
                SQLScriptSource = sQLScriptSource,
                SQLScriptTarget = sQLScriptTarget
            };
            return data;
        }

        public async Task<List<SQLDependencyModel>> GetSQLDependencies(List<SQLObjectModel> SQLObjects)
        {
            var data = SQLObjects == null ? new List<SQLDependencyModel>() : (await GetDependencies(dapperSource)).Where(x => SQLObjects.Any(y => y.Name == x.Name && y.SchemaName == x.SchemaName) || SQLObjects.Any(y => y.Name == x.DepName && y.SchemaName == x.DepSchemaName)).ToList();
            return data;
        }

        public async Task<string> GetSQLCreateObject(string type, string schemaName, string name)
        {
            string sql = "";
            if (type.ToUpper().Trim() == "U")
            {
                SQLCompareModel model = await GetSQLScript(schemaName, name);
                sql = !string.IsNullOrEmpty(model.SQLScriptSource) ? model.SQLScriptSource : model.SQLScriptTarget;
            }
            else if (type.ToUpper().Trim() == "P" || type.ToUpper().Trim() == "FN")
            {
                var sqlSource = await GetSQLObjectDefinition(dapperSource, type, schemaName, name);
                var sqlTarget = await GetSQLObjectDefinition(dapperTarger, type, schemaName, name);
                sql = !string.IsNullOrEmpty(sqlSource) ? sqlSource : sqlTarget;
            }
            return sql;
        }

        private async Task<string> GetSQLObjectDefinition(ISqlDapper dapper, string type, string schemaName, string name)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT  ");
                sqlbuilder.AppendLine("       Object_definition(ob.object_id) sql ");
                sqlbuilder.AppendLine("  FROM sys.all_objects ob ");
                sqlbuilder.AppendLine("  JOIN sys.schemas sc ON ob.schema_id = sc.schema_id ");
                sqlbuilder.AppendLine(" WHERE ob.[type] = @type ");
                sqlbuilder.AppendLine("   AND ob.name = @name ");
                sqlbuilder.AppendLine("   AND sc.name = @schemaName ");

                var data = await dapper.QueryFirstAsync<string>(sqlbuilder.ToString(), new { type, schemaName, name });
                return data;
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
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

                var data = await dapper.QueryListAsync<SQLTableModel>(sqlbuilder.ToString(), new { });
                return data.ToList();
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }

        private async Task<SQLTableModel> GetTable(ISqlDapper dapper, string schemaName, string tableName)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT  ");
                sqlbuilder.AppendLine("       ob.name AS TableName ");
                sqlbuilder.AppendLine("     , sc.name AS SchemaName ");
                sqlbuilder.AppendLine("  FROM sys.all_objects ob ");
                sqlbuilder.AppendLine("  JOIN sys.schemas sc ON ob.schema_id = sc.schema_id ");
                sqlbuilder.AppendLine(" WHERE ob.[type] = 'u' ");
                sqlbuilder.AppendLine("   AND ob.name = @tableName ");
                sqlbuilder.AppendLine("   AND sc.name = @schemaName ");

                var data = await dapper.QueryFirstAsync<SQLTableModel>(sqlbuilder.ToString(), new { schemaName, tableName });
                return data;
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }

        private async Task<List<SQLColumnModel>> GetAllColumns(ISqlDapper dapper, string schemaName, string tableName)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT ");
                sqlbuilder.AppendLine("       co.name AS ColumnName ");
                sqlbuilder.AppendLine("     , ty.name As TypeName ");
                sqlbuilder.AppendLine("     , co.max_length AS MaxLength ");
                sqlbuilder.AppendLine("     , co.precision AS Precision ");
                sqlbuilder.AppendLine("     , co.scale AS Scale ");
                sqlbuilder.AppendLine("     , co.is_nullable AS IsNull ");
                sqlbuilder.AppendLine("  FROM sys.all_columns co ");
                sqlbuilder.AppendLine("  JOIN sys.types ty ON co.user_type_id = ty.user_type_id ");
                sqlbuilder.AppendLine(" WHERE co.object_id = OBJECT_ID(QUOTENAME(@schemaName) + '.' + QUOTENAME(@tableName),'u') ");

                var data = await dapper.QueryListAsync<SQLColumnModel>(sqlbuilder.ToString(), new { schemaName, tableName });
                return data.ToList();
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }

        private async Task<List<SQLDefaultConstraint>> GetAllDefaultConstraints(ISqlDapper dapper, string schemaName, string tableName)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT ");
                sqlbuilder.AppendLine("       cons.name AS ConstraintName ");
                sqlbuilder.AppendLine("     , colu.name AS ColumnName ");
                sqlbuilder.AppendLine("     , cons.definition AS Definition ");
                sqlbuilder.AppendLine("  FROM sys.default_constraints cons ");
                sqlbuilder.AppendLine("  JOIN sys.all_columns colu ON cons.object_id = colu.default_object_id ");
                sqlbuilder.AppendLine(" WHERE cons.parent_object_id = OBJECT_ID(QUOTENAME(@schemaName) + '.' + QUOTENAME(@tableName),'u') ");
                sqlbuilder.AppendLine(" AND colu.object_id = OBJECT_ID(QUOTENAME(@schemaName) + '.' + QUOTENAME(@tableName),'u') ");

                var data = await dapper.QueryListAsync<SQLDefaultConstraint>(sqlbuilder.ToString(), new { schemaName, tableName });
                return data.ToList();
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }

        private async Task<List<string>> GetAllKeys(ISqlDapper dapper, string schemaName, string tableName)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT col.COLUMN_NAME AS name  ");
                sqlbuilder.AppendLine("  FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS tab ");
                sqlbuilder.AppendLine("  JOIN INFORMATION_SCHEMA.CONSTRAINT_COLUMN_USAGE col ");
                sqlbuilder.AppendLine("    ON col.Constraint_Name = tab.Constraint_Name ");
                sqlbuilder.AppendLine("  JOIN sys.all_columns co ");
                sqlbuilder.AppendLine("    ON col.COLUMN_NAME = co.name ");
                sqlbuilder.AppendLine("   AND col.Table_Schema = tab.Table_Schema ");
                sqlbuilder.AppendLine("   AND col.Table_Name = tab.Table_Name ");
                sqlbuilder.AppendLine(" WHERE tab.Constraint_Type = 'PRIMARY KEY' ");
                sqlbuilder.AppendLine("   AND col.Table_Name = @tableName ");
                sqlbuilder.AppendLine("   AND col.Table_Schema = @schemaName ");
                sqlbuilder.AppendLine("   AND co.object_id = OBJECT_ID(QUOTENAME(@schemaName) + '.' + QUOTENAME(@tableName),'u') ");
                sqlbuilder.AppendLine(" ORDER BY co.column_id ");

                var data = await dapper.QueryListAsync<string>(sqlbuilder.ToString(), new { schemaName, tableName });
                return data.ToList();
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }

        private async Task<List<SQLIndexModel>> GetAllIndexs(ISqlDapper dapper, string schemaName, string tableName)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT ");
                sqlbuilder.AppendLine("       STRING_AGG(CASE inc.is_included_column ");
                sqlbuilder.AppendLine("                    WHEN 0 THEN CONCAT(CHAR(9), QUOTENAME(col.name), CASE inc.is_descending_key WHEN 0 THEN ' ASC' ELSE ' DESC' END) ");
                sqlbuilder.AppendLine("                    ELSE NULL END ");
                sqlbuilder.AppendLine("                 , CONCAT(',', CHAR(13), CHAR(10))) ");
                sqlbuilder.AppendLine("              WITHIN GROUP (ORDER BY inc.index_column_id) AS IndexColumn ");
                sqlbuilder.AppendLine("     , STRING_AGG(CASE inc.is_included_column ");
                sqlbuilder.AppendLine("                    WHEN 1 then QUOTENAME(col.name) ");
                sqlbuilder.AppendLine("                    ELSE NULL END ");
                sqlbuilder.AppendLine("                  , ',')");
                sqlbuilder.AppendLine("              WITHIN GROUP (ORDER BY inc.index_column_id) AS IncludedColumn ");
                sqlbuilder.AppendLine("     , ind.name AS IndexName ");
                sqlbuilder.AppendLine("     , ind.type_desc as IndexType ");
                sqlbuilder.AppendLine("     , ind.is_unique as IsUnique ");
                sqlbuilder.AppendLine("  FROM sys.index_columns inc ");
                sqlbuilder.AppendLine("  JOIN sys.indexes ind ");
                sqlbuilder.AppendLine("    ON inc.object_id = ind.object_id ");
                sqlbuilder.AppendLine("   AND inc.index_id = ind.index_id ");
                sqlbuilder.AppendLine("  JOIN sys.all_columns col ");
                sqlbuilder.AppendLine("    ON inc.column_id = col.column_id ");
                sqlbuilder.AppendLine(" WHERE inc.object_id = OBJECT_ID(QUOTENAME(@schemaName) + '.' + QUOTENAME(@tableName),'u') ");
                sqlbuilder.AppendLine("   AND col.object_id = OBJECT_ID(QUOTENAME(@schemaName) + '.' + QUOTENAME(@tableName),'u') ");
                sqlbuilder.AppendLine("   AND ind.type_desc = 'NONCLUSTERED'");
                sqlbuilder.AppendLine(" GROUP BY ind.name, ind.type_desc, ind.is_unique ");

                var data = await dapper.QueryListAsync<SQLIndexModel>(sqlbuilder.ToString(), new { schemaName, tableName });
                return data.ToList();
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }

        private async Task<List<SQLDependencyModel>> GetDependencies(ISqlDapper dapper)
        {
            try
            {
                StringBuilder sqlbuilder = new StringBuilder();
                sqlbuilder.AppendLine("SELECT DISTINCT ");
                sqlbuilder.AppendLine("       ob.type Type ");
                sqlbuilder.AppendLine("     , sc.name SchemaName ");
                sqlbuilder.AppendLine("     , ob.name Name ");
                sqlbuilder.AppendLine("     , ob2.type DepType ");
                sqlbuilder.AppendLine("     , sc2.name DepSchemaName ");
                sqlbuilder.AppendLine("     , ob2.name DepName ");
                sqlbuilder.AppendLine("  FROM sys.sysdepends dep ");
                sqlbuilder.AppendLine("  JOIN sys.all_objects ob ON dep.id = ob.object_id ");
                sqlbuilder.AppendLine("  JOIN sys.schemas sc ON ob.schema_id = sc.schema_id ");
                sqlbuilder.AppendLine("  JOIN sys.all_objects ob2 ON dep.depid = ob2.object_id ");
                sqlbuilder.AppendLine("  JOIN sys.schemas sc2 ON ob2.schema_id = sc2.schema_id ");

                var data = await dapper.QueryListAsync<SQLDependencyModel>(sqlbuilder.ToString(), new { });
                return data.ToList();
            }
            catch (Exception ex)
            {
                throw ex;
            }
            finally { }
        }
    }
}

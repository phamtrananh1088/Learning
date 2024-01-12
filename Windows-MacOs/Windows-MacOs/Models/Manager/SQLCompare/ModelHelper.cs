using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WinMacOs.Models.Manager.SQLCompare
{
    public static class ModelHelper
    {
        public static readonly string[] NamingTypes = { "bit", "tinyint", "smallint", "int", "real", "bigint", "float", "smallmoney", "money", "text", "ntext", "smalldatetime", "date", "datetime", "sql_variant", "xml", "timestamp" };
        public static string GetSQL<T>(this T t) where T : SQLTableModel
        {
            string sql = CreateSQL<T>(t);
            return sql;
        }

        public static string ColumnDefinition<T>(this T co) where T : SQLColumnModel
        {
            string length =
                NamingTypes.Contains(co.TypeName) ? "" :
                co.Precision > 0 ?
                $"({co.Precision}, {co.Scale})"
                : (co.TypeName == "varchar" || co.TypeName == "nvarchar" || co.TypeName == "varbinary") && co.MaxLength == -1 ? "(MAX)" : (co.TypeName == "nchar" || co.TypeName == "nvarchar" ? $"({co.MaxLength / 2})" : $"({co.MaxLength})");
            return $"\t[{co.ColumnName}] [{co.TypeName}]{length} {(co.IsNull ? "NULL" : "NOT NULL")}";
        }
        public static string IndexDefinition<T>(this T ind, SQLTableModel t) where T : SQLIndexModel
        {
            var include = string.IsNullOrEmpty(ind.IncludedColumn) ? "" : $"\nINCLUDE({ind.IncludedColumn}) ";
            var sql = $@"SET ANSI_PADDING ON

CREATE{(ind.IsUnique ? "UNIQUE" : "")} {ind.IndexType} INDEX [{ind.IndexName}] ON [{t.SchemaName}].[{t.TableName}]
(
{ind.IndexColumn}
){include}WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]";
            return sql;
        }
        public static string DefaultConstraintDefinition<T>(this T ct, SQLTableModel t) where T : SQLDefaultConstraint
        {
            return $"ALTER TABLE [{t.SchemaName}].[{t.TableName}] ADD  CONSTRAINT [{ct.ConstraintName}]  DEFAULT {ct.Definition} FOR [{ct.ColumnName}]";
        }
        private static string CreateSQL<T>(T t) where T : SQLTableModel
        {
            var columns = string.Join(",\n", t.SQLColumns.Select((c) =>
            {
                return c.ColumnDefinition();
            }));
            var keys = string.Join(",\n", t.Keys.Select(k =>
            {
                return $"\t[{k}] ASC";
            }));
            var defaultConstraints = "\n" + string.Join("\n\n", t.SQLDefaultConstraint.Select((d) =>
            {
                return d.DefaultConstraintDefinition(t);
            }));
            var indexs = "\n" + string.Join("\n\n", t.SQLIndexs.Select((d) =>
            {
                return d.IndexDefinition(t);
            }));
            var sql = $@"SET ANSI_NULLS ON

SET QUOTED_IDENTIFIER ON

CREATE TABLE [{t.SchemaName}].[{t.TableName}](
{columns},
 CONSTRAINT [PK_{t.TableName}] PRIMARY KEY CLUSTERED 
(
{keys}
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
{indexs}
{defaultConstraints}
";
            return sql;
        }
        private static string UpdateSQL<T>(T t) where T : SQLTableModel
        {
            throw new NotImplementedException();
        }
        private static string DeleteSQL<T>(T t) where T : SQLTableModel
        {
            throw new NotImplementedException();
        }

    }
}
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WinMacOs.Models.Manager.SQLCompare
{
    public static class ModelHelper
    {
        public static readonly string[] NamingTypes = { "bit", "tinyint", "smallint", "int", "real", "bigint", "float", "smallmoney", "money", "text", "ntext", "smalldatetime", "date", "datetime", "sql_variant", "xml" };
        public static string GetSQL<T>(this T t) where T : SQLTableModel
        {
            string sql;
            switch (t.Status)
            {
                case Status.Add:
                    sql = CreateSQL<T>(t);
                    break;
                case Status.Update:
                    sql = UpdateSQL<T>(t);
                    break;
                case Status.Delete:
                    sql = DeleteSQL<T>(t);
                    break;
                default:
                    sql = "";
                    break;
            }
            return sql;
        }

        public static string ColumnDefinition<T>(this T co) where T : SQLColumnModel
        {
            string length =
                NamingTypes.Contains(co.ColumnName) ? "" :
                co.SQLTypeModel.Precision > 0 ?
                $"({co.SQLTypeModel.Precision}, {co.SQLTypeModel.Scale}) "
                : co.ColumnName == "varchar" && co.SQLTypeModel.MaxLength == -1 ? "(MAX)" : $"({co.SQLTypeModel.MaxLength}) ";
            return $"[{co.ColumnName}] [{co.SQLTypeModel.Name}] {length}{(co.SQLTypeModel.IsNull ? "NULL" : "NOT NULL")} ";
        }

        public static string DefaultConstraintDefinition<T>(this T ct, SQLTableModel t) where T : SQLDefaultConstraint
        {
            return $"ALTER TABLE [{t.SchemaName}].[{t.TableName}] ADD CONSTRAINT [{ct.Name}]  DEFAULT {ct.Definition} FOR [{ct.ColumnName}]";
        }
        private static string CreateSQL<T>(T t) where T : SQLTableModel
        {
            return $@"SET ANSI_NULLS ON

SET QUOTED_IDENTIFIER ON

CREATE TABLE [{t.SchemaName}].[{t.TableName}](
	{string.Join(",\n", t.SQLColumns.Select((c) =>
            {
                return c.ColumnDefinition();
            }))},
 CONSTRAINT [PK_{t.TableName}] PRIMARY KEY CLUSTERED 
(
	{string.Join(" ASC,\n", t.Key)}
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

ALTER TABLE [dbo].[F001_受注マスタ] ADD  CONSTRAINT [DF_F001_受注マスタ_INSERT_TIME]  DEFAULT ('') FOR [INSERT_TIME]
{string.Join("\n\n", t.Default.Select((d) =>
            {
                return d.DefaultConstraintDefinition(t);
            }))}

";
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
using System;
using System.Collections.Generic;
using System.Text;


namespace WinMacOs.Utility.SystemModels
{
    public class BaseEntity
    {
        public string INSERT_TIME { get; set; } = String.Empty;

        public string INSERT_PG { get; set; } = String.Empty;

        public string INSERT_HOST { get; set; } = String.Empty;

        public string INSERT_ID { get; set; } = String.Empty;

        public string UPDATE_TIME { get; set; } = String.Empty;

        public string UPDATE_PG { get; set; } = String.Empty;

        public string UPDATE_HOST { get; set; } = String.Empty;

        public string UPDATE_ID { get; set; } = String.Empty;
    }
}

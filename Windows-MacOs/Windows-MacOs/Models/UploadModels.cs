using System.ComponentModel;
using System.Web;

namespace WinMacOs.Models
{
    public class UploadModel
    {
        public HttpPostedFileBase File { get; set; }
    }
}
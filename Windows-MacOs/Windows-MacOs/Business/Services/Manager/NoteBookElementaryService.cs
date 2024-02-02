using System;
using System.Collections.Generic;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.Business.IServices.Manager;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.IRepositories;
using WinMacOs.Models.Manager.Canvas;

namespace WinMacOs.Business.Services.Manager
{
    public partial class NoteBookElementaryService : ServiceBase<IUnitOfWork>, INoteBookElementaryService
    {
        public NoteBookElementaryService(IUnitOfWork repository)
             : base(repository)
        {
            Init(repository);
        }
        public static INoteBookElementaryService Instance
        {
            get { return AutofacContainerModule.GetService<INoteBookElementaryService>(); }
        }

    }
}
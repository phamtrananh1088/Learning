using System;
using System.Collections.Generic;
using System.Text;
using WinMacOs.Business.IServices.Manager;
using WinMacOs.DataRepository.AutofacManager;
using WinMacOs.DataRepository.BaseProvider;
using WinMacOs.DataRepository.IRepositories;

namespace WinMacOs.Business.Services.Manager
{
    public partial class CanvasService : ServiceBase<IUnitOfWork>, ICanvasService
    {
        public CanvasService(IUnitOfWork repository)
             : base(repository)
        {
            Init(repository);
        }
        public static ICanvasService Instance
        {
            get { return AutofacContainerModule.GetService<ICanvasService>(); }
        }
    }
}
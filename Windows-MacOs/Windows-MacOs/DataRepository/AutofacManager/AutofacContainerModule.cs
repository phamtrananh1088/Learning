using WinMacOs.Utility.Extensions;

namespace WinMacOs.DataRepository.AutofacManager
{
    public class AutofacContainerModule
    {
        public static TService GetService<TService>() where TService : class
        {
            return typeof(TService).GetService() as TService;
        }
    }
}

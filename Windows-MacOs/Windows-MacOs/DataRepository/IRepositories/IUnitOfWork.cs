using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WinMacOs.DataRepository.BaseProvider;

namespace WinMacOs.DataRepository.IRepositories
{
    public interface IUnitOfWork : IRepository
    {
        Task BeginTransactionAsync();
        Task SaveChangesAsync();
        void Dispose();
        string INSERT_UPDATE_PG { get; set; }
        //ISrtRepository SrtRepo { get; }
        IS018Repository S018_ドキュメント定義 { get; }
        IF090Repository F090_ドキュメント管理ファイル { get; }
        IF093Repository F093_一時添付ファイル { get; }
        IM015Repository M015_業者ユーザマスタ { get; }
        IS016Repository S016_メッセージマスタ { get; }
        IF140Repository F140_ログイン認証ファイル { get; }
    }
}

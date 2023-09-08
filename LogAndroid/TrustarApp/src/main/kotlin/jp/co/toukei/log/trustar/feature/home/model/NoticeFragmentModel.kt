package jp.co.toukei.log.trustar.feature.home.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.toLiveData
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.common.CommonViewModel
import jp.co.toukei.log.trustar.db.user.entity.Notice
import jp.co.toukei.log.trustar.feature.account.ui.NoticeItemUI
import jp.co.toukei.log.trustar.repo.NoticeRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NoticeFragmentModel : CommonViewModel() {

    private val noticeRepo = NoticeRepo(Current.userDatabase)

    fun unreadImportant(): LiveData<List<Notice>> {
        return noticeRepo.unreadImportant().toLiveData()
    }

    fun unreadNormal(): LiveData<List<Notice>> {
        return noticeRepo.unreadNormal().toLiveData()
    }

    /*お知らせ・お知らせのデータ取得処理*/
    fun selectByRank(rank: Int, unreadOnly: Boolean): LiveData<List<NoticeItemUI.Item>> {
        val ob = if (unreadOnly) noticeRepo.unreadBy(rank) else noticeRepo.allByRank(rank)
        return ob.map { a -> a.map { NoticeItemUI.Item(it) } }.toLiveData()
    }

    /*お知らせ・データ更新処理*/
    fun markRead(rank: Int) {
        vmScope.launch(Dispatchers.IO) {
            noticeRepo.markRead(rank)
            Current.syncNotice()
        }
    }
}

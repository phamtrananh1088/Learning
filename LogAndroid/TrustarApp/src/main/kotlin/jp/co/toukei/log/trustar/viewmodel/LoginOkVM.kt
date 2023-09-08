package jp.co.toukei.log.trustar.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.compose.ComposeData
import jp.co.toukei.log.trustar.repo.NoticeRepo
import jp.co.toukei.log.trustar.user.LoggedUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginOkVM(
    val user: LoggedUser,
) : ViewModel() {

    private val noticeRepo: NoticeRepo = NoticeRepo(user.userDB)

    fun noticeList(rank: Int, unreadOnly: Boolean): Flowable<List<ComposeData.Notice>> {
        val ob = if (unreadOnly) noticeRepo.unreadBy(rank) else noticeRepo.allByRank(rank)
        return ob.map { a ->
            a.map {
                val c = it.noticeText.orEmpty()
                ComposeData.Notice(
                    id = it.recordId,
                    title = it.noticeTitle.orEmpty(),
                    content = c,
                    isNew = it.unreadFlag && c.isNotEmpty(),
                    useCase = it.useCase
                )
            }
        }
    }

    fun noticeMarkRead(rank: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            noticeRepo.markRead(rank)
            Current.syncNotice()
        }
    }

    val showWeather = mutableStateOf(false)
}

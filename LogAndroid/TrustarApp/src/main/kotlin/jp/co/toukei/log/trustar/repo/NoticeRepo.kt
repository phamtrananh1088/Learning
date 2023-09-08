package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.entity.Notice

const val Important_Notice = 1
const val Normal_Notice = 2

class NoticeRepo(private val userDB: UserDB) {

    fun countUnreadImportant(): Flowable<Int> {
        return userDB.noticeDao().countUnreadByRank(Important_Notice)
    }

    fun countUnreadNormal(): Flowable<Int> {
        return userDB.noticeDao().countUnreadByRank(Normal_Notice)
    }

    /**
     * @param rank 1：重要、2：通常
     */
    fun unreadBy(rank: Int): Flowable<List<Notice>> {
        return userDB.noticeDao().unreadByRank(rank)
    }

    fun allByRank(rank: Int): Flowable<List<Notice>> {
        return userDB.noticeDao().selectByRank(rank)
    }

    fun markRead(rank: Int) {
        userDB.noticeDao().apply {
            markAllReadByRank(rank, System.currentTimeMillis())
        }
    }
}

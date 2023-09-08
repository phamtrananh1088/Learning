package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.entity.Notice

class NoticeRepo(private val userDB: UserDB) {

    fun countUnreadImportant(): Int {
        return userDB.noticeDao().countUnreadByRank(1)
    }

    fun unreadImportant(): Flowable<List<Notice>> {
        return unreadBy(1)
    }

    fun unreadNormal(): Flowable<List<Notice>> {
        return unreadBy(2)
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

package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.entity.CollectionGroup
import jp.co.toukei.log.trustar.db.user.entity.CollectionResult

class CollectionRepo(private val userDB: UserDB) {

    fun collectionItems(): Flowable<List<CollectionGroup>> {
        return userDB.collectionItemDao().selectAll()
    }

    fun collectionResults(allocationNo: String, allocationRowNo: Int): CollectionResult? {
        return userDB.collectionResultDao().getResult(allocationNo, allocationRowNo)
    }

    fun saveResult(collectionResult: CollectionResult) {
        userDB.collectionResultDao().insertOrReplace(collectionResult)
    }
}

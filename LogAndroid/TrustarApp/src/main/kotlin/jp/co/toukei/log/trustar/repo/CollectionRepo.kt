package jp.co.toukei.log.trustar.repo

import io.reactivex.rxjava3.core.Flowable
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.trustar.db.user.db.UserDB
import jp.co.toukei.log.trustar.db.user.entity.CollectionGroup
import jp.co.toukei.log.trustar.db.user.entity.CollectionResult
import java.util.Optional

class CollectionRepo(private val userDB: UserDB) {

    private fun collectionItems(): Flowable<List<CollectionGroup>> {
        return userDB.collectionItemDao().selectAll()
    }

    private fun collectionResults(
        allocationNo: String,
        allocationRowNo: Int,
    ): Flowable<Optional<CollectionResult>> {
        return userDB.collectionResultDao().getResult(allocationNo, allocationRowNo)
    }

    fun saveResult(collectionResult: CollectionResult) {
        userDB.collectionResultDao().insertOrReplace(collectionResult)
    }

    fun collectionResultsWithGroup(
        allocationNo: String,
        allocationRowNo: Int,
    ): Flowable<Map<CollectionGroup, List<CollectionResult.Row>>> {
        return Flowable.defer {
            Flowable.combineLatest(
                collectionItems(),
                collectionResults(allocationNo, allocationRowNo)
            ) { g, r ->
                val rows = r.orElseNull()
                    ?.rows()
                    ?.groupBy { it.collectionClass }
                g.associateWith {
                    rows?.get(it.collectionClass) ?: emptyList()
                }
            }
        }
    }
}

package jp.co.toukei.log.lib.util

import androidx.compose.runtime.Immutable
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.disposables.DisposableContainer
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.chains
import jp.co.toukei.log.lib.combineLatest
import jp.co.toukei.log.lib.filterListByQuery
import jp.co.toukei.log.lib.mapToArray
import jp.co.toukei.log.lib.observeOnComputation
import jp.co.toukei.log.lib.queryStr
import jp.co.toukei.log.lib.replayThenAutoConnect
import jp.co.toukei.log.lib.subscribeOnIO
import jp.co.toukei.log.lib.toFlowable
import jp.co.toukei.log.lib.toResultWithLoading
import third.Result
import java.util.concurrent.TimeUnit

@Immutable
data class UserCheck<out T>(
    @JvmField val user: T,
    @JvmField val checked: Boolean,
    @JvmField val keepState: Boolean,
)

class CheckUser<T>(
    private val disposableContainer: DisposableContainer,
    private val userId: T.() -> String,
    private val userName: T.() -> String?,
    private val keepState: (T) -> Boolean,
    private val fetch: BehaviorProcessor<Flowable<out List<List<T>>>>,
) {

    private val currentUser = BehaviorProcessor.create<List<UserCheck<T>>>()

    private val selectedDirtyPool = BehaviorProcessor.createDefault(emptySet<String>())

    fun setQueryText(query: CharSequence?) {
        queryText.onNext(query ?: "")
    }

    private val excludedIds = BehaviorProcessor.createDefault(emptySet<String>())

    fun excludeIds(ids: Iterable<String>?) {
        excludedIds.onNext(ids?.toSet() ?: emptySet())
    }

    private val groups: Flowable<Result<List<List<T>>>> = fetch
        .switchMap { it.toResultWithLoading().subscribeOnIO() }
        .replayThenAutoConnect(disposableContainer)

    private val queryText: BehaviorProcessor<CharSequence> = BehaviorProcessor.createDefault("")

    private val lastList: Flowable<List<List<UserCheck<T>>>> = Flowable
        .combineLatest(
            groups.switchMap { it.toFlowable().onErrorComplete() },
            selectedDirtyPool.observeOnComputation(),
            excludedIds.observeOnComputation(),
            currentUser.startWithItem(emptyList()).observeOnComputation()
        ) { g, s, e, c ->
            g.map { l ->
                l.mapNotNull {
                    val userId = it.userId()
                    if (!e.contains(userId)) {
                        val f = c.firstOrNull { cu -> cu.user.userId() == it.userId() }
                        UserCheck(
                            user = it,
                            checked = s.contains(userId),
                            keepState = f?.keepState ?: keepState(it)
                        )
                    } else null
                }
            }
        }
        .throttleLatest(100, TimeUnit.MILLISECONDS, true)
        .replayThenAutoConnect(disposableContainer)

    private val merge = Flowable
        .combineLatest(
            currentUser.startWithItem(emptyList()).observeOnComputation(),
            lastList
        ) { c, l ->
            c.chains(l.flatten()).distinctBy { it.user.userId() }
        }
        .throttleLatest(100, TimeUnit.MILLISECONDS, true)
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val checkedResult: Flowable<List<UserCheck<T>>> = Flowable
        .combineLatest(
            merge,
            selectedDirtyPool.observeOnComputation()
        ) { l, s ->
            l.filter { it.checked && s.contains(it.user.userId()) }
        }
        .throttleLatest(100, TimeUnit.MILLISECONDS, true)
        .replayThenAutoConnect(disposableContainer)

    @JvmField
    val state: Flowable<out Result<Any>> = groups

    @JvmField
    val displayListR: Flowable<List<List<UserCheck<T>>>> = lastList
        .switchMap { g ->
            g.mapToArray {
                Flowable.just(it).filterListByQuery(queryText, 100) { e, w ->
                    e.user.userName()?.queryStr()?.contains(w.queryStr()) ?: false
                }
            }.combineLatest()
        }
        .replayThenAutoConnect(disposableContainer)

    private val todo1 = BehaviorProcessor.createDefault<List<UserCheck<T>>>(emptyList())
    private val todo2 = BehaviorProcessor.createDefault<List<T>>(emptyList())

    init {
        merge.subscribe(todo1)
        checkedResult.map { it.map { it.user } }.subscribe(todo2)
    }

    private val selectedIds = androidx.collection.ArraySet<String>()

    fun setCurrentSelected(selection: Iterable<T>) {
        val ll = selection.map { UserCheck(it, true, keepState(it)) }
        ll.forEach { selectedIds += it.user.userId() }
        currentUser.onNext(ll)
        selectedDirtyPool.onNext(selectedIds)
    }

    fun selectedUser() = todo2.value

    fun selectId(id: String, select: Boolean): Boolean {
        val f = todo1.value?.firstOrNull { it.user.userId() == id }
        return if (f != null && !f.keepState) {
            if (select) selectedIds += id else selectedIds -= id
            selectedDirtyPool.onNext(selectedIds)
            true
        } else false
    }
}

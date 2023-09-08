package jp.co.toukei.log.trustar.feature.home.fragment

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.fragment.app.FragmentActivity
import jp.co.toukei.log.lib.common.CommonFragment
import jp.co.toukei.log.lib.common.materialAlertDialogBuilder
import jp.co.toukei.log.lib.getActivityModel
import jp.co.toukei.log.trustar.*
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.BinDetail.Companion.checkIfMisdelivered
import jp.co.toukei.log.trustar.db.user.entity.BinHeader
import jp.co.toukei.log.trustar.db.user.entity.Work
import jp.co.toukei.log.trustar.db.user.entity.bin.Place
import jp.co.toukei.log.trustar.feature.home.activity.HomeActivity
import jp.co.toukei.log.trustar.feature.home.data.BinDetailForWork
import jp.co.toukei.log.trustar.feature.home.model.HomeModel
import jp.co.toukei.log.trustar.feature.home.ui.OperationFragmentUI
import jp.co.toukei.log.trustar.feature.nimachi.fragment.SheetList
import jp.co.toukei.log.trustar.other.WorkBin

/**
 * 作業入力
 */
class OperationFragment : CommonFragment<HomeActivity>() {

    private val homeModel: HomeModel by lazy { getActivityModel() }

    private fun Context.startWorkDialog(
        work: Work,
        positiveClick: DialogInterface.OnClickListener
    ) {
        materialAlertDialogBuilder {
            setTitle(getString(R.string.work_start_by_s1_alert_title, work.workNm))
            setMessage(R.string.work_start_alert_msg)
            setNegativeButton(R.string.cancel, null)
            setPositiveButton(R.string.start, positiveClick)
        }.show()
    }

    private fun Context.startNewWork(binHeader: BinHeader, work: Work, place: Place) {
        startWorkDialog(work) { _, _ ->
            homeModel.startNewAddWork(binHeader, work, place, Current.lastLocation)
        }
    }

    private fun Context.startWork(binDetail: BinDetail, work: Work) {
        /*作業入力・複数ある作業内容のボタンの押下時に起動されるイベント*/
        startWorkDialog(work) { _, _ ->
            val location = Current.lastLocation
            if (location != null && checkIfMisdelivered(binDetail, location)) {
                materialAlertDialogBuilder {
                    setTitle(R.string.work_misdelivered_alert_title)
                    setMessage(R.string.work_misdelivered_alert_msg)
                    setNegativeButton(R.string.cancel, null)
                    setPositiveButton(R.string.start) { _, _ ->
                        homeModel.startWork(binDetail, work, location)
                    }
                }.show()
            } else {
                homeModel.startWork(binDetail, work, location)
            }
        }
    }

    private fun setClick(h: OperationFragmentUI, owner: FragmentActivity, binDetail: BinDetail?) {
        if (binDetail == null) {
            h.clickInfo.setOnClickListener(null)
            h.nimachi.setOnClickListener(null)
        } else {
            h.clickInfo.setOnClickListener {
                BinDetailsDialogFragment().apply { arg = binDetail }.showAt(owner)
            }
            h.nimachi.setOnClickListener {
                /*作業入力・待機・附帯登録押下時に起動されるイベント*/
                SheetList().apply { arg = binDetail }.showAt(owner)
            }
        }
    }

    override fun createView(owner: HomeActivity): View {
        val h = OperationFragmentUI(owner)

        homeModel.lastFormattedDataLiveData.observe(h.dateTextView.setTextObserver())

        homeModel.dataSource.currentUnfinishedWork.observeNullable {
            if (it == null) {
                h.setData(null, null, false, null)
            } else {
                val (w, array) = it

                when (w) {
                    is WorkBin.Add.New -> {
                        val name = "追加作業"
                        val place = Place(name)
                        h.setData(array, null, place, false) { work ->
                            startNewWork(w.binHeader, work, place)
                        }
                        setClick(h, owner, null)
                    }
                    is WorkBin.Add.Finished -> {
                        val binDetail = w.binDetail
                        h.setData(array, BinDetailForWork(owner, binDetail), false) { work ->
                            startWork(binDetail, work)
                        }
                        setClick(h, owner, binDetail)
                    }
                    is WorkBin.Bin -> {
                        val binDetail = w.binDetail
                        val isWorking = binDetail.statusType.isWorking()
                        h.setData(array, BinDetailForWork(owner, binDetail), isWorking) { work ->
                            if (isWorking) {
                                homeModel.endWork(binDetail)
                            } else {
                                startWork(binDetail, work)
                            }
                        }
                        setClick(h, owner, binDetail)
                    }
                }
            }
        }
        homeModel.working.observeNullable {
            if (it == null && homeModel.lastSelectedItemId == R.string.home_navigation_operation) {
                homeModel.postMenuId(R.string.home_navigation_deliver, false)
            }
        }
        return h.coordinatorLayout
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) homeModel.dataSource.unsetAdd()
    }
}

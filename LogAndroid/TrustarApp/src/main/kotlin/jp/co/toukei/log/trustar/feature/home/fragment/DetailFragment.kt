package jp.co.toukei.log.trustar.feature.home.fragment

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.toLiveData
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor
import jp.co.toukei.log.lib.applyView
import jp.co.toukei.log.lib.common.CommonFragment
import jp.co.toukei.log.lib.common.enableIf
import jp.co.toukei.log.lib.common.materialAlertDialogBuilder
import jp.co.toukei.log.lib.common.setLayoutParams
import jp.co.toukei.log.lib.common.showOrGone
import jp.co.toukei.log.lib.common.snackbar
import jp.co.toukei.log.lib.getViewModel
import jp.co.toukei.log.lib.isLoading
import jp.co.toukei.log.lib.lazyViewModel
import jp.co.toukei.log.lib.observeNullable
import jp.co.toukei.log.lib.orElseNull
import jp.co.toukei.log.lib.setOnLostFocusHideIME
import jp.co.toukei.log.lib.toBindHolder
import jp.co.toukei.log.lib.util.BindHolder
import jp.co.toukei.log.lib.util.Combine2LiveData
import jp.co.toukei.log.lib.util.ProgressDialogWrapper
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.lib.withBoundValue
import jp.co.toukei.log.trustar.Current
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import jp.co.toukei.log.trustar.db.user.entity.WorkStatus
import jp.co.toukei.log.trustar.feature.home.activity.HomeActivity
import jp.co.toukei.log.trustar.feature.home.data.BinDetailForListItem
import jp.co.toukei.log.trustar.feature.home.fragment.binheader.BinStartDialog
import jp.co.toukei.log.trustar.feature.home.model.BinHeaderModel
import jp.co.toukei.log.trustar.feature.home.model.HomeModel
import jp.co.toukei.log.trustar.feature.home.ui.BinMeterInputUI
import jp.co.toukei.log.trustar.feature.home.ui.DeliverFragmentUI
import jp.co.toukei.log.trustar.feature.home.ui.WorkItemUI
import jp.co.toukei.log.trustar.getLastWeather
import jp.co.toukei.log.trustar.hasFinished
import jp.co.toukei.log.trustar.isWorkingOrMoving
import jp.co.toukei.log.trustar.setTextObserver
import jp.co.toukei.log.trustar.showAt
import splitties.views.textResource
import java.util.Optional


/**
 * 配送計画
 * - 作業入力、追加作業 [OperationFragment]
 * - 配送先詳細 [BinDetailsDialogFragment]
 * - 運行開始押下時 [BinStartDialog]
 */
class DetailFragment : CommonFragment<HomeActivity>() {

    private val snackBarLiveData = MutableLiveData<Int?>()
    private val binHeaderModel by lazyViewModel<BinHeaderModel>()

    override fun createView(owner: HomeActivity): View {
        val homeModel = owner.getViewModel<HomeModel>()

        homeModel.dataSource.selectedBinHeader.observeNullable { bs ->
            binHeaderModel.setAllocationNo(bs?.header?.allocationNo)
        }

        val u = DeliverFragmentUI(owner)

        homeModel.lastFormattedDataLiveData.observe(u.dateTextView.setTextObserver())

        snackBarLiveData.value = null
        snackBarLiveData.observeNonNull {
            u.coordinatorLayout.snackbar(it)
        }
        val inAutoMode = homeModel.inAutoMode
        inAutoMode.observeNullable {
            val n = it ?: false
            u.autoModeSwitch.apply {
                isChecked = n
                textResource = if (n) R.string.work_mode_automatic else R.string.work_mode_manual
            }
        }
        homeModel.enableAddWorkBin.observeNonNull {
            u.workAddEnabled(it)
        }
        u.swipeRefreshLayout.setOnRefreshListener {
            binHeaderModel.reload()
        }
        binHeaderModel.reloadState.observeNonNull {
            u.swipeRefreshLayout.isRefreshing = it.isLoading()
        }

        binHeaderModel.endState.observeNonNull {
            val context = u.coordinatorLayout.context
            if (it == 1) {
                endProgressDialogWrapper.showProgressDialog(context, R.string.data_end_data_sending)
                    .setMessage(context.getString(R.string.data_end_data_sending_msg))
            } else {
                endProgressDialogWrapper.cancel()
                if (it == 3) {
                    context.materialAlertDialogBuilder {
                        setTitle(R.string.server_connection_err)
                        setMessage(R.string.sync_connection_err_alert_msg)
                        setNegativeButton(R.string.cancel) { _, _ ->
                            binHeaderModel.sync(true)
                        }
                        setPositiveButton(R.string.data_resend) { _, _ ->
                            binHeaderModel.sync()
                        }
                        setCancelable(false)
                    }.show()
                }
            }
        }

        val moveTop = BehaviorProcessor.create<Optional<BinDetailForListItem>>()
        moveTop.scan<Pair<BinDetailForListItem?, BinDetailForListItem?>>(
            Pair(null, null)
        ) { t1, t2 -> Pair(t1.second, t2.orElseNull()) }
            .map { (t1, t2) ->
                t1 != null && t2 != null &&
                        t2.detail.statusType.isWorkingOrMoving() &&
                        !t1.sameItem(t2)
            }
            .toLiveData()
            .observeNonNull {
                if (it) u.list.smoothScrollToPosition(0)
            }

        val adapter = SugoiAdapter(1)
        val listBlock = SugoiAdapter.DiffListBlock(WorkItemUI.Diff()) {
            moveTop.onNext(Optional.ofNullable(it.firstOrNull()))
        }.apply { attachToAdapter(adapter) }
        val listCreator = object : SugoiAdapter.Creator<BinDetailForListItem> {
            override fun onCreateViewHolder(
                context: Context,
                parent: ViewGroup,
            ): BindHolder<BinDetailForListItem> {
                return WorkItemUI(parent.context).applyView {
                    setLayoutParams()
                    setOnClickListener { view ->
                        it.withBoundValue {
                            onItemClick?.invoke(view, this)
                        }
                    }
                    it.clickInfo.setOnClickListener { _ ->
                        it.withBoundValue {
                            BinDetailsDialogFragment().apply {
                                arg = detail
                            }.showAt(owner)
                        }
                    }
                }.toBindHolder()
            }

            var onItemClick: (View.(BinDetailForListItem) -> Unit)? = null
        }

        Combine2LiveData(
            homeModel.dataSource.binDetailListOfSelectedBinHeader,
            Current.loggedUser.binLocationTask.throttleLatestRecord(2)
                .concatWith(Flowable.just(Optional.ofNullable(null)))
                .toLiveData()
        ) { a, b ->
            a?.asSequence()?.map { i -> BinDetailForListItem(i, b?.orElseNull()?.location) }
        }.observeNonNull { listBlock.submitList(it, listCreator) }

        u.list.adapter = adapter

        binHeaderModel.binHeaderLiveData.observeNullable {
            val bs = it?.orElseNull()
            val header = bs?.header
            val binStatus = bs?.status?.type

            u.operation.text = header?.allocationNm
            u.carModel.text = bs?.truck?.truckNm

            u.autoModeSwitch.apply {
                showOrGone(binStatus == BinStatus.Type.Working && Current.userInfo.geofenceUseFlag)
                setOnClickListener {
                    binHeaderModel.setAutoMode(isChecked)
                    homeModel.showAutoModeDialog.value = true
                }
            }
            u.workAdd.apply {
                if (header == null || bs.status.hasFinished()) setOnClickListener(null)
                else setOnClickListener { homeModel.navigationWorkAddNew(header) }
            }
            u.setSwitchAreaColor(bs?.status)

            val switchClick = if (header == null) null else View.OnClickListener {
                val allocationNo = header.allocationNo

                when (binStatus) {
                    BinStatus.Type.Ready -> {
                        /*運行開始・運行開始押下時のイベント*/
                        if (homeModel.countBinHeader(BinStatus.Type.Working) > 0) {
                            snackBarLiveData.postValue(R.string.start_when_started_operation_exists_msg)
                        } else {
                            /*配送計画一覧・運行開始押下時に起動されるイベント*/
                            BinStartDialog().apply { arg = allocationNo }.showAt(owner)
                        }
                    }

                    BinStatus.Type.Working -> {
                        /*配送計画一覧・運行終了押下時に起動されるイベント*/
                        endBinHeader(
                            owner,
                            homeModel.countBinDetail(WorkStatus.Type.Ready, allocationNo),
                            homeModel.countBinDetail(WorkStatus.Type.Working, allocationNo)
                        )
                    }

                    else -> {}
                }
            }
            u.binStatusSwitch.setOnClickListener(switchClick)

            listCreator.onItemClick = { binItem ->
                val notAutoMode = !(inAutoMode.value ?: false)
                when (binStatus) {
                    BinStatus.Type.Finished -> if (notAutoMode) snackBarLiveData.postValue(R.string.work_after_operation_finished_tip)
                    BinStatus.Type.Ready -> if (notAutoMode) snackBarLiveData.postValue(R.string.work_before_operation_start_tip)
                    BinStatus.Type.Working -> {
                        val detail = binItem.detail
                        val status = binItem.status.type
                        when {
                            status is WorkStatus.Type.Moving || status is WorkStatus.Type.Working -> {
                                if (notAutoMode) homeModel.navigationBinDetail(detail)
                            }

                            homeModel.countBinDetail(
                                WorkStatus.Type.Working,
                                detail.allocationNo
                            ) > 0 -> {
                                if (notAutoMode) snackBarLiveData.postValue(R.string.move_work_when_working_exists_msg)
                            }

                            status is WorkStatus.Type.Finished -> {
                                if (notAutoMode) homeModel.navigationBinDetail(detail)
                            }

                            status is WorkStatus.Type.Ready -> {
                                if (notAutoMode) {
                                    context.materialAlertDialogBuilder {
                                        setTitle(R.string.work_move_to_destination_title)
                                        setMessage(R.string.work_move_to_destination_msg)
                                        setNegativeButton(R.string.cancel, null)
                                        setPositiveButton(R.string.work_move) { _, _ ->
                                            homeModel.navigationBinDetail(detail)
                                        }
                                    }.show()
                                } else if (detail.placeLocation == null) {
                                    val l = Current.lastLocation
                                    if (l != null) {
                                        context.materialAlertDialogBuilder {
                                            setTitle(R.string.work_start_without_dest_location_msg)
                                            setNegativeButton(R.string.no, null)
                                            setPositiveButton(R.string.yes) { _, _ ->
                                                homeModel.startInAutoMode(
                                                    binItem.binDetailAndStatus,
                                                    l
                                                )
                                            }
                                        }.show()
                                    }
                                }
                            }
                        }
                    }

                    else -> {}
                }
            }
        }
        return u.coordinatorLayout
    }

    private val endProgressDialogWrapper = ProgressDialogWrapper()

    /*配送計画一覧・運行終了押下時に起動されるイベント*/
    /*配送計画一覧・運行終了データ更新処理*/
    private fun endBinHeader(
        owner: FragmentActivity,
        countReady: Int,
        countWorking: Int,
    ) {
        if (countWorking > 0) {
            snackBarLiveData.postValue(R.string.end_when_working_exists_msg)
            return
        }
        val weather = owner.getLastWeather()
        if (weather == null) {
            snackBarLiveData.postValue(R.string.weather_need_to_be_set_msg)
            return
        }
        val header = binHeaderModel.binHeaderLiveData.value?.orElseNull()?.header ?: return
        owner.materialAlertDialogBuilder {
            if (countReady > 0) {
                setTitle(R.string.end_when_ready_exists_alert_title)
                setMessage(R.string.end_when_ready_exists_alert_msg)
            } else {
                setTitle(R.string.confirm)
                setMessage(R.string.operation_end_alert_msg)
            }
            setNegativeButton(R.string.cancel, null)
            setPositiveButton(R.string.operation_end) { _, _ ->
                if (Current.userInfo.meterInputEnabled) {
                    val dialog = BottomSheetDialog(owner)
                    val currentValue = MutableLiveData<Int?>()
                    val dialogView = BinMeterInputUI(owner).apply {
                        title1.textResource = R.string.incoming_meter_input_title
                        title2.textResource = R.string.incoming_meter_in_km

                        currentValue.observeNullable(viewLifecycleOwner) {
                            confirm.enableIf(it != null)
                        }
                        input.doAfterTextChanged {
                            currentValue.value = it?.toString()?.toIntOrNull()
                        }
                        input.setText(header.incomingMeter?.toString())
                        input.setOnLostFocusHideIME()
                        cancel.setOnClickListener { dialog.dismiss() }
                        confirm.setOnClickListener {
                            currentValue.value?.let { km ->
                                binHeaderModel.endBin(weather, Current.lastLocation, km)
                                dialog.dismiss()
                            }
                        }
                    }
                    dialog.setContentView(dialogView.view)
                    dialog.show()
                } else {
                    binHeaderModel.endBin(weather, Current.lastLocation, null)
                }
            }
        }.show()
    }
}

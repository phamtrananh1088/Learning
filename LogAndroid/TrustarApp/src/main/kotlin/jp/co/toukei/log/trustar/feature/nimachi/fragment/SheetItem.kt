package jp.co.toukei.log.trustar.feature.nimachi.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.load.engine.DiskCacheStrategy
import jp.co.toukei.log.lib.*
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.lib.util.SugoiAdapter
import jp.co.toukei.log.lib.util.SugoiAdapter.ValueBlock
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.FullScreenDialogFragment
import jp.co.toukei.log.trustar.db.user.entity.BinDetail
import jp.co.toukei.log.trustar.db.user.entity.IncidentalHeader
import jp.co.toukei.log.trustar.defaultSizeTextDrawable
import jp.co.toukei.log.trustar.deprecated.startActivityForResult
import jp.co.toukei.log.trustar.feature.nimachi.data.TimeItem
import jp.co.toukei.log.trustar.feature.nimachi.data.additionalTime
import jp.co.toukei.log.trustar.feature.nimachi.data.nimachiTime
import jp.co.toukei.log.trustar.feature.nimachi.ui.LabelUI
import jp.co.toukei.log.trustar.feature.nimachi.ui.RecyclerViewButton
import jp.co.toukei.log.trustar.feature.nimachi.ui.SheetHeaderUI
import jp.co.toukei.log.trustar.feature.nimachi.ui.TimeItemUI
import jp.co.toukei.log.trustar.feature.nimachi.vm.SheetItemVM
import jp.co.toukei.log.trustar.feature.sign.SignActivity
import jp.co.toukei.log.trustar.showAt
import jp.co.toukei.log.trustar.withGlide
import splitties.dimensions.dip
import splitties.views.dsl.core.add
import splitties.views.dsl.core.imageView
import splitties.views.dsl.core.margin
import splitties.views.dsl.core.matchParent
import splitties.views.padding
import splitties.views.textResource
import third.Result

/**
 * 待機・附帯作業詳細
 *
 * - 編集[SheetItemEdit]
 * - 一覧[SheetList]
 */
class SheetItem : FullScreenDialogFragment<SheetItem.Arg>() {

    class Arg(
        @JvmField val binDetail: BinDetail,
        @JvmField val listItem: IncidentalHeader
    )

    override fun createView(owner: FragmentActivity): View {
        val u = RecyclerViewButton(owner)
        u.button.textResource = R.string.incidental_back_sheet_list

        u.button.setOnClickListener {
            /*待機・附帯作業詳細・一覧に戻る押下時に起動されるイベント*/
            dismiss()
            arg?.run { SheetList().also { it.arg = binDetail }.showAt(owner) }
        }

        val adapter = SugoiAdapter(10)
        val headCreator = sugoiCreator {
            SheetHeaderUI(this).apply {
                edit.textResource = R.string.edit
                edit.setOnClickListener {
                    /*待機・附帯作業詳細・編集を押下時に起動されるイベント*/
                    arg?.run {
                        withBoundValue {
                            val target = editTarget
                            if (target != null)
                                SheetItemEdit().also {
                                    it.arg = SheetItemEdit.Arg(target)
                                }.showAt(owner)
                        }
                    }
                }
            }
        }
        val labelCreator = LabelUI.Creator()
        val timeCreator = sugoiCreator {
            TimeItemUI(this)
        }

        val headBlock = ValueBlock(headCreator)
        val nimachiPlaceholder = textPlaceholderBlock(R.string.unregistered_placeholder, true)
        val additionalPlaceholder = textPlaceholderBlock(R.string.unregistered_placeholder, true)

        val dateTimeDiffCallback = TimeItemUI.Diff<TimeItem>()

        val nimachiBlock = SugoiAdapter.DiffListBlock(dateTimeDiffCallback) {
            nimachiPlaceholder.offline = it.isNotEmpty()
        }
        val additionalBlock = SugoiAdapter.DiffListBlock(dateTimeDiffCallback) {
            additionalPlaceholder.offline = it.isNotEmpty()
        }

        headBlock.attachToAdapter(adapter)
        ValueBlock(labelCreator).applyValue(R.string.incidental_sheet_await)
            .attachToAdapter(adapter)
        nimachiBlock.attachToAdapter(adapter)
        nimachiPlaceholder.attachToAdapter(adapter)

        ValueBlock(labelCreator).applyValue(R.string.incidental_sheet_addition_work)
            .attachToAdapter(adapter)
        additionalBlock.attachToAdapter(adapter)
        additionalPlaceholder.attachToAdapter(adapter)
        ValueBlock(labelCreator).applyValue(R.string.incidental_sheet_signature)
            .attachToAdapter(adapter)

        singleViewBlock {
            constraintLayout {
                setLayoutParams()
                val img = add(imageView {
                    background = rippleDrawableBorder(dip(4), Color.WHITE, Color.GRAY, dip(1))
                    padding = dip(2)
                }, defaultLParams(matchParent, matchConstraint) {
                    dimensionRatio = "3:1"
                    topToTopParent()
                    bottomToBottomParent()
                    startToStartParent()
                    endToEndParent()
                    margin = dip(8)
                })

                val listener = object : View.OnClickListener, Observer<Result<Uri>> {
                    private val code = 1
                    override fun onClick(v: View?) {
                        /*待機・附帯作業詳細・サインエリアを押下時に起動されるイベント*/
                        val file = itemModel.getPendingSign(code)
                        startActivityForResult<SignActivity>(code, "png_path" to file.absolutePath)
                    }

                    override fun onChanged(t: Result<Uri>) {
                        /*待機・附帯作業詳細・署名画像の取得処理*/
                        val ctx = img.context
                        ctx.withGlide()
                            .run {
                                when (t) {
                                    is Result.Loading -> load(ctx.defaultSizeTextDrawable(R.string.loading_image))
                                    is Result.Value -> load(t.value) // or uri.empty.
                                    is Result.Error -> load(ctx.defaultSizeTextDrawable(R.string.server_connection_err))
                                }
                            }
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .skipMemoryCache(true)
                            .error(ctx.defaultSizeTextDrawable(R.string.tap_to_edit))
                            .fitCenterInto(img)
                    }
                }
                img.setOnClickListener(listener)
                itemModel.signFile.observe(viewLifecycleOwner, listener)
            }
        }.attachToAdapter(adapter)

        u.list.adapter = adapter

        val model = itemModel
        argLiveData.observeNonNull(viewLifecycleOwner) {
            model.setHeader(it.listItem)
        }
        model.itemDataLiveData.observeNullable(viewLifecycleOwner) { detail ->
            if (detail == null) dismiss()
            else {
                headBlock.applyValue(detail)
                nimachiBlock.submitList(detail.times?.nimachiTime(), timeCreator)
                additionalBlock.submitList(detail.times?.additionalTime(), timeCreator)
            }
        }

        model.processing.observeNullable(viewLifecycleOwner) {
            val processing = it == true
            isCancelable = !processing
        }
        return u.view
    }

    private val itemModel: SheetItemVM by lazyViewModel()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            /*署名・署名データの保存処理*/
            /*署名・附帯作業のデータ更新処理*/
            itemModel.commitPendingSign(requestCode)
        }
    }
}

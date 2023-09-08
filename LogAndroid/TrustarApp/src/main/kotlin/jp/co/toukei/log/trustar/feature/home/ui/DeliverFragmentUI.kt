package jp.co.toukei.log.trustar.feature.home.ui

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import jp.co.toukei.log.lib.common.*
import jp.co.toukei.log.trustar.*
import jp.co.toukei.log.trustar.R
import jp.co.toukei.log.trustar.common.ui.CoordinatorUI
import jp.co.toukei.log.trustar.db.user.entity.BinStatus
import splitties.dimensions.dip
import splitties.views.*
import splitties.views.dsl.appcompat.toolbar
import splitties.views.dsl.coordinatorlayout.defaultLParams
import splitties.views.dsl.core.add
import splitties.views.dsl.core.matchParent
import splitties.views.dsl.core.textView
import splitties.views.dsl.core.wrapContent
import splitties.views.dsl.material.appBarLayout
import splitties.views.dsl.material.collapsingToolbarLayout
import splitties.views.dsl.material.defaultLParams
import splitties.views.dsl.recyclerview.recyclerView

class DeliverFragmentUI(context: Context) : CoordinatorUI(context) {
    val operation: TextView
    val carModel: TextView
    val binStatusSwitch: TextView
    val workAdd: TextView
    val autoModeSwitch: SwitchCompat
    val dateTextView: TextView

    private val appBarLayout: AppBarLayout = coordinatorLayout.run {
        add(appBarLayout(), defaultLParams(matchParent, wrapContent) {
            behavior = AppBarLayout.Behavior()
        })
    }

    init {
        appBarLayout.run {
            add(collapsingToolbarLayout {
                isTitleEnabled = false
                contentScrim = null
                statusBarScrim = null
                val lp = defaultLParams(matchParent, matchParent) {
                    collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_OFF
                }
                add(constraintLayout {
                    verticalPadding = dip(8)
                    backgroundColorResId = R.color.defaultBackground

                    operation = add(textView {
                        id = R.id.id3
                        textSize = 20F
                        padding = dip(4)
                    }, defaultLParams(wrapContent, wrapContent) {
                        topToTopParent()
                        startToStartParent()
                        endToStart = R.id.id5
                        horizontalBias = 0F
                        marginStart = dip(8)
                        constrainedWidth = true
                    })
                    carModel = add(textView {
                        id = R.id.id4
                        textSize = 20F
                        padding = dip(4)
                    }, defaultLParams(wrapContent, wrapContent) {
                        startToStartParent()
                        endToStart = R.id.id5
                        below(R.id.id3)
                        horizontalBias = 0F
                        marginStart = dip(8)
                        constrainedWidth = true
                    })
                    autoModeSwitch = add(switchCompact {
                        id = R.id.id5
                        boldTypeface()
                        isChecked = false
                        gone()
                        textResource = R.string.work_mode_manual
                    }, defaultLParams(wrapContent, wrapContent) {
                        endToEndParent()
                        topToTopParent()
                        bottomToBottomParent()
                    })
                }, lp)
                val t = add(toolbar(theme = R.style.ToolbarTheme) {
                    title = ""
                    backgroundColorResId = R.color.colorPrimary
                }, defaultLParams(matchParent, wrapContent) {
                    collapseMode = CollapsingToolbarLayout.LayoutParams.COLLAPSE_MODE_PIN
                })
                t.setOldStyle()
                lp.topMargin = dip(40) // fixme hardcoded.
                dateTextView = t.addRightDateText()
            }, defaultLParams(matchParent, wrapContent) {
                scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS or
                        AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
            })
        }
    }

    private val operationArea: View = appBarLayout.run {
        add(constraintLayout {
            verticalPadding = dip(8)
            horizontalPadding = dip(16)
            backgroundColorResId = R.color.colorPrimary

            binStatusSwitch = add(textView {
                setText(R.string.operation_start)
                whiteButtonStyle()
                verticalPadding = dip(8)
                includeFontPadding = false
                textSize = 14F
                compoundDrawablePadding = dip(4)
            }, defaultLParams(wrapContent, wrapContent) {
                startToStartParent()
                topToTopParent()
                bottomToBottomParent()
            })

            workAdd = add(textView {
                setText(R.string.work_add)
                whiteButtonSmallStyle()
            }, defaultLParams(wrapContent, wrapContent) {
                endToEndParent()
                topToTopParent()
                bottomToBottomParent()
            })
        }, defaultLParams(matchParent, wrapContent) {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
        })
    }


    @JvmField
    val swipeRefreshLayout: SwipeRefreshLayout = coordinatorLayout.run {
        add(swipeRefreshLayout {
            backgroundColorResId = R.color.defaultBackground
        }, defaultLParams(matchParent, matchParent) {
            behavior = AppBarLayout.ScrollingViewBehavior()
        })
    }

    @JvmField
    val list: RecyclerView = swipeRefreshLayout.run {
        add(recyclerView {
            val lm = LinearLayoutManager(context)
            layoutManager = lm
            addDividerItemDecoration(lm.orientation, Color.TRANSPARENT, dip(1))
        }, ViewGroup.LayoutParams(matchParent, matchParent))
    }

    fun workAddEnabled(enabled: Boolean) {
        if (enabled) workAdd.enable() else workAdd.disable()
    }

    fun setSwitchAreaColor(operationStatus: BinStatus?) {
        val context = coordinatorLayout.context

        binStatusSwitch.disable()
        binStatusSwitch.text = null

        val color = context.getColor(
            if (operationStatus.hasFinished()) R.color.orange_out
            else R.color.colorPrimary
        )

        binStatusSwitch.setCompoundDrawables(null, null, context.run {
            compoundDrawable(
                if (operationStatus.ready()) R.drawable.ic_bin_start else R.drawable.ic_bin_stop,
                color,
                dip(20)
            )
        }, null)

        when (operationStatus?.type) {
            is BinStatus.Type.Ready -> {
                operationArea.backgroundColor = color
                binStatusSwitch.textColor = color
                binStatusSwitch.textResource = R.string.operation_start
                binStatusSwitch.enable()
            }
            is BinStatus.Type.Working -> {
                operationArea.backgroundColor = color
                binStatusSwitch.textColor = color
                binStatusSwitch.textResource = R.string.operation_end
                binStatusSwitch.enable()
            }
            is BinStatus.Type.Finished -> {
                operationArea.backgroundColor = color
                binStatusSwitch.textColor = color
                binStatusSwitch.textResource = R.string.operation_end
            }
            else -> {}
        }
    }
}

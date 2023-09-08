package jp.co.toukei.log.trustar.feature.home.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardFragmentModel : ViewModel() {

    @JvmField
    val showAllCheckState = MutableLiveData(true)
}

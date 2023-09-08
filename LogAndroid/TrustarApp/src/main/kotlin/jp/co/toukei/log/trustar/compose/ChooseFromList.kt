@file:OptIn(ExperimentalFoundationApi::class)

package jp.co.toukei.log.trustar.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sebaslogen.resaca.rememberScoped
import jp.co.toukei.log.common.compose.AppPropTodo
 import jp.co.toukei.log.common.compose.VerticalDivider
import jp.co.toukei.log.lib.compose.InputTextField
import jp.co.toukei.log.lib.compose.rememberDistinctBy
import jp.co.toukei.log.lib.compose.verticalScrollbar
import jp.co.toukei.log.lib.filterAll

@Composable
fun <T> ChooseFromList(
    currentTextLabel: String,
    currentText: String,
    placeholder: String,
    listTitle: String,
    list: List<T>,
    key: (T) -> String,
    firstStr: (T) -> String,
    secondStr: (T) -> String,
    filter: (T, String) -> Boolean,
    select: (T) -> Unit,
) {
    val ls = list.rememberDistinctBy(selector = key)

    var t by rememberScoped {
        mutableStateOf("")
    }
    val tt = t
    val reg = remember { Regex("\\s+") }
    val filtered = remember(ls, tt) {
        ls.filterAll(tt.split(reg).mapNotNull { it.takeIf(String::isNotEmpty) }, filter)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = currentTextLabel
            )
            Text(
                modifier = Modifier
                    .padding(8.dp),
                text = currentText
            )
        }
        VerticalDivider()

        InputTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            value = t,
            onValueChange = {
                t = it
            },
            placeholder = placeholder
        )
        PrimaryListHeadTitle(listTitle)

        val state: LazyListState = rememberLazyListState()

        LazyColumn(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1F, true)
                .verticalScrollbar(state)
        ) {
            items(
                items = filtered,
                key = key,
            ) {
                Row(
                    modifier = Modifier
                        .animateItemPlacement()
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            select(it)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = firstStr(it),
                        style = AppPropTodo.Text.bold16,
                    )
                    Text(
                        modifier = Modifier
                            .padding(8.dp),
                        text = secondStr(it),
                        style = AppPropTodo.Text.size16,
                    )
                }
            }
        }
    }
}

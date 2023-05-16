package hu.ait.betledger.ui.screen.betlist

import BetItemFilter
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import hu.ait.betledger.R
import hu.ait.betledger.data.ResolutionStatus

@Composable
fun FilterBetListForm(
    onDialogClose: () -> Unit,
    onSaveFilter: (BetItemFilter) -> Unit,
    filter: BetItemFilter,
) {
    var query by remember { mutableStateOf(filter.query) }
    var party1 by remember { mutableStateOf(filter.party1) }
    var party2 by remember { mutableStateOf(filter.party2) }
    var before by remember { mutableStateOf(filter.before) }
    var after by remember { mutableStateOf(filter.after) }
    var status by remember { mutableStateOf(filter.status) }

    Dialog(onDismissRequest = onDialogClose) {
        Card(
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp
            )
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    item {
                        OutlinedTextField(
                            value = query ?: "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            label = { Text(stringResource(R.string.search_prompt)) },
                            onValueChange = {
                                query = it.ifEmpty { null }
                            },
                        )
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedTextField(
                                value = party1 ?: "",
                                modifier = Modifier
                                    .weight(0.7f)
                                    .padding(end = 2.dp),
                                label = { Text(stringResource(R.string.party1)) },
                                onValueChange = {
                                    party1 = it.ifEmpty { null }
                                },
                            )
                        }
                    }
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            OutlinedTextField(
                                value = party2 ?: "",
                                modifier = Modifier
                                    .weight(0.7f)
                                    .padding(end = 2.dp),
                                label = { Text(stringResource(R.string.party2)) },
                                onValueChange = {
                                    party2 = it.ifEmpty { null }
                                },
                            )
                        }
                    }
                    item {
                        Text(
                            text = stringResource(R.string.resolution_status),
                            modifier = Modifier
                                .align(Alignment.Start)
                                .padding(2.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    item {
                        Spinner(
                            listOf(
                                "All",
                                ResolutionStatus.UNRESOLVED.cleanName,
                                ResolutionStatus.PARTY1_WIN.cleanName,
                                ResolutionStatus.PARTY2_WIN.cleanName
                            ),
                            preselected = status?.cleanName ?: "All",
                            onSelectionChanged = { newStatus ->
                                status = when (newStatus) {
                                    ResolutionStatus.UNRESOLVED.cleanName -> ResolutionStatus.UNRESOLVED
                                    ResolutionStatus.PARTY1_WIN.cleanName -> ResolutionStatus.PARTY1_WIN
                                    ResolutionStatus.PARTY2_WIN.cleanName -> ResolutionStatus.PARTY2_WIN
                                    else -> null
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp)
                        )
                    }
                    item {
                        Button(
                            onClick = {
                                val filterEdit = filter.copy(
                                    query = query,
                                    party1 = party1,
                                    party2 = party2,
                                    before = before,
                                    after = after,
                                    status = status,
                                )
                                onSaveFilter(filterEdit)
                                onDialogClose()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.save),
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
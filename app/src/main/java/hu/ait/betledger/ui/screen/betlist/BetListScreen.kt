package hu.ait.betledger.ui.screen

import BetItemFilter
import BetListViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import hu.ait.betledger.data.BetItem
import hu.ait.betledger.R
import hu.ait.betledger.ui.screen.betlist.AddNewBetForm
import hu.ait.betledger.ui.screen.betlist.BetCard
import hu.ait.betledger.ui.screen.betlist.FilterBetListForm

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetListScreen(
    betListViewModel: BetListViewModel = viewModel(factory = BetListViewModel.factory),
    navController: NavController
) {
    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var showFilterDialog by rememberSaveable {
        mutableStateOf(false)
    }
    var filter by remember { mutableStateOf(BetItemFilter()) }

    val betList by betListViewModel.getAllBetsFiltered(filter).collectAsState(
        emptyList()
    )

    var betToEdit: BetItem? by rememberSaveable {
        mutableStateOf(null)
    }

    var expanded by remember { mutableStateOf(false) }

    Column {
        TopAppBar(
            title = {
                Text(stringResource(R.string.app_name))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor =
                MaterialTheme.colorScheme.secondaryContainer
            ),
            actions = {
                IconButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Filled.Add, null)
                }

                IconButton(onClick = {
                    betListViewModel.clearAllBets()
                }) {
                    Icon(Icons.Filled.Delete, null)
                }

                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Filled.MoreVert, null)
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(onClick = {
                        showFilterDialog = true
                        expanded = false
                    },
                        text = { Text(stringResource(R.string.filter_dropdown)) }
                    )
                }
            })

        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            if (showFilterDialog) {
                FilterBetListForm(
                    onSaveFilter = {
                        filter = it
                    },
                    onDialogClose = {
                        showFilterDialog = false
                    },
                    filter = filter
                )
            }
            if (showAddDialog) {
                AddNewBetForm(
                    onDialogClose = {
                        showAddDialog = false
                        betToEdit = null
                    },
                    betToEdit = betToEdit
                )
            }

            LazyColumn {
                items(betList) {
                    BetCard(betItem = it,
                        onRemoveItem = {
                            betListViewModel.removeBet(it)
                        },
                        onEditItem = {
                            showAddDialog = true
                            betToEdit = it
                        }
                    )
                }
            }
        }
    }
}

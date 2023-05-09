package hu.ait.shoppingcart.ui.screen

import BetListViewModel
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import hu.ait.shoppingcart.data.BetItem
import hu.ait.tododemo.R
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetListScreen(
    betListViewModel: BetListViewModel = viewModel(factory = BetListViewModel.factory),
    navController: NavController
) {
    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val betList by betListViewModel.getAllBets().collectAsState(
        emptyList()
    )

    var betToEdit: BetItem? by rememberSaveable {
        mutableStateOf(null)
    }

    var filter by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }

    val filteredList = when (filter) {
        "Unresolved" -> betList.filter { it.resolutionStatus == null}
        "Resolved Party 1" -> betList.filter { it.resolutionStatus == true}
        "Resolved Party 2" -> betList.filter { it.resolutionStatus == false }
        else -> betList
    }

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
                        filter = "All"
                        expanded = false
                    },
                        text = {Text(stringResource(R.string.show_all))}
                    )
                    DropdownMenuItem(onClick = {
                        filter = "Unresolved"
                        expanded = false
                    },
                        text = {Text(stringResource(R.string.show_unresolved))}
                    )
                    DropdownMenuItem(onClick = {
                        filter = "Resolved Party 1"
                        expanded = false
                    },
                        text = {Text(stringResource(R.string.show_party1_won))}
                    )
                    DropdownMenuItem(onClick = {
                        filter = "Resolved Party 2"
                        expanded = false
                    },
                        text = {Text(stringResource(R.string.show_party2_won))}
                    )
                }
            })

        Column(
            modifier = Modifier.padding(10.dp)
        ) {

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
                items(filteredList) {
                    BetCard(betItem = it,
                        onBetResolutionChange = { status ->
                            betListViewModel.changeBetResolutionStatus(it, status)
                        },
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewBetForm(
    betListViewModel: BetListViewModel = viewModel(),
    onDialogClose: () -> Unit = {},
    betToEdit: BetItem? = null
) {
    var newBetTitle by remember { mutableStateOf(betToEdit?.title ?: "") }
    var newBetDescription by remember { mutableStateOf(betToEdit?.description ?: "") }
    var newParty1 by remember { mutableStateOf(betToEdit?.party1 ?: "") }
    var newParty2 by remember { mutableStateOf(betToEdit?.party2 ?: "") }
    var newParty1WinAmount by remember { mutableStateOf(betToEdit?.party1WinAmount ?: "") }
    var newParty2WinAmount by remember { mutableStateOf(betToEdit?.party2WinAmount ?: "") }
    var resolutionStatus by remember { mutableStateOf(betToEdit?.resolutionStatus ?: false) }

    var titleError by rememberSaveable { mutableStateOf("") }
    var descriptionError by rememberSaveable { mutableStateOf("") }
    var party1Error by rememberSaveable { mutableStateOf("") }
    var party2Error by rememberSaveable { mutableStateOf("") }
    var party1WinAmountError by rememberSaveable { mutableStateOf("") }
    var party2WinAmountError by rememberSaveable { mutableStateOf("") }
    val titleErrorMessage = stringResource(R.string.title_error)
    val descriptionErrorMessage = stringResource(R.string.desc_error)
    val party1ErrorMessage = stringResource(R.string.party_error)
    val party2ErrorMessage = stringResource(R.string.party_error)
    val party1WinAmountErrorMessage = stringResource(R.string.party_win_amount_error)
    val party2WinAmountErrorMessage = stringResource(R.string.party_win_amount_error)

    fun validateInput(): Boolean {
        var isValid = true

        if (newBetTitle.isEmpty()) {
            titleError = titleErrorMessage
            isValid = false
        } else {
            titleError = ""
        }

        if (newBetDescription.isEmpty()) {
            descriptionError = descriptionErrorMessage
            isValid = false
        } else {
            descriptionError = ""
        }

        if (newParty1.isEmpty()) {
            party1Error = party1ErrorMessage
            isValid = false
        } else {
            party1Error = ""
        }

        if (newParty2.isEmpty()) {
            party2Error = party2ErrorMessage
            isValid = false
        } else {
            party2Error = ""
        }

        if (newParty1WinAmount.isEmpty()) {
            party1WinAmountError = party1WinAmountErrorMessage
            isValid = false
        } else {
            party1WinAmountError = ""
        }

        if (newParty2WinAmount.isEmpty()) {
            party2WinAmountError = party2WinAmountErrorMessage
            isValid = false
        } else {
            party2WinAmountError = ""
        }

        return isValid
    }


    Dialog(onDismissRequest = onDialogClose) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(size = 6.dp)
        ) {
            Column() {
                OutlinedTextField(
                    value = newBetTitle,
                    modifier = Modifier.padding(4.dp),
                    label = { Text(stringResource(R.string.bet_title)) },
                    onValueChange = {
                        newBetTitle = it
                    },
                    isError = titleError.isNotEmpty(),
                    supportingText = {
                        if (titleError.isNotEmpty()) {
                            Text(text = titleError, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                )

                OutlinedTextField(
                    value = newBetDescription,
                    modifier = Modifier.padding(4.dp),
                    label = { Text(stringResource(R.string.description)) },
                    onValueChange = {
                        newBetDescription = it
                    },
                    isError = descriptionError.isNotEmpty(),
                    supportingText = {
                        if (descriptionError.isNotEmpty()) {
                            Text(text = descriptionError, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                )

                OutlinedTextField(
                    value = newParty1,
                    modifier = Modifier.padding(4.dp),
                    label = { Text(stringResource(R.string.party1)) },
                    onValueChange = {
                        newParty1 = it
                    },
                    isError = party1Error.isNotEmpty(),
                    supportingText = {
                        if (party1Error.isNotEmpty()) {
                            Text(text = party1Error, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                )

                OutlinedTextField(
                    value = newParty2,
                    modifier = Modifier.padding(4.dp),
                    label = { Text(stringResource(R.string.party2)) },
                    onValueChange = {
                        newParty2 = it
                    },
                    isError = party2Error.isNotEmpty(),
                    supportingText = {
                        if (party2Error.isNotEmpty()) {
                            Text(text = party2Error, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                )

                OutlinedTextField(
                    value = newParty1WinAmount,
                    modifier = Modifier.padding(4.dp),
                    label = { Text(stringResource(R.string.party1_win_amount)) },
                    onValueChange = {
                        newParty1WinAmount = it
                    },
                    isError = party1WinAmountError.isNotEmpty(),
                    supportingText = {
                        if (party1WinAmountError.isNotEmpty()) {
                            Text(text = party1WinAmountError, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                )

                OutlinedTextField(
                    value = newParty2WinAmount,
                    modifier = Modifier.padding(4.dp),
                    label = { Text(stringResource(R.string.party2_win_amount)) },
                    onValueChange = {
                        newParty2WinAmount = it
                    },
                    isError = party2WinAmountError.isNotEmpty(),
                    supportingText = {
                        if (party2WinAmountError.isNotEmpty()) {
                            Text(text = party2WinAmountError, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                )

                Row(modifier = Modifier.padding(4.dp)) {
                    Text(
                        text = stringResource(R.string.resolution_status),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 8.dp)
                    )
                    Checkbox(
                        checked = resolutionStatus,
                        onCheckedChange = { resolutionStatus = it },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Button(
                    onClick = {
                        if (validateInput()) {
                            if (betToEdit == null) {
                                val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
                                val currentDate = sdf.format(Date())

                                betListViewModel.addBet(
                                    BetItem(
                                        title = newBetTitle,
                                        description = newBetDescription,
                                        party1 = newParty1,
                                        party2 = newParty2,
                                        party1WinAmount = newParty1WinAmount,
                                        party2WinAmount = newParty2WinAmount,
                                        resolutionStatus = resolutionStatus,
                                        createDate = currentDate
                                    )
                                )
                            } else { // EDIT mode
                                val betEdited = betToEdit.copy(
                                    title = newBetTitle,
                                    description = newBetDescription,
                                    party1 = newParty1,
                                    party2 = newParty2,
                                    party1WinAmount = newParty1WinAmount,
                                    party2WinAmount = newParty2WinAmount,
                                    resolutionStatus = resolutionStatus
                                )

                                betListViewModel.editBetItem(betEdited)
                            }

                            onDialogClose()
                        }
                    }
                ) {
                    Text(text = stringResource(R.string.save))
                }

            }
        }
    }
}

fun String.capitalizeFirstLetter(): String {
    if (this.isEmpty()) return this
    return this[0].uppercase() + this.substring(1).lowercase()
}

@Composable
fun SpinnerSample(
    list: List<String>,
    preselected: String,
    onSelectionChanged: (myData: String) -> Unit, modifier: Modifier = Modifier
) {
    var selected by remember { mutableStateOf(preselected) }
    var expanded by remember { mutableStateOf(false) } // initial value
    OutlinedCard(
        modifier = modifier.clickable {
            expanded = !expanded
        }) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top,
        ) {
            Text(
                text = selected,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Icon(Icons.Outlined.ArrowDropDown, null, modifier = Modifier.padding(8.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }, modifier = Modifier.fillMaxWidth()
            ) {
                list.forEach { listEntry ->
                    DropdownMenuItem(
                        onClick = {
                            selected = listEntry
                            expanded = false
                            onSelectionChanged(selected)
                        },
                        text = {
                            Text(
                                text = listEntry,
                                modifier = Modifier
                            )
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BetCard(
    betItem: BetItem,
    onBetResolutionChange: (Boolean) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onEditItem: (BetItem) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        var expanded by rememberSaveable { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = betItem.title,
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = betItem.resolutionStatus ?: false,
                        onCheckedChange = {
                            onBetResolutionChange(it)
                        },
                    )
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.clickable {
                            onRemoveItem()
                        },
                        tint = Color.Red
                    )

                    IconButton(onClick = {
                        onEditItem(betItem)
                    }) {
                        Icon(
                            imageVector = Icons.Filled.Settings,
                            contentDescription = null
                        )
                    }

                    IconButton(onClick = { expanded = !expanded }) {
                        Icon(
                            imageVector = if (expanded)
                                Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                            contentDescription = if (expanded) {
                                "Less"
                            } else {
                                "More"
                            }
                        )
                    }

                }
            }

            if (expanded) {
                Text(text = "Description: ${betItem.description}")
                Text(text = "Party 1: ${betItem.party1}")
                Text(text = "Party 2: ${betItem.party2}")
                Text(text = "Party 1 Win Amount: ${betItem.party1WinAmount}")
                Text(text = "Party 2 Win Amount: ${betItem.party2WinAmount}")
                Text(
                    text = "Created Date: ${betItem.createDate}",
                    style = TextStyle(fontSize = 12.sp)
                )
            }
        }
    }
}



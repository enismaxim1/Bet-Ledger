package hu.ait.shoppingcart.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.shoppingcart.data.ShoppingItem
import hu.ait.shoppingcart.data.ShoppingCategory
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import hu.ait.tododemo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListScreen(
    shoppingListViewModel: ShoppingListViewModel = viewModel(factory = ShoppingListViewModel.factory),
    navController: NavController
) {
    var showAddDialog by rememberSaveable {
        mutableStateOf(false)
    }

    val shoppingList by shoppingListViewModel.getAllShoppingList().collectAsState(
        emptyList()
    )

    var shoppingToEdit: ShoppingItem? by rememberSaveable {
        mutableStateOf(null)
    }

    var filter by remember { mutableStateOf("All") }
    var expanded by remember { mutableStateOf(false) }

    val filteredList = when (filter) {
        "Unchecked" -> shoppingList.filter { !it.status }
        "Checked" -> shoppingList.filter { it.status }
        else -> shoppingList
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
                    shoppingListViewModel.clearAllShopping()
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
                    }, text = {Text(stringResource(R.string.show_all))}
                    )
                    DropdownMenuItem(onClick = {
                        filter = "Unchecked"
                        expanded = false
                    },
                        text = {Text(stringResource(R.string.show_unpurchased))}
                    )
                    DropdownMenuItem(onClick = {
                        filter = "Checked"
                        expanded = false
                    },
                        text = {Text(stringResource(R.string.show_purchased))}
                    )
                }
            })

        Column(
            modifier = Modifier.padding(10.dp)
        ) {

            if (showAddDialog) {
                AddNewShoppingForm(
                    onDialogClose = {
                        showAddDialog = false
                        shoppingToEdit = null
                    },
                    shoppingToEdit = shoppingToEdit
                )
            }

            LazyColumn {
                items(filteredList) {
                    ShoppingCard(shoppingItem = it,
                        onShoppingCheckChange = { checked ->
                            shoppingListViewModel.changeShoppingState(it, checked)
                        },
                        onRemoveItem = {
                            shoppingListViewModel.removeShoppingItem(it)
                        },
                        onEditItem = {
                            showAddDialog = true
                            shoppingToEdit = it
                        }
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewShoppingForm(
    shoppingListViewModel: ShoppingListViewModel = viewModel(),
    onDialogClose: () -> Unit = {},
    shoppingToEdit: ShoppingItem? = null
) {
    var newShoppingTitle by remember { mutableStateOf(shoppingToEdit?.title ?: "") }
    var newShoppingDesc by remember { mutableStateOf(shoppingToEdit?.description ?: "") }
    var newEstimatedPrice by remember { mutableStateOf(shoppingToEdit?.estimatedPrice ?: "") }
    var newShoppingCategory by remember { mutableStateOf(shoppingToEdit?.category ?: ShoppingCategory.FOOD) }
    var newStatus by remember { mutableStateOf(shoppingToEdit?.status ?: false) }

    var titleError by rememberSaveable { mutableStateOf("") }
    var descError by rememberSaveable { mutableStateOf("") }
    var priceError by rememberSaveable { mutableStateOf("") }
    val titleErrorMessage = stringResource(R.string.title_error)
    val priceErrorMessage = stringResource(R.string.price_error)
    val descErrorMessage = stringResource(R.string.desc_error)

    fun validateInput(titleErrorMessage: String, priceErrorMessage: String, descErrorMessage: String): Boolean {
        var isValid = true

        if (newShoppingTitle.isEmpty()) {
            titleError = titleErrorMessage
            isValid = false
        } else {
            titleError = ""
        }

        if (newEstimatedPrice.isEmpty()) {
            priceError = priceErrorMessage
            isValid = false
        } else {
            priceError = ""
        }

        if (newShoppingDesc.isEmpty()) {
            descError = descErrorMessage
            isValid = false
        } else {
            descError = ""
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

                OutlinedTextField(value = newShoppingTitle,
                    modifier = Modifier.padding(4.dp),
                    label = { Text(stringResource(R.string.item_name)) },
                    onValueChange = {
                        newShoppingTitle = it
                    },
                    isError = titleError.isNotEmpty(),
                    supportingText = {
                        if (titleError.isNotEmpty()) {
                            Text(text = titleError, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                )


                OutlinedTextField(value = newEstimatedPrice,
                    modifier = Modifier.padding(4.dp),
                    label = { Text(stringResource(R.string.price)) },
                    onValueChange = {
                        newEstimatedPrice = it
                    },
                    isError = priceError.isNotEmpty(),
                    supportingText = {
                        if (priceError.isNotEmpty()) {
                            Text(text = priceError, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                )


                OutlinedTextField(value = newShoppingDesc,
                    modifier = Modifier.padding(bottom = 4.dp),
                    label = { Text(stringResource(R.string.description)) },
                    onValueChange = {
                        newShoppingDesc = it
                    },
                    isError = descError.isNotEmpty(),
                    supportingText = {
                        if (descError.isNotEmpty()) {
                            Text(text = descError, color = Color.Red, fontSize = 12.sp)
                        }
                    }
                )


                SpinnerSample(
                    listOf("Food", "Electronics", "Book"), preselected = newShoppingCategory.name.capitalizeFirstLetter(), onSelectionChanged = { newShoppingCategory = ShoppingCategory.valueOf(
                        it.uppercase(Locale.getDefault())
                    ) },
                    modifier = Modifier.padding(top = 4.dp),
                )

                Row(modifier = Modifier.padding(4.dp)) {
                    Text(
                        text = stringResource(R.string.bought),
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 8.dp)
                    )
                    Checkbox(
                        checked = newStatus,
                        onCheckedChange = { newStatus = it },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }

                Button(
                    onClick = {

                        if (validateInput(titleErrorMessage, priceErrorMessage, descErrorMessage)) {
                            if (shoppingToEdit == null) {
                                shoppingListViewModel.addShoppingList(
                                    ShoppingItem(
                                        title = newShoppingTitle,
                                        description = newShoppingDesc,
                                        createDate = Date(System.currentTimeMillis()).toString(),
                                        category = newShoppingCategory,
                                        status = newStatus,
                                        estimatedPrice = newEstimatedPrice
                                    )
                                )
                            } else { // EDIT mode
                                var shoppingEdited = shoppingToEdit.copy(
                                    title = newShoppingTitle,
                                    description = newShoppingDesc,
                                    category = newShoppingCategory
                                )

                                shoppingListViewModel.editShoppingItem(
                                    shoppingEdited
                                )
                            }

                            onDialogClose()
                        }
                    }
                ) {
                    Text(text = "Save")
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
fun ShoppingCard(
    shoppingItem: ShoppingItem,
    onShoppingCheckChange: (Boolean) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onEditItem: (ShoppingItem) -> Unit = {}
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

                Image(
                    painter = painterResource(id = shoppingItem.category.getIcon()),
                    contentDescription = "Priority",
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 10.dp)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = shoppingItem.title,
                        textDecoration =
                        if (shoppingItem.status) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = shoppingItem.status,
                        onCheckedChange = {
                            onShoppingCheckChange(it)
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
                        onEditItem(shoppingItem)
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

                Text(text = shoppingItem.description)
                Text(
                    text = shoppingItem.createDate,
                    style = TextStyle(fontSize = 12.sp)
                )

            }


        }
    }
}


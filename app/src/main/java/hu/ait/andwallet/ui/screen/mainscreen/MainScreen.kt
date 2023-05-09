package hu.ait.andwallet.ui.screen.mainscreen

import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import hu.ait.highlowgamecompose.R
import hu.ait.andwallet.ui.screen.MoneyItem
import hu.ait.andwallet.ui.screen.MoneyType
import hu.ait.andwallet.ui.screen.summary.MoneyListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    moneyList: MoneyListViewModel = viewModel(),
    onNavigateToSummary: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current

    var titleText by rememberSaveable { mutableStateOf("") }
    var amountText by rememberSaveable { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var titleErrorState by rememberSaveable { mutableStateOf(false) }
    var amountErrorState by rememberSaveable { mutableStateOf(false) }
    var titleErrorText by rememberSaveable {
        mutableStateOf(
            context.getString(
                R.string.text_error_default
            )
        )
    }
    var amountErrorText by rememberSaveable {
        mutableStateOf(
            context.getString(
                R.string.text_error_default
            )
        )
    }

    fun validateTitle(text: String): Boolean {
        val empty = text.isEmpty()

        titleErrorText = "This field must not be empty"
        titleErrorState = empty
        return !empty
    }
    
    fun validateAmount(text: String): Boolean {
        val isNumber = text.toDoubleOrNull() != null

        amountErrorText = "This field must be a number"
        amountErrorState = !isNumber
        return isNumber
    }


    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start
    ) {
        SmallTopAppBar(
            title = {
                Text(
                    text = stringResource(R.string.app_name),
                    modifier = Modifier.padding(8.dp)
                )
            },
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Box(modifier = Modifier.weight(1f)) {
                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(end = 4.dp),
                        value = titleText,
                        label = { Text(stringResource(R.string.title)) },
                        onValueChange = {
                            titleText = it
                            validateTitle(titleText)
                        },
                        isError = titleErrorState,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        trailingIcon = {
                            if (titleErrorState) {
                                Icon(Icons.Filled.Warning, "error", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                    if (titleErrorState) {
                        Text(
                            text = titleErrorText,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
            Box(modifier = Modifier.weight(1f)) {
                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(start = 4.dp),
                        value = amountText,
                        label = { Text(stringResource(R.string.amount)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        trailingIcon = {
                            if (amountErrorState) {
                                Icon(Icons.Filled.Warning, "error", tint = MaterialTheme.colorScheme.error)
                            }
                        },
                        onValueChange = {
                            amountText = it
                            validateAmount(amountText)
                        },
                        isError = amountErrorState,
                    )
                    if (amountErrorState) {
                        Text(
                            text = amountErrorText,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier.padding(top = 16.dp, start = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.income))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = {
                if (validateTitle(titleText) and validateAmount(amountText)) {
                    val type = if (isChecked) MoneyType.INCOME else MoneyType.EXPENSE
                    moneyList.add(MoneyItem(titleText, amountText.toDouble(), type))
                }
            }) {
                Text(stringResource(R.string.save))
            }
            Button(onClick = { moneyList.clear() }) {
                Text(stringResource(R.string.delete))
            }
            Button(onClick = { navController.navigate("summary?income=${moneyList.getIncome()}&expense=${moneyList.getExpense()}") }) {
                Text(stringResource(R.string.summary))
            }
        }

        LazyColumn {
            val list = moneyList.getList()
            items(list.size) {index ->
                    InfoCard(
                        moneyItem = list[index],
                        onDelete = { moneyList.removeAt(index) }
                    )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoCard(moneyItem: MoneyItem, onDelete: () -> Unit) {

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Card(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(8.dp)),
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val isIncome = moneyItem.type == MoneyType.INCOME
                    Icon(
                        imageVector = if (isIncome) Icons.Filled.CheckCircle else Icons.Filled.ShoppingCart,
                        contentDescription = if (isIncome) "Income" else "Expense",
                        tint = if (isIncome) Color.Green else Color.Red,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = moneyItem.title,
                            fontSize = 18.sp,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$${moneyItem.amount}",
                            fontSize = 18.sp,
                        )
                    }
                }
                IconButton(
                    onClick = { onDelete() },
                ) {
                    Icon(
                        imageVector = Icons.Filled.Delete,
                        contentDescription = "Delete",
                    )
                }
            }
        }
    }
}
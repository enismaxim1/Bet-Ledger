package hu.ait.betledger.ui.screen.betlist

import BetListViewModel
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import hu.ait.betledger.R
import hu.ait.betledger.data.BetItem
import hu.ait.betledger.data.ResolutionStatus
import java.text.SimpleDateFormat
import java.util.*

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
    var newParty1WinAmount by remember {
        mutableStateOf(
            betToEdit?.party1WinAmount?.toString() ?: ""
        )
    }
    var newParty2WinAmount by remember {
        mutableStateOf(
            betToEdit?.party2WinAmount?.toString() ?: ""
        )
    }
    var resolutionStatus by remember {
        mutableStateOf(
            betToEdit?.resolutionStatus ?: ResolutionStatus.UNRESOLVED
        )
    }

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

        if (newParty1WinAmount.isEmpty() || newParty1WinAmount == "-") {
            party1WinAmountError = party1WinAmountErrorMessage
            isValid = false
        } else {
            party1WinAmountError = ""
        }

        if (newParty2WinAmount.isEmpty() || newParty2WinAmount == "-") {
            party2WinAmountError = party2WinAmountErrorMessage
            isValid = false
        } else {
            party2WinAmountError = ""
        }

        return isValid
    }

    val context = LocalContext.current
    val toastHandler = Handler(Looper.getMainLooper())
    var isToastDisplayed = false

    fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        toastHandler.postDelayed(
            { isToastDisplayed = false },
            2000
        )
    }


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
                            value = newBetTitle,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            label = { Text(stringResource(R.string.bet_title)) },
                            onValueChange = { newBetTitle = it },
                            isError = titleError.isNotEmpty(),
                            supportingText = {
                                if (titleError.isNotEmpty()) {
                                    Text(text = titleError, color = Color.Red, fontSize = 10.sp)
                                }
                            }
                        )
                    }
                    item {
                        OutlinedTextField(
                            value = newBetDescription,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            label = { Text(stringResource(R.string.description)) },
                            onValueChange = { newBetDescription = it },
                            isError = descriptionError.isNotEmpty(),
                            supportingText = {
                                if (descriptionError.isNotEmpty()) {
                                    Text(
                                        text = descriptionError,
                                        color = Color.Red,
                                        fontSize = 10.sp
                                    )
                                }
                            }
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
                                value = newParty1,
                                modifier = Modifier
                                    .weight(0.7f)
                                    .padding(end = 2.dp),
                                label = { Text(stringResource(R.string.party1)) },
                                onValueChange = { newParty1 = it },
                                isError = party1Error.isNotEmpty(),
                                supportingText = {
                                    if (party1Error.isNotEmpty()) {
                                        Text(
                                            text = party1Error,
                                            color = Color.Red,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            )
                            OutlinedTextField(
                                value = newParty1WinAmount,
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(start = 2.dp),
                                label = { Text(stringResource(R.string.party1_win_amount)) },
                                onValueChange = { newValue ->
                                    try {
                                        newParty1WinAmount = if (newValue != "-") {
                                            if (newValue.isNotEmpty()) {
                                                newValue.toDouble()
                                            }
                                            newValue
                                        } else {
                                            newValue
                                        }
                                    } catch (e: NumberFormatException) {
                                        if (!isToastDisplayed) {
                                            showToast(context.resources.getString(R.string.only_numbers_error))
                                            isToastDisplayed = true
                                        }
                                    }
                                },
                                isError = party1WinAmountError.isNotEmpty(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                supportingText = {
                                    if (party1WinAmountError.isNotEmpty()) {
                                        Text(
                                            text = party1WinAmountError,
                                            color = Color.Red,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
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
                                value = newParty2,
                                modifier = Modifier
                                    .weight(0.7f)
                                    .padding(end = 2.dp),
                                label = { Text(stringResource(R.string.party2)) },
                                onValueChange = { newParty2 = it },
                                isError = party2Error.isNotEmpty(),
                                supportingText = {
                                    if (party2Error.isNotEmpty()) {
                                        Text(
                                            text = party2Error,
                                            color = Color.Red,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
                            )

                            OutlinedTextField(
                                value = newParty2WinAmount,
                                modifier = Modifier
                                    .weight(0.3f)
                                    .padding(start = 2.dp),
                                label = {
                                    Text(stringResource(R.string.party2_win_amount))
                                },
                                onValueChange = { newValue ->
                                    try {
                                        if (newValue.isNotEmpty()) {
                                            newValue.toDouble()
                                        }
                                        newParty2WinAmount = newValue
                                    } catch (e: NumberFormatException) {
                                        if (!isToastDisplayed) {
                                            showToast(context.resources.getString(R.string.only_numbers_error))
                                            isToastDisplayed = true
                                        }
                                    }
                                },
                                isError = party2WinAmountError.isNotEmpty(),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                supportingText = {
                                    if (party2WinAmountError.isNotEmpty()) {
                                        Text(
                                            text = party2WinAmountError,
                                            color = Color.Red,
                                            fontSize = 10.sp
                                        )
                                    }
                                }
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
                            listOf(ResolutionStatus.UNRESOLVED.cleanName, ResolutionStatus.PARTY1_WIN.cleanName, ResolutionStatus.PARTY2_WIN.cleanName),
                            preselected = ResolutionStatus.UNRESOLVED.cleanName,
                            onSelectionChanged = { newStatus ->
                                resolutionStatus = when (newStatus) {
                                    ResolutionStatus.PARTY1_WIN.cleanName -> ResolutionStatus.PARTY1_WIN
                                    ResolutionStatus.PARTY2_WIN.cleanName -> ResolutionStatus.PARTY2_WIN
                                    else -> ResolutionStatus.UNRESOLVED
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
                                                party1WinAmount = newParty1WinAmount.toDouble(),
                                                party2WinAmount = newParty2WinAmount.toDouble(),
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
                                            party1WinAmount = newParty1WinAmount.toDouble(),
                                            party2WinAmount = newParty2WinAmount.toDouble(),
                                            resolutionStatus = resolutionStatus
                                        )

                                        betListViewModel.editBetItem(betEdited)
                                    }

                                    onDialogClose()
                                }
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
package at.fhooe.mc.ada.features.feature_budget_tracker.presentation.add_edit_budget_record

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.AddEditCardEvent
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.AddEditCardViewModel
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.components.CustomOutlinedTextField
import kotlinx.coroutines.flow.collectLatest

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditBudgetRecordScreen(
    navHostController: NavHostController,
    viewModel: AddEditBudgetRecordViewModel = hiltViewModel()
) {
    val budgetRecordNameState = viewModel.budgetRecordName.value
    val budgetRecordDateState = viewModel.budgetRecordDate.value
    val budgetRecordAmountState = viewModel.budgetRecordAmount.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditBudgetRecordViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
                is AddEditBudgetRecordViewModel.UiEvent.SaveBudgetRecord -> {
                    navHostController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(title = {
                if (viewModel.currentBudgetRecordId == null) Text(text = "Add record") else Text(
                    text = "Edit record"
                )
            }, navigationIcon = {
                IconButton(onClick = { navHostController.navigateUp() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Back")
                }
            }, actions = {
                Button(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .scale(0.85f),
                    onClick = { viewModel.onEvent(AddEditBudgetRecordEvent.SaveBudgetRecord) },
                    content = {
                        var text = ""
                        text = if (viewModel.currentBudgetRecordId == null) "Add" else "Edit"
                        Text(text = text, fontSize = 16.sp)
                    }, shape = RoundedCornerShape(10.dp)
                )
            })
        }, scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background
    )
    {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = budgetRecordNameState,
                label = { Text(text = "Name") },
                onValueChange = {
                    viewModel.onEvent(
                        AddEditBudgetRecordEvent.EnteredBudgetRecordName(
                            it
                        )
                    )
                },
            )
            Spacer(modifier = Modifier.padding(10.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = budgetRecordDateState,
                label = { Text(text = "Date") },
                onValueChange = {
                    viewModel.onEvent(
                        AddEditBudgetRecordEvent.EnteredBudgetRecordDate(
                            it
                        )
                    )
                },
            )
            Spacer(modifier = Modifier.padding(10.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = if (budgetRecordAmountState == 0.0) "" else budgetRecordAmountState.toString(),
                label = { Text(text = "Amount") },
                onValueChange = {
                    if (it.isNotEmpty() && it != "-") {
                        viewModel.onEvent(AddEditBudgetRecordEvent.EnteredBudgetRecordAmount(it.toDouble()))
                    }
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}
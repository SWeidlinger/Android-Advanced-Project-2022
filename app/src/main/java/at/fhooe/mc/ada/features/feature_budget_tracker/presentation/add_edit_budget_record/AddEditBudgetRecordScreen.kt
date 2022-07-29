package at.fhooe.mc.ada.features.feature_budget_tracker.presentation.add_edit_budget_record

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.util.BudgetDateMask
import at.fhooe.mc.ada.features.feature_card.domain.repository.CardRepository
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.AddEditCardEvent
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.AddEditCardViewModel
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.components.CustomOutlinedTextField
import at.fhooe.mc.ada.features.feature_card.presentation.util.MultiFab
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditBudgetRecordScreen(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    bottomSheetScaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(),
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
        modifier = modifier,
        topBar = {
            MediumTopAppBar(title = {
                var type = "income"
                if (viewModel.isBudgetRecordExpense.value) {
                    type = "expense"
                }
                if (viewModel.currentBudgetRecordId == null) Text(text = "Add $type") else Text(
                    text = "Edit $type"
                )
            }, navigationIcon = {
                IconButton(onClick = {
                    navHostController.navigateUp()
                }) {
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
                label = { Text(text = "Date (DD/MM/YYYY)") },
                onValueChange = {
                    if (it.length <= 8) {
                        viewModel.onEvent(
                            AddEditBudgetRecordEvent.EnteredBudgetRecordDate(
                                it
                            )
                        )
                    }
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = BudgetDateMask()
            )
            Spacer(modifier = Modifier.padding(10.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = if (budgetRecordAmountState == "") "" else budgetRecordAmountState,
                label = { Text(text = "Amount") },
                onValueChange = {
                    viewModel.onEvent(AddEditBudgetRecordEvent.EnteredBudgetRecordAmount(it))
                }, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}
package at.fhooe.mc.ada.features.feature_budget_tracker.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.core.presentation.Screen
import at.fhooe.mc.ada.features.BottomBar
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.model.BudgetRecord
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.use_case.BudgetRecordUseCases
import at.fhooe.mc.ada.features.feature_budget_tracker.domain.use_case.GetBudgetRecordUseCase
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.add_edit_budget_record.AddEditBudgetRecordEvent
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record.BudgetRecordsViewModel
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record.components.ListItemBudgetTracker
import kotlin.math.round
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BudgetTrackerScreen(
    title: String,
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: BudgetRecordsViewModel = hiltViewModel()
) {
    val viewModelState = viewModel.state.value
    val scope = rememberCoroutineScope()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navHostController.navigate(Screen.AddEditBudgetRecordScreen.route)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add new Item")
            }
        },
        bottomBar = {
            BottomBar(navHostController = navHostController)
        },
        topBar = {
            SmallTopAppBar(title = { Text(text = title) })
        }, content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Budget")
                    val budgetRecordsAmount =
                        viewModelState.budgetRecords.sumOf { budgetRecord -> budgetRecord.budgetRecordAmount }
                    Text(
                        text = if (budgetRecordsAmount == 0.0) "-" else budgetRecordsAmount.toString(),
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.padding(15.dp))

                    LazyColumn(content = {
                        itemsIndexed(viewModelState.budgetRecords) { index, item ->
                            ListItemBudgetTracker(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp, 5.dp, 10.dp, 5.dp),
                                descriptionText = item.budgetRecordName,
                                date = item.budgetRecordDate,
                                amount = round(item.budgetRecordAmount * 100) / 100,
                                onItemClick = {
                                    navHostController.navigate(Screen.AddEditBudgetRecordScreen.route + "?budgetRecordId=${item.id}")
                                }
                            )
                            if (index + 1 == viewModelState.budgetRecords.size) {
                                Spacer(modifier = Modifier.padding(40.dp))
                            }
                        }
                    })
                }
            }
        }
    )
}

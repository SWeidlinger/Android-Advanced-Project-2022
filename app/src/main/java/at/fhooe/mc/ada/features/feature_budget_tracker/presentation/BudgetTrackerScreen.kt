@file:OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterialApi::class
)

package at.fhooe.mc.ada.features.feature_budget_tracker.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.R
import at.fhooe.mc.ada.core.presentation.Screen
import at.fhooe.mc.ada.core.presentation.multiFab.AddNewItemMultiFab
import at.fhooe.mc.ada.features.BottomBar
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.add_edit_budget_record.AddEditBudgetRecordScreen
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record.BudgetRecordsEvent
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record.BudgetRecordsViewModel
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record.components.ListItemBudgetTracker
import at.fhooe.mc.ada.core.presentation.multiFab.MultiFab
import at.fhooe.mc.ada.ui.theme.NegativeRed
import at.fhooe.mc.ada.ui.theme.PositiveGreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "UnusedMaterialScaffoldPaddingParameter",
    "RememberReturnType"
)
@Composable
fun BudgetTrackerScreen(
    title: String,
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: BudgetRecordsViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val viewModelState = viewModel.state.value
    val scope = rememberCoroutineScope()
    val scaffoldStateBottomSheet = rememberBottomSheetScaffoldState()
    val scaffoldState = rememberScaffoldState()

    val multiFabItems = GetItemsMultiFab(navHostController = navHostController)

    val budgetRecordsAmount = viewModelState.budgetRecords.sumOf { budgetRecord ->
        if (budgetRecord.budgetRecordAmount.isNotEmpty()) {
            if (budgetRecord.isBudgetRecordExpense) {
                budgetRecord.budgetRecordAmount.toDouble() * -1
            } else {
                budgetRecord.budgetRecordAmount.toDouble()
            }
        } else {
            0.0
        }
    }

    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = scaffoldStateBottomSheet,
        sheetShape = RoundedCornerShape(20.dp, 20.dp),
        sheetElevation = 5.dp,
        sheetBackgroundColor = Color.LightGray,
        sheetGesturesEnabled = true,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            AddEditBudgetRecordScreen(
                navHostController = navHostController,
                modifier = Modifier.fillMaxHeight(0.5f),
                bottomSheetScaffoldState = scaffoldStateBottomSheet
            )
        })
    {
        Scaffold(floatingActionButton = {
            AddNewItemMultiFab(
                multiFabState = MultiFab.multiFabState.value,
                items = multiFabItems,
                onMultiFabStateChange = {
                    MultiFab.multiFabState.value = it
                }, modifier = Modifier.padding(paddingValues)
            )
        }, scaffoldState = scaffoldState,
            snackbarHost = {
                SnackbarHost(it) { data ->
                    Snackbar(
                        actionColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                        snackbarData = data
                    )
                }
            },
            content = {
                Scaffold(bottomBar = {
                    BottomBar(navHostController = navHostController)
                }, content = {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color =
                        if (budgetRecordsAmount == 0.0) {
                            androidx.compose.material3.MaterialTheme.colorScheme.primary
                        } else {
                            if (budgetRecordsAmount > 0) {
                                PositiveGreen
                            } else {
                                NegativeRed
                            }
                        }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it)
                        ) {
                            Column(
                                Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.3f)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize(),
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(text = stringResource(id = R.string.current_budget))
                                    Text(
                                        text = if (budgetRecordsAmount == 0.0) "-" else budgetRecordsAmount.toString(),
                                        fontSize = 55.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                            Surface(
                                modifier = Modifier.fillMaxSize(),
                                shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
                                color = androidx.compose.material3.MaterialTheme.colorScheme.background,
                                elevation = 5.dp
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(top = 20.dp)
                                ) {
                                    if (viewModelState.budgetRecords.isEmpty()) {
                                        Column(
                                            Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = stringResource(id = R.string.no_records_available),
                                                fontSize = 20.sp
                                            )
                                        }
                                    } else {
                                        LazyColumn(content = {
                                            itemsIndexed(viewModelState.budgetRecords) { index, item ->
                                                val dismissState = rememberDismissState(
                                                    confirmStateChange = { dismissValue ->
                                                        when (dismissValue) {
                                                            //Edit
                                                            DismissValue.DismissedToEnd -> {
                                                                navHostController.navigate(Screen.AddEditBudgetRecordScreen.route + "?budgetRecordId=${item.id}")
                                                                false
                                                            }
                                                            //Delete
                                                            DismissValue.DismissedToStart -> {
                                                                viewModelState.budgetRecords
                                                                viewModel.onEvent(
                                                                    BudgetRecordsEvent.DeleteNode(
                                                                        item
                                                                    )
                                                                )
                                                                scope.launch {
                                                                    val result =
                                                                        scaffoldState.snackbarHostState.showSnackbar(
                                                                            message = "Record (${item.budgetRecordName}) deleted",
                                                                            actionLabel = "Undo"
                                                                        )
                                                                    if (result == SnackbarResult.ActionPerformed) {
                                                                        viewModel.onEvent(
                                                                            BudgetRecordsEvent.RestoreRecord
                                                                        )
                                                                    }
                                                                }
                                                                false
                                                            }
                                                            DismissValue.Default -> {
                                                                false
                                                            }
                                                        }
                                                    }
                                                )
                                                SwipeToDismiss(
                                                    state = dismissState,
                                                    dismissThresholds = {
                                                        androidx.compose.material.FractionalThreshold(
                                                            0.12f
                                                        )
                                                    },
                                                    directions = setOf(
                                                        DismissDirection.StartToEnd,
                                                        DismissDirection.EndToStart
                                                    ),
                                                    background = {
                                                        val direction =
                                                            dismissState.dismissDirection
                                                                ?: return@SwipeToDismiss
                                                        val color by animateColorAsState(
                                                            when (dismissState.targetValue) {
                                                                DismissValue.Default -> Color.LightGray
                                                                DismissValue.DismissedToEnd -> PositiveGreen
                                                                DismissValue.DismissedToStart -> NegativeRed
                                                            }
                                                        )
                                                        val alignment = when (direction) {
                                                            DismissDirection.StartToEnd -> Alignment.CenterStart
                                                            DismissDirection.EndToStart -> Alignment.CenterEnd
                                                        }
                                                        val icon = when (direction) {
                                                            DismissDirection.StartToEnd -> Icons.Default.Edit
                                                            DismissDirection.EndToStart -> Icons.Default.Delete
                                                        }
                                                        val scale by animateFloatAsState(
                                                            if (dismissState.targetValue == DismissValue.Default) 0.75f else 1f
                                                        )

                                                        Surface(
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .padding(horizontal = 10.dp),
                                                            shape = if (index == 0) RoundedCornerShape(
                                                                topStart = 10.dp,
                                                                topEnd = 10.dp
                                                            ) else RoundedCornerShape(
                                                                0.dp
                                                            ),
                                                            color = color
                                                        ) {
                                                            Box(
                                                                Modifier
                                                                    .fillMaxSize()
                                                                    .padding(horizontal = 10.dp),
                                                                contentAlignment = alignment
                                                            ) {
                                                                Icon(
                                                                    icon,
                                                                    contentDescription = "",
                                                                    modifier = Modifier.scale(scale)
                                                                )
                                                            }
                                                        }
                                                    }, dismissContent = {
                                                        var dateString = item.budgetRecordDate
                                                        if (dateString.length == 8) {
                                                            dateString = "${
                                                                item.budgetRecordDate.subSequence(
                                                                    0,
                                                                    2
                                                                )
                                                            }/${
                                                                item.budgetRecordDate.subSequence(
                                                                    2,
                                                                    4
                                                                )
                                                            }/${
                                                                item.budgetRecordDate.subSequence(
                                                                    4,
                                                                    8
                                                                )
                                                            }"
                                                        }
                                                        ListItemBudgetTracker(
                                                            modifier = Modifier
                                                                .fillMaxSize()
                                                                .padding(horizontal = 10.dp),
                                                            index = index,
                                                            descriptionText = item.budgetRecordName,
                                                            date = dateString,
                                                            amount = item.budgetRecordAmount,
                                                            isExpense = item.isBudgetRecordExpense
                                                        )
                                                    })

                                                if (index + 1 == viewModelState.budgetRecords.size) {
                                                    Spacer(modifier = Modifier.padding(40.dp))
                                                } else {
                                                    Spacer(modifier = Modifier.padding(3.dp))
                                                    androidx.compose.material3.Divider(
                                                        color = Color.LightGray,
                                                        modifier = Modifier.padding(horizontal = 10.dp)
                                                    )
                                                    Spacer(modifier = Modifier.padding(3.dp))
                                                }
                                            }
                                        })
                                    }
                                }
                            }
                        }
                    }
                })
                val alphaBackground =
                    if (MultiFab.multiFabState.value == MultiFab.State.EXPANDED) 0.75f else 0f
                Box(
                    modifier = Modifier
                        .alpha(animateFloatAsState(alphaBackground).value)
                        .background(Color.Black)
                        .fillMaxSize()
                )
                //handling user interaction when background is pressed
                if (MultiFab.multiFabState.value == MultiFab.State.EXPANDED) {
                    Box(modifier = Modifier
                        .clickable(
                            //disabling ripple effect
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() })
                        {
                            MultiFab.collapse()
                        }
                        .fillMaxSize())
                }
            })
    }
}


@Composable
fun GetItemsMultiFab(
    navHostController: NavHostController,
): List<MultiFab.Item> {
    return listOf(
        MultiFab.Item(
            icon = Icons.Filled.Savings,
            color = Color.Green,
            label = stringResource(id = R.string.add_income),
            description = "",
            identifier = "AddIncomeFab",
            onClick = {
                MultiFab.collapse()
                navHostController.navigate(Screen.AddEditBudgetRecordScreen.route)
            }
        ),
        MultiFab.Item(
            icon = Icons.Filled.ShoppingCart,
            color = Color.Red,
            label = stringResource(id = R.string.add_expense),
            description = "",
            identifier = "AddExpenseFab",
            onClick = {
                MultiFab.collapse()
                navHostController.navigate(Screen.AddEditBudgetRecordScreen.route + "?isBudgetRecordExpense=${true}")
            }
        )
    )
}
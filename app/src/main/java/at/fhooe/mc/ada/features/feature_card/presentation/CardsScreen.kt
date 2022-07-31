@file:OptIn(
    ExperimentalPagerApi::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3Api::class
)

package at.fhooe.mc.ada.features.feature_card.presentation

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarResult
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.core.presentation.Screen
import at.fhooe.mc.ada.features.BottomBar
import at.fhooe.mc.ada.features.feature_budget_tracker.presentation.budget_record.BudgetRecordsEvent
import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.presentation.card.CardsEvent
import at.fhooe.mc.ada.features.feature_card.presentation.card.CardsViewModel
import at.fhooe.mc.ada.features.feature_card.presentation.card.components.CardDetailedView
import at.fhooe.mc.ada.features.feature_card.presentation.card.components.CardItem
import at.fhooe.mc.ada.features.feature_card.presentation.card.components.OrderSection
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import at.fhooe.mc.ada.R
import at.fhooe.mc.ada.core.util.TestTags

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CardsScreen(
    title: String,
    navHostController: NavHostController,
    viewModel: CardsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val viewModelState = viewModel.state.value

    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val numberCards = viewModelState.cards.size
    val scaffoldState = rememberScaffoldState()

    var currentCard by remember {
        mutableStateOf(
            Card(
                cardName = "",
                cardHolderName = "",
                cardNumber = "",
                securityNumber = "",
                expirationDate = "",
                isLocked = false,
                cardStyle = 0,
                dateAdded = 0L,
                cardChipColor = R.drawable.credit_card_chip_gold1
            )
        )
    }
    val scaffoldStateBottomSheet = rememberBottomSheetScaffoldState()

    var bottomSheetExpanded by remember {
        mutableStateOf(false)
    }

    if (scaffoldStateBottomSheet.bottomSheetState.isAnimationRunning && scaffoldStateBottomSheet.bottomSheetState.targetValue == BottomSheetValue.Collapsed) {
        bottomSheetExpanded = false
    } else if (scaffoldStateBottomSheet.bottomSheetState.isAnimationRunning && scaffoldStateBottomSheet.bottomSheetState.targetValue == BottomSheetValue.Expanded) {
        bottomSheetExpanded = true
    }

    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = scaffoldStateBottomSheet,
        sheetShape = RoundedCornerShape(20.dp, 20.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetElevation = 5.dp,
        sheetGesturesEnabled = true,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            CardDetailedView(
                modifier = Modifier
                    .padding(10.dp),
                card = currentCard,
                onCloseClick = {
                    scope.launch {
                        scaffoldStateBottomSheet.bottomSheetState.collapse()
                    }
                },
                onEditClick = {
                    scope.launch {
                        navHostController.navigate(Screen.AddEditCardScreen.route + "?cardId=${currentCard.id}")
                        scaffoldStateBottomSheet.bottomSheetState.collapse()
                    }
                },
                onDeleteClick = {
                    viewModel.onEvent(CardsEvent.DeleteNode(currentCard))
                    scope.launch {
                        scaffoldStateBottomSheet.bottomSheetState.collapse()
                    }
                    scope.launch {
                        val result =
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = "Card (${currentCard.cardName}) deleted",
                                actionLabel = "Undo"
                            )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.onEvent(CardsEvent.RestoreCard)
                        }
                    }
                })
        }) {
        androidx.compose.material.Scaffold(floatingActionButton = {
            FloatingActionButton(modifier = Modifier.testTag(TestTags.FAB_CARD_SCREEN_ADD),
                onClick = {
                    navHostController.navigate(Screen.AddEditCardScreen.route)
                },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.AddCard,
                    contentDescription = stringResource(id = R.string.add_new_card)
                )
            }
        }, bottomBar = {
            NavigationBar(contentColor = MaterialTheme.colorScheme.secondary) {
                BottomBar(navHostController = navHostController)
            }
        }, scaffoldState = scaffoldState,
            snackbarHost = {
                SnackbarHost(it) { data ->
                    Snackbar(
                        actionColor = MaterialTheme.colorScheme.primary,
                        snackbarData = data
                    )
                }
            },
            backgroundColor = MaterialTheme.colorScheme.background,
            content = {
                androidx.compose.material.Scaffold(topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(text = title) })
                }, backgroundColor = MaterialTheme.colorScheme.background,
                    content = {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(it), verticalArrangement = Arrangement.Top
                        ) {
                            OrderSection(
                                modifier = Modifier.align(Alignment.Start),
                                cardOrder = viewModelState.cardOrder,
                                onOrderChange = { cardOrder ->
                                    viewModel.onEvent(CardsEvent.Order(cardOrder))
                                })
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(it),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            if (viewModelState.cards.isNotEmpty()) {
                                HorizontalPager(
                                    count = numberCards,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.325f),
                                    state = pagerState,
                                    contentPadding = PaddingValues(horizontal = 30.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    itemSpacing = (-55).dp
                                ) { index ->
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .graphicsLayer {
                                                scrollAnimation(
                                                    pagerScope = this@HorizontalPager,
                                                    graphicsLayerScope = this,
                                                    scope = index
                                                )
                                            }
                                    ) {
                                        val card = viewModelState.cards[index]
                                        CardItem(
                                            card,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(10.dp),
                                            onClick = {
                                                currentCard = card
                                                scope.launch {
                                                    scaffoldStateBottomSheet.bottomSheetState.expand()
                                                }
                                            })
                                    }
                                }
                                HorizontalPagerIndicator(
                                    pagerState = pagerState,
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .padding(top = 3.dp, end = 16.dp, start = 16.dp),
                                    activeColor = MaterialTheme.colorScheme.onBackground,
                                    inactiveColor = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                                )
                            } else {
                                Box(
                                    Modifier
                                        .padding(40.dp)
                                        .fillMaxWidth()
                                        .fillMaxSize(0.4f)
                                        .clickable {
                                            navHostController.navigate(Screen.AddEditCardScreen.route)
                                        }
                                        .background(
                                            shape = RoundedCornerShape(10.dp),
                                            color = MaterialTheme.colorScheme.secondary
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = stringResource(id = R.string.add_a_card),
                                        style = MaterialTheme.typography.headlineLarge,
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    })
                val alphaBackground =
                    if (bottomSheetExpanded) 0.75f else 0f
                Box(
                    modifier = Modifier
                        .alpha(animateFloatAsState(alphaBackground).value)
                        .background(Color.Black)
                        .fillMaxSize()
                )
                //handling user interaction when background is pressed
                if (bottomSheetExpanded) {
                    Box(modifier = Modifier
                        .clickable(
                            //disabling ripple effect
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() })
                        {
                            scope.launch {
                                scaffoldStateBottomSheet.bottomSheetState.collapse()
                            }
                        }
                        .fillMaxSize())
                }
            })
    }
}

@OptIn(ExperimentalPagerApi::class)
fun scrollAnimation(
    pagerScope: PagerScope,
    graphicsLayerScope: GraphicsLayerScope,
    scope: Int
) {
    // Calculate the absolute offset for the current page from the
    // scroll position. We use the absolute value which allows us to mirror
    // any effects for both directions
    val pageOffset = pagerScope.calculateCurrentOffsetForPage(scope).absoluteValue

    // We animate the scaleX + scaleY, between 70% and 100%
    lerp(
        start = 0.7f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    ).also { scale ->
        graphicsLayerScope.scaleX = scale
        graphicsLayerScope.scaleY = scale
    }

    // We animate the alpha, between 50% and 100%
    graphicsLayerScope.alpha = lerp(
        start = 0.5f,
        stop = 1f,
        fraction = 1f - pageOffset.coerceIn(0f, 1f)
    )
}
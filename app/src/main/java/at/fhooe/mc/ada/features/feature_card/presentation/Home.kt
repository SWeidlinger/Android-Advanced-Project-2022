package at.fhooe.mc.ada.features.feature_card.presentation

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.R
import at.fhooe.mc.ada.core.presentation.Screen
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.AddEditCardScreen
import at.fhooe.mc.ada.features.feature_card.presentation.card.CardsEvent
import at.fhooe.mc.ada.features.feature_card.presentation.card.CardsViewModel
import at.fhooe.mc.ada.features.feature_card.presentation.card.components.CardItem
import at.fhooe.mc.ada.features.feature_card.presentation.card.components.OrderSection
import at.fhooe.mc.ada.ui.BottomBar
import at.fhooe.mc.ada.features.feature_card.presentation.util.MultiFab
import com.google.accompanist.pager.*
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun HomeScreen(
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: CardsViewModel = hiltViewModel()
) {
    var multiFabState by remember {
        mutableStateOf(MultiFab.State.COLLAPSED)
    }

    val context = LocalContext.current

    val viewModelState = viewModel.state.value

    val multiFabItems = GetItems(navHostController)
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState()
    val numberCards = viewModelState.cards.size

    scope.launch {
        pagerState.scrollToPage(numberCards / 2)
    }

    Scaffold(floatingActionButton = {
        AddNewItemButton(
            multiFabState = multiFabState,
            items = multiFabItems,
            onMultiFabStateChange = {
                multiFabState = it
                MultiFab.multiFabState.value = it
            }, modifier = Modifier.padding(paddingValues)
        )
    }, content = {
        Scaffold(topBar = {
            SmallTopAppBar(
                title = { Text(text = "Cards") },
            )
        }, bottomBar = {
            NavigationBar() {
                BottomBar(navHostController = navHostController)
            }
        }, content = {
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
                            .fillMaxHeight(0.4f),
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
                            CardItem(card, modifier = Modifier.fillMaxSize(), onClick = {
                                navHostController.navigate(Screen.AddEditCardScreen.route + "?cardId=${card.id}")
                            })
                        }
                    }
                    HorizontalPagerIndicator(
                        pagerState = pagerState,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp),
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
                            .background(shape = RoundedCornerShape(10.dp), color = Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add a card!",
                            style = MaterialTheme.typography.headlineLarge,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        })
        val alphaBackground = if (multiFabState == MultiFab.State.EXPANDED) 0.75f else 0f
        Box(
            modifier = Modifier
                .alpha(animateFloatAsState(alphaBackground).value)
                .background(Color.Black)
                .fillMaxSize()
        )

        //handling user interaction when background is pressed
        if (multiFabState == MultiFab.State.EXPANDED) {
            Box(modifier = Modifier
                .clickable(
                    //disabling ripple effect
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() })
                {
                    multiFabState = MultiFab.State.COLLAPSED
                }
                .fillMaxSize())
        }
    })
}

@OptIn(ExperimentalPagerApi::class)
fun scrollAnimation(pagerScope: PagerScope, graphicsLayerScope: GraphicsLayerScope, scope: Int) {
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

@Composable
fun GetItems(navHostController: NavHostController): List<MultiFab.Item> {
    return listOf(
        MultiFab.Item(
            icon = Icons.Filled.List,
            color = Color.Green,
            label = "Add card",
            description = stringResource(id = R.string.add_new_item),
            identifier = "AddCardFab",
            onClick = {
                navHostController.navigate(Screen.AddEditCardScreen.route)
            }
        ),
        MultiFab.Item(
            icon = Icons.Filled.Face,
            color = Color.Green,
            label = "Add invoice",
            description = stringResource(id = R.string.add_new_invoice),
            identifier = "AddInvoiceFab",
            onClick = {

            }
        ),
        MultiFab.Item(
            icon = Icons.Filled.AccountBox,
            color = Color.Green,
            label = "Add ticket",
            description = stringResource(id = R.string.add_new_ticket),
            identifier = "AddTicketFab",
            onClick = {

            }
        )
    )
}

@Composable
fun AddNewItemButton(
    modifier: Modifier,
    multiFabState: MultiFab.State,
    items: List<MultiFab.Item>,
    onMultiFabStateChange: (MultiFab.State) -> Unit
) {
    val transition = updateTransition(targetState = multiFabState, label = "transitionIcon")

    val fabScale by transition.animateFloat(
        label = "fabScale",
        transitionSpec = { tween(durationMillis = 15) }) {
        if (it == MultiFab.State.COLLAPSED) 0f else 1f
    }

    val rotate by transition.animateFloat(label = "transitionIcon") {
        if (it == MultiFab.State.EXPANDED) 45f else 0f
    }

    val alpha by transition.animateFloat(
        label = "alpha",
        transitionSpec = { tween(durationMillis = 50) }) {
        if (it == MultiFab.State.EXPANDED) 1f else 0f
    }

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End) {
        val visible = remember { mutableStateOf(true) }
        var scale: State<Float>
        val fabScale by transition.animateFloat(label = "fabScale") {
            if (it == MultiFab.State.COLLAPSED) 0f else 1f
        }

        if (transition.currentState == MultiFab.State.EXPANDED) {
            visible.value = true
            items.forEachIndexed { index, item ->
                MinFab(
                    item = item,
                    scale = fabScale
                )
                Spacer(modifier = Modifier.size(23.dp))
            }
        } else {
            visible.value = false
        }
        FloatingActionButton(
            onClick = {
                onMultiFabStateChange(
                    if (transition.currentState == MultiFab.State.EXPANDED) MultiFab.State.COLLAPSED else MultiFab.State.EXPANDED
                )
            }, modifier = modifier
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add_new_item),
                modifier = Modifier.rotate(rotate)
            )
        }
    }
}

@Composable
fun MinFab(
    item: MultiFab.Item,
    scale: Float
) {
    Row(
        modifier = Modifier.clickable {
            item.onClick.invoke()
        }, verticalAlignment = Alignment.CenterVertically
    ) {
        var scaleText = 1f
        if (scale < 1) {
            scaleText = 0f
        }
        Text(
            text = item.label,
            modifier = Modifier.scale(scaleText),
            textAlign = TextAlign.End,
            color = Color.White
        )
        Spacer(modifier = Modifier.padding(15.dp))
        SmallFloatingActionButton(
            onClick = {
                item.onClick.invoke()
            },
            Modifier
                .size(35.dp)
                .scale(scale)
                .offset((-10).dp, 0.dp),
            elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.description
            )
        }
    }
}
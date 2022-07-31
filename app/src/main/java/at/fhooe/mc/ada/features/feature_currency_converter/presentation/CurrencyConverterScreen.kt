@file:OptIn(ExperimentalMaterialApi::class)

package at.fhooe.mc.ada.features.feature_currency_converter.presentation

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.features.feature_currency_converter.domain.CurrencyWithRate
import at.fhooe.mc.ada.features.feature_currency_converter.domain.MainViewModel
import at.fhooe.mc.ada.features.feature_currency_converter.data.CurrencyAndCountry
import at.fhooe.mc.ada.features.BottomBar
import at.fhooe.mc.ada.features.feature_currency_converter.data.Constants
import kotlinx.coroutines.launch
import kotlin.math.round
import at.fhooe.mc.ada.R
import at.fhooe.mc.ada.features.feature_currency_converter.presentation.components.CurrencyCard
import at.fhooe.mc.ada.features.feature_currency_converter.presentation.components.CurrencyCardTextField
import at.fhooe.mc.ada.features.feature_currency_converter.presentation.components.CurrencyListItem
import at.fhooe.mc.ada.features.feature_currency_converter.presentation.components.CurrencyPickerListItem

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class
)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState", "MutableCollectionMutableState",
    "UnusedMaterialScaffoldPaddingParameter"
)
@Composable
fun CurrencyConverterScreen(
    title: String,
    navHostController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: MainViewModel = hiltViewModel()
) {
    var fromCurrencyCode by remember {
        mutableStateOf("-")
    }
    var toCurrencyCode by remember {
        mutableStateOf("-")
    }
    var amountValue by remember {
        mutableStateOf(TextFieldValue("1.00"))
    }

    var resultValue by remember {
        mutableStateOf("-")
    }

    var isFrom by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()
    val scaffoldStateBottomSheet = rememberBottomSheetScaffoldState()

    val scaffoldState = rememberScaffoldState()

    var bottomSheetExpanded by remember {
        mutableStateOf(false)
    }

    if (scaffoldStateBottomSheet.bottomSheetState.isAnimationRunning && scaffoldStateBottomSheet.bottomSheetState.targetValue == BottomSheetValue.Collapsed) {
        bottomSheetExpanded = false
    } else if (scaffoldStateBottomSheet.bottomSheetState.isAnimationRunning && scaffoldStateBottomSheet.bottomSheetState.targetValue == BottomSheetValue.Expanded) {
        bottomSheetExpanded = true
    }

    var currencyList by mutableStateOf(mutableStateListOf<CurrencyWithRate>())


    val currencyListState = rememberLazyListState()

    scope.launch {
        viewModel.conversion.collect { event ->
            when (event) {
                is MainViewModel.CurrencyEvent.Success -> {
                    resultValue = event.resultText
                    currencyList = event.rates
                }
                is MainViewModel.CurrencyEvent.Failure -> {
                    resultValue = event.errorText
                }
                is MainViewModel.CurrencyEvent.Loading -> {}
                else -> Unit
            }
        }
    }

    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = scaffoldStateBottomSheet,
        sheetShape = RoundedCornerShape(20.dp, 20.dp),
        sheetElevation = 5.dp,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetGesturesEnabled = true,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.68f), horizontalAlignment = Alignment.Start
            ) {
                IconButton(
                    onClick = {
                        scope.launch {
                            scaffoldStateBottomSheet.bottomSheetState.collapse()
                        }
                    },
                    modifier = Modifier.scale(0.9f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.close_bottom_sheet),
                        modifier = Modifier.scale(1.2f)
                    )
                }
                LazyColumn(modifier = Modifier
                    .padding(10.dp, 0.dp, 10.dp, 0.dp)
                    .fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    state = currencyListState,
                    content = {
                        items(Constants.CURRENCY_CODES_LIST) { item ->
                            CurrencyPickerListItem(item = item, onCurrencyPickerListItemClick = {
                                if (isFrom) {
                                    fromCurrencyCode = item.currencyCode
                                } else {
                                    toCurrencyCode = item.currencyCode
                                }
                                scope.launch {
                                    scaffoldStateBottomSheet.bottomSheetState.collapse()
                                    //used so the beginning of the list is shown always
                                    currencyListState.scrollToItem(0)
                                }
                            })
                            Spacer(modifier = Modifier.padding(5.dp))
                        }
                    })
            }
        },
    ) {
        Scaffold(floatingActionButton = {
            ExtendedFloatingActionButton(onClick = {
                val currentItem =
                    Constants.CURRENCY_CODES_LIST.find { it.currencyCode == toCurrencyCode }
                if (fromCurrencyCode != "-" && toCurrencyCode != "-") {
                    viewModel.convert(
                        amountValue.text,
                        fromCurrencyCode,
                        toCurrencyCode,
                        currentItem?.currencySymbol
                    )
                } else {
                    scope.launch {
                        scaffoldState.snackbarHostState.showSnackbar("Please specify FROM and TO currency")
                    }
                }
            }, containerColor = MaterialTheme.colorScheme.primary) {
                Icon(
                    imageVector = Icons.Default.Cached,
                    contentDescription = stringResource(id = R.string.convert_currency)
                )
                Spacer(modifier = Modifier.padding(end = 5.dp))
                Text(text = stringResource(id = R.string.convert))
            }
        }, bottomBar = {
            BottomBar(navHostController = navHostController)
        }, scaffoldState = scaffoldState,
            content = {
                Column(
                    Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.3f),
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(bottomEnd = 35.dp, bottomStart = 35.dp),
                        elevation = 5.dp
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 25.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(10.dp, 1.dp, 10.dp, 5.dp)
                                        .fillMaxWidth()
                                        .fillMaxHeight(0.5f),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.Top,
                                ) {
                                    Row(
                                        Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxHeight()
                                                .fillMaxWidth(0.5f)
                                        ) {
                                            CurrencyCardTextField(
                                                label = stringResource(id = R.string.amount),
                                                modifier = Modifier.fillMaxSize(),
                                                textFieldValue = amountValue,
                                                onValueChange = { newValue ->
                                                    amountValue = newValue
                                                }
                                            )
                                        }

                                        Spacer(modifier = Modifier.padding(5.dp))
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceAround,
                                            modifier = Modifier
                                                .fillMaxSize(),
                                        ) {
                                            CurrencyCard(
                                                currencyCode = fromCurrencyCode,
                                                label = stringResource(R.string.from),
                                                modifier = Modifier
                                                    .fillMaxWidth(0.5f)
                                                    .fillMaxHeight(),
                                                onCurrencyCardClick = {
                                                    isFrom = true
                                                    scope.launch {
                                                        scaffoldStateBottomSheet.bottomSheetState.expand()
                                                    }
                                                })
                                            Spacer(modifier = Modifier.padding(5.dp))
                                            CurrencyCard(
                                                currencyCode = toCurrencyCode,
                                                label = stringResource(R.string.to),
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .fillMaxHeight(),
                                                onCurrencyCardClick = {
                                                    isFrom = false
                                                    scope.launch {
                                                        scaffoldStateBottomSheet.bottomSheetState.expand()
                                                    }
                                                }
                                            )
                                        }
                                    }
                                }
                                Card(
                                    Modifier
                                        .fillMaxSize()
                                        .padding(10.dp, 0.dp, 10.dp, 0.dp),
                                    elevation = CardDefaults.cardElevation(0.dp),
                                    colors = CardDefaults.cardColors(Color.Transparent)
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Text(
                                            text = "${stringResource(R.string.converted_amount)} ($fromCurrencyCode - $toCurrencyCode)",
                                            textAlign = TextAlign.Center,
                                            fontSize = 12.sp
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(15.dp, 0.dp, 15.dp, 10.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = resultValue,
                                                fontSize = 50.sp,
                                                maxLines = 1,
                                                fontWeight = FontWeight.Bold,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Card(
                        modifier = Modifier
                            .padding(it)
                            .padding(15.dp, 5.dp, 15.dp, 0.dp),
                        shape = RoundedCornerShape(10.dp, 10.dp, 0.dp, 0.dp),
                        colors = CardDefaults.cardColors(Color.Transparent)
                    ) {
                        LazyColumn(content = {
                            itemsIndexed(Constants.CURRENCY_CODES_LIST) { index, item: CurrencyAndCountry ->
                                var amountFromTextField = 0.00
                                if (amountValue.text.isNotEmpty() && !amountValue.text.toDouble()
                                        .isNaN()
                                ) {
                                    amountFromTextField = amountValue.text.toDouble()
                                }
                                if (resultValue != "-") {
                                    CurrencyListItem(
                                        item,
                                        currencyList,
                                        amountFromTextField
                                    )

                                    if (index + 1 == Constants.CURRENCY_CODES_LIST.size) {
                                        Spacer(modifier = Modifier.padding(40.dp))
                                    } else {
                                        Spacer(modifier = Modifier.padding(3.dp))
                                        androidx.compose.material3.Divider(
                                            modifier = Modifier.padding(),
                                            color = Color.LightGray
                                        )
                                        Spacer(modifier = Modifier.padding(3.dp))
                                    }
                                }
                            }
                        })
                    }
                }
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
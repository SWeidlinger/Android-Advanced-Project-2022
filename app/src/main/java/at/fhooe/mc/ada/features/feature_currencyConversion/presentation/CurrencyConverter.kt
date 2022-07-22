package at.fhooe.mc.ada.ui.bottomNavigation.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.features.feature_currencyConversion.domain.CurrencyWithRate
import at.fhooe.mc.ada.features.feature_currencyConversion.domain.MainViewModel
import at.fhooe.mc.ada.features.feature_currencyConversion.data.CurrencyAndCountry
import at.fhooe.mc.ada.ui.BottomBar
import at.fhooe.mc.ada.features.feature_currencyConversion.data.Constants
import kotlinx.coroutines.launch
import kotlin.math.round

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class
)
@SuppressLint(
    "UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition",
    "UnrememberedMutableState", "MutableCollectionMutableState"
)
@Composable
fun CurrencyConverterScreen(
    title: String,
    navController: NavHostController,
    paddingValues: PaddingValues,
    viewModel: MainViewModel
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

    var isFrom = true
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    var bottomSheetExpanded by remember {
        mutableStateOf(false)
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
                is MainViewModel.CurrencyEvent.Loading -> {
                    //TODO
                }
                else -> Unit
            }
        }
    }

    BottomSheetScaffold(
        modifier = Modifier,
        scaffoldState = scaffoldState,
        sheetShape = RoundedCornerShape(20.dp, 20.dp),
        sheetElevation = 5.dp,
        sheetBackgroundColor = Color.LightGray,
        sheetGesturesEnabled = false,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f)
            ) {
                LazyColumn(modifier = Modifier
                    .padding(10.dp)
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
                                    scaffoldState.bottomSheetState.collapse()
                                    bottomSheetExpanded = false
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
                viewModel.convert(
                    amountValue.text,
                    fromCurrencyCode,
                    toCurrencyCode,
                    currentItem?.currencySymbol
                )
            }) {
                Text(text = "Convert")
            }
        },
            topBar = {
                SmallTopAppBar(title = { Text(text = title) })
            }, bottomBar = {
                BottomBar(navHostController = navController)
            }, content = {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(it), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp, 1.dp, 10.dp, 5.dp)
                            .fillMaxWidth()
                            .height(80.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top,
                    ) {
                        Row(
                            Modifier
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Card(
                                colors = CardDefaults.cardColors(Color.Transparent),
                                elevation = CardDefaults.cardElevation(5.dp)
                            ) {
                                TextField(
                                    value = amountValue,
                                    label = { Text(text = "Amount", textAlign = TextAlign.Center) },
                                    singleLine = true,
                                    onValueChange = { newValue ->
                                        amountValue = newValue
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    textStyle = TextStyle(
                                        textAlign = TextAlign.Center,
                                        fontSize = 20.sp
                                    ),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = TextFieldDefaults.textFieldColors(
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.padding(5.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(0.dp, 9.dp, 0.dp, 10.dp),
                            ) {
                                CurrencyCard(
                                    currencyCode = fromCurrencyCode,
                                    label = "from",
                                    modifier = Modifier
                                        .fillMaxWidth(0.5f)
                                        .fillMaxHeight(),
                                    onCurrencyCardClick = {
                                        scope.launch {
                                            isFrom = true
                                            bottomSheetExpanded = true
                                            scaffoldState.bottomSheetState.expand()
                                        }
                                    })
                                Spacer(modifier = Modifier.padding(5.dp))
                                CurrencyCard(
                                    currencyCode = toCurrencyCode, label = "to", modifier = Modifier
                                        .fillMaxWidth()
                                        .fillMaxHeight(), onCurrencyCardClick = {
                                        scope.launch {
                                            isFrom = false
                                            bottomSheetExpanded = true
                                            scaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(10.dp, 0.dp, 10.dp, 0.dp)
                            .height(75.dp),
                        border = BorderStroke(1.dp, Color.Black),
                        elevation = CardDefaults.cardElevation(0.dp),
                        colors = CardDefaults.cardColors(Color.Transparent)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(15.dp, 7.dp, 15.dp, 0.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Converted amount",
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "$fromCurrencyCode - $toCurrencyCode",
                                    textAlign = TextAlign.Center,
                                    fontSize = 12.sp
                                )
                            }
                            Text(
                                text = resultValue,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(10.dp, 0.dp, 10.dp, 5.dp)
                                    .offset(0.dp, (-5).dp),
                                textAlign = TextAlign.Center,
                                fontSize = 40.sp,
                                maxLines = 1,
                                fontWeight = FontWeight.Bold, overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Card(
                        modifier = Modifier.padding(10.dp, 15.dp, 10.dp, 10.dp),
                        colors = CardDefaults.cardColors(Color.LightGray)
                    ) {
                        LazyColumn(content = {
                            items(Constants.CURRENCY_CODES_LIST) { item ->
                                var amountFromTextField = 0.00
                                if (amountValue.text.isNotEmpty() && !amountValue.text.toDouble()
                                        .isNaN()
                                ) {
                                    amountFromTextField = amountValue.text.toDouble()
                                }
                                CurrencyListItem(item, currencyList, amountFromTextField)
                                Spacer(modifier = Modifier.padding(5.dp))
                            }
                        })
                    }
                }
            })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyCard(
    currencyCode: String,
    label: String,
    modifier: Modifier,
    onCurrencyCardClick: () -> Unit
) {
    Card(
        modifier = modifier.offset(0.dp, 1.dp),
        onClick = onCurrencyCardClick,
        elevation = CardDefaults.cardElevation(5.dp),
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = label,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp, 0.dp, 0.dp, 0.dp)
                        .offset(0.dp, 1.dp),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Left
                )
                Text(
                    text = currencyCode,
                    modifier = Modifier.fillMaxWidth(),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        })
}

@Composable
fun CurrencyListItem(
    item: CurrencyAndCountry,
    currencyCountryList: List<CurrencyWithRate>,
    amountFromTextField: Double
) {
    var amount = 0.00
    val currentCountryItem = currencyCountryList.find {
        it.currencyCode == item.currencyCode
    }

    if (currentCountryItem != null) {
        amount = round(amountFromTextField * currentCountryItem.rate * 100) / 100
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = item.currencySymbol,
            fontSize = 20.sp,
            textAlign = TextAlign.Right,
            modifier = Modifier
                .fillMaxSize()
                .padding(0.dp, 0.dp, 15.dp, 0.dp)
        )
//        Icon(
//            imageVector = Icons.Default.ShoppingCart,
//            contentDescription = "",
//            Modifier
//                .padding(10.dp, 10.dp, 0.dp, 10.dp)
//                .size(30.dp)
//                .align(Alignment.CenterStart)
//        )
        Text(
            text = if (amount == 0.00) "-" else amount.toString(),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold, modifier = Modifier
                .fillMaxSize()
        )
        Column(
            Modifier
                .padding(10.dp, 5.dp, 0.dp, 5.dp)
                .fillMaxSize(), horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = item.currencyCode,
                fontSize = 20.sp,
                textAlign = TextAlign.Left,
                fontWeight = FontWeight.Bold, modifier = Modifier.fillMaxWidth()
            )
            Text(
                text = item.countryName,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 10.sp,
                textAlign = TextAlign.Left,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyPickerListItem(item: CurrencyAndCountry, onCurrencyPickerListItemClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        onClick = onCurrencyPickerListItemClick
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(Modifier.padding(10.dp, 5.dp, 0.dp, 5.dp)) {
                Text(
                    text = item.currencyCode,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Left,
                    fontWeight = FontWeight.Bold
                )
                Text(text = item.countryName, fontSize = 12.sp, textAlign = TextAlign.Left)
            }
            Text(
                text = item.currencySymbol,
                fontSize = 25.sp,
                modifier = Modifier.padding(0.dp, 0.dp, 15.dp, 0.dp),
                textAlign = TextAlign.Center
            )
//            androidx.compose.material3.Icon(
//                imageVector = Icons.Default.ShoppingCart,
//                contentDescription = "",
//                Modifier
//                    .padding(10.dp)
//                    .size(35.dp)
//            )
        }
    }
}
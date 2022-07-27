package at.fhooe.mc.ada.features.feature_currencyConversion.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
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
import at.fhooe.mc.ada.features.BottomBar
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
    val scaffoldStateBottomSheet = rememberBottomSheetScaffoldState()

    val scaffoldState = rememberScaffoldState()

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
        scaffoldState = scaffoldStateBottomSheet,
        sheetShape = RoundedCornerShape(20.dp, 20.dp),
        sheetElevation = 5.dp,
        sheetBackgroundColor = Color.LightGray,
        sheetGesturesEnabled = false,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.68f), horizontalAlignment = Alignment.Start
            ) {
                IconButton(
                    onClick = { scope.launch { scaffoldStateBottomSheet.bottomSheetState.collapse() } },
                    modifier = Modifier.scale(0.9f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close bottom sheet", modifier = Modifier.scale(1.4f)
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
        androidx.compose.material.Scaffold(floatingActionButton = {
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
            }) {
                Icon(
                    imageVector = Icons.Default.Cached,
                    contentDescription = "Convert Currency"
                )
                Spacer(modifier = Modifier.padding(end = 5.dp))
                Text(text = "Convert")
            }
        },
            topBar = {
                SmallTopAppBar(title = { Text(text = title) })
            },
            bottomBar = {
                BottomBar(navHostController = navController)
            },
            scaffoldState = scaffoldState,
            backgroundColor = MaterialTheme.colorScheme.background,
            content = {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(it), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(modifier = Modifier.fillMaxHeight(0.27f)) {
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
                                        label = "Amount",
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
                                        label = "from",
                                        modifier = Modifier
                                            .fillMaxWidth(0.5f)
                                            .fillMaxHeight(),
                                        onCurrencyCardClick = {
                                            scope.launch {
                                                isFrom = true
                                                bottomSheetExpanded = true
                                                scaffoldStateBottomSheet.bottomSheetState.expand()
                                            }
                                        })
                                    Spacer(modifier = Modifier.padding(5.dp))
                                    CurrencyCard(
                                        currencyCode = toCurrencyCode,
                                        label = "to",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .fillMaxHeight(),
                                        onCurrencyCardClick = {
                                            scope.launch {
                                                isFrom = false
                                                bottomSheetExpanded = true
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
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(15.dp, 5.dp, 15.dp, 10.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = resultValue,
                                        fontSize = 40.sp,
                                        maxLines = 1,
                                        fontWeight = FontWeight.Bold,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                    Card(
                        modifier = Modifier.padding(10.dp, 15.dp, 10.dp, 0.dp),
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
                                    CurrencyListItem(item, currencyList, amountFromTextField)
                                }
                                Spacer(modifier = Modifier.padding(5.dp))
                                if (index + 1 == Constants.CURRENCY_CODES_LIST.size) {
                                    Spacer(modifier = Modifier.padding(35.dp))
                                }
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
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.padding(start = 10.dp, top = 7.dp)) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = label,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Start
                    )
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = currencyCode,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyCardTextField(
    label: String,
    modifier: Modifier,
    textFieldValue: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Card(
        modifier = modifier.offset(0.dp, 1.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier.padding(start = 10.dp, top = 7.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = label,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize().offset(y = 10.dp), contentAlignment = Alignment.Center
                    ) {
                        TextField(
                            value = textFieldValue,
                            singleLine = true,
                            onValueChange = onValueChange,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = TextStyle(
                                textAlign = TextAlign.Center,
                                fontSize = 24.sp
                            ),
                            shape = RoundedCornerShape(10.dp),
                            colors = TextFieldDefaults.textFieldColors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                containerColor = Color.Transparent
                            )
                        )
                    }
                }
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
        }
    }
}
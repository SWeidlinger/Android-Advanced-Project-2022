package at.fhooe.mc.ada.ui.bottomNavigation.screens

import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.currencyConversion.MainViewModel
import at.fhooe.mc.ada.data.CurrencyAndCountry
import at.fhooe.mc.ada.ui.BottomBar
import at.fhooe.mc.ada.util.Constants
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
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
        mutableStateOf(TextFieldValue("0.00"))
    }

    var resultValue by remember {
        mutableStateOf("result")
    }

    var isFrom = true
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    val currencyListState = rememberLazyListState()

    scope.launch {
        viewModel.conversion.collect { event ->
            when (event) {
                is MainViewModel.CurrencyEvent.Success -> {
                    resultValue = event.resultText
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
        sheetBackgroundColor = Color.LightGray,
        sheetGesturesEnabled = false,
        sheetPeekHeight = 0.dp,
        sheetContent = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            ) {
                LazyColumn(modifier = Modifier
                    .padding(10.dp)
                    .fillMaxSize(),
                    contentPadding = PaddingValues(10.dp),
                    state = currencyListState,
                    content = {
                        items(Constants.CURRENCY_CODES_LIST) { item ->
                            CurrencyListItem(item = item, onCurrencyListItemClick = {
                                if (isFrom) {
                                    fromCurrencyCode = item.currencyCode
                                } else {
                                    toCurrencyCode = item.currencyCode
                                }
                                scope.launch {
                                    scaffoldState.bottomSheetState.collapse()
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
                viewModel.convert(amountValue.text, fromCurrencyCode, toCurrencyCode)
            }) {
                Text(text = "Convert")
            }
        },
            topBar = {
                SmallTopAppBar(title = { Text(text = title) })
            }, bottomBar = {
                BottomBar(navHostController = navController)
            }, content = {
                Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier
                            .padding(it)
                            .padding(10.dp)
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
                            OutlinedTextField(
                                value = amountValue,
                                label = { Text(text = "Amount") },
                                singleLine = true,
                                onValueChange = { newValue ->
                                    amountValue = newValue
                                },
                                modifier = Modifier
                                    .fillMaxWidth(0.5f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            Spacer(modifier = Modifier.padding(5.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight()
                                    .padding(0.dp, 15.dp, 0.dp, 7.dp),
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
                                            scaffoldState.bottomSheetState.expand()
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Text(text = resultValue, textAlign = TextAlign.Center, fontSize = 50.sp)
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
        modifier = modifier,
        onClick = onCurrencyCardClick,
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = label, fontSize = 12.sp)
                Text(
                    text = currencyCode, fontSize = 25.sp, fontWeight = FontWeight.Bold
                )
            }
        })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyListItem(item: CurrencyAndCountry, onCurrencyListItemClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        onClick = onCurrencyListItemClick
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
            androidx.compose.material3.Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "",
                Modifier
                    .padding(10.dp)
                    .size(35.dp)
            )
        }
    }
}
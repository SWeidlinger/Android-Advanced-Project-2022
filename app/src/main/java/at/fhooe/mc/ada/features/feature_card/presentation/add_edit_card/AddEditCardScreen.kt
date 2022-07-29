package at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.features.feature_card.domain.util.CardNumberMask
import at.fhooe.mc.ada.features.feature_card.domain.util.ExpirationDateMask
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.components.CardStyleCard
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.components.CustomOutlinedTextField
import com.google.accompanist.flowlayout.FlowCrossAxisAlignment
import com.google.accompanist.flowlayout.FlowMainAxisAlignment
import com.google.accompanist.flowlayout.FlowRow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditCardScreen(
    navHostController: NavHostController,
    viewModel: AddEditCardViewModel = hiltViewModel()
) {
    val cardNameState = viewModel.cardName.value
    val cardHolderNameState = viewModel.cardHolderName.value
    val cardNumberState = viewModel.cardNumber.value
    val cardSecurityNumberState = viewModel.cardSecurityNumber.value
    val cardExpirationDateState = viewModel.cardExpirationDate.value
    val isLockedState = viewModel.cardIsLocked.value
    val styleState = viewModel.cardStyle.value

    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AddEditCardViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
                is AddEditCardViewModel.UiEvent.SaveCard -> {
                    navHostController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            MediumTopAppBar(title = {
                if (viewModel.currentCardId == null) Text(text = "Add card") else Text(
                    text = "Edit card"
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
                    onClick = { viewModel.onEvent(AddEditCardEvent.SaveCard) },
                    content = {
                        var text = ""
                        text = if (viewModel.currentCardId == null) "Add" else "Edit"
                        Text(text = text, fontSize = 16.sp)
                    }, shape = RoundedCornerShape(10.dp)
                )
            })
        }, scaffoldState = scaffoldState,
        backgroundColor = MaterialTheme.colorScheme.background
    )
    {
        Column(
            Modifier
                .fillMaxSize()
                .padding(50.dp, 10.dp, 50.dp, 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = cardNameState,
                label = { Text(text = "Card name") },
                onValueChange = { viewModel.onEvent(AddEditCardEvent.EnteredCardName(it)) },
            )
            Spacer(modifier = Modifier.padding(10.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = cardHolderNameState,
                label = { Text(text = "Card holder name") },
                onValueChange = { viewModel.onEvent(AddEditCardEvent.EnteredCardHolderName(it)) },
            )
            Spacer(modifier = Modifier.padding(10.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = if (cardNumberState != "") cardNumberState else "",
                label = { Text(text = "Card number") },
                onValueChange = {
                    if (it.length <= 16) {
                        viewModel.onEvent(AddEditCardEvent.EnteredCardNumber(it))
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = CardNumberMask()
            )
            Spacer(modifier = Modifier.padding(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                CustomOutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .padding(end = 10.dp),
                    value = if (cardSecurityNumberState != "") cardSecurityNumberState else "",
                    label = { Text(text = "CCV") },
                    onValueChange = {
                        if (it.length <= 3) {
                            viewModel.onEvent(AddEditCardEvent.EnteredSecurityNumber(it))
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                CustomOutlinedTextField(
                    modifier = Modifier,
                    value = if (cardExpirationDateState != "") cardExpirationDateState else "",
                    label = { Text(text = "MM/YY") },
                    onValueChange = {
                        if (it.length <= 4) {
                            viewModel.onEvent(AddEditCardEvent.EnteredExpirationDate(it))
                        }
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = ExpirationDateMask()
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Encrypt card", fontSize = 18.sp)
                Switch(
                    checked = isLockedState,
                    thumbContent = {
                        if (isLockedState) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "",
                                modifier = Modifier.padding(3.dp)
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "",
                                modifier = Modifier.padding(3.dp)
                            )
                        }
                    },
                    onCheckedChange = {
                        viewModel.onEvent(AddEditCardEvent.ChangeIsLocked(!isLockedState))
                    }, modifier = Modifier.scale(0.9f)
                )
            }

            Spacer(modifier = Modifier.padding(10.dp))
            Text(
                text = "Card style",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.padding(5.dp))
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                mainAxisAlignment = FlowMainAxisAlignment.SpaceBetween,
                mainAxisSpacing = 10.dp,
                crossAxisAlignment = FlowCrossAxisAlignment.Center,
                crossAxisSpacing = 10.dp
            ) {
                at.fhooe.mc.ada.features.feature_card.domain.model.Card.cardStyles.forEach { color ->
                    val colorInt = color.toArgb()
                    CardStyleCard(modifier = Modifier.size(80.dp, 50.dp),
                        color = color,
                        checked = viewModel.cardStyle.value == colorInt,
                        onCardClick = {
                            viewModel.onEvent(AddEditCardEvent.ChangeCardStyle(colorInt))
                        })
                }
            }
        }
    }
}
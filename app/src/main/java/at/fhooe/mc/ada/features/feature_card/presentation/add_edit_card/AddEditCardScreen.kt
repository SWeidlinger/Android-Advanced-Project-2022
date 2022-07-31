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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import at.fhooe.mc.ada.features.feature_card.domain.util.CardNumberMask
import at.fhooe.mc.ada.features.feature_card.domain.util.ExpirationDateMask
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.components.CardStyleCard
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.components.CustomOutlinedTextField
import kotlinx.coroutines.flow.collectLatest
import at.fhooe.mc.ada.R
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.components.CardStyleRows

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
                if (viewModel.currentCardId == null) Text(text = stringResource(id = R.string.add_card)) else Text(
                    text = stringResource(id = R.string.edit_card)
                )
            }, navigationIcon = {
                IconButton(onClick = { navHostController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.back)
                    )
                }
            }, actions = {
                Button(
                    modifier = Modifier
                        .padding(end = 10.dp)
                        .scale(0.85f),
                    onClick = { viewModel.onEvent(AddEditCardEvent.SaveCard) },
                    content = {
                        var text = ""
                        text =
                            if (viewModel.currentCardId == null) stringResource(id = R.string.add) else stringResource(
                                id = R.string.edit
                            )
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
                label = { Text(text = stringResource(id = R.string.card_name)) },
                onValueChange = { viewModel.onEvent(AddEditCardEvent.EnteredCardName(it)) },
            )
            Spacer(modifier = Modifier.padding(10.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = cardHolderNameState,
                label = { Text(text = stringResource(id = R.string.card_holder_name)) },
                onValueChange = { viewModel.onEvent(AddEditCardEvent.EnteredCardHolderName(it)) },
            )
            Spacer(modifier = Modifier.padding(10.dp))

            CustomOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = if (cardNumberState != "") cardNumberState else "",
                label = { Text(text = stringResource(id = R.string.card_number)) },
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
                    label = { Text(text = stringResource(id = R.string.ccv)) },
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
                    label = { Text(text = stringResource(id = R.string.date_mm_yy)) },
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
                Text(text = stringResource(id = R.string.encrypt_card), fontSize = 18.sp)
                Switch(
                    checked = isLockedState,
                    thumbContent = {
                        if (isLockedState) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "",
                                modifier = Modifier.padding(3.dp),
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "",
                                modifier = Modifier.padding(3.dp),
                                tint = MaterialTheme.colorScheme.onBackground
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
                text = stringResource(id = R.string.card_style),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start),
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.padding(5.dp))
            CardStyleRows(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.50f),
                cardStylesList = at.fhooe.mc.ada.features.feature_card.domain.model.Card.cardStyles,
                viewModel = viewModel
            )
        }
    }
}
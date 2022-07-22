package at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AddEditCardScreen(
    navHostController: NavHostController,
    viewModel: AddEditCardViewModel = hiltViewModel()
) {
    val cardNameState = viewModel.cardName.value
    val currentBalanceState = viewModel.cardCurrentBalance.value
    val isLockedState = viewModel.cardIsLocked.value
    val imageState = viewModel.cardImage.value

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

    Scaffold(floatingActionButton = {
        FloatingActionButton(onClick = {
            viewModel.onEvent(AddEditCardEvent.SaveCard)
        }) {
            Icon(imageVector = Icons.Filled.Check, contentDescription = "Save card")
        }
    }, scaffoldState = scaffoldState)
    {
        Column(
            Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            OutlinedTextField(value = cardNameState, onValueChange = {
                viewModel.onEvent(AddEditCardEvent.EnteredCardName(it))
            })
            Spacer(modifier = Modifier.padding(10.dp))

            OutlinedTextField(
                value = currentBalanceState.toString(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                onValueChange = {
                    viewModel.onEvent(AddEditCardEvent.EnteredCurrentBalance(it.toDouble()))
                })

            Spacer(modifier = Modifier.padding(10.dp))

            FilterChip(selected = isLockedState, onClick = {
                viewModel.onEvent(AddEditCardEvent.ChangeIsLocked(!isLockedState))
            }) {
                Text(text = "locked")
            }
        }
    }
}
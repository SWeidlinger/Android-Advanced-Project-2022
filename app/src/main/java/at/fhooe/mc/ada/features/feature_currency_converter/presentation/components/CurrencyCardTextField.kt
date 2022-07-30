package at.fhooe.mc.ada.features.feature_currency_converter.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
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
                            .fillMaxSize()
                            .offset(y = 10.dp), contentAlignment = Alignment.Center
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
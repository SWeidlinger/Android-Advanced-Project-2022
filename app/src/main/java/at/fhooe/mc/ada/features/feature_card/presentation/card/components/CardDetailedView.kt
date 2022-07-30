package at.fhooe.mc.ada.features.feature_card.presentation.card.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.R

@Composable
fun CardDetailedView(
    modifier: Modifier = Modifier,
    card: Card,
    onCloseClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
        ) {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterStart) {
                IconButton(onClick = onCloseClick, modifier = Modifier) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close view")
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onEditClick) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(id = R.string.edit_card))
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = stringResource(id = R.string.delete_card))
                }
            }
        }
        CardDetailedViewItem(stringResource(id = R.string.card_name), card.cardName, "")
        Spacer(modifier = Modifier.padding(5.dp))

        CardDetailedViewItem(stringResource(id = R.string.card_holder_name), card.cardHolderName, "")
        Spacer(modifier = Modifier.padding(5.dp))

        var cardNumberFormatted = card.cardNumber
        if (card.cardNumber.length == 16) {
            cardNumberFormatted = String.format(
                "%s %s %s %s",
                card.cardNumber.subSequence(0, 4),
                card.cardNumber.subSequence(4, 8),
                card.cardNumber.subSequence(8, 12),
                card.cardNumber.subSequence(12, 16)
            )
        }
        CardDetailedViewItem(stringResource(id = R.string.card_number), cardNumberFormatted, "")
        Spacer(modifier = Modifier.padding(5.dp))

        CardDetailedViewItem(stringResource(id = R.string.ccv), card.securityNumber, "")
        Spacer(modifier = Modifier.padding(5.dp))

        var cardExpirationDateFormatted = card.expirationDate
        if (card.expirationDate.length == 4) {
            cardExpirationDateFormatted =
                String.format(
                    "%s/%s",
                    card.expirationDate.subSequence(0, 2),
                    card.expirationDate.subSequence(2, 4)
                )
        }
        CardDetailedViewItem(stringResource(id = R.string.expiration_date), cardExpirationDateFormatted, "")
        Spacer(modifier = Modifier.padding(5.dp))
    }
}

@Composable
fun CardDetailedViewItem(descriptionText: String, value: String, defaultValue: String) {
    val clipboardManager: ClipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(start = 5.dp)
        .clickable {
            if (value != defaultValue) {
                clipboardManager.setText(AnnotatedString(value))
                Toast
                    .makeText(
                        context,
                        "$descriptionText copied to clipboard!",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }) {
        Text(text = descriptionText, fontSize = 12.sp)
        Text(
            text = if (value == defaultValue) "-" else value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
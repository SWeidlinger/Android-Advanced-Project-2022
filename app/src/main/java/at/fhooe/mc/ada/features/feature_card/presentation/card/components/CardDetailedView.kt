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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import at.fhooe.mc.ada.features.feature_card.domain.model.Card

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
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit card")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete card")
                }
            }
        }
        CardDetailedViewItem("Card name", card.cardName, "")
        Spacer(modifier = Modifier.padding(10.dp))

        CardDetailedViewItem("Card holder name", card.cardHolderName, "")
        Spacer(modifier = Modifier.padding(10.dp))

        CardDetailedViewItem("Card number", card.cardNumber.toString(), "-100")
        Spacer(modifier = Modifier.padding(10.dp))

        CardDetailedViewItem("CCV", card.securityNumber.toString(), "-100")
        Spacer(modifier = Modifier.padding(10.dp))

        CardDetailedViewItem("Expiration date", card.expirationDate.toString(), "-100")
        Spacer(modifier = Modifier.padding(10.dp))
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
        Text(text = descriptionText)
        Text(
            text = if (value == defaultValue) "-" else value,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
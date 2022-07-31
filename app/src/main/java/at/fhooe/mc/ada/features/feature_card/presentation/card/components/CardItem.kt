package at.fhooe.mc.ada.features.feature_card.presentation.card.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.graphics.ColorUtils
import at.fhooe.mc.ada.R
import at.fhooe.mc.ada.features.feature_card.domain.model.Card

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CardItem(card: Card, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(Color.Transparent),
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        onClick = onClick
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(
                                ColorUtils.blendARGB(
                                    card.cardStyle,
                                    Color.White.toArgb(),
                                    0.4f
                                )
                            ),
                            Color(card.cardStyle)
                        ),
                        start = Offset(Float.POSITIVE_INFINITY, 0f),
                        end = Offset(0f, Float.POSITIVE_INFINITY)
                    )
                )
                .padding(vertical = 10.dp, horizontal = 15.dp)
        ) {
            Text(
                text = card.cardName,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(5.dp))
            if (card.isLocked) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked",
                        modifier = Modifier.fillMaxSize(0.5f)
                    )
                }
            } else {
                Column(
                    Modifier
                        .fillMaxHeight(0.8f)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier.scale(1.1f),
                        painter = painterResource(getRandomChipImage(card)),
                        contentDescription = "",
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.padding(3.dp))

                    var cardNumberFormatted = card.cardNumber
                    if (card.cardNumber.length == 16) {
                        cardNumberFormatted = String.format(
                            "%s  %s  %s  %s",
                            card.cardNumber.subSequence(0, 4),
                            card.cardNumber.subSequence(4, 8),
                            card.cardNumber.subSequence(8, 12),
                            card.cardNumber.subSequence(12, 16)
                        )
                    }

                    Text(
                        text = cardNumberFormatted,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxSize().padding(bottom = 10.dp),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = card.cardHolderName,
                        fontSize = 13.sp,
                    )

                    var cardExpirationDateFormatted = card.expirationDate
                    if (card.expirationDate.length == 4) {
                        cardExpirationDateFormatted =
                            String.format(
                                "%s/%s",
                                card.expirationDate.subSequence(0, 2),
                                card.expirationDate.subSequence(2, 4)
                            )
                    }
                    Text(
                        text = cardExpirationDateFormatted,
                        fontSize = 13.sp,
                    )
                }
            }
        }
    }
}

fun getRandomChipImage(card: Card): Int {
    when (card.cardChipColor) {
        0 -> return R.drawable.credit_card_chip_gold1
        1 -> return R.drawable.credit_card_chip_gold2
        2 -> return R.drawable.credit_card_chip_gold3
        3 -> return R.drawable.credit_card_chip_gold4
        4 -> return R.drawable.credit_card_chip_gold5
        5 -> return R.drawable.credit_card_chip_gold6
    }
    return R.drawable.credit_card_chip_gold1
}
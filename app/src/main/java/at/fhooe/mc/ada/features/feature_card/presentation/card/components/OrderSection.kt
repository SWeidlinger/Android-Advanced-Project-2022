package at.fhooe.mc.ada.features.feature_card.presentation.card.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import at.fhooe.mc.ada.features.feature_card.domain.util.CardOrder
import at.fhooe.mc.ada.core.domain.util.OrderType
import at.fhooe.mc.ada.R

@Composable
fun OrderSection(
    modifier: Modifier,
    cardOrder: CardOrder = CardOrder.DateAdded(OrderType.Ascending),
    onOrderChange: (CardOrder) -> Unit
) {
    Column(modifier = modifier) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            DefaultFilterChip(
                text = stringResource(id = R.string.card_name),
                selected = cardOrder is CardOrder.CardName,
                onSelect = { onOrderChange(CardOrder.CardName(cardOrder.orderType)) }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            DefaultFilterChip(
                text = stringResource(id = R.string.card_holder_name),
                selected = cardOrder is CardOrder.CardHolderName,
                onSelect = { onOrderChange(CardOrder.CardHolderName(cardOrder.orderType)) }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            DefaultFilterChip(
                text = stringResource(id = R.string.date_added),
                selected = cardOrder is CardOrder.DateAdded,
                onSelect = { onOrderChange(CardOrder.DateAdded(cardOrder.orderType)) }
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            DefaultFilterChip(
                text = stringResource(id = R.string.ascending),
                selected = cardOrder.orderType is OrderType.Ascending,
                onSelect = { onOrderChange(cardOrder.copy(OrderType.Ascending)) }
            )
            Spacer(modifier = Modifier.padding(5.dp))
            DefaultFilterChip(
                text = stringResource(id = R.string.descending),
                selected = cardOrder.orderType is OrderType.Descending,
                onSelect = { onOrderChange(cardOrder.copy(OrderType.Descending)) }
            )
        }
    }
}
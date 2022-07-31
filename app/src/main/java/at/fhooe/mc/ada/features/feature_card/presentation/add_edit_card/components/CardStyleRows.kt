package at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.AddEditCardEvent
import at.fhooe.mc.ada.features.feature_card.presentation.add_edit_card.AddEditCardViewModel

@Composable
fun CardStyleRows(
    modifier: Modifier = Modifier,
    cardStylesList: List<Color>,
    viewModel: AddEditCardViewModel
) {
    val color1 = cardStylesList[0]
    val colorInt1 = color1.toArgb()

    val color2 = cardStylesList[1]
    val colorInt2 = color2.toArgb()

    val color3 = cardStylesList[2]
    val colorInt3 = color3.toArgb()

    val color4 = cardStylesList[3]
    val colorInt4 = color4.toArgb()

    val color5 = cardStylesList[4]
    val colorInt5 = color5.toArgb()

    val color6 = cardStylesList[5]
    val colorInt6 = color6.toArgb()

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier
                .padding(bottom = 10.dp)
                .fillMaxWidth()
                .fillMaxHeight(0.5f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CardStyleCard(modifier = Modifier
                .fillMaxWidth(0.3f)
                .padding(end = 10.dp),
                color = color1,
                checked = viewModel.cardStyle.value == colorInt1,
                onCardClick = {
                    viewModel.onEvent(AddEditCardEvent.ChangeCardStyle(colorInt1))
                })

            CardStyleCard(modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(horizontal = 10.dp),
                color = color2,
                checked = viewModel.cardStyle.value == colorInt2,
                onCardClick = {
                    viewModel.onEvent(AddEditCardEvent.ChangeCardStyle(colorInt2))
                })

            CardStyleCard(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
                color = color3,
                checked = viewModel.cardStyle.value == colorInt3,
                onCardClick = {
                    viewModel.onEvent(AddEditCardEvent.ChangeCardStyle(colorInt3))
                })
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            CardStyleCard(modifier = Modifier
                .fillMaxWidth(0.3f)
                .padding(end = 10.dp),
                color = color4,
                checked = viewModel.cardStyle.value == colorInt4,
                onCardClick = {
                    viewModel.onEvent(AddEditCardEvent.ChangeCardStyle(colorInt4))
                })

            CardStyleCard(modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(horizontal = 10.dp),
                color = color5,
                checked = viewModel.cardStyle.value == colorInt5,
                onCardClick = {
                    viewModel.onEvent(AddEditCardEvent.ChangeCardStyle(colorInt5))
                })

            CardStyleCard(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
                color = color6,
                checked = viewModel.cardStyle.value == colorInt6,
                onCardClick = {
                    viewModel.onEvent(AddEditCardEvent.ChangeCardStyle(colorInt6))
                })
        }
    }
}
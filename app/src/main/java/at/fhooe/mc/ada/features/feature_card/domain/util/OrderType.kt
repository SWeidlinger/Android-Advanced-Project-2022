package at.fhooe.mc.ada.features.feature_card.domain.util

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}

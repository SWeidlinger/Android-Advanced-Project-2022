package at.fhooe.mc.ada.core.domain.util

sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}

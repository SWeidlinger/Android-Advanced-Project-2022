package at.fhooe.mc.ada.core.domain.util

//Order Types
sealed class OrderType{
    object Ascending: OrderType()
    object Descending: OrderType()
}

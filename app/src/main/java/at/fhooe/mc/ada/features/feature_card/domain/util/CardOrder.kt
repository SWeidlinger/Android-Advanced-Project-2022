package at.fhooe.mc.ada.features.feature_card.domain.util

sealed class CardOrder(val orderType: OrderType) {
    class CardName(orderType: OrderType) : CardOrder(orderType)
    class CurrentBalance(orderType: OrderType) : CardOrder(orderType)
    class DateAdded(orderType: OrderType) : CardOrder(orderType)

    fun copy(orderType: OrderType): CardOrder {
        return when (this) {
            is CardName -> CardName(orderType)
            is DateAdded -> DateAdded(orderType)
            is CurrentBalance -> CurrentBalance(orderType)
        }
    }
}

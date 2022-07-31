package at.fhooe.mc.ada.features.feature_card.domain.use_case

import at.fhooe.mc.ada.core.domain.util.OrderType
import at.fhooe.mc.ada.features.feature_card.data.repository.FakeCardRepository
import at.fhooe.mc.ada.features.feature_card.domain.model.Card
import at.fhooe.mc.ada.features.feature_card.domain.util.CardOrder
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetCardsUseCaseTest {
    private lateinit var getCardsUseCase: GetCardsUseCase
    private lateinit var fakeRepository: FakeCardRepository

    @Before
    fun setUp() {
        fakeRepository = FakeCardRepository()
        getCardsUseCase = GetCardsUseCase(fakeRepository)

        val cardsToInsert = mutableListOf<Card>()

        ('a'..'z').forEachIndexed { index, c ->
            cardsToInsert.add(
                Card(
                    cardName = c.toString(),
                    cardHolderName = c.toString(),
                    cardNumber = "",
                    securityNumber = "",
                    expirationDate = "",
                    isLocked = false,
                    cardStyle = 0,
                    dateAdded = index.toLong(),
                    cardChipColor = 0
                )
            )
        }
        cardsToInsert.shuffle()
        cardsToInsert.forEach {
            runBlocking {
                fakeRepository.insertCard(it)
            }
        }
    }

    @Test
    fun `Order cards by cardName ascending, correct order`() = runBlocking {
        val cards = getCardsUseCase(CardOrder.CardName(OrderType.Ascending)).first()

        for (i in 0..cards.size - 2) {
            assertThat(cards[i].cardName).isLessThan(cards[i + 1].cardName)
        }
    }

    @Test
    fun `Order cards by cardName descending, correct order`() = runBlocking {
        val cards = getCardsUseCase(CardOrder.CardName(OrderType.Descending)).first()

        for (i in 0..cards.size - 2) {
            assertThat(cards[i].cardName).isGreaterThan(cards[i + 1].cardName)
        }
    }

    @Test
    fun `Order cards by cardHolderName ascending, correct order`() = runBlocking {
        val cards = getCardsUseCase(CardOrder.CardHolderName(OrderType.Ascending)).first()

        for (i in 0..cards.size - 2) {
            assertThat(cards[i].cardHolderName).isLessThan(cards[i + 1].cardHolderName)
        }
    }

    @Test
    fun `Order cards by cardHolderName descending, correct order`() = runBlocking {
        val cards = getCardsUseCase(CardOrder.CardHolderName(OrderType.Descending)).first()

        for (i in 0..cards.size - 2) {
            assertThat(cards[i].cardHolderName).isGreaterThan(cards[i + 1].cardHolderName)
        }
    }

    @Test
    fun `Order cards by dateAdded ascending, correct order`() = runBlocking {
        val cards = getCardsUseCase(CardOrder.DateAdded(OrderType.Ascending)).first()

        for (i in 0..cards.size - 2) {
            assertThat(cards[i].dateAdded).isLessThan(cards[i + 1].dateAdded)
        }
    }

    @Test
    fun `Order cards by dateAdded descending, correct order`() = runBlocking {
        val cards = getCardsUseCase(CardOrder.DateAdded(OrderType.Descending)).first()

        for (i in 0..cards.size - 2) {
            assertThat(cards[i].dateAdded).isGreaterThan(cards[i + 1].dateAdded)
        }
    }
}
package lion.coding.gamememo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CardViewModel : ViewModel() {

    private val indexOfSingleSelectedCard: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    //CARDS VARIABLES
    private var card1 = MutableLiveData<Card>()
    private var card2 = MutableLiveData<Card>()
    private var card3 = MutableLiveData<Card>()
    private var card4 = MutableLiveData<Card>()
    private var card5 = MutableLiveData<Card>()
    private var card6 = MutableLiveData<Card>()
    private var card7 = MutableLiveData<Card>()
    private var card8 = MutableLiveData<Card>()
    private var card9 = MutableLiveData<Card>()
    private var card10 = MutableLiveData<Card>()
    private var card11 = MutableLiveData<Card>()
    private var card12 = MutableLiveData<Card>()
    private var card13 = MutableLiveData<Card>()
    private var card14 = MutableLiveData<Card>()
    private var card15 = MutableLiveData<Card>()
    private var card16 = MutableLiveData<Card>()

    var cards = mutableListOf(
        card1, card2, card3, card4, card5, card6, card7, card8, card9,
        card10, card11, card12, card13, card14, card15, card16
    )

    var _clicked = 0
    val clicked: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    private var _turnOver = false
    val turnOver: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private var _lastClicked = -1
    val lastClicked: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

    init {
        val cardsFromProvider = CardProvider.getCardsLevelHard()
        cards.forEachIndexed { index, card ->
            val c = Card(cardsFromProvider[index])
            card.value = c
        }

        clicked.postValue(_clicked)
        turnOver.postValue(_turnOver)
        lastClicked.postValue(_lastClicked)

    }

    fun updateModels(position: Int) {
        val card = cards[position]
        if (card.value!!.isFaceUp && !turnOver.value!!) {
            if (clicked.value == 0) {
                lastClicked.value = position
            }
            clicked.value = _clicked++
        } else if (!card.value!!.isFaceUp) {
            clicked.value = _clicked--
        }

        if (clicked.value == 2) {
            turnOver.value = true
            checkForMatch(card.value!!.id, cards[lastClicked.value!!].value!!.id)

            buttons[i].isClickable = false
            buttons[lastClicked].isClickable = false
            turnOver.value = false
            clicked.value = 0
        } else if (clicked.value == 0) {
            turnOver.value = false
        }
        card.value!!.isFaceUp = !card.value!!.isFaceUp
    }

    private fun restoreCards() {
        for (card in cards) {
            if (!card.value!!.isMatched) {
                card.value!!.isFaceUp = false
            }
        }
    }

    private fun checkForMatch(position1: Int, position2: Int) {
        if (cards[position1].value!!.id == cards[position2].value!!.id) {
            cards[position1].value!!.isMatched = true
            cards[position2].value!!.isMatched = true
        }
    }
}
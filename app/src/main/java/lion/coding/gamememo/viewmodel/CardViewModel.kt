package lion.coding.gamememo.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import lion.coding.gamememo.model.Card
import lion.coding.gamememo.model.CardProvider

class CardViewModel : ViewModel() {

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

    private val indexOfSingleSelectedCard: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }

//    private var _clicked = 0
//    val clicked: MutableLiveData<Int> by lazy {
//        MutableLiveData<Int>()
//    }
//
//    private var _turnOver = false
//    val turnOver: MutableLiveData<Boolean> by lazy {
//        MutableLiveData<Boolean>()
//    }
//    private var _lastClicked = -1
//    private val lastClicked: MutableLiveData<Int> by lazy {
//        MutableLiveData<Int>()
//    }

    //other variables
    private var _cardsMatchedScore: Int = 0
    var cardsMatchedScore = MutableLiveData<Int>()
    var allMatchedCards = MutableLiveData<Boolean>()

    //MOVEMENTS VARIABLES
    private var movesCounter: Int = 0
    var movesLiveData = MutableLiveData<Int>()

    //TIMER VARIABLES
    private var timeRemaining: Long? = null
    private var time: Long? = null
    private var millisInFuture: Long = 40000
    private var countDownInterval: Long = 1000
    private var countDownTimer: CountDownTimer? = null
    private var isPaused: Boolean = false
    private val _secondsLiveData = MutableLiveData<Int>()
    val secondsLiveData: LiveData<Int> get() = _secondsLiveData
    private val _finished = MutableLiveData<Boolean>()
    val finished: LiveData<Boolean> get() = _finished

    init {
        val cardsFromProvider = CardProvider.getCardsLevelHard()
        cards.forEachIndexed { index, card ->
            val c = Card(cardsFromProvider[index])
            card.value = c
        }

//        clicked.postValue(_clicked)
//        turnOver.postValue(_turnOver)
//        lastClicked.postValue(_lastClicked)
        movesLiveData.postValue(movesCounter)
        cardsMatchedScore.postValue(_cardsMatchedScore)
        allMatchedCards.postValue(false)
    }

    fun updateModels(position: Int) {
        val card = cards[position]
        // Error checking:
        if (card.value!!.isFaceUp) {
            return
        }

        val cardsMatched = cards.filter { it.value!!.isMatched }
        cardsMatchedScore.postValue(cardsMatched.size)
        if (cardsMatched.size == 16) {
            allMatchedCards.postValue(true)
        }
        // Three cases
        // 0 cards previously flipped over => restore cards + flip over the selected card
        // 1 card previously flipped over => flip over the selected card + check if the images match
        // 2 cards previously flipped over => restore cards + flip over the selected card
        if (indexOfSingleSelectedCard.value == null) {
            // 0 or 2 selected cards previously
                restoreCards()
            indexOfSingleSelectedCard.postValue(position)
        } else {
            // exactly 1 card was selected previously
            checkForMatch(indexOfSingleSelectedCard.value!!.toInt(), position)
            indexOfSingleSelectedCard.postValue(null)
            movesCounter++
            movesLiveData.postValue(movesCounter)
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
            _cardsMatchedScore++
            cardsMatchedScore.postValue(_cardsMatchedScore)
        }
    }

    fun pauseCounter() {
        isPaused = true
    }

    fun resumeCounter() {
        isPaused = false
        val millisInFuture = timeRemaining!!
        val countDownInterval: Long = 1000
        object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                //Do something in every tick
                if (isPaused) {
                    //If user requested to pause or cancel the count down timer
                    cancel()
                } else {
                    time = millisUntilFinished / 1000
                    _secondsLiveData.value = time!!.toInt()
                    timeRemaining = millisUntilFinished
                }
            }

            override fun onFinish() {
                cardsMatchedScore.postValue(_cardsMatchedScore)
                _finished.value = true
            }
        }.start()

    }

    fun startCounter() {
        countDownTimer = object : CountDownTimer(millisInFuture, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                if (isPaused) {
                    cancel()
                } else {
                    time = millisUntilFinished / 1000
                    _secondsLiveData.value = time!!.toInt()
                    timeRemaining = millisUntilFinished
                }
            }

            override fun onFinish() {
                cardsMatchedScore.postValue(_cardsMatchedScore)
                _finished.value = true
            }
        }.start()
    }
}
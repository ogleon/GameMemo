package lion.coding.gamememo.views

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import lion.coding.gamememo.R
import lion.coding.gamememo.databinding.ActivityGame2Binding
import lion.coding.gamememo.viewmodel.CardViewModel2

class GameActivity2 : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>
    private lateinit var binding: ActivityGame2Binding
    private val cardViewModel2: CardViewModel2 by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGame2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        buttons = listOf(
            binding.imgButton012,
            binding.imgButton022,
            binding.imgButton032,
            binding.imgButton042,
            binding.imgButton052,
            binding.imgButton062,
            binding.imgButton072,
            binding.imgButton082,
            binding.imgButton092,
            binding.imgButton102
        )

        binding.pauseButton.setOnClickListener {
            cardViewModel2.pauseCounter()
            showPauseDialog()
        }

        gameEngine()
    }

    private fun updateViews() {
        checkIfAllMatched()
        cardViewModel2.cards.forEachIndexed { index, card ->
            val button = buttons[index]
            card.observe(this, {
                if (it.isMatched) {
                    button.alpha = 0.1f
                    button.isClickable = false
                    it.isFaceUp = true
                }
                button.setImageResource(if (it.isFaceUp) it.id else R.drawable.ic_default)
            })
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        cardViewModel2.pauseCounter()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        cardViewModel2.resumeCounter()
        updateViews()
    }

    private fun gameEngine() {
        cardViewModel2.startCounter()
        observeTimeLiveData()
        observeGameStatus()
        observeMovesLiveData()
        observeFinished()
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                cardViewModel2.updateModels(index)
                updateViews()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun observeMovesLiveData() {
        val movesLiveData = Observer<Int> { moves ->
            binding.textViewMovesValue.text = moves.toString()
        }
        cardViewModel2.movesLiveData.observe(this, movesLiveData)
    }

    private fun observeGameStatus() {
        val allCardsMatched = Observer<Boolean> { matched ->
            if (matched) {
                sendScores()
            }
        }
        cardViewModel2.allMatchedCards.observe(this, allCardsMatched)
    }

    @SuppressLint("SetTextI18n")
    private fun observeTimeLiveData() {
        val secondsObserve = Observer<Int> { seconds ->
            binding.textViewCounter.text = seconds.toString()
        }
        cardViewModel2.secondsLiveData.observe(this, secondsObserve)
    }

    private fun observeFinished() {
        val finished = Observer<Boolean> { finish ->
            if (finish) {
                sendScores()
            }
        }
        cardViewModel2.finished.observe(this, finished)
    }

    private fun sendScores() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", binding.textViewMovesValue.text.toString())
        intent.putExtra("matches", cardViewModel2.cardsMatchedScore.value.toString())
        intent.putExtra("activity", GameActivity2::class.java.simpleName)
        startActivity(intent)
    }

    private fun showPauseDialog() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Pause Menu")
            setCancelable(false)

            setPositiveButton("Restart") { _, _ ->
                reloadGame()
            }
            setNegativeButton("Return") { _, _ ->
                setCancelable(true)
                cardViewModel2.resumeCounter()
            }
        }.create().show()
    }

    private fun reloadGame() {
        startActivity(Intent(this, GameActivity2::class.java))
    }

    private fun checkIfAllMatched(){
        val cardsMatched = cardViewModel2.cards.filter { it.value!!.isMatched }
        cardViewModel2.cardsMatchedScore.value = cardsMatched.size
        if (cardsMatched.size == 10) {
            cardViewModel2.allMatchedCards.value = true
        }
    }
}

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
import lion.coding.gamememo.databinding.ActivityGameBinding
import lion.coding.gamememo.viewmodel.CardViewModel

class GameActivity : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>
    private lateinit var binding: ActivityGameBinding
    private val cardViewModel: CardViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        buttons = listOf(
            binding.imgButton01,
            binding.imgButton02,
            binding.imgButton03,
            binding.imgButton04,
            binding.imgButton05,
            binding.imgButton06,
            binding.imgButton07,
            binding.imgButton08,
            binding.imgButton09,
            binding.imgButton10,
            binding.imgButton11,
            binding.imgButton12,
            binding.imgButton13,
            binding.imgButton14,
            binding.imgButton15,
            binding.imgButton16
        )

        binding.pauseButton.setOnClickListener {
            cardViewModel.pauseCounter()
            showPauseDialog()
        }

        gameEngine()
    }

    //GAME FUNCTIONS

    private fun updateViews() {
        checkIfAllMatched()
        cardViewModel.cards.forEachIndexed { index, card ->
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

    private fun checkIfAllMatched() {
        val cardsMatched = cardViewModel.cards.filter { it.value!!.isMatched }
        cardViewModel.cardsMatchedScore.value = cardsMatched.size
        if (cardsMatched.size == 16) {
            cardViewModel.allMatchedCards.value = true
        }
    }

    private fun gameEngine() {
        cardViewModel.startCounter()
        observeTimeLiveData()
        observeMovesLiveData()
        observeGameStatus()
        observeFinishedLiveData()
        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                cardViewModel.updateModels(index)
                updateViews()
            }
        }
    }

    private fun sendScores() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", binding.textViewMovesValue.text.toString())
        intent.putExtra("matches", cardViewModel.cardsMatchedScore.value.toString())
        intent.putExtra("activity", GameActivity::class.java.simpleName)
        startActivity(intent)
    }

    private fun reloadGame() {
        startActivity(Intent(this, GameActivity::class.java))
    }

    private fun showPauseDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialog)
        alertDialog.apply {
            setTitle(getString(R.string.titleAlertDialogPause))
            setCancelable(false)
            setPositiveButton(getString(R.string.option1AlertDialogPause)) { _, _ ->
                reloadGame()
            }
            setNegativeButton(getString(R.string.option2AlertDialogPause)) { _, _ ->
                setCancelable(true)
                cardViewModel.resumeCounter()
            }
        }.create().show()
    }

    //OBSERVERS

    private fun observeGameStatus() {
        val allCardsMatched = Observer<Boolean> { allMatched ->
            if (allMatched) {
                sendScores()
            }
        }
        cardViewModel.allMatchedCards.observe(this, allCardsMatched)
    }

    @SuppressLint("SetTextI18n")
    private fun observeMovesLiveData() {
        val movesLiveData = Observer<Int> { moves ->
            binding.textViewMovesValue.text = moves.toString()
        }
        cardViewModel.movesLiveData.observe(this, movesLiveData)
    }

    @SuppressLint("SetTextI18n")
    private fun observeTimeLiveData() {
        val secondsObserve = Observer<Int> { seconds ->
            binding.textViewCounter.text = seconds.toString()
        }
        cardViewModel.secondsLiveData.observe(this, secondsObserve)
    }

    private fun observeFinishedLiveData() {
        val finished = Observer<Boolean> { finish ->
            if (finish) {
                sendScores()
            }
        }
        cardViewModel.finished.observe(this, finished)
    }

    //HANDLES SCREEN ROTATIONS

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        cardViewModel.pauseCounter()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        cardViewModel.resumeCounter()
        updateViews()
    }
}

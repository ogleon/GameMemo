package lion.coding.gamememo

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import lion.coding.gamememo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var buttons: List<ImageButton>
    private lateinit var binding: ActivityMainBinding
    private val cardViewModel: CardViewModel by viewModels()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
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

        buttons.forEachIndexed { index, button ->
            button.setOnClickListener {
                cardViewModel.updateModels(index)
                updateViews()
            }
        }
    }

    private fun updateViews() {
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        updateViews()
    }
}

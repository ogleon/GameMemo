package lion.coding.gamememo.views

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import lion.coding.gamememo.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {

    private var activity: String? = null
    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scoresGame()

        binding.bPlayAgain.setOnClickListener {
            reloadGame()
        }

        binding.bShareMenu.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, binding.textViewScoreValue.text)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }

        binding.bReturnMenu.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    MenuActivity::class.java
                )
            )
        }

    }

    @SuppressLint("SetTextI18n")
    fun scoresGame() {
        val score = intent.getStringExtra("score")
        val matches = intent.getStringExtra("matches")
        activity = intent.getStringExtra("activity")
        if (activity == "GameActivity2") {
            binding.textViewScoreValue.text =
                "Moves: " + score + "\n" + "Matches: " + (matches!!.toInt() / 2) + "/5"
        } else if (activity == "GameActivity") {
            binding.textViewScoreValue.text =
                "Moves: " + score + "\n" + "Matches: " + (matches!!.toInt() / 2) + "/8"
        }
    }

    private fun reloadGame() {
        if (activity.equals("GameActivity2")) {
            startActivity(Intent(this, GameActivity2::class.java))
        } else if (activity.equals("GameActivity")) {
            startActivity(Intent(this, GameActivity::class.java))
        }
    }

}
package lion.coding.gamememo.views

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import lion.coding.gamememo.R
import lion.coding.gamememo.databinding.ActivityMenuBinding

class MenuActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private var difficultySelected: Int = 0
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ArrayAdapter.createFromResource(
            this,
            R.array.Difficulties,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinner2.adapter = adapter
        }

        binding.spinner2.onItemSelectedListener = this

        binding.buttonPlay.setOnClickListener {
            if (difficultySelected == 0) {
                startActivity(Intent(this, GameActivity2::class.java))
            }
            if (difficultySelected == 1) {
                startActivity(Intent(this, GameActivity::class.java))
            }
        }

        binding.buttonHelp.setOnClickListener {
            showDefaultDialog()
        }
    }

    private fun showDefaultDialog() {
        val alertDialog = AlertDialog.Builder(this, R.style.AlertDialog)
        alertDialog.apply {
            setTitle("Instructions/Rules:")
            setMessage(
                "1. Tap on any of the cards to flip it.\n\n" +
                        "2. Match the image of the card.\n\n" +
                        "3. Only two cards can be flipped at the same time.\n\n" +
                        "4. Pair up as many cards before the time runs out."
            )
        }.create().show()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        difficultySelected = position
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
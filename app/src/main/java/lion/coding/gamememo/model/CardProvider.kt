package lion.coding.gamememo.model

import lion.coding.gamememo.R.drawable.*

class CardProvider {

    companion object {

        private val imagesLevelHard: MutableList<Int> =
            mutableListOf(
                ic_camel,
                ic_chicken,
                ic_coala,
                ic_cow,
                ic_croco,
                ic_eagle,
                ic_elephant,
                ic_frog,

                ic_camel,
                ic_chicken,
                ic_coala,
                ic_cow,
                ic_croco,
                ic_eagle,
                ic_elephant,
                ic_frog
            )

        private val imagesLevelNormal: MutableList<Int> =
            mutableListOf(
                ic_camel,
                ic_chicken,
                ic_coala,
                ic_cow,
                ic_croco,

                ic_camel,
                ic_chicken,
                ic_coala,
                ic_cow,
                ic_croco,
            )

        fun getCardsLevelHard(): List<Int> {
            imagesLevelHard.shuffle()
            return imagesLevelHard
        }

        fun getCardsLevelNormal(): List<Int> {
            imagesLevelNormal.shuffle()
            return imagesLevelNormal
        }
    }
}
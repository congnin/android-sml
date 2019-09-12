package vn.kingbee.widget.viewpager.viewpager2.cards

import android.os.Bundle

class Card private constructor(val suit: String, val value: String) {
    val cornerLabel: String
        get() = value + "\n" + suit

    fun toBundle(): Bundle {
        val args = Bundle(1)
        args.putStringArray(ARG_BUNDLE, arrayOf(suit, value))
        return args
    }

    override fun toString(): String {
        return "$value $suit"
    }

    companion object {
        internal val ARG_BUNDLE = Card::class.java.name + ":Bundle"

        val SUITS = setOf("♣" /* clubs*/, "♦" /* diamonds*/, "♥" /* hearts*/, "♠" /*spades*/)
        val VALUES = setOf("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A")
        val DECK = SUITS.flatMap { suit ->
            VALUES.map { value -> Card(suit, value) }
        }

        fun fromBundle(bundle: Bundle): Card {
            val spec = bundle.getStringArray(ARG_BUNDLE)
            return Card(spec!![0], spec[1])
        }
    }
}
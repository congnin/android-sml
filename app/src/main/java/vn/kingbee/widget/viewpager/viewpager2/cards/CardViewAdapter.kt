package vn.kingbee.widget.viewpager.viewpager2.cards

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CardViewAdapter : RecyclerView.Adapter<CardViewAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        return CardViewHolder(CardView(LayoutInflater.from(parent.context), parent))
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(Card.DECK[position])
    }

    override fun getItemCount(): Int {
        return Card.DECK.size
    }

    class CardViewHolder internal constructor(private val cardView: CardView) :
        RecyclerView.ViewHolder(cardView.view) {
        internal fun bind(card: Card) {
            cardView.bind(card)
        }
    }
}
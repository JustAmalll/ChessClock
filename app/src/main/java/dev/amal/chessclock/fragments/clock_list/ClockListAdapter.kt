package dev.amal.chessclock.fragments.clock_list

import android.annotation.SuppressLint
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import dev.amal.chessclock.R
import dev.amal.chessclock.database.ChessClock
import dev.amal.chessclock.utils.ChessUtils
import dev.amal.chessclock.utils.ChessUtils.Companion.BLITZ
import dev.amal.chessclock.utils.ChessUtils.Companion.BULLET
import dev.amal.chessclock.utils.ChessUtils.Companion.RAPID

class ClockListAdapter(
    var currentClockId: Long,
    private val clockItemListener: ClockItemListener
) : RecyclerView.Adapter<ClockListAdapter.ViewHolder>() {

    var data = listOf<ChessClock>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.from(parent, clockItemListener)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position], currentClockId)
    }

    override fun getItemCount(): Int = data.size

    class ViewHolder(
        itemView: View, private val clockItemListener: ClockItemListener
    ) : RecyclerView.ViewHolder(itemView), View.OnCreateContextMenuListener {

        private val cardView: MaterialCardView = itemView.findViewById(R.id.card_view)
        private val clockThumbnail: ImageView = itemView.findViewById(R.id.clock_item_thumbnail)
        private val gameType: TextView = itemView.findViewById(R.id.clock_item_game_type)
        private val gameTimes: TextView = itemView.findViewById(R.id.clock_item_game_times)
        private lateinit var chessClock: ChessClock

        companion object {
            fun from(parent: ViewGroup, clockItemListener: ClockItemListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.chess_clock_item_list, parent, false)
                return ViewHolder(view, clockItemListener)
            }
        }

        init {
            itemView.setOnClickListener {
                clockItemListener.onClickItem(chessClock.id)
            }
            itemView.setOnCreateContextMenuListener(this)
        }

        fun bind(clock: ChessClock, currentClockId: Long) {
            this.chessClock = clock

            gameTimes.text = ChessUtils.getTimesText(
                itemView.resources,
                chessClock.firstPlayerTime,
                chessClock.secondPlayerTime,
                chessClock.increment
            )

            when (clock.gameType) {
                BULLET -> {
                    clockThumbnail.setImageResource(R.drawable.ic_bullet_game)
                    gameType.text = itemView.resources.getString(R.string.bullet_type)
                }
                BLITZ -> {
                    clockThumbnail.setImageResource(R.drawable.ic_blitz_game)
                    gameType.text = itemView.resources.getString(R.string.blitz_type)
                }
                RAPID -> {
                    clockThumbnail.setImageResource(R.drawable.ic_rapid_game)
                    gameType.text = itemView.resources.getString(R.string.rapid_type)
                }
                else -> {
                    clockThumbnail.setImageResource(R.drawable.ic_classic_game)
                    gameType.text = itemView.resources.getString(R.string.classic_type)
                }
            }

            if (currentClockId == clock.id) {
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.grey_800)
                )
                gameType.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_50))
                gameTimes.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_50))
            } else {
                cardView.setCardBackgroundColor(
                    ContextCompat.getColor(itemView.context, R.color.grey_100)
                )
                gameType.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_800))
                gameTimes.setTextColor(ContextCompat.getColor(itemView.context, R.color.grey_800))
            }
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            val editItem = menu?.add(R.string.edit_menu_item)
            val deleteItem = menu?.add(R.string.delete_menu_item)

            editItem?.setOnMenuItemClickListener {
                clockItemListener.onEditItem(chessClock.id)
                true
            }

            deleteItem?.setOnMenuItemClickListener {
                clockItemListener.onRemoveItem(chessClock.id)
                true
            }
        }
    }
}

interface ClockItemListener {
    fun onClickItem(clockId: Long)
    fun onEditItem(clockId: Long)
    fun onRemoveItem(clockId: Long)
}
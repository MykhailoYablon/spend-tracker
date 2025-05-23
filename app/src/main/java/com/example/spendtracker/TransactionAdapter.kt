package com.example.spendtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.spendtracker.model.Transaction
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionAdapter(private var items: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {

    class TransactionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val amount: TextView = itemView.findViewById(R.id.textAmount)
        val category: TextView = itemView.findViewById(R.id.textCategory)
        val date: TextView = itemView.findViewById(R.id.textDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return TransactionViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = items[position]
        holder.amount.text = "₹%.2f".format(item.amount)
        holder.category.text = item.category
        holder.date.text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(item.date))
    }

    override fun getItemCount(): Int = items.size

    fun updateList(newItems: List<Transaction>) {
        items = newItems
        notifyDataSetChanged()
    }
}

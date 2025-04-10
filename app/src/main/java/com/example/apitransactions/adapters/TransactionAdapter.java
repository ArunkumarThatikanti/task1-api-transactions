package com.example.apitransactions.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.apitransactions.R;
import com.example.apitransactions.model.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>{
    private List<Transaction> transactionList;

    public TransactionAdapter(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction txn = transactionList.get(position);
        System.out.println("textTitle   "+txn.getTitle()+"  "+txn.getAmount()+"  "+txn.getCategory()+"  "+txn.getDate()+"  "+txn.getId());
        holder.titleText.setText(txn.getTitle());
        holder.dateText.setText(txn.getDate());
        holder.amountText.setText("₹ " + txn.getAmount());
        holder.tvCategory.setText(txn.getCategory());
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView titleText, dateText, amountText, tvCategory;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.tvDescription);
            dateText = itemView.findViewById(R.id.tvDate);
            amountText = itemView.findViewById(R.id.tvAmount);
            tvCategory = itemView.findViewById(R.id.tvCategory);
        }
    }
}

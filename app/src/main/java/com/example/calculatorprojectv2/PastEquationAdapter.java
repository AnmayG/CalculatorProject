package com.example.calculatorprojectv2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

// Taken from https://guides.codepath.com/android/using-the-recyclerview#using-the-recyclerview
public class PastEquationAdapter extends RecyclerView.Adapter<PastEquationAdapter.ViewHolder>{
    private final List<PastEquationContent> mContent;

    public PastEquationAdapter(List<PastEquationContent> content) {
        this.mContent = content;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.past_equation_layout, parent, false);

        // Return a new holder instance
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        PastEquationContent contact = mContent.get(position);

        // Set item views based on your views and data model
        TextView equationView = holder.equationView;
        equationView.setText(contact.getEquation());
        TextView resultView = holder.resultView;
        resultView.setText(contact.getResult());
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView equationView;
        public final TextView resultView;

        public ViewHolder(View itemView) {
            super(itemView);
            equationView = itemView.findViewById(R.id.pastDisplay);
            resultView = itemView.findViewById(R.id.resultDisplay);
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + equationView.getText() + "'";
        }
    }
}

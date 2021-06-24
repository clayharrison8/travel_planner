package com.example.planner.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;
import com.example.planner.ui.mytrips.MyTripsResultsAdapter;
import com.example.planner.ui.planner.PlannerAdapter;

import java.util.ArrayList;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.MyViewHolder> {
    private ArrayList<Integer> days;

    public DaysAdapter(ArrayList<Integer> days1) {
        days = days1;
    }

    @NonNull
    @Override
    public DaysAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.planner_day_button, viewGroup, false);

        return new DaysAdapter.MyViewHolder(view);
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int i) {
        // Setting up numbers to display to user
        int buttonValue = (days.get(i)) + 1;

        myViewHolder.day.setText(String.valueOf(buttonValue));

        myViewHolder.day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setting position of arraylist displayed
                if (PlannerAdapter.getInstance() != null) {
                    PlannerAdapter.getInstance().setPosition(i);
                }
                if (MyTripsResultsAdapter.getInstance() != null) {
                    MyTripsResultsAdapter.getInstance().setPosition(i);
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return days.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private Button day;

        MyViewHolder(View itemView) {
            super(itemView);

            day = itemView.findViewById(R.id.planner_button);

        }

    }

}

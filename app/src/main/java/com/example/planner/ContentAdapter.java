package com.example.planner;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.algorithms.summariser.SummaryTool;
import com.example.planner.model.StructuredContentSection;

import java.util.ArrayList;

import static android.text.Html.FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH;

public class ContentAdapter extends RecyclerView.Adapter<ContentAdapter.MyViewHolder>{

    private ArrayList<StructuredContentSection> structuredContentSections;
    private View view;
    private SummaryTool summaryTool = new SummaryTool();

    ContentAdapter(ArrayList<StructuredContentSection> structuredContentSection){
        structuredContentSections = structuredContentSection;
    }

    @NonNull
    @Override
    public ContentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row, viewGroup, false);

        return new MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final ContentAdapter.MyViewHolder myViewHolder, final int i) {

            try {
                // Checking if item has empty value and removing it
                if (structuredContentSections.get(i).getBody().equals("") || structuredContentSections.get(i).getTitle().equals("")){
                    myViewHolder.body.setVisibility(View.GONE);
                    myViewHolder.title.setVisibility(View.GONE);
                }
                else {
                    // Putting html into something android can read
                    myViewHolder.title.setText(Html.fromHtml(structuredContentSections.get(i).getTitle()));
                    // Setting body to gone in order for quick loading speed (summary algorithm causes slowdown)
                    myViewHolder.body.setVisibility(View.GONE);
                }

            }

            catch (IndexOutOfBoundsException e){
                e.printStackTrace();
            }

            myViewHolder.title.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expandList(myViewHolder.body, i);
                }
            });

        myViewHolder.dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandList(myViewHolder.body, i);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return structuredContentSections.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title, body;
        ImageView dropdown;

        MyViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
            dropdown = itemView.findViewById(R.id.android_dropdown);

        }
    }


    private void expandList(TextView textView,int i){
        String html;
        String summary;
        // Toggle between gone and visible for body
        if (textView.getVisibility() == View.GONE){
            textView.setVisibility(View.VISIBLE);
            // Putting html into something android can read
            html = Html.fromHtml(structuredContentSections.get(i).getBody(), FROM_HTML_SEPARATOR_LINE_BREAK_PARAGRAPH).toString();
            // running summary algorithm on section
            summary = summaryTool.summarise(html, view.getContext());
            textView.setText(summary);

//                    System.out.println("HTML");
//                    System.out.println(html);
//                    System.out.println("Summary");
//                    System.out.println(summary);

        }
        else {
            textView.setVisibility(View.GONE);

        }
    }




}

package com.example.planner.ui.mytrips;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.MainActivity;
import com.example.planner.R;
import com.example.planner.Reusable;
import com.example.planner.adapter.ImageBindingAdapter;
import com.example.planner.model.POI;
import com.example.planner.ui.attractions.AttractionFragment;

import java.util.List;
import java.util.Random;


public class MyTripsResultsAdapter extends RecyclerView.Adapter<MyTripsResultsAdapter.MyViewHolder> {
    private List<List<POI>> activities;
    private Integer position;
    @SuppressLint("StaticFieldLeak")
    private static MyTripsResultsAdapter instance;
    private View view;
    private Random rand = new Random();
    private Reusable reusable = new Reusable();


    MyTripsResultsAdapter(List<List<POI>> countries1) {
        activities = countries1;
    }

    public void setPosition(int position) {
        this.position = position;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // Getting the root view
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mytrips_item, viewGroup, false);

        instance = this;

        // set the view's size, margins, paddings and layout parameters
        return new MyTripsResultsAdapter.MyViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        if (position == null) {
            position = 0;
        }

        String score = "Score: " + activities.get(position).get(i).getScore();
        myViewHolder.name.setText(activities.get(position).get(i).getName());
        myViewHolder.snippet.setText(activities.get(position).get(i).getSnippet());
        myViewHolder.score.setText(score);

        // Calculating the time to get from Point A to Point B
        if (i < activities.get(position).size() - 1) {
            double lat1 = activities.get(position).get(i).getCoordinates().getLat();
            double lat2 = activities.get(position).get(i + 1).getCoordinates().getLat();
            double lon1 = activities.get(position).get(i).getCoordinates().getLng();
            double lon2 = activities.get(position).get(i + 1).getCoordinates().getLng();

            double distance = reusable.calculateDistance(lat1, lat2, lon1, lon2);
            myViewHolder.distance.setText(reusable.calculateTime(distance));

        }
        else {
            myViewHolder.distance.setVisibility(View.GONE);
        }

        ImageBindingAdapter.bindThumbnail(myViewHolder.imageView, activities.get(position).get(i).getImages().get(rand.nextInt(activities.get(position).get(i).getImages().size())).getSizes().getMedium().getUrl());

        myViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Saving POI and changing page
                MainActivity.savePOI(activities.get(position).get(i));
                FragmentTransaction transaction = reusable.transactions(view);
                transaction.add(R.id.frag_container, new AttractionFragment()).addToBackStack(null).commit();
            }
        });

        myViewHolder.dotsPopUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Inflating popup menu
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.popup_menu, popup.getMenu());

                // Removing irrelevant menu items
                popup.getMenu().findItem(R.id.book).setVisible(false);
                popup.getMenu().findItem(R.id.add_to_favs).setVisible(false);
//                popup.getMenu().findItem(R.id.remove).setVisible(false);
                popup.getMenu().findItem(R.id.remove_from_favs).setVisible(false);
                popup.getMenu().findItem(R.id.remove).setVisible(false);


                // Show popup menu
                popup.show();

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                                // Display activity in google maps
                                MainActivity.showInMap(activities.get(position).get(i).getCoordinates().getLat(), activities.get(position).get(i).getCoordinates().getLng(), activities.get(position).get(i).getName(), view.getContext());

                        return true;
                    }
                });
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (activities != null) {
            return activities.get(0).size();
        } else {
            return 0;
        }
    }

    public void setCityList(List<List<POI>> cities) {
        this.activities = cities;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, snippet, score, distance;
        private ImageView imageView, dotsPopUp;
        private ConstraintLayout cardView, entire_card, image;

        private MyViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cityName);
            snippet = itemView.findViewById(R.id.citySnippet);
            score = itemView.findViewById(R.id.score);
            imageView = itemView.findViewById(R.id.ivPic);
            cardView = itemView.findViewById(R.id.card);
            dotsPopUp = itemView.findViewById(R.id.dotsPopUp);
            distance = itemView.findViewById(R.id.distance);
            entire_card = itemView.findViewById(R.id.entire_Card);
            image = itemView.findViewById(R.id.imageView);


        }

    }

    public static MyTripsResultsAdapter getInstance() {
        return instance;
    }
}

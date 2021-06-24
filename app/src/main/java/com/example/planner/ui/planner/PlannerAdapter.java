package com.example.planner.ui.planner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.CustomClickListener;
import com.example.planner.MainActivity;
import com.example.planner.R;
import com.example.planner.Reusable;
import com.example.planner.databinding.PlannerItemBinding;
import com.example.planner.model.POI;
import com.example.planner.ui.attractions.AttractionFragment;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;


public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.MyViewHolder> implements CustomClickListener {
    private List<List<POI>> activities;
    private Integer position = 0;
    @SuppressLint("StaticFieldLeak")
    private static PlannerAdapter instance;
    private View view;
    private Reusable reusable = new Reusable();
    private List<POI> list = new ArrayList<>();

    public void setPosition(int position) {
        this.position = position;
        list = activities.get(position);
        notifyDataSetChanged();
    }

//    PlannerAdapter(ArrayList<POI> days1) {
//        days = days1;
//    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        // Getting the root view
        PlannerItemBinding plannerItemBinding = DataBindingUtil.inflate(LayoutInflater.from(viewGroup.getContext()), R.layout.planner_item, viewGroup, false);

        view = viewGroup;

        System.out.println("List = " + list);
        instance = this;

        // set the view's size, margins, paddings and layout parameters
        return new MyViewHolder(plannerItemBinding);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        try {
            myViewHolder.bindTo(list.get(i));
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        catch (ConcurrentModificationException e){
            e.printStackTrace();
        }
        myViewHolder.plannerItemBinding.setItemClickListener(this);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (activities != null) {
            return list.size();
        } else {
            return 0;
        }
    }

    void setCityList(List<List<POI>> activities) {
        this.activities = activities;
        list = this.activities.get(position);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private PlannerItemBinding plannerItemBinding;

        MyViewHolder(@NonNull PlannerItemBinding plannerItemBinding) {
            super(plannerItemBinding.getRoot());

            this.plannerItemBinding = plannerItemBinding;

        }

        void bindTo(POI POI) {
            plannerItemBinding.setVariable(com.example.planner.BR.city, POI);
            plannerItemBinding.setVariable(com.example.planner.BR.adapterPosition, getLayoutPosition());
            plannerItemBinding.setVariable(com.example.planner.BR.countryImageMedium, POI.getImages().get(0).getSizes().getMedium());
            plannerItemBinding.executePendingBindings();
        }
    }

    public static PlannerAdapter getInstance() {
        return instance;
    }

    public void cardClicked(POI POI) {
        MainActivity.savePOI(POI);

        FragmentTransaction transaction = reusable.transactions(view);
        transaction.add(R.id.frag_container, new AttractionFragment()).addToBackStack(null).commit();

    }

    public void PopUpClicked(final POI POI, final Integer integer){
        final Double latitude = POI.getCoordinates().getLat();
        final Double longitude = POI.getCoordinates().getLng();
        final String name = POI.getName();

        // Display popup menu
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.popup_menu, popup.getMenu());

        // Removing irrelevant items
        popup.getMenu().findItem(R.id.remove_from_favs).setVisible(false);
        popup.getMenu().findItem(R.id.add_to_favs).setVisible(false);
        popup.getMenu().findItem(R.id.remove).setVisible(false);


        if(POI.getBookingInfo() == null){
            popup.getMenu().findItem(R.id.book).setVisible(false);

        }

        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.book:
                        // Redirecting user to booking website
                        Uri uri = Uri.parse(POI.getBookingInfo().getVendor_object_url());
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        view.getContext().startActivity(intent);
                        break;
                    case R.id.show_in_map:
                        MainActivity.showInMap(latitude,longitude,name,view.getContext());
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

    }


}

package com.example.planner.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;

import com.example.planner.R;
import com.example.planner.Reusable;

import java.util.Objects;


public class UtilityFragment extends ListFragment implements AdapterView.OnItemClickListener {

    private Reusable reusable = new Reusable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_utilities, container, false);
    }

    // Setting up list displayed
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.Utilities, android.R.layout.simple_list_item_1);
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    // Handling item clicked
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
        FragmentTransaction transaction = reusable.transactions(view);
        transaction.add(R.id.frag_container, new CurrencyConverterFragment()).addToBackStack(null).commit();

    }

    // Showing toolbar and setting title when fragment showed
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Utilities");

    }
}
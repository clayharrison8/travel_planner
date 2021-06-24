package com.example.planner.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import com.example.planner.R;

import org.json.JSONObject;

import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Based on code by Github user arpitbatra123. Available at https://github.com/arpitbatra123/CurrencyConverter
public class CurrencyConverterFragment extends Fragment {

    private EditText fromInput;
    private TextView toOutput;
    private Button convertButton;
    private Spinner fromDropdown, toDropdown;
    private String fromCurrency, toCurrency;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_currency, container, false);

        fromInput = view.findViewById(R.id.fromCurrency);
        toOutput = view.findViewById(R.id.toCurrency);
        convertButton = view.findViewById(R.id.convertButton);
        fromDropdown = view.findViewById(R.id.fromDropdown);
        toDropdown = view.findViewById(R.id.toDropdown);

        setUpListeners();

        // Inflate the layout for this fragment
        return view;
    }

    // Method for checking if connected to the internet
    private boolean isNetworkConnected() {
        // TODO: Check if connected to internet
        return true;
    }

    private void setUpListeners(){

        convertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting currencies selected by user
                fromCurrency = fromDropdown.getSelectedItem().toString();
                toCurrency = toDropdown.getSelectedItem().toString();

               // Check if the network is connected
                if (!isNetworkConnected()) {
                    Toast.makeText(getContext(), "Host unreachable, check your internet connection and try again", Toast.LENGTH_SHORT).show();
                }
                // Check if the input and output values are the same
                else if (fromCurrency.equals(toCurrency)) {
                    Toast.makeText(getContext(), "both values are same.", Toast.LENGTH_SHORT).show();
                }
                // Check if the input is empty
                else if (fromInput.getText().toString().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a value to convert.", Toast.LENGTH_SHORT).show();
                }
                //
                else {
                    // Getting the input value
                    JSONFetch obj = new JSONFetch();
                    obj.execute(fromInput.getText().toString());
                }
            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    class JSONFetch extends AsyncTask<String, Void, Void> {
        ProgressDialog dialog = new ProgressDialog(getContext());
        String error = "", apiResponse = "";

        @Override
        protected Void doInBackground(String... params) {

            // Creating a request object
            OkHttpClient client = new OkHttpClient();
            // Requesting specific data
            Request request = new Request.Builder().url("https://frankfurter.app/latest?amount=" + params[0] + "&from=" + fromCurrency + "&to=" + toCurrency).build();

            try {
                // Making the request and putting it in a string
                Response response = client.newCall(request).execute();
                apiResponse = Objects.requireNonNull(response.body()).string();
            }
            // Printing the error
            catch (Exception e) {
                error = e.toString();
            }
            return null;
        }

        // Showing pop up on screen while calculating conversion
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Converting...");
            dialog.show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();
            if (!error.isEmpty()) {
                Toast.makeText(getContext(), "Something went wrong " + error, Toast.LENGTH_SHORT).show();
            }
            try {
                // Setting output field to converted currency
                toOutput.setText(new JSONObject(apiResponse).getJSONObject("rates").getString(toCurrency));
            } catch (Exception e) {
                Toast.makeText(getContext(), "Something went wrong " + e.toString(), Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(v);
        }
    }

    // Removing toolbar when fragment loaded
    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Currency Converter");

    }


}

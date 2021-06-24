package com.example.planner;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.FragmentManager;
import androidx.preference.DialogPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class Setting extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;

    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String[] listItems;
    // List checking if item is added
    private boolean[] checkedItems;
    private ArrayList<Integer> userItems = new ArrayList<>();
    private String clicked;
    // Getting current user and interests node in database

    // Indicate here the XML resource you created above that holds the preferences
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.root_preferences);

        // Setting default values shown in xml
        PreferenceManager.setDefaultValues(Objects.requireNonNull(getActivity()), R.xml.root_preferences, false);
        // Getting current user and setting node from database
        String userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("Settings");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("users").child(userID).child("Settings").child("Interests");
        listItems = getResources().getStringArray(R.array.interest_items);
        checkedItems = new boolean[listItems.length];

        initSummary(getPreferenceScreen());

        updateEmail();

        Preference delete_account = getPreferenceScreen().findPreference("delete_account");
        assert delete_account != null;
        delete_account.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                clicked = "delete";
                enterPassword();
                return true;
            }
        });

        Preference interests = getPreferenceScreen().findPreference("interests");
        assert interests != null;
        interests.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                setInterests();
                return true;
            }
        });
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        try {
            // If user has changed score, update UI and database
            updateBookableSwitch(findPreference(key));
            String value = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getContext())).getString(key, null);
            databaseReference.child(key).setValue(value);
            updateSummary(findPreference(key));
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }


    // Updating UI when page first loads
    private void initSummary(Preference preference) {
        // Checking that score has been clicked
        if (preference instanceof PreferenceGroup) {
            PreferenceGroup preferenceGroup = (PreferenceGroup) preference;
            // Looping through settings list
            for (int i = 0; i < preferenceGroup.getPreferenceCount(); i++) {
                initSummary(preferenceGroup.getPreference(i));
            }
        } else {
            updateSummary(preference);
            updateBookableSwitch(preference);
        }
    }

    // Update UI and database when score is updated
    private void updateSummary(final Preference preference) {
        if (preference instanceof ListPreference) {
            ListPreference listPreference = (ListPreference) preference;
            try {
                preference.setSummary(listPreference.getEntry().toString());
                databaseReference.child(listPreference.getKey()).child("label").setValue(listPreference.getEntry().toString());
                databaseReference.child(listPreference.getKey()).child("value").setValue(listPreference.getValue());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }

    }

    private void updateBookableSwitch(Preference preference) {

        // Checking if switch was clicked
        if (preference instanceof SwitchPreference) {
            // Retrieve data from database and set switch value accordingly
            if (databaseReference.child("Bookable").equals("1")) {
                preference.setDefaultValue("1");
            } else {
                preference.setDefaultValue("0");
            }

            // Update database when switch is clicked
            if (((SwitchPreference) preference).isChecked()) {
                databaseReference.child("Bookable").setValue("1");
            } else {
                databaseReference.child("Bookable").setValue("0");

            }

        }
    }

    // Show toolbar and set title when fragment is shown
    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).show();
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Settings");
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    @Override
    public void onStop() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    private void deleteAccount() {

        new AlertDialog.Builder(Objects.requireNonNull(getContext())).setIcon(android.R.drawable.ic_dialog_alert).setTitle("Delete Account").setMessage("Are you sure you want to delete your account?").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                assert user != null;
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Getting the user data based off email
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            Query applesQuery = mDatabase.child("users").orderByChild("email").equalTo(user.getEmail());

                            // Receiving user data from database
                            applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                                    // Looping through user information and deleting everything
                                    for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                        appleSnapshot.getRef().removeValue();
                                    }
                                }

                                @Override
                                public void onCancelled(@NotNull DatabaseError databaseError) {
                                    Log.e(TAG, "onCancelled", databaseError.toException());
                                }
                            });
                            // Sends user back to the login page
                            Toast.makeText(getContext(), "Account Deleted", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getContext(), Login.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        })
                .setNegativeButton("No", null)
                .show();
    }

    private void enterPassword(){

        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        builder.setTitle("Enter Password");

        // Set up the input
        final EditText input = new EditText(getContext());
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        builder.setView(input);

        // Set up score buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Get auth credentials from the user for re-authentication. The example below shows
                // email and password credentials but there are multiple possible providers,
                // such as GoogleAuthProvider or FacebookAuthProvider.
                assert user != null;
                AuthCredential credential = EmailAuthProvider.getCredential(Objects.requireNonNull(user.getEmail()), input.getText().toString());

                // Prompt the user to re-provide their sign-in credentials
                user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                System.out.println("User re-authenticated.");
                            }

                        });

                if (clicked.equals("email")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
                    builder.setTitle("Enter new email");

                    // Set up the input
                    final EditText input = new EditText(getContext());
                    // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);

                    // Set up score buttons
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            assert user != null;
                            user.updateEmail(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("Updated");
                                    } else {
                                        System.out.println("Not Updated");
                                    }
                                }
                            });

                            System.out.println(input.getText().toString());
                        }
                    });


                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    builder.show();

                }
                else {
                    deleteAccount();

                }


//                assert user != null;
//                user.updateEmail(input.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            System.out.println("Updated");
//                        } else {
//                            System.out.println("Not Updated");
//                        }
//                    }
//                });

                System.out.println(input.getText().toString());
            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void updateEmail(){
        Preference dialogPreference = getPreferenceScreen().findPreference("dialog_preference");
        assert dialogPreference != null;
        dialogPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                clicked = "email";
                enterPassword();

                return true;
            }
        });
    }

    private void setInterests(){
        // get appropriate amount of checkboxes
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle("Interests");
        // When user clicks checkbox
        builder.setMultiChoiceItems(listItems, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                // Adding item from list of interests if checked, removing otherwise
                if(isChecked){
                    userItems.add(which);
                }else{
                    userItems.remove((Integer.valueOf(which)));
                }
            }
        });

        builder.setCancelable(false);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringBuilder item = new StringBuilder();
                for(int i = 0; i < userItems.size(); i++){
                    // Adding all selected items to string
                    item.append(listItems[userItems.get(i)]);
                    if (i != userItems.size() - 1){
                        // Building string for JSON request
                        item.append("|");
                    }

                }
                // Removing interests node (JSON request will not work if string is empty)
                if(item.toString().equals("")){
                    databaseReference2.removeValue();
                }

                // Adding interests string to database
                else {
                    databaseReference2.setValue(item.toString().toLowerCase());
                    MainActivity.saveInterests(item.toString().toLowerCase());
                }

            }
        });

        builder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Removing all items from database and list
        builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                for (int i = 0; i < checkedItems.length; i++){
                    checkedItems[i] = false;
                    userItems.clear();
                    databaseReference2.removeValue();
                }
            }

        });


        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

}



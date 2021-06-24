package com.example.planner;


import android.support.test.InstrumentationRegistry;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginTest {

    @Rule
    public ActivityTestRule<Login> mActivityTestRule = new ActivityTestRule<>(Login.class);

    @Before
    public void setUp() throws Exception{
        Intents.init();
    }

    @After
    public void endUp() throws Exception {
        Intents.release();
    }


    @Test
    public void invalidUser() {
        // Typing user not recognised in database
        onView(withId(R.id.username)).perform(typeText("invaliduser@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Invalid User"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        // Checking if toast message is displayed
        onView(withText("Login error")).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void validUser(){
        // Typing user recognised in database
        onView(withId(R.id.username)).perform(typeText("hellohelloyou@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("Apple123!"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        // Checking if toast message is displayed
        onView(withText("Login Success")).inRoot(withDecorView(not(mActivityTestRule.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));
    }

    @Test
    public void emptyUsername() {
        // Typing user not recognised in database
        onView(withId(R.id.login)).perform(click());
        // Checking if error message is displayed
        onView(withId(R.id.username)).check(matches(hasErrorText("Field cannot be empty")));
    }

    @Test
    public void emptyPassword() {
        // Typing user not recognised in database
        onView(withId(R.id.username)).perform(typeText("newuser@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.login)).perform(click());
        // Checking if error message is displayed
        onView(withId(R.id.password)).check(matches(hasErrorText("Field cannot be empty")));
    }

}

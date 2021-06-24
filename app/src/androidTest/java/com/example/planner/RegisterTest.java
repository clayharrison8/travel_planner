package com.example.planner;


import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@LargeTest
@RunWith(AndroidJUnit4ClassRunner.class)
public class RegisterTest {

    @Rule
    public ActivityTestRule<Register> mActivityTestRule = new ActivityTestRule<>(Register.class);

//    @Testing
//    public void validUser(){
//        // Typing user recognised in database
//        onView(withId(R.id.username)).perform(typeText("newuser@gmail.com"), closeSoftKeyboard());
//        onView(withId(R.id.password)).perform(typeText("Apple123!"), closeSoftKeyboard());
//        onView(withId(R.id.login)).perform(click());
//        // Check if activity changed
////        intended(hasComponent(MainActivity.class.getName()));
//        Intents.init();
//        onView(withId(R.layout.activity_main)).check(matches(isDisplayed()));
//
//
//    }

    @Test
    public void emptyEmail() {
        // Typing user not recognised in database
        onView(withId(R.id.register)).perform(click());
        // Checking if toast message is displayed
        onView(withId(R.id.email)).check(matches(hasErrorText("Field cannot be empty")));
    }

    @Test
    public void emptyPassword() {
        // Typing user not recognised in database
        onView(withId(R.id.email)).perform(typeText("newuser@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.register)).perform(click());
        // Checking if toast message is displayed
        onView(withId(R.id.password)).check(matches(hasErrorText("Field cannot be empty")));
    }

    @Test
    public void emptyMatchingPassword() {
        // Typing user not recognised in database
        onView(withId(R.id.email)).perform(typeText("newuser@gmail.com"), closeSoftKeyboard());
        onView(withId(R.id.password)).perform(typeText("newuser@gmail.com"), closeSoftKeyboard());

        onView(withId(R.id.register)).perform(click());
        // Checking if toast message is displayed
        onView(withId(R.id.confirm_password)).check(matches(hasErrorText("Field cannot be empty")));
    }

}

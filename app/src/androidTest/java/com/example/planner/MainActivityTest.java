package com.example.planner;


import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.filters.LargeTest;
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

import java.util.concurrent.TimeUnit;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {


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


    // User needs to be logged in for tests to work
    @Test
    public void clickingListItem(){
        // Checks if home page displayed
        onView(withId(R.id.home_recycler)).check(matches(isDisplayed()));
        // Checking toolbar title
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Home"))));
        try {
            // Waiting so list will load
            Thread.sleep(1000);
            // Clicking item and checking if nightlife button is displayed
            onView(withId(R.id.home_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.night_life)).check(matches(isDisplayed()));
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }

    @Test
    public void checkingCataloguePage(){
        // Checks if home page displayed
        onView(withId(R.id.home_recycler)).check(matches(isDisplayed()));
        // Checking toolbar title
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Home"))));

        openDrawer();
        // Clicking catalogue and checking if successful
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.catalogue));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Catalogue"))));

    }

//    @Test
//    public void addToFavourites(){
//        // Checks if home page displayed
//        onView(withId(R.id.home_recycler)).check(matches(isDisplayed()));
//        // Checking toolbar title
//        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Home"))));
//
//        openDrawer();
//        // Clicking catalogue and checking if successful
//        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.catalogue));
//        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Catalogue"))));
//
//        try {
//            // Waiting so list will load
//            Thread.sleep(2000);
//            // Clicking item and checking if nightlife button is displayed
//            onView(withId(R.id.catalogue_recycler)).perform(RecyclerViewActions.actionOnItem(hasDescendant(withId(R.id.add_to_favs)), ViewActions.click()
//                            )
//                    );
////            onView(allOf(withId(R.id.catalogue_recycler), isDisplayed())).perform(actionOnItem(withChild(withText("Add to Favour")), click()));
////            onView(withId(R.id.home_recycler)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
////            onView(withId(R.id.night_life)).check(matches(isDisplayed()));
//        }
//        catch (InterruptedException e){
//            e.printStackTrace();
//        }
//
//
//
//    }

    @Test
    public void checkingMyFavouritesPage(){
        openDrawer();
        // Clicking catalogue and checking if successful
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.favourites));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Favourites"))));

    }

    @Test
    public void checkingPlannerPage(){
        openDrawer();
        // Clicking catalogue and checking if successful
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.planner));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Planner"))));

    }

    @Test
    public void checkingSettingsPage(){
        openDrawer();
        // Clicking settings and checking if successful
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.settings));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Settings"))));

    }

    @Test
    public void checkingUtilitiesPage(){
        openDrawer();
        // Clicking utilities and checking if successful
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.utilities));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Utilities"))));

    }

    @Test
    public void checkingProfilePage(){
        openDrawer();
        // Clicking profile and checking if successful
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.profile));
        onView(withId(R.id.toolbar)).check(matches(hasDescendant(withText("Profile"))));

    }

    @Test
    public void logOut(){
        openDrawer();
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.sign_out));
        // Clicking yes in popup
        onView(withText("YES")).inRoot(isDialog()).check(matches(isDisplayed())).perform(click());
        // Check if directed back to login page
        intended(hasComponent(Login.class.getName()));
    }



    @Test
    public void searchBar(){
        // Clicking search icon
        onView(withId(R.id.action_Search)).perform(click());

        // Clicking search icon again so list will expand
        ViewInteraction appCompatImageView = onView(
                allOf(withClassName(is("androidx.appcompat.widget.AppCompatImageView")), withContentDescription("Search"),
                        childAtPosition(allOf(withClassName(is("android.widget.LinearLayout")), childAtPosition(withId(R.id.action_Search), 0)), 1), isDisplayed()));
        appCompatImageView.perform(click());

        try {
            // Waiting so list will load
            Thread.sleep(2000);

            // Typing "united" text
            ViewInteraction searchAutoComplete = onView(
                    allOf(withClassName(is("android.widget.SearchView$SearchAutoComplete")),
                            childAtPosition(allOf(withClassName(is("android.widget.LinearLayout")),
                                            childAtPosition(withClassName(is("android.widget.LinearLayout")),
                                                    1)), 0), isDisplayed()));
            searchAutoComplete.perform(replaceText("united"), closeSoftKeyboard());

            // Clicking on united kingdom list item
            ViewInteraction linearLayout = onView(allOf(withId(R.id.card), childAtPosition(childAtPosition(withId(R.id.favourites_recycler), 1), 0), isDisplayed()));
            linearLayout.perform(click());
            // Checking if on united kingdom page
            onView(withId(R.id.regions)).check(matches(isDisplayed()));

        }
        catch (InterruptedException e){
            e.printStackTrace();
        }


    }



    private void openDrawer(){
        // Clicking navigation button
        onView(withId(R.id.drawer)).check(matches(isDisplayed()));
        onView(withId(R.id.drawer)).perform(DrawerActions.open());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}



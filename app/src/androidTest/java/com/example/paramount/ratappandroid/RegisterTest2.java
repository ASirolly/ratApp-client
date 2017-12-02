package com.example.paramount.ratappandroid;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@RunWith(AndroidJUnit4.class)

/*
  Runs test for the Registration UI and ensures the branch statements for registration are covered
 */
public class RegisterTest2 {

    private static final int DEL_MS = 700;

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    /**
     * Makes sure that the registration UI works properly by having no username,
     * no password, incorrect confirmed password, and no user type button selected
     */
    @Test
    public void registerTest2() {
        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.text_registration), withText("Sign up"),
                        childAtPosition(
                                allOf(withId(R.id.registration_test),
                                        childAtPosition(
                                                withClassName(is
                                                        ("android.support.constraint.ConstraintLayout")),
                                                2)),
                                1),
                        isDisplayed()));
        appCompatTextView.perform(click());
        slowdown();
        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.submitButton), withText("submit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                8),
                        isDisplayed()));
        appCompatButton.perform(click());
        slowdown();
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.usernameInput),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                6),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("u"), closeSoftKeyboard());
        slowdown();
        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.submitButton), withText("submit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                8),
                        isDisplayed()));
        appCompatButton2.perform(click());
        slowdown();
        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.passwordInput),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("p"), closeSoftKeyboard());
        slowdown();
        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.submitButton), withText("submit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                8),
                        isDisplayed()));
        appCompatButton3.perform(click());
        slowdown();
        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.passwordConfirmationInput),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("p"), closeSoftKeyboard());
        slowdown();
        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.submitButton), withText("submit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                8),
                        isDisplayed()));
        appCompatButton4.perform(click());
        slowdown();
        ViewInteraction appCompatRadioButton = onView(
                allOf(withId(R.id.adminRadioButton), withText("admin"),
                        childAtPosition(
                                allOf(withId(R.id.accountTypeRadioGroup),
                                        childAtPosition(
                                                withClassName(is
                                                        ("android.support.constraint.ConstraintLayout")),
                                                9)),
                                0),
                        isDisplayed()));
        appCompatRadioButton.perform(click());
        slowdown();
        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.submitButton), withText("submit"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                8),
                        isDisplayed()));
        appCompatButton5.perform(click());

    }

    private static void slowdown() {
        try {
            Thread.sleep(DEL_MS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

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
                return (parent instanceof ViewGroup) && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

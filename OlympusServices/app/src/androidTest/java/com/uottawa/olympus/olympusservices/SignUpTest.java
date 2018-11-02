package com.uottawa.olympus.olympusservices;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static java.util.regex.Pattern.matches;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SignUpTest {

    @Rule
    public ActivityTestRule<SignUp> mActivityTestRule = new ActivityTestRule<SignUp>(SignUp.class);
    private SignUp mActivity=null;
    private TextView text;

    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }

    @Test
    public void checkSignUp1() throws Exception{
        onView(withId(R.id.UsernameInput)).perform(typeText("John123"), closeSoftKeyboard());
        onView(withId(R.id.PasswordInput)).perform(typeText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.FirstNameInput)).perform(typeText("John"), closeSoftKeyboard());
        onView(withId(R.id.LastNameInput)).perform(typeText("Doe"), closeSoftKeyboard());
        onView(withId(R.id.SignUp)).perform(click());
    }

    @Test
    public void checkSignUp2() throws Exception{
        onView(withId(R.id.RoleInput)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Service Provider"))).perform(click());
        onView(withId(R.id.UsernameInput)).perform(typeText("Service123"), closeSoftKeyboard());
        onView(withId(R.id.PasswordInput)).perform(typeText("1234567890"), closeSoftKeyboard());
        onView(withId(R.id.FirstNameInput)).perform(typeText("Jane"), closeSoftKeyboard());
        onView(withId(R.id.LastNameInput)).perform(typeText("Doe"), closeSoftKeyboard());
        onView(withId(R.id.SignUp)).perform(click());
    }

    @After
    public void tearDown() throws Exception {
        mActivity=null;
    }
}
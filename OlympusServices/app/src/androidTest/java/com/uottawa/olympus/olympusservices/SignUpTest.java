package com.uottawa.olympus.olympusservices;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignUpTest {
    @Rule
    public ActivityTestRule<SignUp> mActivityTestRule = new ActivityTestRule<SignUp>(SignUp.class);
    private SignUp mActivity = null;
    private TextView text;

    @Before
    public void signUp() throws Exception{

    }

    @Test
    @UiThreadTest
    public void checkSignUp() throws Exception{

    }
}

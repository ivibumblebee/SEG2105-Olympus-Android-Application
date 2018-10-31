package com.uottawa.olympus.olympusservices;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class LogInTest {
    @Rule
    public ActivityTestRule<LogIn> mActivityTestRule = new ActivityTestRule<LogIn>(LogIn.class);
    private LogIn mActivity = null;
    private TextView text;

    @Test
    public void signInInvalid() throws Exception{

    }
}

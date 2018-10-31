package com.uottawa.olympus.olympusservices;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ServiceProviderTest {
    @Rule
    public ActivityTestRule<ServiceProvider> mActivityTestRule = new ActivityTestRule<ServiceProvider>(ServiceProvider.class);
    private ServiceProvider mActivity = null;
    private TextView text;

    @Before
    public void addService() throws Exception{

    }

    @Test
    @UiThreadTest
    public void checkServices() throws Exception{

    }
}

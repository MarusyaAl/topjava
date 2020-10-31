package ru.javawebinar.topjava.service;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static org.slf4j.LoggerFactory.getLogger;

public class MyTotalStopWatch extends TestWatcher {

    private static final org.slf4j.Logger log = getLogger(MyTotalStopWatch.class.getName());

    @Override
    protected void succeeded(Description description) {
        log.info(MyStopWatch.allTestInfo.toString());
    }
}

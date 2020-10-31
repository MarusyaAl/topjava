package ru.javawebinar.topjava.service;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

public class MyStopWatch extends Stopwatch {

    static List<String> allTestInfo = new ArrayList<>();

    private static final org.slf4j.Logger log = getLogger(MyStopWatch.class.getName());

    @Override
    protected void finished(long nanos, Description description) {
        String testName = description.getMethodName();
        String testInfo = String.format("\n %s: %d mcs",
                testName, TimeUnit.NANOSECONDS.toMicros(nanos));
        log.info(testInfo);
        allTestInfo.add(testInfo);
    }
}

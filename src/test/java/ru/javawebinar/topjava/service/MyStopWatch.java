package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MyStopWatch extends Stopwatch {

    static List<String> allTestInfo = new ArrayList<>();

    private static Logger log = Logger.getLogger(MyStopWatch.class.getName());

    private static void logInfo(Description description, long nanos) {
        String testName = description.getMethodName();
        String testInfo = String.format("Test \"%s\", duration of run: %d microseconds",
                testName, TimeUnit.NANOSECONDS.toMicros(nanos));
        log.info(testInfo);
        allTestInfo.add(testInfo);
    }

    @Override
    protected void succeeded(long nanos, Description description) {
        logInfo(description, nanos);
    }

    @AfterClass
    public static void totalLogInfo() {
        for (String line : allTestInfo) {
            log.info(line);
        }
    }
}

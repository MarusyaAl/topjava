package ru.javawebinar.topjava.service;

import org.junit.rules.Stopwatch;
import org.junit.runner.Description;

import java.util.logging.Logger;

public class MyTotalStopWatch extends Stopwatch {

    private static Logger log = Logger.getLogger(MyStopWatch.class.getName());

    @Override
    protected void succeeded(long nanos, Description description) {
        for (String line : MyStopWatch.allTestInfo) {
            log.info(line);
        }
    }
}

package ru.javawebinar.topjava.service;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;

import java.text.SimpleDateFormat;

public class TimeRule implements TestRule {
    private static final Logger log = LoggerFactory.getLogger(TimeRule.class);

    private DateFormat sdf = new SimpleDateFormat("mm:ss.S");

    @Override
    public final Statement apply(
            final Statement statement,
            final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                long start = System.currentTimeMillis();
                statement.evaluate();
                long end = System.currentTimeMillis();
                long totalTime = end - start;

                log.info("Execution time is {} miliseconds ", totalTime);
                log.info("Execution time is {}", sdf.format(totalTime));
            }
        };
    }
}

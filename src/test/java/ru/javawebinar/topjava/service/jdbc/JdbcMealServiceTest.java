package ru.javawebinar.topjava.service.jdbc;

import org.junit.Ignore;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.util.ValidationUtil.getRootCause;

@ActiveProfiles(JDBC)
public class JdbcMealServiceTest extends AbstractMealServiceTest {

}
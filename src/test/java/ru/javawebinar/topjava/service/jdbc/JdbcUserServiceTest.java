package ru.javawebinar.topjava.service.jdbc;

import org.junit.Ignore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.repository.JpaUtil;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.Profiles.JDBC;
import static ru.javawebinar.topjava.util.ValidationUtil.getRootCause;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {

}
package ru.javawebinar.topjava.service.jdbc;

import org.junit.Test;
import org.springframework.core.env.Profiles;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static org.junit.Assume.assumeFalse;
import static ru.javawebinar.topjava.Profiles.JDBC;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends AbstractUserServiceTest {
    @Override
    @Test
    public void createWithException() throws Exception {
        assumeFalse("Validation isn't supported for JDBC",
                environment.acceptsProfiles(Profiles.of(JDBC)));
    }
}
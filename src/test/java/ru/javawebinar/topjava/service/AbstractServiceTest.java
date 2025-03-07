package ru.javawebinar.topjava.service;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContextManager;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.ActiveDbProfileResolver;
import ru.javawebinar.topjava.Profiles;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.slf4j.LoggerFactory.getLogger;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles(resolver = ActiveDbProfileResolver.class)
public abstract class AbstractServiceTest {
    private static final Logger log = getLogger("result");

    private static final Map<String, Map<String, StringBuilder>> testResults = new LinkedHashMap<>();

    @Rule
    public final Stopwatch stopwatch = new Stopwatch() {
        @Override
        protected void finished(long nanos, Description description) {
            String testType = getTestType();
            String profile = getCurrentProfile();
            StringBuilder results = testResults
                    .computeIfAbsent(testType, k -> new LinkedHashMap<>())
                    .computeIfAbsent(profile, k -> new StringBuilder());
            String result = String.format("\n%-25s %7d", description.getMethodName(), TimeUnit.NANOSECONDS.toMillis(nanos));
            results.append(result);
            log.info(result + " ms\n");
        }
    };

    @AfterClass
    public static void printResult() {
        testResults.forEach((testType, profileResults) -> {
            log.info("\n=================================");
            log.info("Test Type: " + testType);
            profileResults.forEach((profile, results) -> {
                log.info("\n---------------------------------" +
                        "\nProfile: " + profile +
                        "\nTest                 Duration, ms" +
                        "\n---------------------------------" +
                        results +
                        "\n---------------------------------");
            });
        });
    }

    private String getTestType() {
        String className = getClass().getSimpleName();
        if (className.contains("Meal")) {
            return "MealServiceTest";
        } else if (className.contains("User")) {
            return "UserServiceTest";
        }
        return "UnknownTest";
    }

    private String getCurrentProfile() {
        try {
            TestContextManager testContextManager = new TestContextManager(getClass());
            String[] activeProfiles = testContextManager.getTestContext().getApplicationContext().getEnvironment().getActiveProfiles();
            for (String profile : activeProfiles) {
                if (profile.equals(Profiles.JDBC) || profile.equals(Profiles.JPA) || profile.equals(Profiles.DATAJPA)) {
                    return profile;
                }
            }
        } catch (Exception e) {
            log.error("Failed to get active profiles", e);
        }
        return "default";
    }
}

package ru.javawebinar.topjava.repository.jdbc;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.MEAL1;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class JdbcMealRepositoryImplTest {

    @Autowired
    private MealRepository repository;

    @Test
    public void save() {
        Meal created = getCreated();
        repository.save(created, USER_ID);
        assertMatch(repository.getAll(USER_ID), created, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void delete() {
        repository.delete(ADMIN_MEAL_ID, ADMIN_ID);
        assertMatch(repository.getAll(ADMIN_ID), ADMIN_MEAL2);
    }

    @Test
    public void get() {
        Meal meal = repository.get(MEAL_ID, USER_ID);
        assertMatch(meal, MEAL1);
    }

    @Test
    public void getAll() {
        List<Meal> all = repository.getAll(USER_ID);
        assertMatch(all, MEALS);

    }

    @Test
    public void getBetween() {
        assertMatch(repository.getBetween(
                LocalDateTime.of(2015, Month.MAY, 30, 0, 0),
                LocalDateTime.of(2015, Month.MAY, 30, 23, 59),
                USER_ID), MEAL3, MEAL2, MEAL1);
    }
}
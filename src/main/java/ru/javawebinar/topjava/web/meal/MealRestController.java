package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private static final Logger log = LoggerFactory.getLogger(MealRestController.class);

    private MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public Collection<Meal> getAll() {
        int userId = SecurityUtil.authUserId();
        log.info("getAll for user", userId);
        return service.getAll(userId);
    }

    public Meal get(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("get meal {} for user {}", id, userId);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        int userId = SecurityUtil.authUserId();
        checkNew(meal);
        log.info("create meal {} for user {}", meal, userId);
        return service.create(meal, userId);
    }

    public void delete(int id) {
        int userId = SecurityUtil.authUserId();
        log.info("delete {} for user{}", id, userId);
         service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        int userId = SecurityUtil.authUserId();
        log.info("update {} for user {}", meal, userId);
        assureIdConsistent(meal, id);
        service.update(meal, userId);
    }

    public List<MealWithExceed> getBetweenDates(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetweenDates startDate {} - endDate {}, startTime {} - endTime {}, user {}", startDate, endDate, startTime, endTime, userId);

        Collection<Meal> mealsDateFiltered = service.getBetweenDates(
           startDate != null ? startDate : DateTimeUtil.MIN_DATE,
           endDate != null ? endDate : DateTimeUtil.MAX_DATE, userId);

        return MealsUtil.getFilteredWithExceeded(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(),
            startTime != null ? startTime : LocalTime.MIN,
            endTime != null ? endTime : LocalTime.MAX);
    }



}
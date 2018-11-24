package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.repository.mock.InMemoryUserRepositoryImpl.ADM_ID;
import static ru.javawebinar.topjava.repository.mock.InMemoryUserRepositoryImpl.USR_ID;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    // Еда всех пользователей, map внутри map
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, ADM_ID));
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 29, 9, 0), "Завтрак ", 400), USR_ID);
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 29, 13, 0), "Обед 2", 600), USR_ID);
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 29, 16, 0), "Полдник", 200), USR_ID);
        save(new Meal(LocalDateTime.of(2015, Month.MAY, 29, 19, 0), "Ужин+Ланч", 500), USR_ID);
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> meals = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFilteredAndSorted(userId, p -> true);
    }

    @Override
    public List<Meal> getBetweenDates(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return getFilteredAndSorted(userId, meal -> DateTimeUtil.isBetween(meal.getDateTime(), startDateTime, endDateTime));
    }


    public List<Meal> getFilteredAndSorted(int userId, Predicate<Meal> filter) {
        Map<Integer, Meal> meals = repository.get(userId);
        return CollectionUtils.isEmpty(meals) ? Collections.emptyList() :
                meals.values().stream()
                        .filter(filter)
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                        .collect(Collectors.toList());
    }


}


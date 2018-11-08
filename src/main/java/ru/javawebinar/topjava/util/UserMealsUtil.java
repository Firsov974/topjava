package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 11, 30), "Ланч", 300),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        List<UserMealWithExceed> mealListWithExc = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);

        for (UserMealWithExceed mealWithExc : mealListWithExc) {
            System.out.println(mealWithExc);
        }

    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // Return filtered list with correctly exceeded field

        List<UserMealWithExceed> mealWithExceededList = new ArrayList<>();
        List<UserMeal> mealListTemp = new ArrayList<>();

        /* Сгруппируем по дате */
        Map<LocalDate, Integer> mealsByDate = mealList
           .stream()
           .collect(Collectors.groupingBy(UserMeal::getDate, Collectors.summingInt(UserMeal::getCalories)));

        mealList
           .stream()
           .filter((p)-> p.getDateTime().toLocalTime().isAfter(startTime)
               && p.getDateTime().toLocalTime().isBefore(endTime))
           .forEach(meal -> {
              final boolean exceed;
              if (mealsByDate.get(meal.getDate()) > caloriesPerDay)
                 exceed = true;
              else
                 exceed = false;
              UserMealWithExceed mealWithExceed = new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed);
              mealWithExceededList.add(mealWithExceed);

           });

        return mealWithExceededList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededOld(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> caloriesSumPerDay = new HashMap<>();
        for(UserMeal meal : mealList) {
           LocalDate mealDate = meal.getDateTime().toLocalDate();
           caloriesSumPerDay.put(mealDate, caloriesSumPerDay.getOrDefault(mealDate, 0) + meal.getCalories());
        }

        List<UserMealWithExceed> mealWithExceededList = new ArrayList<>();
        for(UserMeal meal : mealList) {
           LocalDateTime dateTime = meal.getDateTime();
           if(TimeUtil.isBetween(dateTime.toLocalTime(), startTime, endTime)){
              mealWithExceededList.add(new UserMealWithExceed(dateTime, meal.getDescription(), meal.getCalories(),
                 caloriesSumPerDay.get(dateTime.toLocalDate()) > caloriesPerDay));
           }
        }

        return mealWithExceededList;

    }
}

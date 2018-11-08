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
            System.out.println("LocalDateTime: " + mealWithExc.getDateTime() + " description: "+ mealWithExc.getDescription() + " exceed: " + mealWithExc.getExceed());
        }

//        .toLocalDate();
//        .toLocalTime();
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
              if (mealsByDate.get(meal.getDate()).intValue()  > caloriesPerDay)
                 exceed = true;
              else
                 exceed = false;
              UserMealWithExceed mealWithExceed = new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), exceed);
              mealWithExceededList.add(mealWithExceed);

           });

        return mealWithExceededList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededOld(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> mealWithExceededList = new ArrayList<>();
        HashMap<LocalDate, Integer> calloriesPerDay = new HashMap<>();

        LocalDate curDay = null;
        Integer curCallories = 0;

        for (UserMeal meal : mealList) {
            curDay = meal.getDateTime().toLocalDate();
            curCallories = calloriesPerDay.merge(curDay, meal.getCalories(), (v1,v2)-> v1 + v2);
            if (curCallories > caloriesPerDay){
                for (UserMealWithExceed mealWithExceed : mealWithExceededList) {
                    if( mealWithExceed.getDateTime().toLocalDate().equals(curDay) ){
                        mealWithExceed.setExceed(true);
                    }
                }
            }
            if (meal.getDateTime().toLocalTime().isAfter(startTime) &&
                    meal.getDateTime().toLocalTime().isBefore(endTime)) {
                UserMealWithExceed mealWithExceed = new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false);
                mealWithExceededList.add(mealWithExceed);
            }
        }

        return mealWithExceededList;

    }
}

package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );

        for (UserMealWithExceed meal : getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000)) {
            System.out.println(meal.toString());
        }

//        .toLocalDate();
//        .toLocalTime();
    }

    // Complexity O(N+M)
    private static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        List<UserMealWithExceed> output = new ArrayList<>();
        Map<LocalDate, Integer> dailyCalories = new HashMap<>();

        // Calculate daily calories for each day
        for (UserMeal userMeal : mealList) {
            LocalDate date = userMeal.getDateTime().toLocalDate();
            int updatedCalories = userMeal.getCalories();

            if (dailyCalories.containsKey(date)) {
                updatedCalories += dailyCalories.get(date);
            }
            dailyCalories.put(date, updatedCalories);
        }

        // Filter results by dates
        for (UserMeal userMeal : mealList) {
            LocalDate date = userMeal.getDateTime().toLocalDate();
            LocalTime time = userMeal.getDateTime().toLocalTime();

            if (TimeUtil.isBetween(time, startTime, endTime)) {
                Boolean exceed = dailyCalories.get(date) > caloriesPerDay;
                UserMealWithExceed userMealWithExceed = new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), exceed);
                output.add(userMealWithExceed);
//                System.out.println(userMealWithExceed);
            }
        }

        return output;
    }
}

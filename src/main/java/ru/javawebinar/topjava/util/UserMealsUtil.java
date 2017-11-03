package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {

    // required for recursion solution
    private static Map<LocalDate, Integer> mapDay = new HashMap<>();

    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );

        for (UserMealWithExceed meal : getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000)) {
            System.out.println(meal.toString());
        }

        // Stream print out
        getFilteredWithExceededByStream(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);

        // Recursion test
        getFilteredWithExceededRecursion(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000).forEach(System.out::println);
    }

    // Complexity O(N+M)
    private static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        // Calculate daily calories for each day
        Map<LocalDate, Integer> dailyCalories = new HashMap<>();
        for (UserMeal userMeal : mealList) {
            LocalDate date = userMeal.getDateTime().toLocalDate();

            // Map.merge function replaces code below
            dailyCalories.merge(date, userMeal.getCalories(), Integer::sum);

            // Optional with Map.getOrDefault
//            dailyCalories.put(date, dailyCalories.getOrDefault(date, 0) + userMeal.getCalories());
        }

        // Filter results by dates
        List<UserMealWithExceed> output = new ArrayList<>();
        for (UserMeal userMeal : mealList) {
            LocalDate date = userMeal.getDateTime().toLocalDate();
            LocalTime time = userMeal.getDateTime().toLocalTime();

            if (TimeUtil.isBetween(time, startTime, endTime)) {
                output.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(),
                        dailyCalories.get(date) > caloriesPerDay));
            }
        }

        return output;
    }

    private static List<UserMealWithExceed> getFilteredWithExceededByStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> dailyCalories = mealList.stream()
                .collect(Collectors.groupingBy(um -> um.getDateTime().toLocalDate(),
                         Collectors.summingInt(UserMeal::getCalories)));

        return mealList.stream()
                .filter(um -> TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(),
                        dailyCalories.get(um.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    private static List<UserMealWithExceed> getFilteredWithExceededRecursion(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> list = new ArrayList<>();

        // (a, b) -> a + b equivalent to Interger::sum
        mapDay.merge(mealList.get(0).getDateTime().toLocalDate(), mealList.get(0).getCalories(), (a, b) -> a + b);
        if (mealList.size() > 1) {
            list.addAll(getFilteredWithExceededRecursion(mealList.subList(1, mealList.size()), startTime, endTime, caloriesPerDay));
        }

        LocalTime time = mealList.get(0).getDateTime().toLocalTime();
        if (TimeUtil.isBetween(time, startTime, endTime)) {
            list.add(new UserMealWithExceed(
                    mealList.get(0).getDateTime(),
                    mealList.get(0).getDescription(),
                    mealList.get(0).getCalories(),
                    mapDay.get(mealList.get(0).getDateTime().toLocalDate()) <= caloriesPerDay));
        }

        return list;
    }
}

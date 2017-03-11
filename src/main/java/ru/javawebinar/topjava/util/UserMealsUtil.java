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
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Boolean> dailyExceedOrNot = new HashMap<>();
        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<>();
        List<UserMeal> tempList = new ArrayList<>(mealList);
        tempList.sort(new Comparator<UserMeal>() {
            @Override
            public int compare(UserMeal o1, UserMeal o2) {
                return o1.getDateTime().compareTo(o2.getDateTime());
            }
        });
        tempList.add(new UserMeal(LocalDateTime.MAX, "", 0));
        LocalDate currentDate = tempList.get(0).getDateTime().toLocalDate();
        LocalDate localDate;
        LocalTime localTime;
        int totalCaloriesPerDay = 0;
        for (UserMeal userMeal : tempList) {
            if(userMeal.getDateTime()==null)continue;
            localDate = userMeal.getDateTime().toLocalDate();
            if (!localDate.equals(currentDate)) {
                dailyExceedOrNot.put(currentDate, totalCaloriesPerDay > caloriesPerDay);
                totalCaloriesPerDay = userMeal.getCalories();
                currentDate = localDate;
            }
            totalCaloriesPerDay += userMeal.getCalories();
        }

        for (UserMeal userMeal : tempList) {
            if(userMeal.getDateTime()==null)continue;
            localDate = userMeal.getDateTime().toLocalDate();
            localTime = userMeal.getDateTime().toLocalTime();
            if (TimeUtil.isBetween(localTime, startTime, endTime)) {
                userMealWithExceeds.add(new UserMealWithExceed(
                        userMeal.getDateTime(),
                        userMeal.getDescription(),
                        userMeal.getCalories(),
                        dailyExceedOrNot.getOrDefault(localDate, false)));
            }
        }
        return userMealWithExceeds;
    }
}


package ru.javawebinar.topjava.repository.mock;

import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private int userId = AuthorizedUser.id();

    // default meals list will be added to any authorized user
    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        Map<Integer, Meal> mealRepository = getMealRepository(userId);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
        }
        mealRepository.put(meal.getId(), meal);
        return meal;
    }

    @Override
    public void delete(int id) {
        getMealRepository(userId).remove(id);
    }

    @Override
    public Meal get(int id) {
        return getMealRepository(userId).get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return getMealRepository(userId).values().stream()
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private Map<Integer, Meal> getMealRepository(int id) {
        if (repository.containsKey(id)) return repository.get(id);
        else                            return new ConcurrentHashMap<>();
    }
}


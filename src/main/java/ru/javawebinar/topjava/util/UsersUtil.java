package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UsersUtil {
    public static final List<User> USERS = Arrays.asList(
        new User("user1", "user1@email.ad", "u1pass", 2000),
        new User("user2", "user2@email.ad", "u2pass", 1500),
        new User("user3", "user3@email.ad", "u3pass", 1800),
        new User("user4", "user4@email.ad", "u4pass", 2300)
    );

/*
    public static void main(String[] args) {
        Map<Integer, User> userRepository = new ConcurrentHashMap<>();
        int i = 0;
        for (User user : USERS) {
            userRepository.put(i++, user);
        }

        System.out.println(Arrays.toString(userRepository.entrySet()));
    }
*/
}

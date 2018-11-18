package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UserUtil {
    public static final List<User> USERS = Arrays.asList(
            new User(null, "Vasya", "vasya@mail.ru", "pass001", Role.ROLE_ADMIN),
            new User(null, "Petya", "petya@mail.ru", "pass002", Role.ROLE_USER),
            new User(null, "Elena", "elena@mail.ru", "pass003", Role.ROLE_ADMIN),
            new User(null, "Galina", "galina@mail.ru", "pass004", Role.ROLE_USER),
            new User(null, "Fedor", "fedor@mail.ru", "pass005", Role.ROLE_ADMIN),
            new User(null, "Egor", "egor@mail.ru", "pass006", Role.ROLE_USER)
    );

}
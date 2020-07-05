package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.Comparator;

public class UserNameEmailComparator implements Comparator<User> {
    @Override
    public int compare(User u1, User u2) {
        if (u1.getName().equals(u2.getName())) {
            return u1.getEmail().compareTo(u2.getEmail());
        }
        return u1.getName().compareTo(u2.getName());
    }
}

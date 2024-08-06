package com.example.talent_man.models.user;
public class UserRequestContext {
    private static ThreadLocal<String> currentUser = new InheritableThreadLocal<>();
    public static String getCurrentUser() {
        return currentUser.get();
    }
    public static void setCurrentUser(String userName) {
        currentUser.set(userName);
    }
    public static void clear() {
        currentUser.set(null);
    }
}

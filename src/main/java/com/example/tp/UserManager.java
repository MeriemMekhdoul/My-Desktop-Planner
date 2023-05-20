package com.example.tp;

public class UserManager {
        private static User user;

        public static void setUser(User newUser) {
            user = newUser;
        }

        public static User getUser() {
            return user;
        }


}

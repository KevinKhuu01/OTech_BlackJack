package backend;

import database.DatabaseManager;

public class LoginHandler {
    // Create a.java new player / user
    public LoginHandler(){}

    // check login information
    public static boolean checkLogin(String username, String password)
    {
        return DatabaseManager.loginUser(username, password);
    }

    // create new login / player
    public static boolean newUser(String username, String password)
    {
        int random = (int) (Math.random()*1000);
        return DatabaseManager.newUser(random, username, password);
    }
}

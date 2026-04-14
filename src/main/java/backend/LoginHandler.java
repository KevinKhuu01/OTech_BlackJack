package backend;

import database.DatabaseManager;

/** HANDLES LOGIN THROUGH DATABASE CONNECTION --------------------------------------------------------------------------------------------------- **/
public class LoginHandler {
    /** Create a.java new player/user **/
    public LoginHandler(){}

    /** Check login information
     * @param username The username the user has entered
     * @param password The password the user has entered
     **/
    public static boolean checkLogin(String username, String password)
    {
        return DatabaseManager.loginUser(username, password);
    }

    /** Create new login/player
     * @param username The username that the new user has entered
     * @param password The password the the new user has entered
     **/
    public static boolean newUser(String username, String password)
    {
        int random = (int) (Math.random()*1000);
        return DatabaseManager.newUser(random, username, password);
    }
}
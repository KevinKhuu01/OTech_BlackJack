package backend;

import database.DatabaseManager;

/** HANDLES LOGIN THROUGH DATABASE CONNECTION --------------------------------------------------------------------------------------------------- **/
public class LoginHandler {
    /** Create a.java new player/user **/
    public LoginHandler(){}

    /** Check login information
     * @return boolean true if login was successful, false otherwise (password and username match database record)
     * @param username The username the user has entered
     * @param password The password the user has entered
     **/
    public static boolean checkLogin(String username, String password)
    {
        return DatabaseManager.loginUser(username, password);
    }

    /** Create new login/player
     * Calls the database manager to create a new user with a random integer id (primary key)
     * @param username The username that the new user has entered
     * @param password The password that the new user has entered
     **/
    public static boolean newUser(String username, String password)
    {
        int random = (int) (Math.random()*1000);
        return DatabaseManager.newUser(random, username, password);
    }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Infosys
 */
public class LoginDAO {

    /**
     *
     */
    public boolean loginResult;

    /**
     *
     */
    public LoginDAO() {
    }

    ;
        
    /**
     *
     * @param mail
     * @param password
     * @return
     */
    public boolean LoginChecker(String mail, String password) {

        loginResult = false;

        {
            // First open a database connnection
            DatabaseConnection connection = new DatabaseConnection();
            if (connection.openConnection()) {
                //If a connection was successfully setup, execute the SELECT statement.

                String s = String.format("SELECT * FROM `users`");
                ResultSet resultset = connection.executeSQLSelectStatement(s);

                try {
                    while (resultset.next()) {
                        String emailDB = resultset.getString("email");
                        String passDB = resultset.getString("password");

                        if (emailDB.equals(mail) && passDB.equals(password)) {
                            loginResult = true;
                        }

                    }
                } catch (Exception e) {
                }

                connection.closeConnection();
            }
        }
        return loginResult;
    }

}

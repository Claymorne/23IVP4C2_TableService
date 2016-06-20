/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastorage;

import domain.Order;
import java.sql.ResultSet;

/**
 *
 * @author Ray
 */
public class InsertDAO {

    public InsertDAO() {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }

    public static void saveOrder() {

        {
            // First open a database connnection
            DatabaseConnection connection = new DatabaseConnection();
            if (connection.openConnection()) {
                // If a connection was successfully setup, execute the SELECT statement.
                String s = String.format("INSERT INTO `order`(`ID`, `Table`) VALUES (9,9)");
                ResultSet resultset = connection.executeSQLInsertStatement(s);
                System.out.println("Test");

                connection.closeConnection();
            }
        }
    }

}

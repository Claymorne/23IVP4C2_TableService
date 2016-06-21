/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastorage;

import java.sql.ResultSet;

/**
 * @author Infosys
 */
public class TableDAO {


    public TableDAO() {
    }

    ;
        
    /**
     * @param OldTableNumber
     * @param NewTableNumber
     */
    public static void changeTable(int OldTableNumber, int NewTableNumber) {

        {
            // First open a database connnection
            DatabaseConnection connection = new DatabaseConnection();
            if (connection.openConnection()) {
                // If a connection was successfully setup, execute the SELECT statement.  
                String s = String.format("UPDATE `order` SET `TableNumber` = %d WHERE `TableNumber` = %d AND `StatusNumber` <> 6", NewTableNumber, OldTableNumber);
                ResultSet resultset = connection.executeSQLInsertStatement(s);

                connection.closeConnection();
            }
        }
    }

}

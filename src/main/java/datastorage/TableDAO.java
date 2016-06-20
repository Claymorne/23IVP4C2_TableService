/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastorage;

import java.sql.ResultSet;

/**
 *
 * @author Ray
 */
public class TableDAO {

    public TableDAO() {
    }

    ;
        
                 
        
        public static void changeTable(int OldTableNumber, int NewTableNumber) {

        {
            // First open a database connnection
            DatabaseConnection connection = new DatabaseConnection();
            if (connection.openConnection()) {
                // If a connection was successfully setup, execute the SELECT statement.    '%1$d' '%2$d', NewTableNumber, OldTableNumber

                //UPDATE `order` SET `TableNumber` = 10 WHERE `TableNumber` = 6 AND `StatusNumber` <> 6
                String s = String.format("UPDATE `order` SET `TableNumber` = %s WHERE `TableNumber` = %d AND `StatusNumber` <> 6", NewTableNumber, OldTableNumber);
                ResultSet resultset = connection.executeSQLInsertStatement(s);

                connection.closeConnection();
            }
        }
    }

}

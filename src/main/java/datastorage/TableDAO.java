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
        public TableDAO(){};
        
                 
        
        public static void changeTable(int OldTableNumber, int NewTableNumber){
                       
                       
                       
        {
            // First open a database connnection
            DatabaseConnection connection = new DatabaseConnection();
            if(connection.openConnection())
            {
                // If a connection was successfully setup, execute the SELECT statement.    '%1$d' '%2$d', NewTableNumber, OldTableNumber
                String s = String.format( "UPDATE `order` SET `Table`=4 WHERE `Table`=6") ;
                ResultSet resultset = connection.executeSQLInsertStatement(s);       
                System.out.println("Test2");
  
                connection.closeConnection();
            }
            else{System.out.println("kanker");}
           }    
}
        
        
        
    
}

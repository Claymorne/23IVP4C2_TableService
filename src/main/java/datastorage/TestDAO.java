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
 * @author Infosys
 */
public class TestDAO {

    /**
     *
     */
    public TestDAO() {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }

    /**
     *
     */
    public static void testChangeTable() {

        {
            // First open a database connnection
            DatabaseConnection connection = new DatabaseConnection();
            if (connection.openConnection()) {
                // If a connection was successfully setup, execute the SELECT statement.    '%1$d' '%2$d', NewTableNumber, OldTableNumber

                //UPDATE `order` SET `TableNumber` = 10 WHERE `TableNumber` = 6 AND `StatusNumber` <> 6
                String s = String.format("UPDATE `order` SET `TableNumber` = 20 WHERE `TableNumber` = 21 AND `StatusNumber` <> 6");
                ResultSet resultset = connection.executeSQLInsertStatement(s);

                connection.closeConnection();
            }
        }}
        
    /**
     *
     */
    public static void testSetReadyAppetizer() {

        {
            // First open a database connnection
            DatabaseConnection connection = new DatabaseConnection();
            if (connection.openConnection()) {
                // If a connection was successfully setup, execute the SELECT statement.    '%1$d' '%2$d', NewTableNumber, OldTableNumber

                //UPDATE `order` SET `TableNumber` = 10 WHERE `TableNumber` = 6 AND `StatusNumber` <> 6
                String s = String.format("UPDATE `consumptionorder` SET `ConsumptionStatus`= '3' WHERE `OrderNumber` = 20");
                ResultSet resultset = connection.executeSQLInsertStatement(s);

                connection.closeConnection();
            }
        }}

    /**
     *
     */
    public static void testSetReadyMain() {

        {
            // First open a database connnection
            DatabaseConnection connection = new DatabaseConnection();
            if (connection.openConnection()) {
                // If a connection was successfully setup, execute the SELECT statement.    '%1$d' '%2$d', NewTableNumber, OldTableNumber

                //UPDATE `order` SET `TableNumber` = 10 WHERE `TableNumber` = 6 AND `StatusNumber` <> 6
                String s = String.format("UPDATE `consumptionorder` SET `ConsumptionStatus`= '3' WHERE `OrderNumber` = 21");
                ResultSet resultset = connection.executeSQLInsertStatement(s);

                connection.closeConnection();
            }
        }
    }
        
    /**
     *
     */
    public static void testSetInvoice() {

        {
            // First open a database connnection
            DatabaseConnection connection = new DatabaseConnection();
            if (connection.openConnection()) {
                // If a connection was successfully setup, execute the SELECT statement.    '%1$d' '%2$d', NewTableNumber, OldTableNumber

                //UPDATE `order` SET `TableNumber` = 10 WHERE `TableNumber` = 6 AND `StatusNumber` <> 6
                String s = String.format("UPDATE `table` SET `Pay`=1 WHERE `TableNumber` = 20");
                ResultSet resultset = connection.executeSQLInsertStatement(s);

                connection.closeConnection();
            }
        }
    }

}

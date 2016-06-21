/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datastorage;

import domain.Order;
import domain.Order.ConsumptionType;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Infosys
 */
public class OrderDAO {

    public OrderDAO() {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }

    /**
     * Loads orders and puts them in an array.
     */
    public ArrayList<Order> loadOrders() {

        drinkHelper();
        
        ArrayList<Order> orderLijst = new ArrayList<>();

        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT statement.

            ResultSet resultset = connection.executeSQLSelectStatement(
                    "SELECT * FROM `order`,`consumption`,`consumptionorder`,`table`\n"
                    + "WHERE `order`.`OrderNumber` = `consumptionorder`.`OrderNumber` AND `consumptionorder`.`ConsumptionNumber` = `consumption`.`ConsumptionNumber` AND `order`.`TableNumber` = `table`.`TableNumber` AND StatusNumber <> 6");
            //Select all the orders and their content

            try {
                while (resultset.next()) {

                    int orderID = resultset.getInt("OrderNumber");
                    int TableID = resultset.getInt("TableNumber");
                    String consumptionName = resultset.getString("ConsumptionName");
                    String ConsumptionFullType = resultset.getString("ConsumptionType");
                    int orderContentID = resultset.getInt("consumptionorderid");
                    int contentStatus = resultset.getInt("ConsumptionStatus");
                    double price = resultset.getInt("ConsumptionPrice");
                    boolean wantsinvoice = resultset.getBoolean("Pay");

                    ConsumptionType consumptionType = ConsumptionType.MEAL;

                    if (ConsumptionFullType.equals("appetizer")) {
                        consumptionType = ConsumptionType.MEAL;
                    }

                    if (ConsumptionFullType.equals("maindish")) {
                        consumptionType = ConsumptionType.MEAL;
                    }

                    if (ConsumptionFullType.equals("salad")) {
                        consumptionType = ConsumptionType.MEAL;
                    }

                    if (ConsumptionFullType.equals("dessert")) {
                        consumptionType = ConsumptionType.MEAL;
                    }

                    if (ConsumptionFullType.equals("hot beverage")) {
                        consumptionType = ConsumptionType.DRINK;
                    }

                    if (ConsumptionFullType.equals("drink")) {
                        consumptionType = ConsumptionType.DRINK;
                    }

                    //Creates a new order with their obtained information
                    Order o = new Order(orderID, TableID, consumptionName, consumptionType, orderContentID, contentStatus, price, wantsinvoice);
                    //add them to the arraylist
                    orderLijst.add(o);

                }
            } catch (SQLException ex) {

            }

            connection.closeConnection();
        }

        return orderLijst;
        //Give array of the orders 
    }

    /**
     * @param consumptionType
     * @param tableID
     * @param employeeId
     */
    public void updateStatus(ConsumptionType consumptionType, int tableID, int employeeId) {

        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {

            //The status will be updated from "will be served" to "served"
            if (consumptionType == ConsumptionType.MEAL) {
                String s = String.format("UPDATE `consumptionorder` "
                        + "INNER JOIN `consumption` "
                        + "on `consumptionorder`.`ConsumptionNumber` = `consumption`.`ConsumptionNumber` "
                        + "INNER JOIN `order` "
                        + "on `order`.`OrderNumber` = `consumptionorder`.`OrderNumber` "
                        + "INNER JOIN `table` "
                        + "on `order`.`TableNumber` = `table`.`TableNumber` "
                        + "SET `consumptionorder`.`ConsumptionStatus` = '4', "
                        + "`EmployeeNr` = '%s' "
                        + "WHERE `order`.`TableNumber` = '%d' \n"
                        + "AND `consumptionorder`.`ConsumptionStatus` = '3' "
                        + "AND (`consumption`.ConsumptionType = 'appetizer' "
                        + "OR `consumption`.ConsumptionType = 'maindish' "
                        + "OR `consumption`.ConsumptionType = 'salad' "
                        + "OR `consumption`.ConsumptionType = 'dessert')", employeeId, tableID);
                boolean succesfulupdate = connection.executeSQLDeleteStatement(s);

            }

            if (consumptionType == ConsumptionType.DRINK) {
                String s = String.format("UPDATE `consumptionorder` \n"
                        + "INNER JOIN `consumption` \n"
                        + "on `consumptionorder`.`ConsumptionNumber` = `consumption`.`ConsumptionNumber` \n"
                        + "INNER JOIN `order` \n"
                        + "on `order`.`OrderNumber` = `consumptionorder`.`OrderNumber` \n"
                        + "INNER JOIN `table` \n"
                        + "on `order`.`TableNumber` = `table`.`TableNumber` \n"
                        + "SET `consumptionorder`.`ConsumptionStatus` = '4', \n"
                        + "`EmployeeNr` = '%s' \n"
                        + "WHERE `order`.`TableNumber` = '%d' \n"
                        + "AND `consumptionorder`.`ConsumptionStatus` = '3' \n"
                        + "AND \n"
                        + "(`consumption`.ConsumptionType =  'hot beverage' \n"
                        + "OR\n"
                        + "`consumption`.ConsumptionType =  'drink')", employeeId, tableID);
                boolean succesfulupdate = connection.executeSQLDeleteStatement(s);
            }

            connection.closeConnection();
        }
    }

    /**
     * @param tableID
     * @param totalCost
     */
    public void invoiceTable(int tableID, int totalCost) {

        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {

            String s = String.format("UPDATE `order` INNER JOIN `table` on `order`.`TableNumber` = `table`.`TableNumber`\n"
                    + "INNER JOIN `invoice` on `invoice`.`InvoiceNumber` = `order`.`InvoiceNumber`\n"
                    + "SET `order`.`StatusNumber` = 6, `table`.`Pay` = 0, `invoice`.`TotalCost` = '%d' \n"
                    + "WHERE `table`.`TableNumber` = '%d' AND `table`.`Pay` = 1 AND `order`.`StatusNumber` <> 6 ", totalCost, tableID);
            boolean succesfulUpdate = connection.executeSQLUpdateStatement(s);

            connection.closeConnection();
        }
    }

    public void drinkHelper() {

        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {

            String s = String.format("UPDATE `consumptionorder`\n"
                    + "INNER JOIN `consumption`\n"
                    + "ON `consumptionorder`.`ConsumptionNumber` = `consumption`.`ConsumptionNumber`\n"
                    + "SET `consumptionorder`.`ConsumptionStatus` = '3' WHERE (`consumption`.`ConsumptionType` = \"drink\" OR `consumption`.`ConsumptionType` = \"hot beverage\") AND (`consumptionorder`.`ConsumptionStatus` = '1' OR `consumptionorder`.`ConsumptionStatus` = '2')");
            boolean succesfulUpdate = connection.executeSQLUpdateStatement(s);

            connection.closeConnection();
        }
    }

}

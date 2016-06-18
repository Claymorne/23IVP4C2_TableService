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
 * @author ppthgast
 */
public class OrderDAO {

    public OrderDAO() {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }

    /**
     * Tries to find the loans for the given in the persistent data store, in
     * this case a MySQL database.In this POC, the lend copies of the books are
     * not loaded - it is out of scope for now.
     *
     * @param member identifies the member whose loans are to be loaded from the
     * database
     *
     * @return an ArrayList object containing the Loan objects that were found.
     * In case no loan could be found, still a valid ArrayList object is
     * returned. It does not contain any objects.
     */
    public ArrayList<Order> loadOrders() {

        ArrayList<Order> orderLijst = new ArrayList<>();

        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            // If a connection was successfully setup, execute the SELECT statement.

            ResultSet resultset = connection.executeSQLSelectStatement(
                    "SELECT * FROM `order`,`consumption`,`consumptionorder`,`table`\n"
                    + "WHERE `order`.`OrderNumber` = `consumptionorder`.`OrderNumber` AND `consumptionorder`.`ConsumptionNumber` = `consumption`.`ConsumptionNumber` AND `order`.`TableNumber` = `table`.`TableNumber`");
            //Selecteert alle orders en bijbehorende content

            try {
                while (resultset.next()) {

                    int orderID = resultset.getInt("OrderNumber");
                    int TableID = resultset.getInt("TableNumber");
                    String consumptionName = resultset.getString("ConsumptionName");
                    String ConsumptionFullType = resultset.getString("ConsumptionType");
                    int orderContentID = resultset.getInt("consumptionorderid");
                    int contentStatus = resultset.getInt("ConsumptionStatus");
                    double price = resultset.getInt("ConsumptionPrice");
                    boolean wantsinvoice = resultset.getBoolean("Status");

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

                    //maakt nieuw order met verkregen gegegevens
                    Order o = new Order(orderID, TableID, consumptionName, consumptionType, orderContentID, contentStatus, price, wantsinvoice);
                    //voegt ze toe in arraylist
                    orderLijst.add(o);

                }
            } catch (SQLException ex) {

            }

            connection.closeConnection();
        }

        return orderLijst;
        //geeft array van orders 
    }

    public void updateStatus(ConsumptionType consumptionType, int tableID, int employeeId) {

        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {

            //update de status van word geserveerd naar is geserveerd
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

                //updateOrderHelper(tableID);

            }
/*
            String s = String.format("UPDATE `ordercontent` inner join "
                    + "`order` on `order`.ID = `ordercontent`.OrderID SET"
                    + " `ContentStatus`= 4 WHERE"
                    + " `Consumptiontype` = '%s' AND `Table` = '%d'",
                    consumptionType.toString().toLowerCase(), tableID);
            boolean succesfulUpdate = connection.executeSQLUpdateStatement(s);

            //voert updateorderhelper uit
            updateOrderHelper(tableID);  */

            connection.closeConnection();
        }
    }

    public void updateOrderHelper(int tableID) {

        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {
            //Select alles van de gegeven tafel
            String s = String.format("SELECT * FROM `ordercontent` "
                    + "inner join `order` on `order`.ID "
                    + "= `ordercontent`.OrderID WHERE `Table` = '%d' ",
                    tableID);

            ResultSet resultset = connection.executeSQLSelectStatement(s);

            //gevonden = default false
            boolean found = false;
            //alles klopt = default true
            boolean allStatusCorrect = true;
            try {
                while (resultset.next()) {

                    //als het gelukt is, staat found op true
                    found = true;
                    //Verkrijgt contentstatus
                    int contentStatus = resultset.getInt("ContentStatus");
                    //als contentstatus geen 4 (is geserveerd) is, gaat alles correct op false
                    //anders blijft het true
                    if (contentStatus != 4) {
                        allStatusCorrect = false;
                    }

                }
            } catch (SQLException ex) {

            }
            //als beiden true is gaat de order van word geserveerd naar is geserveerd
            if (found && allStatusCorrect) {
                String s2 = String.format("UPDATE `order` SET `StatusNr`=5 WHERE `Table`='%d'",
                        tableID);
                boolean succesfulUpdate = connection.executeSQLUpdateStatement(s2);
            }

            connection.closeConnection();
        }

    }

    public void invoiceTable(int tableID) {

        DatabaseConnection connection = new DatabaseConnection();
        if (connection.openConnection()) {

            String s = String.format("UPDATE `ordercontent` inner join "
                    + "`order` on `order`.ID = `ordercontent`.OrderID SET"
                    + " `ContentStatus`= 5 WHERE"
                    + "  `Table` = '%d'",
                    tableID);
            boolean succesfulUpdate = connection.executeSQLUpdateStatement(s);

            updateOrderHelper(tableID);

            connection.closeConnection();
        }
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic;

import datastorage.LoginDAO;
import datastorage.OrderDAO;
import datastorage.TableDAO;
import datastorage.TestDAO;
import domain.Order;
import domain.Order.ConsumptionType;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Infosys
 */
public class TableServiceManager {

    ArrayList<Order> orders;
    boolean loginResult;

    /**
     *
     */
    public TableServiceManager() {
        updateOrders();
    }

    /**
     *
     */
    public void updateOrders() {
        this.orders = (new OrderDAO()).loadOrders();
        //load orders and put them in an array
    }

    /**
     *
     * @param consumptionType
     * @return
     */
    public ArrayList<Order> getOrders(ConsumptionType consumptionType) {
        ArrayList<Order> returnList = new ArrayList<>();

        for (Order o : this.orders) {
            //if the dish/drink (type) is equal to the type you want  , and the contentstatus is "will be served", 
            //then add them to the returnlist
            if (o.getConsumptionType() == consumptionType && o.getContentStatus() == 3) {
                returnList.add(o);
                //add to the returned arraylist 
            }
        }

        return returnList;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getListOfSortedUniqueTables() {
        ArrayList<Integer> returnList = new ArrayList<>();

        for (Order o : this.orders) {
            int tafelId = o.getTableID();
            //If tableID does not exist yet and still must be served
            //then add to the arraylist
            if (returnList.contains(tafelId) == false && o.getContentStatus() == 3) {
                returnList.add(tafelId);
            }
        }

        Collections.sort(returnList);
        return returnList;
    }

    /**
     *
     * @return
     */
    public ArrayList<Integer> getListOfSortedUniqueInvoiceTables() {
        ArrayList<Integer> returnList = new ArrayList<>();

        for (Order o : this.orders) {
            int tafelId = o.getTableID();
            //If tableID does not exist yet and you want to pay
            //then add to the arraylist
            if (returnList.contains(tafelId) == false && o.GetWantsInvoice() == true) {
                returnList.add(tafelId);
            }
        }

        Collections.sort(returnList);
        return returnList;
    }

    /**
     *
     * @param consumptionType
     * @return
     */
    public ArrayList<Order> getUniqueOrders(ConsumptionType consumptionType) {
        ArrayList<Order> returnList = new ArrayList<>();

        ArrayList UniqueOrders = new ArrayList();
        for (Order o : this.orders) {
            if (o.getConsumptionType() == consumptionType && UniqueOrders.contains(o.getOrderID()) == false && o.getContentStatus() != 5) {
                //If the dish/drink is equal to the ordertype you want, and the OrderID does not is unique (does not already exist),
                // then add to the arraylist.
                returnList.add(o);
                int uniqueOrderID = o.getOrderID();
                UniqueOrders.add(uniqueOrderID);

            }
        }

        return returnList;
    }

    /**
     *
     * @param tableID
     * @return
     */
    public ArrayList<Order> getInvoiceOrders(int tableID) {
        ArrayList<Order> returnList = new ArrayList<>();

        for (Order o : this.orders) {
            // If tableID equals the tableIDyou want and they want a bill,
            // then add to the arraylist.
            if (o.getTableID() == tableID && o.GetWantsInvoice() == true) {
                returnList.add(o);
            }
        }

        return returnList;
    }

    /**
     *
     * @param consumptionType
     * @param tableID
     * @param employeeId
     */
    public void updateOrdersStatus(ConsumptionType consumptionType, int tableID, int employeeId) {
        (new OrderDAO()).updateStatus(consumptionType, tableID, employeeId);
        //The order status information will be updated
    }

    /**
     *
     * @param tableID
     * @param totalCost
     */
    public void invoiceTable(int tableID, int totalCost) {
        (new OrderDAO()).invoiceTable(tableID, totalCost);
        //The bill information will be updated
    }

    /**
     *
     * @param OldTableNumber
     * @param NewTableNumber
     */
    public void changeTable(int OldTableNumber, int NewTableNumber) {

        TableDAO.changeTable(OldTableNumber, NewTableNumber);
        //The tablenumber change will be updated
    }

    /**
     *
     * @param mail
     * @param password
     * @return
     */
    public boolean loginCheck(String mail, String password) {
        loginResult = (new LoginDAO()).LoginChecker(mail, password);
        return loginResult;
    }

    /**
     *
     */
    public void testAppetizer() {
        TestDAO.testSetReadyAppetizer();
    }

    /**
     *
     */
    public void testMaindish() {
        TestDAO.testSetReadyMain();
    }

    /**
     *
     */
    public void testTable() {
        TestDAO.testChangeTable();
    }

    /**
     *
     */
    public void testInvoice() {
        TestDAO.testSetInvoice();
    }
}

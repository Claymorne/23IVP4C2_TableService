/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic;

import datastorage.OrderDAO;
import datastorage.TableDAO;
import domain.Order;
import domain.Order.ConsumptionType;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Ray
 */
public class TableServiceManager {

    ArrayList<Order> orders;

    public TableServiceManager() {
        updateOrders();
    }

    public void updateOrders() {
        this.orders = (new OrderDAO()).loadOrders();
        //load orders and put them in an array
    }

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

    public ArrayList<Order> getUniqueOrders(ConsumptionType consumptionType) {
        ArrayList<Order> returnList = new ArrayList<>();

        ArrayList UniqueOrders = new ArrayList();
        for (Order o : this.orders) {
            if (o.getConsumptionType() == consumptionType && UniqueOrders.contains(o.getOrderID()) == false) {
                //If the dish/drink is equal to the ordertype you want, and the OrderID does not is unique (does not already exist),
                // then add to the arraylist.
                returnList.add(o);
                int uniqueOrderID = o.getOrderID();
                UniqueOrders.add(uniqueOrderID);

            }
        }

        return returnList;
    }

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

    public void updateOrdersStatus(ConsumptionType consumptionType, int tableID, int employeeId) {
        (new OrderDAO()).updateStatus(consumptionType, tableID, employeeId);
        //The order status information will be updated
    }

    public void invoiceTable(int tableID, double totalCost) {
        (new OrderDAO()).invoiceTable(tableID, totalCost);
        //The bill information will be updated
    }

    public void changeTable(int OldTableNumber, int NewTableNumber) {

        TableDAO.changeTable(OldTableNumber, NewTableNumber);
        //The tablenumber change will be updated
    }
}

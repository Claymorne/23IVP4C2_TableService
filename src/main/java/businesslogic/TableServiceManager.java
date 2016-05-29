/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic;

import com.sun.javafx.geom.AreaOp;
import datastorage.OrderDAO;
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


    public void updateOrders()
    {
        this.orders = (new OrderDAO()).loadOrders();
    }
    
    public ArrayList<Order> getOrders(ConsumptionType consumptionType)
    {
        ArrayList<Order> returnList = new ArrayList<>();
        
        for(Order o : this.orders)
        {
            if (o.getConsumptionType() == consumptionType)
            {
                returnList.add(o);
            }
        }
        
        return returnList;
    }
    
    public ArrayList<Integer> getListOfSortedUniqueTables()
    {
        ArrayList<Integer> returnList = new ArrayList<>();
        
        for(Order o : this.orders)
        {
            int tafelId = o.getTableID();
            
            if ( returnList.contains(tafelId) == false)
            {
                returnList.add(tafelId);
            }
        }
        
        Collections.sort(returnList);
        return returnList;
    }
    
        public ArrayList<Order> getUniqueOrders(ConsumptionType consumptionType)
    {
        ArrayList<Order> returnList = new ArrayList<>();
        
        ArrayList UniqueOrders = new ArrayList();
        for(Order o : this.orders)
        {
            if (o.getConsumptionType() == consumptionType && UniqueOrders.contains(o.getOrderID()) == false)
            {
                
                returnList.add(o);
                int uniqueOrderID = o.getOrderID();
                UniqueOrders.add(uniqueOrderID);
                
            }
        }
        
        return returnList;
    }
    
    
    
    public void updateOrdersStatus(ConsumptionType consumptionType, int tableID) {
        (new OrderDAO()).updateStatus(consumptionType, tableID);
    }
}
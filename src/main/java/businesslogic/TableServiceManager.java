/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package businesslogic;

import com.sun.javafx.geom.AreaOp;
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


    public void updateOrders()
    {
        this.orders = (new OrderDAO()).loadOrders();
        //laad orders en zet in array
    }
    
    public ArrayList<Order> getOrders(ConsumptionType consumptionType)
    {
        ArrayList<Order> returnList = new ArrayList<>();
        
        for(Order o : this.orders)
        {
            //als gerecht/drank klopt, en contenstatus word geserveerd is, 
            //voeg dan toe aan returnlist
            if (o.getConsumptionType() == consumptionType && o.getContentStatus() == 3)
            {
                returnList.add(o);
                //voeg toe aan de arraylist die gereturnt word
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
            //als tafelid nog niet bestaat en moet geserveerd worden, voeg toe
            //aan arraylist
            if ( returnList.contains(tafelId) == false && o.getContentStatus() == 3)
            {
                returnList.add(tafelId);
            }
        }
        
        Collections.sort(returnList);
        return returnList;
    }
    
        public ArrayList<Integer> getListOfSortedUniqueInvoiceTables()
    {
        ArrayList<Integer> returnList = new ArrayList<>();
        
        for(Order o : this.orders)
        {
            int tafelId = o.getTableID();
            //als tafelid nog niet bestaat en wil betalen,
            //voeg toe aan arraylist
            if ( returnList.contains(tafelId) == false && o.GetWantsInvoice()== true)
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
    
        public ArrayList<Order> getInvoiceOrders(int tableID)
    {
        ArrayList<Order> returnList = new ArrayList<>();
        
        for(Order o : this.orders)
        {
            if (o.getTableID() == tableID && o.GetWantsInvoice() == true)
            {
                returnList.add(o);
            }
        }
        
        return returnList;
    }
        
        
    
    public void updateOrdersStatus(ConsumptionType consumptionType, int tableID, int employeeId) {
        (new OrderDAO()).updateStatus(consumptionType, tableID, employeeId);
    }
    
    public void invoiceTable(int tableID) {
        (new OrderDAO()).invoiceTable(tableID);
    }
    
    
    public void changeTable(int OldTableNumber, int NewTableNumber){
       
    TableDAO.changeTable (OldTableNumber, NewTableNumber);
}}
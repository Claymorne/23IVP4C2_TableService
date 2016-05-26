/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author Ray
 */
public class Order {
    
    private int orderID, orderContentID;
    private int tableID;
    private String consumptionName;
    private ConsumptionType consumptionType;
            
    public enum ConsumptionType {
        GERECHT,
        DRANK
    };
    
        public Order(int OrderID , int TableID , String consumptionName,
                ConsumptionType consumptionType, int orderContentID )
    {
        this.orderID = OrderID;
        this.tableID = TableID;
        this.consumptionName = consumptionName;
        this.consumptionType = consumptionType;
        this.orderContentID = orderContentID;
        
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getOrderContentID() {
        return orderContentID;
    }

    public void setOrderContentID(int orderContentID) {
        this.orderContentID = orderContentID;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public String getConsumptionName() {
        return consumptionName;
    }

    public void setConsumptionName(String consumptionName) {
        this.consumptionName = consumptionName;
    }

    public ConsumptionType getConsumptionType() {
        return consumptionType;
    }

    public void setConsumptionType(ConsumptionType consumptionType) {
        this.consumptionType = consumptionType;
    }
        
        
}

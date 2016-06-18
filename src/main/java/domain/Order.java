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



    private int orderID, orderContentID, contentStatus;
    private int tableID;
    private String consumptionName;
    private ConsumptionType consumptionType;
    private double price;
    private boolean wantsInvoice;
            
    public enum ConsumptionType {
        MEAL,
        DRINK
    };
    
        public Order(int OrderID , int TableID , String consumptionName,
                ConsumptionType consumptionType, int orderContentID,
                int contentStatus, double price, boolean wantsinvoice)
    {
        this.orderID = OrderID;
        this.tableID = TableID;
        this.consumptionName = consumptionName;
        this.consumptionType = consumptionType;
        this.orderContentID = orderContentID;
        this.contentStatus = contentStatus;
        this.price = price;
        this.wantsInvoice =  wantsinvoice;
        
    }

    public double getPrice() {
        return price;
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
    
        public boolean GetWantsInvoice() {
        return wantsInvoice;
    }


    public void setConsumptionType(ConsumptionType consumptionType) {
        this.consumptionType = consumptionType;
    }
    
        public int getContentStatus() {
        return contentStatus;
    }
        public String getContentStatusString() {
            
            if (contentStatus == 0) {
                return "Nog niet besteld";
            }
            
            if (contentStatus == 1) {
                return "Is besteld";
            }
            
            if (contentStatus == 2) {
                return "Wordt bereid";
            }
            
            if (contentStatus == 3) {
                return "Wordt opgediend";
            }
            
            if (contentStatus == 4) {
                return "Opgediend";
            }
            
            if (contentStatus == 5) {
                return "Betaald";
            }
            else{
                return "Error: Geen contentStatus";
            }
            
        }
        
        
}

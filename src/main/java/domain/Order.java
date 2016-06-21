/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

/**
 *
 * @author Infosys
 */
public class Order {

    private int orderID, orderContentID, contentStatus;
    private int tableID;
    private String consumptionName;
    private ConsumptionType consumptionType;
    private double price;
    private boolean wantsInvoice;

    /**
     *
     */
    public enum ConsumptionType {

        /**
         *
         */
        MEAL,
        /**
         *
         */
        DRINK
    };

    /**
     *
     * @param OrderID
     * @param TableID
     * @param consumptionName
     * @param consumptionType
     * @param orderContentID
     * @param contentStatus
     * @param price
     * @param wantsinvoice
     */
    public Order(int OrderID, int TableID, String consumptionName,
            ConsumptionType consumptionType, int orderContentID,
            int contentStatus, double price, boolean wantsinvoice) {
        this.orderID = OrderID;
        this.tableID = TableID;
        this.consumptionName = consumptionName;
        this.consumptionType = consumptionType;
        this.orderContentID = orderContentID;
        this.contentStatus = contentStatus;
        this.price = price;
        this.wantsInvoice = wantsinvoice;

    }

    /**
     *
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     *
     * @return
     */
    public int getOrderID() {
        return orderID;
    }

    /**
     *
     * @param orderID
     */
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    /**
     *
     * @return
     */
    public int getOrderContentID() {
        return orderContentID;
    }

    /**
     *
     * @param orderContentID
     */
    public void setOrderContentID(int orderContentID) {
        this.orderContentID = orderContentID;
    }

    /**
     *
     * @return
     */
    public int getTableID() {
        return tableID;
    }

    /**
     *
     * @param tableID
     */
    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    /**
     *
     * @return
     */
    public String getConsumptionName() {
        return consumptionName;
    }

    /**
     *
     * @param consumptionName
     */
    public void setConsumptionName(String consumptionName) {
        this.consumptionName = consumptionName;
    }

    /**
     *
     * @return
     */
    public ConsumptionType getConsumptionType() {
        return consumptionType;
    }

    /**
     *
     * @return
     */
    public boolean GetWantsInvoice() {
        return wantsInvoice;
    }

    /**
     *
     * @param consumptionType
     */
    public void setConsumptionType(ConsumptionType consumptionType) {
        this.consumptionType = consumptionType;
    }

    /**
     *
     * @return
     */
    public int getContentStatus() {
        return contentStatus;
    }

    /**
     *
     * @return
     */
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
        } else {
            return "Error: Geen contentStatus";
        }

    }

}

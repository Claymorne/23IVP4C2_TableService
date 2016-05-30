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
public class OrderDAO
{
    public OrderDAO()
    {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitely make this clear.
    }
    
    /**
     * Tries to find the loans for the given in the persistent data store, in
     * this case a MySQL database.In this POC, the lend copies of the books are
     * not loaded - it is out of scope for now.
     * 
     * @param member identifies the member whose loans are to be
     * loaded from the database
     * 
     * @return an ArrayList object containing the Loan objects that were found.
     * In case no loan could be found, still a valid ArrayList object is returned.
     * It does not contain any objects.
     */
   public ArrayList<Order> loadOrders() {
    
        ArrayList<Order> orderLijst = new ArrayList<>();
        
        DatabaseConnection connection = new DatabaseConnection();
            if(connection.openConnection())
            {
                // If a connection was successfully setup, execute the SELECT statement.
                
                        
                ResultSet resultset = connection.executeSQLSelectStatement(
                    "SELECT * FROM `order`, `ordercontent` WHERE `order`.ID = `ordercontent`.OrderID");
                
            try {
                while(resultset.next()){
                    
                    
                    
                    int orderID = resultset.getInt("OrderID");
                    int TableID = resultset.getInt("Table");
                    String consumptionName =  resultset.getString("ConsumptionName");
                    ConsumptionType consumptionType = ConsumptionType.valueOf(
                                            resultset.getString("ConsumptionType").toUpperCase());
                    int orderContentID = resultset.getInt("OrderContentID");
                    int contentStatus = resultset.getInt("ContentStatus");
                    double price = resultset.getDouble("Prijs");
                    
                    //        Order(int OrderID , int TableID , String consumptionName,
                    //        ConsumptionType consumptionType, int orderContentID )
                    Order o = new Order(orderID, TableID, consumptionName,consumptionType , orderContentID , contentStatus, price);
                    
                    orderLijst.add(o);
                    
                    
                }
            } catch (SQLException ex) {
             
            }

  
                connection.closeConnection();
            }
            
            return orderLijst;
    }
   
   public void updateStatus(ConsumptionType consumptionType, int tableID){
       
        DatabaseConnection connection = new DatabaseConnection();
            if(connection.openConnection()){
                
                String s = String.format( "UPDATE `ordercontent` inner join "
                        + "`order` on `order`.ID = `ordercontent`.OrderID SET"
                        + " `ContentStatus`= 5 WHERE"
                        + " `Consumptiontype` = '%s' AND `Table` = '%d'",
                        consumptionType.toString().toLowerCase(), tableID) ;
                boolean succesfulUpdate = connection.executeSQLUpdateStatement(s);
                
                updateOrderHelper(tableID);
                
                connection.closeConnection();
            }
   }
   
    public void updateOrderHelper(int tableID){
       
   DatabaseConnection connection = new DatabaseConnection();
            if(connection.openConnection())
            {
                // If a connection was successfully setup, execute the SELECT statement.
                String s = String.format("SELECT * FROM `ordercontent` "
                        + "inner join `order` on `order`.ID "
                        + "= `ordercontent`.OrderID WHERE `Table` = '%d' ", 
                        tableID);
                        
                ResultSet resultset = connection.executeSQLSelectStatement(s);
                    
             boolean found = false;
             boolean allStatusCorrect = true;
            try {
                while(resultset.next()){
                    
                   found = true; 
                   
                   int contentStatus = resultset.getInt("ContentStatus");
                   
                   if (contentStatus != 5)
                   {
                       allStatusCorrect = false;
                   }
                    
                }
            } catch (SQLException ex) {
             
            }
                
                if (found && allStatusCorrect)
                {
                    String s2 = String.format( "UPDATE `order` SET `StatusNr`=5 WHERE `Table`='%d'",
                        tableID) ;
                    boolean succesfulUpdate = connection.executeSQLUpdateStatement(s2);
                }
  
                connection.closeConnection();
            }
            
   }       
    
       public void invoiceTable(int tableID){
       
        DatabaseConnection connection = new DatabaseConnection();
            if(connection.openConnection()){
                
                String s = String.format( "UPDATE `ordercontent` inner join "
                        + "`order` on `order`.ID = `ordercontent`.OrderID SET"
                        + " `ContentStatus`= 7 WHERE"
                        + "  `Table` = '%d'",
                         tableID) ;
                boolean succesfulUpdate = connection.executeSQLUpdateStatement(s);
                
                updateOrderHelper(tableID);
                
                connection.closeConnection();
            }
   }
            
}

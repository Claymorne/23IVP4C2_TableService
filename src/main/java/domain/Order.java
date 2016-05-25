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
    
    private int bestellingID;
    private int tafelID;
    
    
        public Order(int bestellingID , int tafelID )
    {
        this.bestellingID = bestellingID;
        this.tafelID = tafelID;
    }
}

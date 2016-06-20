/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import datastorage.OrderDAO;
import datastorage.TableDAO;
import domain.Order;
import java.util.ArrayList;
import javax.swing.JFrame;

/**
 *
 * @author Ray
 */
public class main {

    public static void main(String[] args) {

        //Creates new JFrame, contentpane is ServiceGUI
        JFrame Frame = new JFrame();
        Frame.setSize(1000, 500);
        Frame.setDefaultCloseOperation(
                JFrame.EXIT_ON_CLOSE);
        Frame.setTitle("Hartige Hap");
        Frame.setContentPane(new presentation.ServiceGUI());
        Frame.setVisible(true);
    }
}

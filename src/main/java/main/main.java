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
import presentation.LoginGUI;

/**
 *
 * @author Infosys
 */
public class main {

    /**
     * @param args
     */
    public static void main(String[] args) {

        JFrame LoginFrame = new JFrame();
        LoginFrame.setSize(370, 250);
        LoginFrame.setTitle("Hartige Hap Bediening");
        LoginFrame.setContentPane(new presentation.LoginGUI());
        LoginFrame.setVisible(true);
        LoginFrame.setResizable(false);

        //Creates new JFrame, contentpane is LoginGUI
    }
}

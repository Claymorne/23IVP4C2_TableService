/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import businesslogic.TableServiceManager;
import com.sun.javafx.geom.Curve;
import datastorage.InsertDAO;
import domain.Order;
import datastorage.DatabaseConnection;
import datastorage.TableDAO;
import domain.Order.ConsumptionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;

/**
 *
 * @author Ray
 */
public class ServiceGUI extends javax.swing.JPanel {
    TableServiceManager tsManager;
    ButtonGroup consumptionGroup;
    /**
     * Creates new form ServiceGUI
     */
    public ServiceGUI() {
        tsManager = new TableServiceManager(); //Managerklasse wordt object
        initComponents(); // Maakt alle componenten
        initConsumptionGroup(); // Maakt de gerecht/drank kies knoppen
        refreshUIContent(); // dit wordt door de GUI designer aangeroepen
    }      

    private void initConsumptionGroup() {
        consumptionGroup = new ButtonGroup();//maakt buttongroup
        
        //Voegt de radio buttons toe, om te kiezen of het afgeronde gaat over
        //drankjes of gerechten.
        consumptionGroup.add(radioDrink);
        consumptionGroup.add(radioMeal);
        
        radioDrink.setSelected(true); //zet default op drank, reden omdat deze het vaakst wordt gedaan.
    }
    
    private void initComboBox()
    {
        //verwijdert eerst alles uit de ComboBox
        cbTable.removeAllItems();
        cbInvoice.removeAllItems();
        //Alleen bestaande tafels waar wat besteld is staat in de combobox
        for (int i : tsManager.getListOfSortedUniqueTables() )
        {
            cbTable.addItem("Tafel " + i);
        }
        
                for (int i : tsManager.getListOfSortedUniqueInvoiceTables() )
        {
            cbInvoice.addItem("Tafel " + i);
        }
    }
    
    
    
    
    
    private void refreshUIContent() {
        clearAllTables(); //Eerst word alles leeggehaald
        tsManager.updateOrders(); // Laad dan de database opnieuw alles lezen
        initComboBox(); // En dan de Combobox vullen

       
        DefaultTableModel tmDrinks = (DefaultTableModel)drinksTable.getModel();
        DefaultTableModel tmMeals = (DefaultTableModel)mealsTable.getModel();
        DefaultTableModel tmOrders = (DefaultTableModel)ordersTable.getModel();

        //vult de tabellen
        fillTable(tmMeals, Order.ConsumptionType.MEAL);
        fillTable(tmDrinks, Order.ConsumptionType.DRINK);
        fillOrderTable(tmOrders, Order.ConsumptionType.MEAL);
        
    }

    private void fillTable(DefaultTableModel tm, Order.ConsumptionType consumptionType) {
        int rowIndex = 0;
        
        for(Order o : tsManager.getOrders(consumptionType) )
        {
            String tafelNummer = "" + o.getTableID(); //tafelnummer
            String gerechtNaam = "" + o.getConsumptionName(); //naam consumptie
            String bestellingID = "" + o.getOrderID(); //BestellingID


            Object[] row = new Object[]{tafelNummer, gerechtNaam, bestellingID};
                //rows worden gevuld
            tm.insertRow(rowIndex, row);

            rowIndex += 1;
        }
    }
    
        private void fillOrderTable(DefaultTableModel tm, ConsumptionType GERECHT) {
        int rowIndex = 0;
        
        for(Order o : tsManager.getUniqueOrders(ConsumptionType.MEAL))
        {
            String tafelNummer = "" + o.getTableID(); //Tafel
            String soortBestelling = "Gerecht" ; //Soort
            String contentStatus = o.getContentStatusString(); //status
                                                      //status

            Object[] row = new Object[]{tafelNummer,soortBestelling, contentStatus};
                //rows worden gevuld
            tm.insertRow(rowIndex, row);

            rowIndex += 1;
        }
        
        for(Order o : tsManager.getUniqueOrders(ConsumptionType.DRINK))
        {
            String tafelNummer = "" + o.getTableID(); //Tafel
            String soortBestelling = "Drank" ; //Soort
            String contentStatus = o.getContentStatusString(); //status
            
            Object[] row = new Object[]{tafelNummer, soortBestelling, contentStatus};
                //rows worden gevuld
            tm.insertRow(rowIndex, row);

            rowIndex += 1;
        }
        
        sortOrderTable();
            }
    
        private void sortOrderTable(){
        
        DefaultTableModel tmOrders = (DefaultTableModel)ordersTable.getModel();
        
        
        
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tmOrders);
        
        
            ordersTable.setRowSorter(sorter);
            List<RowSorter.SortKey> sortKeys = new ArrayList<>();
 
            int columnIndexToSort = 0;
            sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));
 
            sorter.setSortKeys(sortKeys);
            sorter.sort();
        }
    
        
        private void fillInvoiceTable(){
                    
            DefaultTableModel tmInvoice = (DefaultTableModel)invoiceTable.getModel();
            
            int rowIndex = 0;
            double totalPrice = 0;
            
            
            
            String str = (String)cbInvoice.getSelectedItem(); // get string zoals "Tafel 1"
            str = str.replace("Tafel ", ""); // Verander "Tafel 1" naar "1"
            int tableId = Integer.parseInt(str); // vernader "1" naar 1 (int)
            
            
                for(Order o : tsManager.getInvoiceOrders(tableId) )
        {
            String gerechtNaam = "" + o.getConsumptionName(); //naam consumptie
            String price = "€ " + o.getPrice(); //prijs
            
            totalPrice += o.getPrice();
            invoiceTotalPrice.setText("€ " + totalPrice);


            Object[] row = new Object[]{gerechtNaam, price};
                //rows worden gevuld
            tmInvoice.insertRow(rowIndex, row);

            rowIndex += 1;
        }
        
        
            
        }
        
        
        
    private void clearAllTables() {
        DefaultTableModel tmDrinks = (DefaultTableModel)drinksTable.getModel();
        DefaultTableModel tmMeals = (DefaultTableModel)mealsTable.getModel();
        DefaultTableModel tmOrders = (DefaultTableModel)ordersTable.getModel();
        
        //haalt alle tables leeg
        clearTable(tmDrinks);
        clearTable(tmMeals);
        clearTable(tmOrders);
    }

    private void clearTable(DefaultTableModel tm) {
        int rowCount = tm.getRowCount();
        //functie om een table te legen
        for (int i = 0; i < rowCount; i++)
        {
            tm.removeRow(0);
        }
    }
    
    private void updateOrderStatus()
    {
        String str = (String)cbTable.getSelectedItem(); // get string zoals "Tafel 1"
        str = str.replace("Tafel ", ""); // Verander "Tafel 1" naar "1"
        int tableId = Integer.parseInt(str); // vernader "1" naar 1 (int)
        
        //verkrijg employeenr
        int employeeId = Integer.parseInt(employeeField.getText());
        
        // default value
        ConsumptionType ct = Order.ConsumptionType.DRINK;
        
        if (radioMeal.isSelected() == true)
        {
            ct = Order.ConsumptionType.MEAL;
        } //als meal radiobutton is geselecteerd dan word de type op MEAL
          //gezet, en anders blijft de DRINK staan  
        
        tsManager.updateOrdersStatus(ct, tableId,employeeId);
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        drinksTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        mealsTable = new javax.swing.JTable();
        radioDrink = new javax.swing.JRadioButton();
        radioMeal = new javax.swing.JRadioButton();
        handleButton = new javax.swing.JButton();
        cbTable = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        employeeField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        invoiceTable = new javax.swing.JTable();
        invoiceTotalPrice = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        invoiceHandle = new javax.swing.JButton();
        invoiceTableSearch = new javax.swing.JButton();
        cbInvoice = new javax.swing.JComboBox<>();
        refreshButton2 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ordersTable = new javax.swing.JTable();
        refreshButton3 = new javax.swing.JButton();

        refreshButton.setText("Refresh");
        refreshButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButtonActionPerformed(evt);
            }
        });

        drinksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tafelnummer", "Drankje naam", "BestellingID"
            }
        ));
        jScrollPane1.setViewportView(drinksTable);

        mealsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tafelnummer", "Gerechten naam", "BestellingID"
            }
        ));
        jScrollPane2.setViewportView(mealsTable);

        radioDrink.setText("Drankje");
        radioDrink.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioDrinkActionPerformed(evt);
            }
        });

        radioMeal.setText("Gerecht");

        handleButton.setText("Handel af");
        handleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                handleButtonActionPerformed(evt);
            }
        });

        cbTable.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTableActionPerformed(evt);
            }
        });

        jLabel2.setText("Drankjes");

        jLabel3.setText("Gerechten");

        employeeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(jLabel2)
                        .addGap(374, 374, 374)
                        .addComponent(jLabel3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(60, 60, 60)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(radioDrink, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(radioMeal, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(cbTable, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(employeeField, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(handleButton))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(refreshButton)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(radioDrink)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(radioMeal)
                                .addGap(13, 13, 13)
                                .addComponent(cbTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(employeeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(handleButton))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(refreshButton))
                .addContainerGap(190, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Overzicht", jPanel1);

        jLabel8.setText("Verander tafel ");

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jLabel9.setText("naar tafel");

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButton4.setText("Update");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(79, 79, 79)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(jButton4)))
                .addContainerGap(729, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jButton4)
                .addContainerGap(359, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tafel Veranderen", jPanel2);

        jLabel10.setText("Welke tafel wil afrekenen?");

        invoiceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Naam", "Prijs"
            }
        ));
        jScrollPane4.setViewportView(invoiceTable);

        invoiceTotalPrice.setEditable(false);
        invoiceTotalPrice.setBackground(new java.awt.Color(255, 255, 51));
        invoiceTotalPrice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceTotalPriceActionPerformed(evt);
            }
        });

        jLabel1.setText("Totaalprijs");

        invoiceHandle.setText("Reken af");
        invoiceHandle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceHandleActionPerformed(evt);
            }
        });

        invoiceTableSearch.setText("Search");
        invoiceTableSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                invoiceTableSearchActionPerformed(evt);
            }
        });

        cbInvoice.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbInvoice.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbInvoiceActionPerformed(evt);
            }
        });

        refreshButton2.setText("Refresh");
        refreshButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel10))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(34, 34, 34)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(invoiceTableSearch)
                            .addComponent(cbInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(47, 47, 47)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(invoiceHandle)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(108, 108, 108)
                        .addComponent(invoiceTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 470, Short.MAX_VALUE)
                .addComponent(refreshButton2)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbInvoice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(13, 13, 13)
                        .addComponent(invoiceTableSearch))
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(refreshButton2))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(invoiceTotalPrice, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addComponent(invoiceHandle)
                .addContainerGap(159, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Afrekenen", jPanel3);

        ordersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Tafel", "Soort", "Status"
            }
        ));
        jScrollPane3.setViewportView(ordersTable);

        refreshButton3.setText("Refresh");
        refreshButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 434, Short.MAX_VALUE)
                .addComponent(refreshButton3)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(refreshButton3)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabbedPane1.addTab("Status Bestellingen", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 982, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 473, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
         // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        refreshUIContent();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
       // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed

            
            int OldTableNumber = Integer.parseInt(jTextField1.getText());
            int NewTableNumber = Integer.parseInt(jTextField3.getText());
            tsManager.changeTable(OldTableNumber,NewTableNumber);
            
    }//GEN-LAST:event_jButton4ActionPerformed

    private void handleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_handleButtonActionPerformed
                updateOrderStatus();
    }//GEN-LAST:event_handleButtonActionPerformed

    private void radioDrinkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioDrinkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioDrinkActionPerformed

    private void employeeFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeFieldActionPerformed

    private void invoiceTotalPriceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceTotalPriceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_invoiceTotalPriceActionPerformed

    private void invoiceHandleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceHandleActionPerformed
            DefaultTableModel tmInvoice = (DefaultTableModel)invoiceTable.getModel();        
        clearTable(tmInvoice);
        invoiceTotalPrice.setText("");

        
        String str = (String)cbInvoice.getSelectedItem(); // get string zoals "Tafel 1"
        str = str.replace("Tafel ", ""); // Verander "Tafel 1" naar "1"
        int tableId = Integer.parseInt(str); // vernader "1" naar 1 (int)
        
        tsManager.invoiceTable(tableId); 
    }//GEN-LAST:event_invoiceHandleActionPerformed

    private void invoiceTableSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceTableSearchActionPerformed
        
        DefaultTableModel tmInvoice = (DefaultTableModel)invoiceTable.getModel();        
        clearTable(tmInvoice);
        fillInvoiceTable();    
    }//GEN-LAST:event_invoiceTableSearchActionPerformed

    private void cbInvoiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbInvoiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbInvoiceActionPerformed

    private void cbTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTableActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTableActionPerformed

    private void refreshButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButton2ActionPerformed
    refreshUIContent();
    }//GEN-LAST:event_refreshButton2ActionPerformed

    private void refreshButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButton3ActionPerformed
    refreshUIContent();
    }//GEN-LAST:event_refreshButton3ActionPerformed

            
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbInvoice;
    private javax.swing.JComboBox<String> cbTable;
    private javax.swing.JTable drinksTable;
    private javax.swing.JTextField employeeField;
    private javax.swing.JButton handleButton;
    private javax.swing.JButton invoiceHandle;
    private javax.swing.JTable invoiceTable;
    private javax.swing.JButton invoiceTableSearch;
    private javax.swing.JTextField invoiceTotalPrice;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTable mealsTable;
    private javax.swing.JTable ordersTable;
    private javax.swing.JRadioButton radioDrink;
    private javax.swing.JRadioButton radioMeal;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton refreshButton2;
    private javax.swing.JButton refreshButton3;
    // End of variables declaration//GEN-END:variables
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presentation;

import businesslogic.TableServiceManager;
import domain.Order;
import domain.Order.ConsumptionType;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ButtonGroup;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.*;

/**
 *
 * @author Infosys
 */
public class ServiceGUI extends javax.swing.JPanel {

    TableServiceManager tsManager;
    ButtonGroup consumptionGroup;

    /**
     * Creates new form ServiceGUI
     */
    public ServiceGUI() {
        tsManager = new TableServiceManager(); //Managerclass is object
        initComponents(); // Creates all components
        initConsumptionGroup(); // Creates the dish/drink choose button
        refreshUIContent(); // This gets invoked by the GUI designer
    }

    private void initConsumptionGroup() {
        consumptionGroup = new ButtonGroup();//Makes buttongroup

        //add the radio buttons , to choose if the drinks and dishes finishes or not.
        consumptionGroup.add(radioDrink);
        consumptionGroup.add(radioMeal);

        radioDrink.setSelected(true); //Put default to drinks, reason is because this is done the most.
    }

    private void initComboBox() {
        //This Deletes everything from the ComboBox first. 
        cbTable.removeAllItems();
        cbInvoice.removeAllItems();
        //Only the existing tables where their are orders are shown in the ComboBox 
        for (int i : tsManager.getListOfSortedUniqueTables()) {
            cbTable.addItem("Tafel " + i);
        }

        for (int i : tsManager.getListOfSortedUniqueInvoiceTables()) {
            cbInvoice.addItem("Tafel " + i);
        }
    }

    private void refreshUIContent() {
        clearAllTables(); //Everything will be cleared first
        tsManager.updateOrders(); // Then load the database again and then scan everything
        initComboBox(); // Then fill the Combobox

        DefaultTableModel tmDrinks = (DefaultTableModel) drinksTable.getModel();
        DefaultTableModel tmMeals = (DefaultTableModel) mealsTable.getModel();
        DefaultTableModel tmOrders = (DefaultTableModel) ordersTable.getModel();

        //Fills the tables 
        fillTable(tmMeals, Order.ConsumptionType.MEAL);
        fillTable(tmDrinks, Order.ConsumptionType.DRINK);
        fillOrderTable(tmOrders, Order.ConsumptionType.MEAL);

    }

    private void fillTable(DefaultTableModel tm, Order.ConsumptionType consumptionType) {
        int rowIndex = 0;

        for (Order o : tsManager.getOrders(consumptionType)) {
            String tafelNummer = "" + o.getTableID(); //Tablennumber
            String gerechtNaam = "" + o.getConsumptionName(); //Name consumption
            String bestellingID = "" + o.getOrderID(); //OrderID

            Object[] row = new Object[]{tafelNummer, gerechtNaam, bestellingID};
            //rows will be filled
            tm.insertRow(rowIndex, row);

            rowIndex += 1;
        }

        sortDrinksTable();
        sortMealsTable();
    }

    private void fillOrderTable(DefaultTableModel tm, ConsumptionType GERECHT) {
        int rowIndex = 0;

        for (Order o : tsManager.getUniqueOrders(ConsumptionType.MEAL)) {
            String tableNumber = "" + o.getTableID(); //Table
            String orderType = "Gerecht"; //Type
            String contentStatus = o.getContentStatusString(); //Status
            //Status

            Object[] row = new Object[]{tableNumber, orderType, contentStatus};
            //rows will be filled
            tm.insertRow(rowIndex, row);

            rowIndex += 1;
        }

        for (Order o : tsManager.getUniqueOrders(ConsumptionType.DRINK)) {
            String tableNumber = "" + o.getTableID(); //Table
            String orderType = "Drank"; //Type
            String contentStatus = o.getContentStatusString(); //Status

            Object[] row = new Object[]{tableNumber, orderType, contentStatus};
            //rows will be filled
            tm.insertRow(rowIndex, row);

            rowIndex += 1;
        }

        sortOrderTable();
    }

    private void sortOrderTable() {

        DefaultTableModel tmOrders = (DefaultTableModel) ordersTable.getModel();

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tmOrders);

        ordersTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int columnIndexToSort = 0;
        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));

        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

    private void sortMealsTable() {

        DefaultTableModel tmMeals = (DefaultTableModel) mealsTable.getModel();

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tmMeals);

        mealsTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int columnIndexToSort = 0;
        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));

        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

    private void sortDrinksTable() {

        DefaultTableModel tmDrinks = (DefaultTableModel) drinksTable.getModel();

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tmDrinks);

        drinksTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();

        int columnIndexToSort = 0;
        sortKeys.add(new RowSorter.SortKey(columnIndexToSort, SortOrder.ASCENDING));

        sorter.setSortKeys(sortKeys);
        sorter.sort();
    }

    private void fillInvoiceTable() {

        DefaultTableModel tmInvoice = (DefaultTableModel) invoiceTable.getModel();

        int rowIndex = 0;
        double totalPrice = 0;

        String str = (String) cbInvoice.getSelectedItem(); // get the string like "Table 1"
        str = str.replace("Tafel ", ""); // Change "Table 1" to "1"
        int tableId = Integer.parseInt(str); // Change "1" to 1 (int)

        for (Order o : tsManager.getInvoiceOrders(tableId)) {
            String consumptionName = "" + o.getConsumptionName(); //Name consumption
            String price = "€ " + o.getPrice(); //Price

            totalPrice += o.getPrice();
            invoiceTotalPrice.setText("€ " + totalPrice);

            Object[] row = new Object[]{consumptionName, price};
            //rows will be filled
            tmInvoice.insertRow(rowIndex, row);

            rowIndex += 1;
        }

    }

    private void clearAllTables() {
        DefaultTableModel tmDrinks = (DefaultTableModel) drinksTable.getModel();
        DefaultTableModel tmMeals = (DefaultTableModel) mealsTable.getModel();
        DefaultTableModel tmOrders = (DefaultTableModel) ordersTable.getModel();

        //Clears all tables
        clearTable(tmDrinks);
        clearTable(tmMeals);
        clearTable(tmOrders);
    }

    private void clearTable(DefaultTableModel tm) {
        int rowCount = tm.getRowCount();
        //Function to clear a table
        for (int i = 0; i < rowCount; i++) {
            tm.removeRow(0);
        }
    }

    private void updateOrderStatus() {
        String str = (String) cbTable.getSelectedItem(); // get a string like "Table 1"
        str = str.replace("Tafel ", ""); // Change "Table 1" to "1"
        int tableId = Integer.parseInt(str); // Change "1" to 1 (int)     

        try {
            int employeeId = Integer.parseInt(employeeField.getText());
            //Get employee
            // default value
            ConsumptionType ct = Order.ConsumptionType.DRINK;

            if (radioMeal.isSelected() == true) {
                ct = Order.ConsumptionType.MEAL;
            } //If meal radiobutton is selected then the type will be put on MEAL,
            //and otherwise it will stay on DRINK

            tsManager.updateOrdersStatus(ct, tableId, employeeId);
        } catch (NumberFormatException nfe) {
            employeeField.setText("Foute werknemerscode");
        }

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
        drinkLabel = new javax.swing.JLabel();
        mealLabel = new javax.swing.JLabel();
        employeeField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        oldTableField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        newTableField = new javax.swing.JTextField();
        updateTableButton = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
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
        jLabel6 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        ordersTable = new javax.swing.JTable();
        refreshButton3 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        testAppetizerButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        testMaindishButton = new javax.swing.JButton();
        testTableButton = new javax.swing.JButton();
        testInvoiceButton = new javax.swing.JButton();

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

        drinkLabel.setText("Drankjes");

        mealLabel.setText("Gerechten");

        employeeField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeFieldActionPerformed(evt);
            }
        });

        jLabel3.setText("Medewerker");

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo_HH.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(135, 135, 135)
                        .addComponent(drinkLabel)
                        .addGap(374, 374, 374)
                        .addComponent(mealLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(refreshButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(77, 77, 77)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(60, 60, 60)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(radioDrink, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(radioMeal, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addComponent(cbTable, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(33, 33, 33)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(employeeField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(handleButton)))
                        .addGap(0, 70, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(drinkLabel)
                        .addComponent(mealLabel))
                    .addComponent(refreshButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(radioDrink)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radioMeal)
                        .addGap(13, 13, 13)
                        .addComponent(cbTable, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(employeeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(handleButton))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 219, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Overzicht", jPanel1);

        jLabel8.setText("Verander tafel ");

        oldTableField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                oldTableFieldActionPerformed(evt);
            }
        });

        jLabel9.setText("naar tafel");

        newTableField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newTableFieldActionPerformed(evt);
            }
        });

        updateTableButton.setText("Update");
        updateTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateTableButtonActionPerformed(evt);
            }
        });

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo_HH.png"))); // NOI18N

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
                        .addComponent(oldTableField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(newTableField, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addComponent(updateTableButton)))
                .addContainerGap(729, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(oldTableField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(newTableField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(updateTableButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 177, Short.MAX_VALUE)
                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
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

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo_HH.png"))); // NOI18N

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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 126, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addComponent(refreshButton2)
                        .addContainerGap())
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(invoiceHandle)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Logo_HH.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 90, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(refreshButton3)
                        .addContainerGap())
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 425, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(refreshButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 175, Short.MAX_VALUE))))
        );

        jTabbedPane1.addTab("Status Bestellingen", jPanel4);

        testAppetizerButton.setText("Zet Voorgerecht op \"Wordt geserveerd\"");
        testAppetizerButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testAppetizerButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Deze tests worden gedaan op OrderID's 20 en 21, wat bestaat uit 2 voorgerechten met drinken, en 2 hoofdgerechten met drinken.");

        testMaindishButton.setText("Zet Hoofdgerechten op \"Wordt geserveerd\"");
        testMaindishButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testMaindishButtonActionPerformed(evt);
            }
        });

        testTableButton.setText("Zet Tafel terug naar tafel 20");
        testTableButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testTableButtonActionPerformed(evt);
            }
        });

        testInvoiceButton.setText("Zet tafel 20 op wil afrekenen.");
        testInvoiceButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                testInvoiceButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(testInvoiceButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(testTableButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(testAppetizerButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(testMaindishButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(307, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(testAppetizerButton)
                .addGap(18, 18, 18)
                .addComponent(testMaindishButton)
                .addGap(18, 18, 18)
                .addComponent(testTableButton)
                .addGap(18, 18, 18)
                .addComponent(testInvoiceButton)
                .addContainerGap(424, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Tests ", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 982, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 641, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void oldTableFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_oldTableFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_oldTableFieldActionPerformed

    private void refreshButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshButtonActionPerformed
        refreshUIContent();
    }//GEN-LAST:event_refreshButtonActionPerformed

    private void newTableFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newTableFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_newTableFieldActionPerformed

    private void updateTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateTableButtonActionPerformed

        int OldTableNumber = Integer.parseInt(oldTableField.getText());
        int NewTableNumber = Integer.parseInt(newTableField.getText());
        tsManager.changeTable(OldTableNumber, NewTableNumber);

    }//GEN-LAST:event_updateTableButtonActionPerformed

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
        DefaultTableModel tmInvoice = (DefaultTableModel) invoiceTable.getModel();
        clearTable(tmInvoice);
        invoiceTotalPrice.setText("");

        String str = (String) cbInvoice.getSelectedItem(); // get string like "Table 1"
        str = str.replace("Tafel ", ""); // Change "Table 1" to "1"
        int tableId = Integer.parseInt(str); // Change "1" to 1 (int)

        int totalPrice = 0;
        for (Order o : tsManager.getInvoiceOrders(tableId)) {

            totalPrice += o.getPrice();
        }

        tsManager.invoiceTable(tableId, totalPrice);
    }//GEN-LAST:event_invoiceHandleActionPerformed

    private void invoiceTableSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_invoiceTableSearchActionPerformed

        DefaultTableModel tmInvoice = (DefaultTableModel) invoiceTable.getModel();
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

    private void testAppetizerButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testAppetizerButtonActionPerformed
        tsManager.testAppetizer();
    }//GEN-LAST:event_testAppetizerButtonActionPerformed

    private void testMaindishButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testMaindishButtonActionPerformed
        tsManager.testMaindish();
    }//GEN-LAST:event_testMaindishButtonActionPerformed

    private void testTableButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testTableButtonActionPerformed
        tsManager.testTable();
    }//GEN-LAST:event_testTableButtonActionPerformed

    private void testInvoiceButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_testInvoiceButtonActionPerformed
        tsManager.testInvoice();
    }//GEN-LAST:event_testInvoiceButtonActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> cbInvoice;
    private javax.swing.JComboBox<String> cbTable;
    private javax.swing.JLabel drinkLabel;
    private javax.swing.JTable drinksTable;
    private javax.swing.JTextField employeeField;
    private javax.swing.JButton handleButton;
    private javax.swing.JButton invoiceHandle;
    private javax.swing.JTable invoiceTable;
    private javax.swing.JButton invoiceTableSearch;
    private javax.swing.JTextField invoiceTotalPrice;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel mealLabel;
    private javax.swing.JTable mealsTable;
    private javax.swing.JTextField newTableField;
    private javax.swing.JTextField oldTableField;
    private javax.swing.JTable ordersTable;
    private javax.swing.JRadioButton radioDrink;
    private javax.swing.JRadioButton radioMeal;
    private javax.swing.JButton refreshButton;
    private javax.swing.JButton refreshButton2;
    private javax.swing.JButton refreshButton3;
    private javax.swing.JButton testAppetizerButton;
    private javax.swing.JButton testInvoiceButton;
    private javax.swing.JButton testMaindishButton;
    private javax.swing.JButton testTableButton;
    private javax.swing.JButton updateTableButton;
    // End of variables declaration//GEN-END:variables
}

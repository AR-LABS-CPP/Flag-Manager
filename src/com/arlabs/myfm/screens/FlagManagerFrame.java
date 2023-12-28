package com.arlabs.myfm.screens;

import com.arlabs.myfm.clients.APIClient;
import com.arlabs.myfm.dbcontrollers.BindingDBController;
import com.arlabs.myfm.dbcontrollers.FlagManagerDBController;
import com.arlabs.myfm.interfaces.EntryAddedListener;
import com.arlabs.myfm.pojos.APIResponse;
import com.arlabs.myfm.pojos.FlagUpdateRequest;
import com.arlabs.myfm.services.APIService;
import com.arlabs.myfm.states.FlagManagerStates;
import com.arlabs.myfm.states.ProjectStates;
import com.arlabs.myfm.utils.MessageConsole;
import com.arlabs.myfm.utils.NativeLogger;
import com.arlabs.myfm.utils.NumberExtractor;
import com.arlabs.myfm.utils.TableUtils;
import java.awt.Color;
import java.awt.Component;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 *
 * @author AR-LABS
 */
public class FlagManagerFrame extends javax.swing.JFrame implements EntryAddedListener {

    private final FlagManagerDBController flagManagerDbController;
    private final BindingDBController bindingDbController;
    private final DefaultTableModel flagsTableModel;
    private final DefaultTableModel webhooksTableModel; 
    private final DefaultTableModel bindingsTableModel;
    private final DefaultTableModel requestsTableModel;
    private final APIService apiService;
    
    private final MessageConsole messageConsole;
    
    private final CreateBindingDialog createBindingDialog;
    
    public FlagManagerFrame() {
        initComponents();
        
        apiService = APIClient.getInstance().getAPIService();
        
        flagManagerDbController = new FlagManagerDBController();
        bindingDbController = new BindingDBController();
        
        messageConsole = new MessageConsole(this.txtLogs);
        messageConsole.redirectOut(null, System.out);
        
        lblProjectName.setText(ProjectStates.PROJECT_NAME);
        
        flagsTableModel = (DefaultTableModel) tblFlags.getModel();
        webhooksTableModel = (DefaultTableModel) tblWebhooks.getModel();
        bindingsTableModel = (DefaultTableModel) tblBindings.getModel();
        requestsTableModel = (DefaultTableModel) tblRequests.getModel();
        
        tblRequests.getColumnModel().getColumn(1)
                .setCellRenderer(getRequestStatusRenderer());
        
        TableUtils.centerTableCellAndHeader(tblFlags);
        TableUtils.centerTableCellAndHeader(tblWebhooks);
        TableUtils.centerTableCellAndHeader(tblBindings);
        TableUtils.centerTableCellAndHeader(tblRequests);
        
        createBindingDialog = new CreateBindingDialog(this, true, bindingDbController, this);
        
        SwingUtilities.invokeLater(() -> {
            populateFlagsTable();
            populateWebhooksTable();
            populateBindingsTable();
        });
    }
    
    private void populateFlagsTable() {
        flagsTableModel.setRowCount(0);
        
        FlagManagerStates.FLAGS_LIST = flagManagerDbController.getAllFlags(ProjectStates.PROJECT_ID);
        
        for(Map.Entry<Integer, List<Object>> data : FlagManagerStates.FLAGS_LIST.entrySet()) {
            flagsTableModel.addRow(new Object[] {
                data.getKey(),
                data.getValue().get(0),
                data.getValue().get(1),
            });
        }
    }
    
    private void populateWebhooksTable() {
        webhooksTableModel.setRowCount(0);
        
        FlagManagerStates.WEBHOOKS_LIST = flagManagerDbController.getAllWebhooks(ProjectStates.PROJECT_ID);
        
        for(Map.Entry<Integer, List<Object>> data : FlagManagerStates.WEBHOOKS_LIST.entrySet()) {
            webhooksTableModel.addRow(new Object[] {
                data.getKey(),
                data.getValue().get(0),
                data.getValue().get(1),
            });
        }
    }
    
    private void populateBindingsTable() {
        bindingsTableModel.setRowCount(0);
        
        FlagManagerStates.BINDINGS_LIST = bindingDbController.getAllBindings(ProjectStates.PROJECT_ID);
        
        for(Map.Entry<Object, List<Object>> entry : FlagManagerStates.BINDINGS_LIST.entrySet()) {
            bindingsTableModel.addRow(new Object[] {
                entry.getKey(),
                entry.getValue()
            });
            
            // Populate the inverse bindings
            for(Object flag : entry.getValue()) {
                FlagManagerStates.INVERSE_BINDINGS
                        .put(Integer.valueOf(flag.toString()), Integer.valueOf(entry.getKey().toString()));
            }
        }
    }
    
    @Override
    public void entryAdded(int webhookId, List<Integer> flagIds) {
        bindingsTableModel.addRow(new Object[] { webhookId, flagIds });
        
        // Will switch to a better data structure for more optimal representation
        for(int flagId : flagIds) {
            FlagManagerStates.INVERSE_BINDINGS.put(flagId, webhookId);
        }
    }
    
    private TableCellRenderer getRequestStatusRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column
            ) {
                Component tableCellRendererComponent = 
                        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                ((DefaultTableCellRenderer) tableCellRendererComponent)
                        .setHorizontalAlignment(SwingConstants.CENTER);
                ((DefaultTableCellRenderer) tableCellRendererComponent)
                        .setForeground(Color.WHITE);
                
                if("Success".equals(value)) {
                    tableCellRendererComponent.setBackground(new Color(0, 128, 0));
                }
                else if("Not Found".equals(value)) {
                    tableCellRendererComponent.setBackground(new Color(30, 144, 255));
                }
                else if("Timed Out".equals(value)) {
                    tableCellRendererComponent.setBackground(new Color(255, 165, 0));
                }
                else {
                    tableCellRendererComponent.setBackground(new Color(255, 0, 0));
                }
                
                return tableCellRendererComponent;
            }
        };
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblProjectName = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFlags = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        btnCreateFlag = new javax.swing.JButton();
        btnEditFlag = new javax.swing.JButton();
        btnDeleteFlag = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblWebhooks = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        btnCreateBinding = new javax.swing.JButton();
        btnDeleteBinding = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnCreateWebhook = new javax.swing.JButton();
        btnEditWebhook = new javax.swing.JButton();
        btnDeleteWebhook = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblBindings = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtLogs = new javax.swing.JTextArea();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblRequests = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuMain = new javax.swing.JMenu();
        mnuItemExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Flag Manager");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblProjectName.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblProjectName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblProjectName.setText("Project Name");
        lblProjectName.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Flag List"));

        tblFlags.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Flag Id", "Flag Name", "Flag Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFlags.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tblFlags);

        btnCreateFlag.setText("Create Flag");
        btnCreateFlag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateFlagActionPerformed(evt);
            }
        });
        jPanel5.add(btnCreateFlag);

        btnEditFlag.setText("Edit Flag");
        btnEditFlag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditFlagActionPerformed(evt);
            }
        });
        jPanel5.add(btnEditFlag);

        btnDeleteFlag.setText("Delete Flag");
        btnDeleteFlag.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteFlagActionPerformed(evt);
            }
        });
        jPanel5.add(btnDeleteFlag);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 437, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Webooks & Binding Table"));

        tblWebhooks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Webhook Id", "Webhook Name", "Webhook Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(tblWebhooks);

        btnCreateBinding.setText("Create Binding");
        btnCreateBinding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateBindingActionPerformed(evt);
            }
        });
        jPanel3.add(btnCreateBinding);

        btnDeleteBinding.setText("Delete Binding");
        btnDeleteBinding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteBindingActionPerformed(evt);
            }
        });
        jPanel3.add(btnDeleteBinding);

        btnCreateWebhook.setText("Create Webhook");
        btnCreateWebhook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreateWebhookActionPerformed(evt);
            }
        });
        jPanel4.add(btnCreateWebhook);

        btnEditWebhook.setText("Edit Webhook");
        btnEditWebhook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditWebhookActionPerformed(evt);
            }
        });
        jPanel4.add(btnEditWebhook);

        btnDeleteWebhook.setText("Delete Webhook");
        btnDeleteWebhook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteWebhookActionPerformed(evt);
            }
        });
        jPanel4.add(btnDeleteWebhook);

        tblBindings.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Webhook", "Flags"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblBindings);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Logs"));

        txtLogs.setColumns(20);
        txtLogs.setRows(5);
        txtLogs.setWrapStyleWord(true);
        txtLogs.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane3.setViewportView(txtLogs);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Requests"));

        tblRequests.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "URL", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRequests.setShowGrid(true);
        jScrollPane4.setViewportView(tblRequests);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 425, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mnuMain.setText("File");

        mnuItemExit.setText("Exit");
        mnuItemExit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                mnuItemExitMousePressed(evt);
            }
        });
        mnuMain.add(mnuItemExit);

        jMenuBar1.add(mnuMain);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblProjectName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 3, Short.MAX_VALUE)
                                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblProjectName, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        NativeLogger.logInfo("[JFrame]: window closing");
        flagManagerDbController.closeDbConnection();
    }//GEN-LAST:event_formWindowClosing

    private void btnCreateFlagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateFlagActionPerformed
        String flagName = JOptionPane.showInputDialog("Enter flag name");
        String flagValue = JOptionPane.showInputDialog("Enter flag value");
        
        if(flagName.isBlank() || flagValue.isBlank()) {
            JOptionPane.showMessageDialog(this, "Flag name & value, both are required");
            return;
        }
        
        int newFlagId = flagManagerDbController
                .addNewFlag(ProjectStates.PROJECT_ID, flagName, flagValue);
        
        if(newFlagId != -1) {
            JOptionPane.showMessageDialog(this, "New flag added successfully");
        }
        
        flagsTableModel.addRow(new Object[] { newFlagId, flagName, flagValue });
    }//GEN-LAST:event_btnCreateFlagActionPerformed

    private void btnCreateWebhookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateWebhookActionPerformed
        String webhookName = JOptionPane.showInputDialog("Enter webhook name");
        String webhookValue = JOptionPane.showInputDialog("Enter webhook value");
        
        if(webhookName.isBlank() || webhookValue.isBlank()) {
            JOptionPane.showMessageDialog(this, "Webhook name & value, both are required");
            return;
        }
        
        int newWebhookId = flagManagerDbController
                .addWebhook(ProjectStates.PROJECT_ID, webhookName, webhookValue);
        
        if(newWebhookId != -1) {
            JOptionPane.showMessageDialog(this, "New webhook added successfully");
        }
        
        webhooksTableModel.addRow(new Object[] { newWebhookId, webhookName, webhookValue });
    }//GEN-LAST:event_btnCreateWebhookActionPerformed

    private void btnDeleteFlagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteFlagActionPerformed
        if(tblFlags.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flag to delete");
            return;
        }
        
        if(flagManagerDbController
                .deleteFlag(tblFlags.getValueAt(tblFlags.getSelectedRow(), 0).toString())) {
            JOptionPane.showMessageDialog(this, "Flag deleted!");
            flagsTableModel.removeRow(tblFlags.getSelectedRow());
        }
    }//GEN-LAST:event_btnDeleteFlagActionPerformed

    private void btnEditFlagActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditFlagActionPerformed
        if(tblFlags.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flag to edit");
            return;
        }
        
        String flagName = JOptionPane.showInputDialog("Enter new flag name");
        String flagValue = JOptionPane.showInputDialog("Enter new flag value");
        
        if(flagName == null || flagName.isBlank() || flagValue == null || flagValue.isBlank()) {
            JOptionPane.showMessageDialog(this, "Flag name & value, both are required");
            return;
        }
        
        if(flagManagerDbController.editFlag(tblFlags.getValueAt(tblFlags.getSelectedRow(), 0).toString(), flagName, flagValue)) {
            JOptionPane.showMessageDialog(this, "Flag edited!");
            flagsTableModel.setValueAt(flagName, tblFlags.getSelectedRow(), 1);
            flagsTableModel.setValueAt(flagValue, tblFlags.getSelectedRow(), 2);
        }
        
        int flagId = Integer.parseInt(tblFlags.getValueAt(tblFlags.getSelectedRow(), 0).toString());
                
        if(FlagManagerStates.INVERSE_BINDINGS.get(flagId) != null) {
            int webhookId = FlagManagerStates.INVERSE_BINDINGS.get(flagId);
            
            String webhookValue = FlagManagerStates.WEBHOOKS_LIST.get(webhookId).get(1).toString();
        
            Call<APIResponse> postUpdateResponse = 
                    apiService.postUpdate(webhookValue, new FlagUpdateRequest(flagName, flagValue));
            
            postUpdateResponse.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> rspns) {
                    switch(rspns.code()) {
                        case 200 -> {
                            requestsTableModel.addRow(new Object[] { webhookValue, "Success" });
                            break;
                        }
                        
                        case 404 -> {
                            requestsTableModel.addRow(new Object[] { webhookValue, "Not Found"});
                            break;
                        }
                        
                        case 500 -> {
                            requestsTableModel.addRow(new Object[] { webhookValue, "Server Error"});
                            break;
                        }
                        
                        default -> {
                            requestsTableModel.addRow(new Object[] { webhookValue, "Unknown response" });
                            break;
                        }
                    }
                    
                    System.out.println(call);
                    System.out.println(rspns);
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable thrwbl) {
                    if(thrwbl instanceof ConnectException) {
                        requestsTableModel.addRow(new Object[] { webhookValue, "Timed Out"});
                    }
                    
                    System.out.println(thrwbl);
                }
            });
        }
    }//GEN-LAST:event_btnEditFlagActionPerformed

    private void btnEditWebhookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditWebhookActionPerformed
        if(tblWebhooks.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a webhook to edit");
            return;
        }
        
        String webhookName = JOptionPane.showInputDialog("Enter new webhook name");
        String webhookValue = JOptionPane.showInputDialog("Enter new webhook value");
        
        if(webhookName.isBlank() || webhookValue.isBlank()) {
            JOptionPane.showMessageDialog(this, "webhook name & value, both are required");
            return;
        }
        
        String webhookId = tblWebhooks.getValueAt(tblWebhooks.getSelectedRow(), 0).toString();
        
        if(flagManagerDbController
                .editWebhook(webhookId, webhookName, webhookValue)) {
            JOptionPane.showMessageDialog(this, "Flag edited!");
            webhooksTableModel.setValueAt(webhookName, tblWebhooks.getSelectedRow(), 1);
            webhooksTableModel.setValueAt(webhookValue, tblWebhooks.getSelectedRow(), 2);
        }
        
        List<Object> webhookValues = new ArrayList<>(FlagManagerStates.WEBHOOKS_LIST.get(Integer.valueOf(webhookId)));
        webhookValues.set(1, webhookValue);
        
        FlagManagerStates.WEBHOOKS_LIST.put(Integer.valueOf(webhookId), webhookValues);
    }//GEN-LAST:event_btnEditWebhookActionPerformed

    private void btnDeleteWebhookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteWebhookActionPerformed
        if(tblWebhooks.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a flag to delete");
            return;
        }
        
        if(flagManagerDbController
                .deleteWebhook(tblWebhooks.getValueAt(tblWebhooks.getSelectedRow(), 0).toString())) {
            JOptionPane.showMessageDialog(this, "Webhook deleted!");
            webhooksTableModel.removeRow(tblWebhooks.getSelectedRow());
        }
    }//GEN-LAST:event_btnDeleteWebhookActionPerformed

    private void mnuItemExitMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuItemExitMousePressed
        flagManagerDbController.closeDbConnection();
        this.dispose();
    }//GEN-LAST:event_mnuItemExitMousePressed

    private void btnCreateBindingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreateBindingActionPerformed
        createBindingDialog.setVisible(true);
    }//GEN-LAST:event_btnCreateBindingActionPerformed

    private void btnDeleteBindingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteBindingActionPerformed
        if(tblBindings.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a binding to delete");
            return;
        }
        
        int webhook = Integer.parseInt(tblBindings.getValueAt(tblBindings.getSelectedRow(), 0).toString());
        List<Integer> flags = NumberExtractor.extractNumbers(tblBindings.getValueAt(tblBindings.getSelectedRow(), 1).toString());
        
        if(bindingDbController.deleteMultipleBindings(Integer.parseInt(ProjectStates.PROJECT_ID), webhook, flags)) {
            JOptionPane.showMessageDialog(this, "Bindings deleted successfully");
        }
        
        for(int flag : flags) {
            if(FlagManagerStates.INVERSE_BINDINGS.get(flag) != null) {
                FlagManagerStates.INVERSE_BINDINGS.remove(flag);
            }
        }
        
        bindingsTableModel.removeRow(tblBindings.getSelectedRow());
    }//GEN-LAST:event_btnDeleteBindingActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateBinding;
    private javax.swing.JButton btnCreateFlag;
    private javax.swing.JButton btnCreateWebhook;
    private javax.swing.JButton btnDeleteBinding;
    private javax.swing.JButton btnDeleteFlag;
    private javax.swing.JButton btnDeleteWebhook;
    private javax.swing.JButton btnEditFlag;
    private javax.swing.JButton btnEditWebhook;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblProjectName;
    private javax.swing.JMenuItem mnuItemExit;
    private javax.swing.JMenu mnuMain;
    private javax.swing.JTable tblBindings;
    private javax.swing.JTable tblFlags;
    private javax.swing.JTable tblRequests;
    private javax.swing.JTable tblWebhooks;
    private javax.swing.JTextArea txtLogs;
    // End of variables declaration//GEN-END:variables

}

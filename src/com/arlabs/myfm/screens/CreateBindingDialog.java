package com.arlabs.myfm.screens;

import com.arlabs.myfm.dbcontrollers.BindingDBController;
import com.arlabs.myfm.interfaces.EntryAddedListener;
import com.arlabs.myfm.states.FlagManagerStates;
import com.arlabs.myfm.states.ProjectStates;
import com.arlabs.myfm.utils.NumberExtractor;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author AR-LABS
 */
public class CreateBindingDialog extends javax.swing.JDialog {
    
    private final BindingDBController bindingDbController;
    private final EntryAddedListener entryAddedListener;
    
    public CreateBindingDialog(java.awt.Frame parent,boolean modal,
            BindingDBController dbController,
            EntryAddedListener listener) {
        super(parent, modal);
        initComponents();
        
        this.bindingDbController = dbController;
        this.entryAddedListener = listener;
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtWebhookId = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtFlags = new javax.swing.JTextField();
        btnConfirmBinding = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Create Binding");
        setResizable(false);

        jLabel1.setText("Webhook Id");

        jLabel2.setText("Flag Ids (Comma Separated)");

        btnConfirmBinding.setText("Confirm");
        btnConfirmBinding.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfirmBindingActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtWebhookId)
                    .addComponent(txtFlags)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addGap(0, 227, Short.MAX_VALUE))
                    .addComponent(btnConfirmBinding, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtWebhookId, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(txtFlags, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnConfirmBinding, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnConfirmBindingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfirmBindingActionPerformed
        if(txtWebhookId.getText().isBlank() || txtFlags.getText().isBlank()) {
            JOptionPane.showMessageDialog(this, "Webhook Id & Flags list, both are required");
            return;
        }
        
        int webhookId = 0;
        List<Integer> flagIds = NumberExtractor.extractNumbers(txtFlags.getText().strip());
        
        for(int flagId : flagIds) {
            if(FlagManagerStates.INVERSE_BINDINGS.get(flagId) != null) {
                JOptionPane.showMessageDialog(this, "One or more given flag is already binded");
                return;
            }
        }
        
        try {
            webhookId = Integer.parseInt(txtWebhookId.getText().strip());
        }
        catch(NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid webhook id");
        }
        
        if(flagIds.isEmpty()) {
            JOptionPane.showMessageDialog(this, "At least one flag is required for binding");
        }
        
        if(bindingDbController.addMultipleBindings(Integer.parseInt(ProjectStates.PROJECT_ID), webhookId, flagIds)) {
            JOptionPane.showMessageDialog(this, "Bindings created successfully");
            
            entryAddedListener.entryAdded(webhookId, flagIds);
        }
    }//GEN-LAST:event_btnConfirmBindingActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnConfirmBinding;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField txtFlags;
    private javax.swing.JTextField txtWebhookId;
    // End of variables declaration//GEN-END:variables
}

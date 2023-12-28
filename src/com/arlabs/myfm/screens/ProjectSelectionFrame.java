package com.arlabs.myfm.screens;

import com.arlabs.myfm.dbcontrollers.ProjectSelectionDBController;
import com.arlabs.myfm.states.ProjectStates;
import com.arlabs.myfm.utils.NativeLogger;
import com.arlabs.myfm.utils.TableUtils;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author AR-LABS
 */
public class ProjectSelectionFrame extends javax.swing.JFrame {
    
    private final ProjectSelectionDBController projectSelectionDbController;
    private final DefaultTableModel projectsTableModel;
    
    private List<List<Object>> projectsList;
    
    public ProjectSelectionFrame() {
        initComponents();
        projectSelectionDbController = new ProjectSelectionDBController();
        
        projectsTableModel = (DefaultTableModel) tblProjectsList.getModel();
        
        setupPopupMenu();
        
        TableUtils.centerTableCellAndHeader(tblProjectsList);
        
        SwingUtilities.invokeLater(() -> refreshTable());
    }
    
    private void setupPopupMenu() {        
        tblProjectsList.setComponentPopupMenu(pmTableOptionsMenu);
    }
    
    private void refreshTable() {
        projectsTableModel.setRowCount(0);
        
        projectsList = projectSelectionDbController.getProjectsWithFlagAndWebhookCount();
        
        for(List<Object> data : projectsList) {
            projectsTableModel.addRow(new Object[] {
                data.get(0),
                data.get(1),
                data.get(2),
                data.get(3),
                data.get(4),
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pmTableOptionsMenu = new javax.swing.JPopupMenu();
        itemDeleteProject = new javax.swing.JMenuItem();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProjectsList = new javax.swing.JTable();
        btnAddNewProject = new javax.swing.JButton();
        btnDeleteProject = new javax.swing.JButton();

        itemDeleteProject.setText("Delete Project");
        itemDeleteProject.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                itemDeleteProjectMousePressed(evt);
            }
        });
        pmTableOptionsMenu.add(itemDeleteProject);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Project Selection");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 153, 153));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("My FM");

        tblProjectsList.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Project Id", "Project Name", "Project Creation Date", "Flag Count", "Webhook Count"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProjectsList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProjectsListMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblProjectsListMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblProjectsList);

        btnAddNewProject.setText("Add New Project");
        btnAddNewProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddNewProjectActionPerformed(evt);
            }
        });

        btnDeleteProject.setText("Delete Project");
        btnDeleteProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProjectActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnAddNewProject)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteProject)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 743, Short.MAX_VALUE))
                .addContainerGap())
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnAddNewProject, btnDeleteProject});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnAddNewProject)
                            .addComponent(btnDeleteProject))
                        .addGap(23, 23, 23)))
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 320, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        NativeLogger.logInfo("[JFrame]: window closing");
        projectSelectionDbController.closeDbConnection();
    }//GEN-LAST:event_formWindowClosing

    private void btnAddNewProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddNewProjectActionPerformed
        String projectName = JOptionPane.showInputDialog("Enter project name: ");
        
        if(projectName != null && !projectName.isBlank()) {
            int projectId = projectSelectionDbController.addNewProject(projectName);
            
            projectsTableModel.addRow(new Object[] {
                projectId,
                projectName,
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                0,
                0
            });
            
            projectsList.add(List.of(
                projectId,
                projectName,
                new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
                0,
                0
            ));
        }
    }//GEN-LAST:event_btnAddNewProjectActionPerformed

    private void tblProjectsListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProjectsListMouseClicked
        if(evt.getClickCount() > 1) {
            int row = tblProjectsList.getSelectedRow();
            ProjectStates.PROJECT_ID = projectsList.get(row).get(0).toString();
            ProjectStates.PROJECT_NAME = projectSelectionDbController
                    .getProjectName(ProjectStates.PROJECT_ID);
            
            new FlagManagerFrame().setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_tblProjectsListMouseClicked

    private void tblProjectsListMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProjectsListMousePressed
        Point point = evt.getPoint();
        int currentRow = tblProjectsList.rowAtPoint(point);
        
        tblProjectsList.setRowSelectionInterval(currentRow, currentRow);
    }//GEN-LAST:event_tblProjectsListMousePressed

    private void itemDeleteProjectMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itemDeleteProjectMousePressed
        projectSelectionDbController
                    .deleteProject(tblProjectsList
                            .getValueAt(tblProjectsList.getSelectedRow(), 0).toString());
        
        refreshTable();
    }//GEN-LAST:event_itemDeleteProjectMousePressed

    private void btnDeleteProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProjectActionPerformed
        if(tblProjectsList.getSelectedRow() == -1) {
            JOptionPane.showMessageDialog(this, "Please select a project to delete");
            return;
        }
        
        if(projectSelectionDbController.deleteProject(tblProjectsList
                .getValueAt(tblProjectsList.getSelectedRow(), 0).toString())) {
            JOptionPane.showMessageDialog(this, "Project deleted successfully");
        }
        
        projectsTableModel.removeRow(tblProjectsList.getSelectedRow());
    }//GEN-LAST:event_btnDeleteProjectActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddNewProject;
    private javax.swing.JButton btnDeleteProject;
    private javax.swing.JMenuItem itemDeleteProject;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu pmTableOptionsMenu;
    private javax.swing.JTable tblProjectsList;
    // End of variables declaration//GEN-END:variables
}

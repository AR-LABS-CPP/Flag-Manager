package com.arlabs.myfm.utils;

import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

/**
 *
 * @author AR-LABS
 */
public class TableUtils {
        public static void centerTableCellAndHeader(JTable tbl) {
        ((DefaultTableCellRenderer) tbl
                .getDefaultRenderer(String.class))
                .setHorizontalAlignment(0);
        
        JTableHeader tableHeader = tbl.getTableHeader();
        DefaultTableCellRenderer headerRenderer = 
                (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
    }
}

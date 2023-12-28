package com.arlabs.myfm.utils;

import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author AR-LABS
 */
public class ImageUtils {
    private static ImageIcon imageIcon;
    
    public static void setImageOnJLabel(JLabel component, String filePath) {
        try {
            File file = new File(filePath);

            if (file.exists()) {
                java.awt.Image image = javax.imageio.ImageIO.read(file);

                ImageUtils.imageIcon = new ImageIcon(image.getScaledInstance(
                        component.getWidth() - 20, component.getHeight() - 10, java.awt.Image.SCALE_SMOOTH));

                component.setIcon(ImageUtils.imageIcon);
            } else {
                JOptionPane.showMessageDialog(null, "File not found: " + filePath);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
    }
}

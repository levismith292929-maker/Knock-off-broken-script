package dev.unpredictable;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.GraphicsEnvironment;
/** Visible standard message dialogs only. */
public final class WindowsPopups {
    private WindowsPopups() {}
    public static void show(String title,String message) {
        if(!ConsentState.isAccepted() || !System.getProperty("os.name","").toLowerCase().contains("win") || GraphicsEnvironment.isHeadless())return;
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null,message,title,JOptionPane.INFORMATION_MESSAGE));
    }
}

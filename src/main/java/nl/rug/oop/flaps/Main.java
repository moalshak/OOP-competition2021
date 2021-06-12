package nl.rug.oop.flaps;

import com.formdev.flatlaf.FlatDarculaLaf;
import nl.rug.oop.flaps.simulation.model.world.World;
import nl.rug.oop.flaps.simulation.model.world.WorldInitializer;
import nl.rug.oop.flaps.simulation.view.FlapsFrame;

import javax.swing.*;
import java.io.IOException;

/**
 *  This is the starting point of the application. It prepares the user
 *  interface by first calling {@link FlatDarculaLaf#install()} to set a better
 *  look and feel, then initializes the world and opens a new {@link FlapsFrame}.
 *
 * @author T.O.W.E.R.
 */
public class Main {
    public static void main(String[] args) {
        FlatDarculaLaf.install();
        System.setProperty("sun.java2d.opengl", "true");
        try {
            World world = new WorldInitializer().initializeWorld();
            SwingUtilities.invokeLater(() -> new FlapsFrame(world));
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "FLAPS encountered an unexpected error.",
                    "Program Crashed",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}

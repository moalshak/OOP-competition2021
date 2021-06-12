package nl.rug.oop.flaps.aircraft_editor.controller.errors;

import lombok.Getter;
import lombok.Setter;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditorFrame;

import javax.swing.*;
/**
 * this class uses {@link JOptionPane} to show user an error message indicating that the no more
 * passengers can be added. This happens <b> once </b> when exceeding. So the user does not get it repeatedly after
 * each passenger.
 * */
public class MaxSeatsExceeded extends JOptionPane {
    private static final String MAX_SEATS  = ("All seats are occupied! You will need to free seats afterwards for these passengers. ");
    private static final String title = "Too many passengers ";

    @Getter @Setter
    private static boolean warned = false;

    public MaxSeatsExceeded() {
        showErrorMessage();
    }
    /**
     * shows the error message
     * */
    public void showErrorMessage() {
        JOptionPane.showMessageDialog(EditorFrame.getFrame(), MAX_SEATS, title, JOptionPane.ERROR_MESSAGE);
        warned = true;
    }
}

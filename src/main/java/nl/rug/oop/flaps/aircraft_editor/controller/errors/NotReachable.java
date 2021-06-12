package nl.rug.oop.flaps.aircraft_editor.controller.errors;

import lombok.Getter;
import nl.rug.oop.flaps.aircraft_editor.view.frame.EditorFrame;

import javax.swing.*;

/**
 * this class uses {@link JOptionPane} to show the user that the aircraft cannot reach the selected destination
 * even if the tanks are full. The user still has the option to configure the airplane if they choose to.
 * */
public class NotReachable extends JOptionPane {
    private static final String NOT_REACHABLE  = ("Be advised, this airplane will never reach it's destination even with fully loaded tanks. " +
            "Please choose an aircraft with a higher fuel capacity or a closer destination to be able to depart.\n\n " +
            "Configure the aircraft anyways ? ");
    private static final String title = "Not enough fuel capacity ";
    @Getter
    private boolean configureAnyWay;


    public NotReachable() {
        configureAnyWay = false;
        showOptions();
    }
    /**
     * shows options to user and let them choose
     * */
    public void showOptions() {
        int res = JOptionPane.showOptionDialog(EditorFrame.getFrame(), NOT_REACHABLE,title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                new Object[] { "Yes", "No" }, JOptionPane.YES_OPTION);

        if (res == JOptionPane.YES_OPTION) {
            configureAnyWay = true;
            System.out.println("Selected Yes!");
        }
    }
}

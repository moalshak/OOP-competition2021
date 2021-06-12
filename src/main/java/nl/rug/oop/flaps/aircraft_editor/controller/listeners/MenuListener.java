package nl.rug.oop.flaps.aircraft_editor.controller.listeners;

import lombok.extern.java.Log;

import javax.swing.*;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Log
public class MenuListener implements ActionListener {

    public MenuListener(UndoManager manager, JMenuItem undoItem, JMenuItem redoItem) {

        //undoing an edit
        undoItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    manager.undo();
                } catch (CannotUndoException cannotUndoException) {
                    log.info("Nothing to undo");
                    JOptionPane.showMessageDialog(null,
                            "There is nothing left to undo",
                            "Nothing To Undo",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //redoing an edit
        redoItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    manager.redo();
                } catch (CannotRedoException cannotRedoException) {
                    log.info("Nothing to redo");
                    JOptionPane.showMessageDialog(null,
                            "There is nothing left to redo",
                            "Nothing To Redo",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }
}

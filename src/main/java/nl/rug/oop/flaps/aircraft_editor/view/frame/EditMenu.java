package nl.rug.oop.flaps.aircraft_editor.view.frame;

import lombok.Getter;
import nl.rug.oop.flaps.aircraft_editor.controller.listeners.MenuListener;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

/**
 * Creates the EditMenu that allows user to click on to undo or redo an action or use a command shortcut
 */
public class EditMenu extends JMenuBar implements UndoableEditListener {
    private final UndoManager manager;
    @Getter
    private static UndoableEditSupport undoSupport;

    public EditMenu() {
        //preparing undo manager for the edit menu
        manager = new UndoManager();
        undoSupport = new UndoableEditSupport();
        undoSupport.addUndoableEditListener(this);

        JMenu fileMenu = new JMenu("Edit");
        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));

        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));

        fileMenu.addActionListener(new MenuListener(manager, undoItem, redoItem));

        fileMenu.add(undoItem);
        fileMenu.add(redoItem);
        add(fileMenu);
    }

    @Override
    public void undoableEditHappened(UndoableEditEvent e) {
        UndoableEdit edit = e.getEdit();
        manager.addEdit(edit);
    }

}

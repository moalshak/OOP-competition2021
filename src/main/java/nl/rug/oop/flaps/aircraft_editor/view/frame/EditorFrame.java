package nl.rug.oop.flaps.aircraft_editor.view.frame;

import lombok.Getter;
import nl.rug.oop.flaps.aircraft_editor.controller.errors.NotReachable;
import nl.rug.oop.flaps.aircraft_editor.util.IsDestinationReachable;
import nl.rug.oop.flaps.aircraft_editor.view.panels.blueprint.BluePrintPanel;
import nl.rug.oop.flaps.aircraft_editor.view.panels.aircraft_info.InfoPanel;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.world.WorldSelectionModel;

import javax.swing.*;
import java.awt.*;

/**
 * The main frame in which the editor is be displayed.
 *
 * @author T.O.W.E.R.
 */
@Getter
public class EditorFrame extends JFrame {
    @Getter
    private static final int WIDTH = 1600;
    @Getter
    private static final int HEIGHT = 800;

    private static JSplitPane mainSplitPane;
    @Getter
    private static JFrame frame;

    public EditorFrame(Aircraft aircraft, WorldSelectionModel selectionModel) {
        super("Aircraft Editor");
        /* if destination is not reachable with even full tanks. The user is asked if they want to configure it anyways*/
        if(!IsDestinationReachable.isDestinationReachable(aircraft, selectionModel)) {
            NotReachable choice = new NotReachable();
            if(!choice.isConfigureAnyWay()) {
                return;
            }
        }

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame = this;
        BluePrintPanel bluePrintPanel = new BluePrintPanel(aircraft);
        new InfoPanel(aircraft, selectionModel, bluePrintPanel);

        /* splits the blueprint part and the info panel part */
        mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                bluePrintPanel.getBluePrintScrollPane(),
                InfoPanel.getInfoSplitPane());
        mainSplitPane.setOneTouchExpandable(false);
        mainSplitPane.setDividerLocation(BluePrintPanel.getWIDTH()+10);

        this.add(mainSplitPane, BorderLayout.CENTER);

        addMenuBar();

        setExtendedState(JFrame.MAXIMIZED_BOTH);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

    }

    /**
     * adds a {@link MenuBar} on top of the frame
     * */
    private void addMenuBar() {
        EditMenu editMenu = new EditMenu();
        frame.setJMenuBar(editMenu);
    }

}

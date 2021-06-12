package nl.rug.oop.flaps.simulation.view.panels.airport;

import nl.rug.oop.flaps.simulation.controller.SpriteClickListener;
import nl.rug.oop.flaps.simulation.model.aircraft.Aircraft;
import nl.rug.oop.flaps.simulation.model.aircraft.AircraftType;
import nl.rug.oop.flaps.simulation.model.world.World;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * A special type of label which shows a small sprite of an aircraft's type, and
 * the aircraft's identifier as the label text.
 * <p>
 *     Uses the {@link AircraftIconLabel#iconCache} to cache loaded aircraft
 *     sprites during runtime, so that we don't need to waste time preparing
 *     smoothly-scaled images each time a label is constructed.
 * </p>
 * <p>
 *     Icons are set to have a fixed height according to {@link AircraftIconLabel#ICON_HEIGHT}
 *     but their width is dynamic so that the aspect ratio of the original image
 *     is preserved.
 * </p>
 *
 * @author T.O.W.E.R.
 */
public class AircraftIconLabel extends JLabel {
    private static final int ICON_HEIGHT = 40;

    private static final Map<AircraftType, ImageIcon> iconCache = new HashMap<>();

    public AircraftIconLabel(Aircraft aircraft, World world) {
        super(aircraft.getIdentifier());
        setIcon(loadIcon(aircraft.getType()));
        setVerticalTextPosition(JLabel.BOTTOM);
        setHorizontalTextPosition(JLabel.CENTER);
        addMouseListener(new SpriteClickListener(aircraft, world));
    }

    public void select() {
        setFont(new Font(Font.SANS_SERIF, Font.BOLD, this.getFont().getSize() + 2));
    }

    /**
     * Fetches an icon for an aircraft type from the cache, loading it for the
     * first time if necessary.
     * @param type The type to get an icon for.
     * @return The icon to use for the aircraft type.
     */
    private static ImageIcon loadIcon(AircraftType type) {
        if (!iconCache.containsKey(type)) {
            Image image = type.getSpriteImage();
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            double scaleFactor = ICON_HEIGHT / (double) height;
            iconCache.put(type, new ImageIcon(image.getScaledInstance((int) (width * scaleFactor), ICON_HEIGHT, Image.SCALE_SMOOTH)));
        }
        return iconCache.get(type);
    }
}

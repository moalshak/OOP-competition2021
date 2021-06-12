package nl.rug.oop.flaps.simulation.model.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.java.Log;
import nl.rug.oop.flaps.simulation.model.airport.Airport;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 * This class is responsible for loading the set of airports from various YAML
 * files and other resources.
 *
 * @author T.O.W.E.R.
 */
@Log
public class AirportLoader {
	private static final Path AIRPORTS_PATH = Path.of("data", "airports");
	private static final String YAML_FILE_PATTERN = "glob:**airport.yaml";
	private static final String BANNER_FILE_PATTERN = "glob:**banner.*";

	private final ObjectMapper mapper;

	public AirportLoader() {
		this(new ObjectMapper(new YAMLFactory()));
	}

	public AirportLoader(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public Set<Airport> loadAirports() throws IOException {
		Set<Airport> airports = new HashSet<>();
		for (var airportFile : FileUtils.findMatches(AIRPORTS_PATH, YAML_FILE_PATTERN)) {
			Airport airport = this.mapper.readValue(airportFile.toFile(), Airport.class);
			var optionalBannerFile = FileUtils.findMatch(airportFile.getParent(), BANNER_FILE_PATTERN);
			optionalBannerFile.ifPresent(imagePath -> new Thread(() -> {
				int attempts = 10; // Try this many times to read the file (due to weird JDK concurrency issues with AWT graphics).
				boolean success = false;
				while (attempts > 0) {
					try { // Load images in a separate thread, to speed things up.
						airport.setBannerImage(ImageIO.read(imagePath.toFile()));
						success = true;
						break;
					} catch (Exception e) {
						attempts--;
					}
				}
				if (!success) {
					log.warning("Could not load banner image for " + airport + " at " + imagePath);
				}
			}).start());
			airports.add(airport);
		}
		return airports;
	}
}

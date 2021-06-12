package nl.rug.oop.flaps.simulation.model.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.extern.java.Log;
import nl.rug.oop.flaps.simulation.model.aircraft.AircraftType;
import nl.rug.oop.flaps.simulation.model.aircraft.FuelType;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utility for loading aircraft types from configuration files.
 *
 * @author T.O.W.E.R.
 */
@Log
public class AircraftTypeLoader {
	private static final String YAML_FILE_PATTERN = "glob:**type.yaml";
	private static final String BANNER_FILE_PATTERN = "glob:**banner.*";
	private static final String BLUEPRINT_FILE_PATTERN = "glob:**blueprint.*";
	private static final String SPRITE_FILE_PATTERN = "glob:**sprite.*";
	private static final String TAKEOFF_FILE_NAME = "takeoff.wav";


	private static final Path AIRCRAFT_TYPES_PATH = Path.of("data", "aircraft_types");
	private static final Path FUEL_TYPES_PATH = Path.of("data", "fuel");

	private final ObjectMapper mapper;

	private Map<String, FuelType> fuelTypes;

	public AircraftTypeLoader() {
		this(new ObjectMapper(new YAMLFactory()));
	}

	public AircraftTypeLoader(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	/**
	 * Loads a set of aircraft types from a base path. This method uses
	 * {@link Files#walk(Path, FileVisitOption...)} to recursively collect a
	 * stream of all files in the given path, which we can then filter with a
	 * path matcher to obtain only YAML configuration files.
	 * @return The set of aircraft types that were found.
	 */
	public Set<AircraftType> loadTypes() throws IOException {
		Set<AircraftType> types = new HashSet<>();
		for (var typeFile : FileUtils.findMatches(AIRCRAFT_TYPES_PATH, YAML_FILE_PATTERN)) {
			AircraftType type = this.mapper.readValue(typeFile.toFile(), AircraftType.class);
			type.setFuelType(this.getFuelTypes().get(type.getFuelTypeName()));
			this.loadAssets(type, typeFile);
			types.add(type);
		}
		return types;
	}

	private void loadAssets(AircraftType type, Path typeFile) throws IOException {
		type.setBannerImage(ImageIO.read(FileUtils.findMatchOrThrow(typeFile.getParent(), BANNER_FILE_PATTERN).toFile()));
		type.setBlueprintImage(ImageIO.read(FileUtils.findMatchOrThrow(typeFile.getParent(), BLUEPRINT_FILE_PATTERN).toFile()));
		type.setSpriteImage(ImageIO.read(FileUtils.findMatchOrThrow(typeFile.getParent(), SPRITE_FILE_PATTERN).toFile()));
		Path takeoffClipPath = typeFile.resolveSibling(TAKEOFF_FILE_NAME);
		if (Files.exists(takeoffClipPath)) {
			type.setTakeoffClipPath(takeoffClipPath);
		}
	}

	/**
	 * Lazily fetches the fuel types that exist for this application.
	 * @return The mapping of fuel types.
	 */
	public Map<String, FuelType> getFuelTypes() {
		if (this.fuelTypes == null) {
			this.fuelTypes = new HashMap<>();
			var matcher = FileSystems.getDefault().getPathMatcher("glob:**.yaml");
			try (var paths = Files.walk(FUEL_TYPES_PATH)) {
				var fuelFiles = paths.filter(matcher::matches).collect(Collectors.toList());
				for (var fuelFile : fuelFiles) {
					FuelType type = this.mapper.readValue(fuelFile.toFile(), FuelType.class);
					this.fuelTypes.put(type.getName(), type);
				}
			} catch (IOException e) {
				log.warning("Exception while loading fuel types: " + e.getMessage());
			}
		}
		return this.fuelTypes;
	}
}

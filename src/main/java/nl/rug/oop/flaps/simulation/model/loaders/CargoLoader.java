package nl.rug.oop.flaps.simulation.model.loaders;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rug.oop.flaps.simulation.model.cargo.CargoType;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

/**
 *
 *
 * @author T.O.W.E.R.
 */
public class CargoLoader {
	private static final Path CARGO_TYPES_PATH = Path.of("data", "cargo");
	private static final String YAML_FILE_PATTERN = "glob:**cargo.yaml";
	private static final String TEXTURE_FILE_PATTERN = "glob:**texture.*";

	private final ObjectMapper mapper;

	public CargoLoader(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	public Set<CargoType> loadTypes() throws IOException {
		Set<CargoType> types = new HashSet<>();
		for (var typeFile : FileUtils.findMatches(CARGO_TYPES_PATH, YAML_FILE_PATTERN)) {
			CargoType type = this.mapper.readValue(typeFile.toFile(), CargoType.class);
			type.setImage(ImageIO.read(FileUtils.findMatchOrThrow(typeFile.getParent(), TEXTURE_FILE_PATTERN).toFile()));
			types.add(type);
		}
		return types;
	}
}

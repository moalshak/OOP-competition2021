package nl.rug.oop.flaps.simulation.model.loaders;

import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 *
 * @author T.O.W.E.R.
 */
@Log
public class FileUtils {
	public static List<Path> findMatches(Path directory, String pattern) throws IOException {
		var matcher = FileSystems.getDefault().getPathMatcher(pattern);
		try (var paths = Files.walk(directory)) {
			return paths.filter(matcher::matches).collect(Collectors.toList());
		}
	}

	public static Optional<Path> findMatch(Path directory, String pattern) throws IOException {
		var matcher = FileSystems.getDefault().getPathMatcher(pattern);
		try (var paths = Files.walk(directory)) {
			return paths.filter(matcher::matches).findAny();
		}
	}

	public static Path findMatchOrThrow(Path directory, String pattern) throws IOException {
		return findMatch(directory, pattern)
				.orElseThrow(() -> new IOException("No match found for pattern " + pattern + " in directory " + directory + "."));
	}
}

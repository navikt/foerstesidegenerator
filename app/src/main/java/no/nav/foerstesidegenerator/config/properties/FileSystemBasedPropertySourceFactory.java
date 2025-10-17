package no.nav.foerstesidegenerator.config.properties;

import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.stream.Stream;

public class FileSystemBasedPropertySourceFactory implements PropertySourceFactory {
	@Override
	public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
		String filename = resource.getResource().getFilename();
		if (filename != null) {
			Properties props = new Properties();
			if (resource.getResource().isFile()) {
				File file = resource.getResource().getFile();
				String basename = resource.getResource().getFile().getCanonicalPath();
				if (file.isDirectory()) {
					try (Stream<Path> filesInDirectoryTree = Files.walk(file.toPath())) {
						filesInDirectoryTree
								.filter(Files::isRegularFile)
								.forEach(path -> {
									try {
										String propertyName = convertPathToPropertyName(removeBasePath(path.toFile().getCanonicalPath(), basename));
										String value = Files.readString(path, StandardCharsets.UTF_8).trim();
										props.setProperty(propertyName, value);
									} catch (IOException e) {
										throw new RuntimeException(e);
									}
								});
					} catch (RuntimeException e) {
						if (e.getCause() instanceof IOException originalIoException) {
							throw originalIoException;
						}
						throw e;
					}
				} else {
					props.setProperty(convertPathToPropertyName(filename), new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8));
				}
			}
			return new org.springframework.core.env.PropertiesPropertySource(name != null ? name : filename, props);
		} else {
			throw new IOException("Filename can not be null!");
		}
	}

	private static String convertPathToPropertyName(String basePath) {
		return basePath.replace('/', '.').replace("_", ".");
	}

	private static String removeBasePath(String canonicalPath, String basename) {
		// dette fjerner implisitt en trailing slash som ikke er present i basename
		return canonicalPath.substring(basename.length() + 1);
	}


}

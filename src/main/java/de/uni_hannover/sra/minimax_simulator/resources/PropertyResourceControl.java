package de.uni_hannover.sra.minimax_simulator.resources;

import de.uni_hannover.sra.minimax_simulator.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.Map.Entry;
import java.util.ResourceBundle.Control;

/**
 * The {@code PropertyResourceControl} is used for handling property files.
 *
 * @see ResourceBundle.Control
 *
 * @author Martin L&uuml;ck
 */
public class PropertyResourceControl extends Control {

    private final String basePath;

    /**
     * Constructs a new {@code PropertyResourceControl} with the specified base path.
     *
     * @param basePath
     *          the base path
     */
    public PropertyResourceControl(String basePath) {
        if (!basePath.endsWith("/")) {
            basePath = basePath + "/";
        }
        this.basePath = basePath;
    }

    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
            boolean reload) throws IllegalAccessException, InstantiationException, IOException {
        if (baseName == null || locale == null || format == null || loader == null) {
            throw new NullPointerException();
        }

        String bundleName = toBundleName("", locale);
        if (bundleName.isEmpty()) {
            bundleName = "_base";
        }

        String bundleFile = basePath + "loc" + bundleName + "/" + baseName + ".properties";

        Map<String, Object> entries = new HashMap<>();

        InputStream is = null;
        try {
            is = this.getClass().getClassLoader().getResourceAsStream(bundleFile);
            if (is == null) {
                throw new FileNotFoundException("Resource not found: " + (new File(bundleFile)).getAbsolutePath());
            }
            parsePropertiesFile(is, entries);
        } finally {
            IOUtils.closeQuietly(is);
        }

        return new MapResourceBundle(entries);
    }

    /**
     * Parses a properties file as {@link InputStream} and puts the parsed properties to the
     * specified map.
     *
     * @param is
     *          the properties file as {@code InputStream}
     * @param map
     *          the map to hold the parsed properties
     * @throws IOException
     *          thrown if there was an IO error during parsing
     */
    protected static void parsePropertiesFile(InputStream is, Map<String, Object> map) throws IOException {
        Properties prop = new Properties();
        prop.load(is);
        for (Entry<Object, Object> e : prop.entrySet()) {
            map.put((String) e.getKey(), e.getValue());
        }
        prop.clear();
    }
}

/*
 * Copyright 2015 Digitising Scotland project:
 * <http://digitisingscotland.cs.st-andrews.ac.uk/>
 *
 * This file is part of the module ciesvium.
 *
 * ciesvium is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * ciesvium is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with ciesvium. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package uk.ac.standrews.cs.util.tools;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FileManipulation {

    public static final Charset FILE_CHARSET = StandardCharsets.UTF_8;

    private static final String JAR_PREFIX = "jar";
    private static final String FILE_PREFIX = "file";
    private static final int LENGTH_OF_FILE_PREFIX = (FILE_PREFIX + ":").length();


    public static InputStreamReader getInputStreamReader(Path path) throws IOException {

        InputStream input_stream = Files.newInputStream(path);
        return new InputStreamReader(input_stream, FILE_CHARSET);
    }

    public static OutputStreamWriter getOutputStreamWriter(Path path) throws IOException {

        OutputStream output_stream = Files.newOutputStream(path);
        return new OutputStreamWriter(output_stream, FILE_CHARSET);
    }

    public static Path getResourcePath(Class the_class, String resource_name) {

        URL resource = getResource(the_class, resource_name);
        try {
            return Paths.get(resource.toURI());

        } catch (URISyntaxException e) {
            throw new RuntimeException("invalid URI for resource path: " + e.getMessage());
        }
    }

    public static InputStreamReader getInputStreamReaderForResource(Class the_class, String resource_name) {

        return new InputStreamReader(getResourceAsStream(the_class, resource_name));
    }

    public static InputStream getResourceAsStream(Class the_class, String resource_name) {

        return the_class.getResourceAsStream(getResourceNamePrefixedWithClass(the_class, resource_name));
    }

    private static String getResourceNamePrefixedWithClass(Class the_class, String resource_name) {

        return the_class.getSimpleName() + "/" + resource_name;
    }

    public static void deleteDirectory(final String directory_path) throws IOException {

        deleteDirectory(Paths.get(directory_path));
    }

    public static void deleteDirectory(final Path directory) throws IOException {

        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(final Path file, final BasicFileAttributes attrs) throws IOException {

                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(final Path dir, final IOException exc) throws IOException {

                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

    public static void createFileIfDoesNotExist(final Path path) throws IOException {

        if (!Files.exists(path)) {

            createParentDirectoryIfDoesNotExist(path);
            Files.createFile(path);
        }
    }

    public static void createDirectoryIfDoesNotExist(final String directory_path) throws IOException {

        createDirectoryIfDoesNotExist(new File(directory_path));
    }

    public static void createDirectoryIfDoesNotExist(final File directory) throws IOException {

        createDirectoryIfDoesNotExist(Paths.get(directory.getAbsolutePath()));
    }

    public static void createDirectoryIfDoesNotExist(final Path path) throws IOException {

        Files.createDirectories(path);
    }

    public static void createParentDirectoryIfDoesNotExist(final Path path) throws IOException {

        Path parent_dir = path.getParent();
        if (parent_dir != null) {
            createDirectoryIfDoesNotExist(parent_dir);
        }
    }

    public static List<Path> getDirectoryEntries(final Path directory) throws IOException {

        List<Path> result = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
            for (Path entry : stream) {
                result.add(entry);
            }
        } catch (DirectoryIteratorException e) {
            throw e.getCause();
        }
        return result;
    }

    public static int countLinesInFile(final Path path) throws IOException {

        try (BufferedReader reader = Files.newBufferedReader(path, FILE_CHARSET)) {

            int count = 0;
            while (reader.readLine() != null) count++;
            return count;
        }
    }

    public static void assertThatFilesHaveSameContent(final Path path1, final Path path2) throws IOException {

        try (
                BufferedReader reader1 = Files.newBufferedReader(path1, FILE_CHARSET);
                BufferedReader reader2 = Files.newBufferedReader(path2, FILE_CHARSET)) {

            String line1;

            while ((line1 = reader1.readLine()) != null) {
                final String line2 = reader2.readLine();
                assertEquals(line1, line2);
            }
            assertNull(reader2.readLine());
        }
    }

    /**
     * Returns the top-level entries in the given resource directory.
     *
     * @param resource_directory_path the absolute path to a directory, with initial slash mapping to the root of the resource directory
     * @param class_loader
     */
    public static List<String> getResourceDirectoryEntries(String resource_directory_path, ClassLoader class_loader) throws IOException {

        final String relative_path = getRelativePath(resource_directory_path);
        final URL path_url = class_loader.getResource(relative_path);

        if (path_url != null) {

            final String path_url_protocol = path_url.getProtocol();

            if (path_url_protocol.equals(FILE_PREFIX)) {
                return getResourceDirectoryEntriesFromFileSystem(path_url);
            }

            if (path_url_protocol.equals(JAR_PREFIX)) {
                return getResourceDirectoryEntriesFromJar(relative_path, path_url);
            }
        }

        throw new IOException("can't access resource directory: " + resource_directory_path);
    }

    public static List<String> getResourceDirectoryEntries(Path resource_directory_path, ClassLoader class_loader) throws IOException {

        return getResourceDirectoryEntries(resource_directory_path.toString(), class_loader);
    }

    /**
     * Returns the top-level entries in the given resource directory.
     *
     * @param relative_directory_path the path to a directory relative to the root of the resource directory e.g. "directory/path/"
     * @param resource_directory_url  the URL for the directory, in the form "file:/absolute/path/of/jar!/directory/path/"
     */
    private static List<String> getResourceDirectoryEntriesFromJar(final String relative_directory_path, final URL resource_directory_url) throws IOException {

        JarFile jar_file = getJarFile(resource_directory_url);

        // Gets all entries in the jar file, including sub-directories and files.
        Enumeration<JarEntry> jar_entries = jar_file.entries();

        Set<String> entries = new HashSet<>();

        while (jar_entries.hasMoreElements()) {

            // The path of this entry relative to the resource root.
            final String entry_path = jar_entries.nextElement().getName();

            // Check whether this entry is a child of the specified resource directory.
            if (entry_path.startsWith(relative_directory_path) && !entry_path.equals(relative_directory_path)) {

                entries.add(getChildName(entry_path, relative_directory_path));
            }
        }

        return setToList(entries);
    }

    private static List<String> setToList(Set<String> entries) {

        return Arrays.asList(entries.toArray(new String[entries.size()]));
    }

    /**
     * @param entry_path              "a/b/c/d/e"
     * @param relative_directory_path e.g. "a/b/c"
     * @return the name of the element that is a child of the relative directory e.g. "d"
     */
    private static String getChildName(String entry_path, String relative_directory_path) {

        return firstPartOfPath(remainingPathAfter(entry_path, relative_directory_path.length()));
    }

    private static String firstPartOfPath(String path) {

        int i = path.indexOf("/");

        if (i == -1) {
            return path;
        } else {
            return path.substring(0, i);
        }
    }

    /**
     * @param resource_directory_url the URL for the directory, in the form "file:/absolute/path/of/jar!/directory/path/"
     */
    private static JarFile getJarFile(URL resource_directory_url) throws IOException {

        // String representation of the full path including absolute path of jar file and path to directory relative to resource root within jar.
        final String path = resource_directory_url.getPath();

        // Discard "file:" prefix and resource directory to give jar file path.
        final String absolute_path_of_jar_file = path.substring(LENGTH_OF_FILE_PREFIX, path.indexOf("!"));

        return new JarFile(URLDecoder.decode(absolute_path_of_jar_file, "UTF-8"));
    }

    private static List<String> getResourceDirectoryEntriesFromFileSystem(URL path_url) throws IOException {

        try {
            return Arrays.asList(new File(path_url.toURI()).list());

        } catch (URISyntaxException e) {
            throw new IOException("can't access resource URL: " + path_url);
        }
    }

    private static String getRelativePath(String resource_directory_path) {

        return resource_directory_path.startsWith("/") ? remainingPathAfter(resource_directory_path, 1) : resource_directory_path;
    }

    private static String remainingPathAfter(String resource_directory_path, int start_index) {

        return resource_directory_path.substring(start_index);
    }

    private static URL getResource(Class the_class, String resource_name) {

        return the_class.getResource(getResourceNamePrefixedWithClass(the_class, resource_name));
    }
}

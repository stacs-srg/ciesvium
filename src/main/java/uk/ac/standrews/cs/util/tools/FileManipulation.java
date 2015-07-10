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
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class FileManipulation {

    public static final Charset FILE_CHARSET = Charset.forName("UTF-8");

    public static InputStreamReader getInputStreamReader(Path path) throws IOException {

        InputStream input_stream = Files.newInputStream(path);
        return new InputStreamReader(input_stream, FILE_CHARSET);
    }

    public static OutputStreamWriter getOutputStreamWriter(Path path) throws IOException {

        OutputStream output_stream = Files.newOutputStream(path);
        return new OutputStreamWriter(output_stream, FILE_CHARSET);
    }

    public static String getResourceFilePath(Class the_class, String resource_name) {

        URL resource = getResource(the_class, resource_name);
        return resource.getFile();
    }

    public static File getResourceFile(Class the_class, String resource_name) {

        URL resource = getResource(the_class, resource_name);
        return new File(resource.getFile());
    }

    public static Path getResourcePath(Class the_class, String resource_name) {

        URL resource = getResource(the_class, resource_name);
        return Paths.get(resource.getFile());
    }

    public static URL getResource(Class the_class, String resource_name) {

        return the_class.getResource(getResourceNamePrefixedWithClass(the_class, resource_name));
    }

    public static InputStreamReader getInputStreamReaderForResource(Class the_class, String resource_name) {

        return new InputStreamReader(getResourceAsStream(the_class, resource_name));
    }

    public static InputStream getResourceAsStream(Class the_class, String resource_name) {

        return the_class.getResourceAsStream(getResourceNamePrefixedWithClass(the_class, resource_name));
    }

    protected static String getResourceNamePrefixedWithClass(Class the_class, String resource_name) {

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
}

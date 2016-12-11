/*
 * Copyright 2016 Digitising Scotland project:
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

import org.junit.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

public class FileManipulationTest {

    private static final int DIRECTORY_COUNT = 10;
    private static final int FILE_PER_DIRECTORY_COUNT = 50;
    public static final String TEMP_FILE_TREE_ROOT = "src/test/resources/temp_file_tree";
    private static final Random RANDOM = new Random(42);

    private String temp_file_tree_root;

    @Before
    public void setUp() throws IOException {

        temp_file_tree_root = TEMP_FILE_TREE_ROOT + RANDOM.nextInt();
        createFileTree();
        assertTreeContainsExpectedNumberOfDirectories();
    }

    @After
    public void tearDown() throws IOException {

        assertNotExists(temp_file_tree_root);
    }

    @Test
    public void createAndDeleteFileTree() throws IOException {

        FileManipulation.deleteDirectory(temp_file_tree_root);
    }

    private void createFileTree() throws IOException {

        createRoot();

        for (int i = 0; i < DIRECTORY_COUNT; i++) {

            final String random_dir_name = String.valueOf(RANDOM.nextLong());

            final File sub_directory = new File(temp_file_tree_root, random_dir_name);
            createSubDirectory(sub_directory);

            for (int j = 0; j < FILE_PER_DIRECTORY_COUNT; j++) {

                final File file = new File(sub_directory, String.valueOf(RANDOM.nextLong()));
                try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(file))) {

                    final byte[] random_content = new byte[0xFFF];
                    RANDOM.nextBytes(random_content);
                    out.write(random_content);
                }
            }

            assertNumberOfEntries(sub_directory, FILE_PER_DIRECTORY_COUNT);
        }
    }

    private void assertNumberOfEntries(File directory, int count) {

        File[] entries = directory.listFiles();
        assertNotNull(entries);
        assertEquals(count, entries.length);
    }

    private void createSubDirectory(File sub_directory) {

        sub_directory.mkdirs();
        assertExists(sub_directory);
    }

    private void assertTreeContainsExpectedNumberOfDirectories() {

        assertNumberOfEntries(new File(temp_file_tree_root), DIRECTORY_COUNT);
    }

    private void assertExists(String directory_path) {

        assertExists(new File(directory_path));
    }

    private void assertNotExists(String directory_path) {

        if (new File(directory_path).exists()) fail();
    }

    private void assertExists(File directory) {

        if (!directory.exists()) fail();
    }

    private void createRoot() throws IOException {

        Files.createDirectories(Paths.get(temp_file_tree_root));
        assertExists(temp_file_tree_root);
    }
}

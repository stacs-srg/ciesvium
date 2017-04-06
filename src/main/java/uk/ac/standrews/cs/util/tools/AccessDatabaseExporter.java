/*
 * Copyright 2017 Systems Research Group, University of St Andrews:
 * <https://github.com/stacs-srg>
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

import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.util.ExportUtil;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Allows a MS Access database to be dumped to plain text files.
 *
 * @see <a href="http://jackcess.sourceforge.net">http://jackcess.sourceforge.net</a>
 */
public class AccessDatabaseExporter {

    private static final String FIELD_SEPARATOR = ",";
    static final String FILE_SUFFIX = "csv";

    private ExportUtil.Builder builder;
    private File export_directory;

    /**
     * Dumps all the tables in the given Access database to plain text files.
     *
     * @param args the path to the database file, followed by the path to the output directory.
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public static void main(final String[] args) throws IOException {

        new AccessDatabaseExporter(args[0], args[1]).exportDatabase();
    }

    AccessDatabaseExporter(String database_path, String output_directory_path) throws IOException {

        Database database = DatabaseBuilder.open(new File(database_path));

        builder = new ExportUtil.Builder(database).setDelimiter(FIELD_SEPARATOR).setFileNameExtension(FILE_SUFFIX).setHeader(true);
        export_directory = Paths.get(output_directory_path).toFile();
    }

    void exportDatabase() throws IOException {

        builder.exportAll(export_directory);
    }
}

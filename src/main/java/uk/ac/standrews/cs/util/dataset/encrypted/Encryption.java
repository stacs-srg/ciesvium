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
package uk.ac.standrews.cs.util.dataset.encrypted;

import java.io.*;

public abstract class Encryption {

    private static final int INPUT_BUFFER_SIZE_IN_BYTES = 512;

    static byte[] readAllBytes(final InputStream inputStream) throws IOException {

        ByteArrayOutputStream temporary_byte_array_stream = new ByteArrayOutputStream();
        byte[] buffer = new byte[INPUT_BUFFER_SIZE_IN_BYTES];

        int i;
        while ((i = inputStream.read(buffer)) != -1) {
            temporary_byte_array_stream.write(buffer, 0, i);
        }
        return temporary_byte_array_stream.toByteArray();
    }
}

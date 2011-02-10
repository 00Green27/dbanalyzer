/*
 * Copyright 2011 00Green27 <00Green27@gmail.com>
 *
 * This file is part of mDBExplorer.
 *
 * This code is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this work.  If not, see http://www.gnu.org/licenses/.
 */

package ru.green.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Чтение информации из файла MANIFEST.MF
 * 
 * @author ehd
 */
public class ManifestReader {

	private static final ManifestReader self;
	static {
		self = new ManifestReader();
	}

	public static String getBuiltDate() {
		return getInstance().attributes.getValue("Built-Date");

	}

	public static String getImplementationVersion() {
		// ManifestReader.class.getPackage().getImplementationVersion();
		return getInstance().attributes.getValue("Implementation-Version");
	}

	private static ManifestReader getInstance() {
		return self;
	}

	private Attributes attributes;

	private ManifestReader() {
		try {
			InputStream stream = getClass().getResourceAsStream(
					"/META-INF/MANIFEST.MF");
			Manifest manifest = new Manifest(stream);

			attributes = manifest.getMainAttributes();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

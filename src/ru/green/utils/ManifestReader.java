/*
 * ManifestReader.java - Класс чтения информации из файла MANIFEST.MF
 *
 * Copyright (C) 2010 ehd
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
/*
 * IOUtils.java - Класс методов-утилит для работы с файлами
 *
 * Copyright (C) 2010 ehd
 */
package ru.stavjust.utils;

import java.io.*;

/**
 * Класс методов-утилит для работы с файлами
 * 
 * @author ehd
 */
public class IOUtils {
    public static boolean writeFile(String text, String destinationFile) {
        return writeFile(text, destinationFile, false);
    }

	public static boolean writeFile(String text, String destinationFile, boolean append) {
		OutputStreamWriter oStream = null;
		try {
			oStream = new OutputStreamWriter(
					/* new BufferedOutputStream( */new FileOutputStream(
							destinationFile, append)/* ) */, "windows-1251");
			oStream.write(text);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if (oStream != null) {
				try {
					oStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}




    public static String readFile(String srcFile) {
        StringBuffer contents = new StringBuffer();
        try {
            BufferedReader reader = null;
            try{
                reader = new BufferedReader(new InputStreamReader(
                        new FileInputStream(srcFile), "windows-1251"));
                String text = null;
                while ((text = reader.readLine()) != null) {
                    contents.append(text).append(System.getProperty("line.separator"));
                }
            }
            finally{
                if(reader !=null)
                    reader.close();
            }
        }  catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return contents.toString();
    }

	public static String[] getLst() {
		String[] list = new File("etc/").list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".lst");
			}
		});
		for (int i = 0; i < list.length; i++) {
			list[i] = list[i].substring(0, list[i].indexOf(".lst"));
		}
		return list;
	}
}

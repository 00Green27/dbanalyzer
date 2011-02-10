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

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

package ru.green.report;

import java.io.FileInputStream;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

/**
 * Класс создает xls-файл с выгруженной информацией
 *
 * @author ehd
 */
public class Report {

	private HSSFWorkbook wb;
	private HSSFCell cell;
	private HSSFCellStyle cellStyle;
	private HSSFCellStyle captionStyle;
	private HSSFSheet sheet;
	private HSSFFont font;

	/**
	 * Прочитать шаблон.
	 *
	 * @throws IOException
	 *             Ошибки чтения файла
	 */
	public void readTemplate() throws IOException {
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(
				"etc/grid.xlt"));
		wb = new HSSFWorkbook(fs);
		sheet = wb.getSheetAt(0);
		cellStyle = wb.createCellStyle();
		captionStyle = wb.createCellStyle();
		font = wb.createFont();
		createCaptionStyle();
		createCellStyle();
	}

	/**
	 * Метод предназначен для печати строки в документ с использованием
	 * оформления.
	 *
	 * @param rowNumber
	 *            Номер строки.
	 * @param cellNumber
	 *            Номер ячейки.
	 * @param cellValue
	 *            Значение ячейки.
	 */
	public void setCell(int rowNumber, int cellNumber, String cellValue) {
		HSSFRow row = createRow(rowNumber);
		cell = row.createCell(cellNumber);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(new HSSFRichTextString(cellValue));
	}

	/**
	 * Метод предназначен для печати  числа в документ с использованием
	 * оформления.
	 *
	 * @param rowNumber
	 *            Номер строки.
	 * @param cellNumber
	 *            Номер ячейки.
	 * @param cellValue
	 *            Значение ячейки.
	 */
	public void setNumericCell(int rowNumber, int cellNumber, double cellValue) {
		HSSFRow row = createRow(rowNumber);
		cell = row.createCell(cellNumber);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);
	}

	public void setFormulaCell(int rowNumber, int cellNumber, String formula) {
		HSSFRow row = createRow(rowNumber);
		cell = row.createCell(cellNumber);
		cell.setCellStyle(cellStyle);
		cell.setCellFormula(formula);

	}

	/**
	 * Метод предназначен для печати числа в документ с использованием
	 * оформления.
	 *
	 * @param rowNumber
	 *            Номер строки.
	 * @param cellNumber
	 *            Номер ячейки.
	 * @param cellValue
	 *            Значение ячейки.
	 */
	public void setCell(int rowNumber, int cellNumber, int cellValue) {
		HSSFRow row = createRow(rowNumber);
		cell = row.createCell(cellNumber);
		cell.setCellStyle(cellStyle);
		cell.setCellValue(cellValue);
	}

	/**
	 * Метод предназначен для печати строки в документ.
	 *
	 * @param rowNumber
	 *            Номер строки.
	 * @param cellNumber
	 *            Номер ячейки.
	 * @param cellValue
	 *            Значение ячейки.
	 */
	public void setMiscCell(int rowNumber, int cellNumber, String cellValue) {
		HSSFRow row = createRow(rowNumber);
		cell = row.createCell(cellNumber);
		cell.setCellValue(new HSSFRichTextString(cellValue));
	}

	public void setCaptionCell(int rowNumber, int cellNumber, String cellValue) {
		HSSFRow row = createRow(rowNumber);
		cell = row.createCell(cellNumber);

		cell.setCellStyle(captionStyle);
		cell.setCellValue(new HSSFRichTextString(cellValue));
	}

	private HSSFRow createRow(int rowNumber) {
		HSSFRow row = null;
		if (sheet.getRow(rowNumber) == null)
			row = sheet.createRow(rowNumber);
		else
			row = sheet.getRow(rowNumber);
		return row;
	}

	/**
	 * Метод формирует стиль для ячейки.
	 */
	private void createCellStyle() {
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
	}

	private void createCaptionStyle() {
		captionStyle.setBorderTop(CellStyle.BORDER_MEDIUM);
		captionStyle.setBorderBottom(CellStyle.BORDER_MEDIUM);
		captionStyle.setBorderLeft(CellStyle.BORDER_MEDIUM);
		captionStyle.setBorderRight(CellStyle.BORDER_MEDIUM);
		captionStyle.setAlignment(CellStyle.ALIGN_CENTER);
		captionStyle.setWrapText(true);
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		captionStyle.setFont(font);
	}

	/**
	 * Записать шаблон в файл.
	 *
	 * @param file
	 *            Имя файла.
	 * @throws IOException
	 *             Проблемы с записью файла.
	 */
	public void write(String file) throws IOException {
		for (int i = 0; i < 256; i++)
			sheet.autoSizeColumn((short) i);
		FileOutputStream output = new FileOutputStream(file);
		wb.write(output);
		output.flush();
		output.close();
	}

	public void addMergedRegion(int lastCol) {
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, lastCol - 1));
	}

}

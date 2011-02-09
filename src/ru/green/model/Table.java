package ru.green.model;

import java.util.ArrayList;

/**
 * @author ehd
 */
public class Table {
	private String name;
	private String title;
	private ArrayList<Field> fileds;

	public Table(String name, String title, ArrayList<Field> fields) {
		this.name = name;
		this.title = title;
		fileds = fields;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setFileds(ArrayList<Field> fileds) {
		this.fileds = fileds;
	}

	public ArrayList<Field> getFileds() {
		return fileds;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

}

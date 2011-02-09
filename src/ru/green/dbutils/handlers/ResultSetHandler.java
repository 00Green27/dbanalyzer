package ru.green.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ehd
 */
public interface ResultSetHandler {
	public Object handle(ResultSet rs) throws SQLException;
}

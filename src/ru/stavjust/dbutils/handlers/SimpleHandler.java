package ru.stavjust.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author ehd
 */
public class SimpleHandler implements ResultSetHandler {
	@Override
	public Object handle(ResultSet rs) throws SQLException {
		if (!rs.next()) {
			return null;
		}

		return rs.getString(1);
	}
}

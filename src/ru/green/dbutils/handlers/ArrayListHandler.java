/*
 *Copyright 2011 00Green27 <00Green27@gmail.com>
 *
 *This file is part of mDBExplorer.
 *
 *This code is free software: you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation, either version 3 of the License, or
 *(at your option) any later version.
 *
 *This code is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this work.  If not, see http://www.gnu.org/licenses/.
 */

package ru.green.dbutils.handlers;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import ru.green.utils.Utils;

/**
 * @author ehd
 */
public class ArrayListHandler implements ResultSetHandler {

	private ResultSetMetaData meta;

	@Override
	public Object handle(ResultSet rs) throws SQLException {
		List<ArrayList<Object>> rows = new ArrayList<ArrayList<Object>>();
		while (rs.next()) {
			meta = rs.getMetaData();
			int cols = meta.getColumnCount();
			ArrayList<Object> result = new ArrayList<Object>();

			for (int i = 0; i < cols; i++) {
				result.add(getColumn(rs, i + 1));
				// result.add(rs.getObject(i + 1));
			}
			rows.add(result);
		}
		return rows;
	}

	public Object getColumn(ResultSet rs, int column) throws SQLException {
		int type = 0;
		try {
			type = meta.getColumnType(column);
		} catch (SQLException e) {
		}
		switch (type) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			return rs.getString(column);

		case Types.BIT:
			return rs.getBoolean(column);

		case Types.TINYINT:
		case Types.SMALLINT:
		case Types.INTEGER:
			return rs.getInt(column);

		case Types.BIGINT:
			return rs.getLong(column);

		case Types.FLOAT:
		case Types.DOUBLE:
			// BigDecimal bd = BigDecimal.valueOf(rs.getDouble(column));
			// return bd.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
			// formatter.setMaximumFractionDigits(2);
			// return fmt.format(rs.getDouble(column));
			// return rs.getDouble(column);
			return Utils.formatDecimal(rs.getDouble(column));

		case Types.DATE:
		case Types.TIMESTAMP:
			if (rs.getDate(column) == null)
				return "";
			return Utils.formatDate(rs.getDate(column));
		case Types.NULL:
			return "";
		default:
			return rs.getObject(column);
		}
	}
}

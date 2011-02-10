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

package ru.green.dbutils.handlers;

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

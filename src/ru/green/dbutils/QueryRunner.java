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

package ru.green.dbutils;

import ru.green.DBAnalyzer;
import ru.green.dbutils.handlers.ResultSetHandler;
import ru.green.model.Database;

import java.sql.*;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Класс для работы с БД
 * 
 * @author ehd
 */
public class QueryRunner {
	private Connection connection;
	@SuppressWarnings("unused")
	private Statement statement;
	@SuppressWarnings("unused")
	private ResultSet resultSet;
	@SuppressWarnings("unused")
	private ResultSetMetaData metaData;
	private Database db;
	private volatile boolean pmdKnownBroken = false;
	private String[] columns;

	public QueryRunner(Database db) {
		this.db = db;
	}

	/**
	 * Установить соединение.
	 * 
	 * @throws java.sql.SQLException
	 *             Ошибки при работе с базой.
	 */
	public void ensureConnection() throws SQLException {
		if (connection != null) {
			return;
		}
		try {
			Class.forName(DBAnalyzer.getPreference("connection.driver-class"));
		} catch (ClassNotFoundException error) {
			Logger.getLogger(QueryRunner.class.getName()).log(Level.SEVERE,
					"Cannot find the database driver classes.", error);
		}

		String user = DBAnalyzer.getPreference("connection.user"); // Settings.getUser();
		String password = DBAnalyzer.getPreference("connection.password");// Settings.getPassword();

		if (password.isEmpty()) {
			password = "dbrnjhbz";
		}

		boolean hasError = true;
		try {
			connection = DriverManager.getConnection(db.getUrl(), user,
					password);
			hasError = false;
			// } catch (SQLException error) {
			// Logger.getLogger(QueryRunner.class.getName()).log(Level.SEVERE,
			// "Cannot connect to this database.", error);
		} finally {
			if (hasError) {
				invalidateConnection();
			}
		}

	}

	/**
	 * Разорвать соединение.
	 */
	public void invalidateConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException ignored) {
				// Logger.getLogger(QueryRunner.class.getName()).log(Level.SEVERE,
				// "Cannot close connection to this database.", ignored);
			} finally {
				connection = null;
			}
		}
	}

	/**
	 * Метод возвращает название подключения.
	 * 
	 * @return Название подключения.
	 */
	public String connectedTo() {
		return db.getName();
	}

	/**
	 * Метод выполняет запрос SELECT к базе.
	 * 
	 * @param sql
	 *            Запрос для выполнения.
	 * @param rsh
	 *            Обработчик, преобразующий результат в объект.
	 * @return Объект, который возвращает обработчик.
	 * @throws SQLException
	 *             возможные проблемы с доступом к базе.
	 */
	public Object query(String sql, ResultSetHandler rsh) throws SQLException {
        System.out.println(sql);
		return this.query(sql, rsh, null);
	}

	/**
	 * Метод выполняет запрос SELECT к базе.
	 * 
	 * @param sql
	 *            Запрос для выполнения.
	 * @param rsh
	 *            Обработчик, преобразующий результат в объект.
	 * @param params
	 *            Параметры запроса.
	 * @return Объект, который возвращает обработчик.
	 * @throws SQLException
	 *             возможные проблемы с доступом к базе.
	 */
	public Object query(String sql, ResultSetHandler rsh, Object[] params)
			throws SQLException {

		ensureConnection();

		PreparedStatement stmt;
		ResultSet rs;
		Object result = null;

		try {
			stmt = connection.prepareStatement(sql);
			fillStatement(stmt, params);
			rs = stmt.executeQuery();
			setColumnsName(rs);
			result = rsh.handle(rs);
		} catch (SQLException e) {
			rethrow(e, sql, params);
		}

		return result;

	}

	public int executeUpdate(String sql) throws SQLException {

		ensureConnection();

		Statement stmt;
		int result = 0;

		try {
			stmt = connection.createStatement();
			result = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			rethrow(e, sql, null);
		}

		return result;

	}

	public void setColumnsName(ResultSet rs) throws SQLException {
		ResultSetMetaData rsMetaData = rs.getMetaData();
		int numberOfColumns = rsMetaData.getColumnCount();
		String[] columns = new String[numberOfColumns];
		for (int i = 1; i <= numberOfColumns; i++) {
			columns[i - 1] = rsMetaData.getColumnName(i);
		}
		this.columns = columns;
	}

	public String[] getColumnsName() {
		return columns;
	}

	protected void rethrow(SQLException cause, String sql, Object[] params)
			throws SQLException {

		String causeMessage = cause.getMessage();
		if (causeMessage == null) {
			causeMessage = "";
		}
		StringBuffer msg = new StringBuffer(causeMessage);

		msg.append(" Query: ");
		msg.append(sql);
		msg.append(" Parameters: ");

		if (params == null) {
			msg.append("[]");
		} else {
			msg.append(Arrays.asList(params));
		}

		SQLException e = new SQLException(msg.toString(), cause.getSQLState(),
				cause.getErrorCode());
		e.setNextException(cause);

		throw e;
	}

	private void fillStatement(PreparedStatement stmt, Object[] params)
			throws SQLException {

		if (params == null) {
			return;
		}

		ParameterMetaData pmd = stmt.getParameterMetaData();
		if (pmd.getParameterCount() < params.length) {
			throw new SQLException("Too many parameters: expected "
					+ pmd.getParameterCount() + ", was given " + params.length);
		}
		for (int i = 0; i < params.length; i++) {
			if (params[i] != null) {
				stmt.setObject(i + 1, params[i]);
			} else {
				int sqlType = Types.VARCHAR;
				if (!pmdKnownBroken) {
					try {
						sqlType = pmd.getParameterType(i + 1);
					} catch (SQLException e) {
						pmdKnownBroken = true;
					}
				}
				stmt.setNull(i + 1, sqlType);
			}
		}
	}

	/*
	 * public String getDepartmentName() throws SQLException {
	 * ensureConnection(); Statement stmt = connection.createStatement(); try {
	 * ResultSet rs = stmt.executeQuery(DEPARTMENT); rs.next(); return
	 * rs.getString(1); } finally { if (stmt != null) { try { stmt.close(); }
	 * catch (SQLException ignored) { } } }
	 * 
	 * }
	 */
}

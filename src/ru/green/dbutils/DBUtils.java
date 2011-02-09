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

package ru.green.dbutils;

import org.fife.ui.autocomplete.BasicCompletion;
import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

/**
 * Класс методов-утилит для работы с БД
 * 
 * @author ehd
 */
public class DBUtils {
	public static CompletionProvider createCompletionProvider() {
		DefaultCompletionProvider provider = new DefaultCompletionProvider();
		provider.addCompletion(new BasicCompletion(provider, "ADD"));
		provider.addCompletion(new BasicCompletion(provider, "ALTER"));
		provider.addCompletion(new BasicCompletion(provider, "ANSI_NULLS"));
		provider.addCompletion(new BasicCompletion(provider, "AS"));
		provider.addCompletion(new BasicCompletion(provider, "ASC"));
		provider.addCompletion(new BasicCompletion(provider, "AUTHORIZATION"));
		provider.addCompletion(new BasicCompletion(provider, "BACKUP"));
		provider.addCompletion(new BasicCompletion(provider, "BEGIN"));
		provider.addCompletion(new BasicCompletion(provider, "BREAK"));
		provider.addCompletion(new BasicCompletion(provider, "BROWSE"));
		provider.addCompletion(new BasicCompletion(provider, "BULK"));
		provider.addCompletion(new BasicCompletion(provider, "BY"));
		provider.addCompletion(new BasicCompletion(provider, "CASCADE"));
		provider.addCompletion(new BasicCompletion(provider, "CHECK"));
		provider.addCompletion(new BasicCompletion(provider, "CHECKPOINT"));
		provider.addCompletion(new BasicCompletion(provider, "CLOSE"));
		provider.addCompletion(new BasicCompletion(provider, "CLUSTERED"));
		provider.addCompletion(new BasicCompletion(provider, "COLUMN"));
		provider.addCompletion(new BasicCompletion(provider, "COMMIT"));
		provider.addCompletion(new BasicCompletion(provider, "COMMITTED"));
		provider.addCompletion(new BasicCompletion(provider, "COMPUTE"));
		provider.addCompletion(new BasicCompletion(provider, "CONFIRM"));
		provider.addCompletion(new BasicCompletion(provider, "CONSTRAINT"));
		provider.addCompletion(new BasicCompletion(provider, "CONTAINS"));
		provider.addCompletion(new BasicCompletion(provider, "CONTAINSTABLE"));
		provider.addCompletion(new BasicCompletion(provider, "CONTINUE"));
		provider.addCompletion(new BasicCompletion(provider, "CONTROLROW"));
		provider.addCompletion(new BasicCompletion(provider, "CREATE"));
		provider.addCompletion(new BasicCompletion(provider, "CURRENT"));
		provider.addCompletion(new BasicCompletion(provider, "CONTAINING"));
		provider.addCompletion(new BasicCompletion(provider, "CURRENT_DATE"));
		provider.addCompletion(new BasicCompletion(provider, "CURRENT_TIME"));
		provider.addCompletion(new BasicCompletion(provider, "CURSOR"));
		provider.addCompletion(new BasicCompletion(provider, "DATABASE"));
		provider.addCompletion(new BasicCompletion(provider, "DBCC"));
		provider.addCompletion(new BasicCompletion(provider, "DEALLOCATE"));
		provider.addCompletion(new BasicCompletion(provider, "DECLARE"));
		provider.addCompletion(new BasicCompletion(provider, "DEFAULT"));
		provider.addCompletion(new BasicCompletion(provider, "DELETE"));
		provider.addCompletion(new BasicCompletion(provider, "DENY"));
		provider.addCompletion(new BasicCompletion(provider, "DESC"));
		provider.addCompletion(new BasicCompletion(provider, "DISK"));
		provider.addCompletion(new BasicCompletion(provider, "DISTINCT"));
		provider.addCompletion(new BasicCompletion(provider, "DISTRIBUTED"));
		provider.addCompletion(new BasicCompletion(provider, "DOUBLE"));
		provider.addCompletion(new BasicCompletion(provider, "DROP"));
		provider.addCompletion(new BasicCompletion(provider, "DUMMY"));
		provider.addCompletion(new BasicCompletion(provider, "DUMP"));
		provider.addCompletion(new BasicCompletion(provider, "ELSE"));
		provider.addCompletion(new BasicCompletion(provider, "END"));
		provider.addCompletion(new BasicCompletion(provider, "ERRLVL"));
		provider.addCompletion(new BasicCompletion(provider, "ERROREXIT"));
		provider.addCompletion(new BasicCompletion(provider, "ESCAPE"));
		provider.addCompletion(new BasicCompletion(provider, "EXCEPT"));
		provider.addCompletion(new BasicCompletion(provider, "EXEC"));
		provider.addCompletion(new BasicCompletion(provider, "EXECUTE"));
		provider.addCompletion(new BasicCompletion(provider, "EXIT"));
		provider.addCompletion(new BasicCompletion(provider, "FETCH"));
		provider.addCompletion(new BasicCompletion(provider, "FILE"));
		provider.addCompletion(new BasicCompletion(provider, "FILLFACTOR"));
		provider.addCompletion(new BasicCompletion(provider, "FLOPPY"));
		provider.addCompletion(new BasicCompletion(provider, "FOR"));
		provider.addCompletion(new BasicCompletion(provider, "FOREIGN"));
		provider.addCompletion(new BasicCompletion(provider, "FREETEXT"));
		provider.addCompletion(new BasicCompletion(provider, "FREETEXTTABLE"));
		provider.addCompletion(new BasicCompletion(provider, "FROM"));
		provider.addCompletion(new BasicCompletion(provider, "FULL"));
		provider.addCompletion(new BasicCompletion(provider, "GOTO"));
		provider.addCompletion(new BasicCompletion(provider, "GRANT"));
		provider.addCompletion(new BasicCompletion(provider, "GROUP"));
		provider.addCompletion(new BasicCompletion(provider, "HAVING"));
		provider.addCompletion(new BasicCompletion(provider, "HOLDLOCK"));
		provider.addCompletion(new BasicCompletion(provider, "IDENTITY_INSERT"));
		provider.addCompletion(new BasicCompletion(provider, "IDENTITYCOL"));
		provider.addCompletion(new BasicCompletion(provider, "ID"));
		provider.addCompletion(new BasicCompletion(provider, "IF"));
		provider.addCompletion(new BasicCompletion(provider, "INDEX"));
		provider.addCompletion(new BasicCompletion(provider, "INNER"));
		provider.addCompletion(new BasicCompletion(provider, "INSERT"));
		provider.addCompletion(new BasicCompletion(provider, "INTO"));
		provider.addCompletion(new BasicCompletion(provider, "IS"));
		provider.addCompletion(new BasicCompletion(provider, "ISOLATION"));
		provider.addCompletion(new BasicCompletion(provider, "KEY"));
		provider.addCompletion(new BasicCompletion(provider, "KILL"));
		provider.addCompletion(new BasicCompletion(provider, "LEVEL"));
		provider.addCompletion(new BasicCompletion(provider, "LIKE"));
		provider.addCompletion(new BasicCompletion(provider, "LINENO"));
		provider.addCompletion(new BasicCompletion(provider, "LOAD"));
		provider.addCompletion(new BasicCompletion(provider, "MAX"));
		provider.addCompletion(new BasicCompletion(provider, "MIN"));
		provider.addCompletion(new BasicCompletion(provider, "MIRROREXIT"));
		provider.addCompletion(new BasicCompletion(provider, "NATIONAL"));
		provider.addCompletion(new BasicCompletion(provider, "NOCHECK"));
		provider.addCompletion(new BasicCompletion(provider, "NONCLUSTERED"));
		provider.addCompletion(new BasicCompletion(provider, "OF"));
		provider.addCompletion(new BasicCompletion(provider, "OFF"));
		provider.addCompletion(new BasicCompletion(provider, "OFFSETS"));
		provider.addCompletion(new BasicCompletion(provider, "ON"));
		provider.addCompletion(new BasicCompletion(provider, "ONCE"));
		provider.addCompletion(new BasicCompletion(provider, "ONLY"));
		provider.addCompletion(new BasicCompletion(provider, "OPEN"));
		provider.addCompletion(new BasicCompletion(provider, "OPENDATASOURCE"));
		provider.addCompletion(new BasicCompletion(provider, "OPENQUERY"));
		provider.addCompletion(new BasicCompletion(provider, "OPENROWSET"));
		provider.addCompletion(new BasicCompletion(provider, "OPTION"));
		provider.addCompletion(new BasicCompletion(provider, "ORDER"));
		provider.addCompletion(new BasicCompletion(provider, "OVER"));
		provider.addCompletion(new BasicCompletion(provider, "PERCENT"));
		provider.addCompletion(new BasicCompletion(provider, "PERM"));
		provider.addCompletion(new BasicCompletion(provider, "PERMANENT"));
		provider.addCompletion(new BasicCompletion(provider, "PIPE"));
		provider.addCompletion(new BasicCompletion(provider, "PLAN"));
		provider.addCompletion(new BasicCompletion(provider, "PRECISION"));
		provider.addCompletion(new BasicCompletion(provider, "PREPARE"));
		provider.addCompletion(new BasicCompletion(provider, "PRIMARY"));
		provider.addCompletion(new BasicCompletion(provider, "PRINT"));
		provider.addCompletion(new BasicCompletion(provider, "PRIVILEGES"));
		provider.addCompletion(new BasicCompletion(provider, "PROC"));
		provider.addCompletion(new BasicCompletion(provider, "PROCEDURE"));
		provider.addCompletion(new BasicCompletion(provider, "PROCESSEXIT"));
		provider.addCompletion(new BasicCompletion(provider, "PUBLIC"));
		provider.addCompletion(new BasicCompletion(provider,
				"QUOTED_IDENTIFIER"));
		provider.addCompletion(new BasicCompletion(provider, "RAISERROR"));
		provider.addCompletion(new BasicCompletion(provider, "READ"));
		provider.addCompletion(new BasicCompletion(provider, "READTEXT"));
		provider.addCompletion(new BasicCompletion(provider, "RECONFIGURE"));
		provider.addCompletion(new BasicCompletion(provider, "REFERENCES"));
		provider.addCompletion(new BasicCompletion(provider, "REPEATABLE"));
		provider.addCompletion(new BasicCompletion(provider, "REPLICATION"));
		provider.addCompletion(new BasicCompletion(provider, "RESTORE"));
		provider.addCompletion(new BasicCompletion(provider, "RESTRICT"));
		provider.addCompletion(new BasicCompletion(provider, "RETURN"));
		provider.addCompletion(new BasicCompletion(provider, "REVOKE"));
		provider.addCompletion(new BasicCompletion(provider, "ROLLBACK"));
		provider.addCompletion(new BasicCompletion(provider, "ROWGUIDCOL"));
		provider.addCompletion(new BasicCompletion(provider, "RULE"));
		provider.addCompletion(new BasicCompletion(provider, "SAVE"));
		provider.addCompletion(new BasicCompletion(provider, "SCHEMA"));
		provider.addCompletion(new BasicCompletion(provider, "SELECT"));
		provider.addCompletion(new BasicCompletion(provider, "SERIALIZABLE"));
		provider.addCompletion(new BasicCompletion(provider, "SET"));
		provider.addCompletion(new BasicCompletion(provider, "SETUSER"));
		provider.addCompletion(new BasicCompletion(provider, "SHUTDOWN"));
		provider.addCompletion(new BasicCompletion(provider, "STATISTICS"));
		provider.addCompletion(new BasicCompletion(provider, "TABLE"));
		provider.addCompletion(new BasicCompletion(provider, "TAPE"));
		provider.addCompletion(new BasicCompletion(provider, "TEMP"));
		provider.addCompletion(new BasicCompletion(provider, "TEMPORARY"));
		provider.addCompletion(new BasicCompletion(provider, "TEXTIMAGE_ON"));
		provider.addCompletion(new BasicCompletion(provider, "THEN"));
		provider.addCompletion(new BasicCompletion(provider, "TO"));
		provider.addCompletion(new BasicCompletion(provider, "TOP"));
		provider.addCompletion(new BasicCompletion(provider, "TRAN"));
		provider.addCompletion(new BasicCompletion(provider, "TRANSACTION"));
		provider.addCompletion(new BasicCompletion(provider, "TRIGGER"));
		provider.addCompletion(new BasicCompletion(provider, "TRUNCATE"));
		provider.addCompletion(new BasicCompletion(provider, "TSEQUAL"));
		provider.addCompletion(new BasicCompletion(provider, "UNCOMMITTED"));
		provider.addCompletion(new BasicCompletion(provider, "UNION"));
		provider.addCompletion(new BasicCompletion(provider, "UNIQUE"));
		provider.addCompletion(new BasicCompletion(provider, "UPDATE"));
		provider.addCompletion(new BasicCompletion(provider, "UPDATETEXT"));
		provider.addCompletion(new BasicCompletion(provider, "USE"));
		provider.addCompletion(new BasicCompletion(provider, "VALUES"));
		provider.addCompletion(new BasicCompletion(provider, "VARYING"));
		provider.addCompletion(new BasicCompletion(provider, "VIEW"));
		provider.addCompletion(new BasicCompletion(provider, "WAITFOR"));
		provider.addCompletion(new BasicCompletion(provider, "WHEN"));
		provider.addCompletion(new BasicCompletion(provider, "WHERE"));
		provider.addCompletion(new BasicCompletion(provider, "WHILE"));
		provider.addCompletion(new BasicCompletion(provider, "WITH"));
		provider.addCompletion(new BasicCompletion(provider, "WORK"));
		provider.addCompletion(new BasicCompletion(provider, "WRITETEXT"));
		return provider;
	}
}

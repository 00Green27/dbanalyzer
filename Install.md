# Что требуется #
Для работы DBAnalyzer требуется наличие JRE 1.6. Работать должно везде, где работает java. Проверено на Linux и на Windows.


# Как установить #
Скачать из Downloads последнюю сборку и распаковать ее в любое место. Выполнить необходимые настройки либо в файле etc/preferences.xml, либо через GUI.

# Состав сборки #
Каталоги:
  * etc - файлы настроек, шаблоны и etc ;)
  * lib - библиотеки, необходимые для работы
  * sql - закладки с запросами
Файлы:
  * DBAnalyzer.jar - исполняемый файл для запуска на любой платформе
  * DBAnalyzer.exe - исполняемый файл для запуска в Windows
  * DBAnalyzer.sh - исполняемый файл для запуска в Linux

# Конфигурация #
Все настройки находятся в файле etc/preferences.xml. Его надо настроить под себя. Можно через текстовый редактор, а можно через DBAnalyzer.
```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<!DOCTYPE properties SYSTEM "http://java.sun.com/dtd/properties.dtd">
<properties>
	<comment>DBAnalyzer settings</comment>
	<entry key="view.editor">true</entry>
	<entry key="view.tray">true</entry>
	<entry key="misc.bookmark">sql</entry>
	<entry key="connection.user">sysdba</entry>
	<entry key="connection.password"/>
	<entry key="connection.protocol">jdbc:firebirdsql:</entry>
	<entry key="connection.list">etc/2010.lst</entry>
	<entry key="view.theme">false</entry>
	<entry key="connection.driver-class">org.firebirdsql.jdbc.FBDriver</entry>
	<entry key="connection.parameters">lc_ctype=WIN1251;defaultResultSetHoldable=True</entry>
</properties>
```

_connection.user_ - имя пользователя для подключения к БД<br>
<i>connection.password</i> - пароль пользователя для подключения к БД<br>
<i>connection.protocol</i> - jdbc протокол<br>
<i>connection.driver-class</i> - jdbc драйвер<br>
<i>connection.parameters</i> - параметры подключения<br>
<i>connection.list</i> - список баз по умолчанию<br>
<i>misc.bookmark</i> - место расположение закладок<br>
<i>view.editor</i> - показывать редактор SQL кода при старте программы<br>
<i>view.tray</i> - отображать заначек в области уведомления<br>
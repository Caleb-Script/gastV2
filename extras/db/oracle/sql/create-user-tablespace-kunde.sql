-- noinspection SqlNoDataSourceInspectionForFile

-- Copyright (C) 2022 - present Juergen Zimmermann, Hochschule Karlsruhe
--
-- This program is free software: you can redistribute it and/or modify
-- it under the terms of the GNU General Public License as published by
-- the Free Software Foundation, either version 3 of the License, or
-- (at your option) any later version.
--
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
--
-- You should have received a copy of the GNU General Public License
-- along with this program.  If not, see <https://www.gnu.org/licenses/>.

-- Oracle 23:
-- docker compose exec db sqlplus SYS/p@FREEPDB1 as SYSDBA '@/sql/create-user-tablespace-gast.sql'
-- docker compose exec db sqlplus gast/p@FREEPDB1 '@/sql/create-schema-gast.sql'
-- docker compose exec db sqlplus gast/p@FREEPDB1 '@/sql/V1.0__Create.sql'
-- docker compose exec db sqlplus gast/p@FREEPDB1 '@/sql/V1.1__Insert.sql'

-- Oracle 21:
-- docker compose exec db sqlplus SYS/p@XEPDB1 as SYSDBA '@/sql/create-user-tablespace-gast.sql'
-- docker compose exec db sqlplus gast/p@XEPDB1 '@/sql/create-schema-gast.sql'

-- https://docs.oracle.com/en/database/oracle/oracle-database/23/sqlrf/CREATE-USER.html
-- https://blogs.oracle.com/sql/post/how-to-create-users-grant-them-privileges-and-remove-them-in-oracle-database
CREATE USER gast IDENTIFIED BY p;

GRANT RESOURCE,
  CONNECT,
  CREATE SESSION,
  CREATE TABLE,
  CREATE VIEW,
  CREATE SEQUENCE,
  CREATE ANY TRIGGER,
  CREATE ANY PROCEDURE,
  DROP ANY TABLE,
  ALTER ANY TABLE,
  READ ANY TABLE,
  INSERT ANY TABLE,
  UPDATE ANY TABLE,
  DELETE ANY TABLE,
  CREATE ANY INDEX,
  DROP ANY INDEX
TO gast;

GRANT UNLIMITED TABLESPACE TO gast;

-- Remote-Login für Oracle XE zulassen
EXEC DBMS_XDB.SETLISTENERLOCALACCESS(FALSE);

-- https://docs.oracle.com/en/database/oracle/oracle-database/21/sqlrf/CREATE-TABLESPACE.html
-- https://www.carajandb.com/blog/2018/oracle-18-xe-und-multitenant/
-- hier: initiale Groesse 10 MB mit 500 KB Extensions
-- dbf = data(base) file, default: im Verzeichnis /opt/oracle/product/18c/dbhomeXE/dbs
-- ALTER SYSTEM SET DB_CREATE_FILE_DEST = '/opt/oracle/tablespace';
-- DROP TABLESPACE kundespace INCLUDING CONTENTS AND DATAFILES;
-- CREATE TABLESPACE kundespace DATAFILE 'gast.dbf' SIZE 10M;

exit

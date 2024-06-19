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

-- Flyway unterstuetzt nur Oracle 21 in der lizenzpflichtigen Version https://documentation.red-gate.com/fd/oracle-184127602.html
-- org.flywaydb.core.internal.database.DatabaseTypeRegister.getDatabaseTypeForConnection()

-- docker compose exec oracle sqlplus gast/p@FREEPDB1 '@/sql/...'

-- https://docs.oracle.com/en/database/oracle/oracle-database/23/sqlrf/CREATE-TABLE.html
-- https://docs.oracle.com/en/database/oracle/oracle-database/23/sqlrf/Data-Types.html
CREATE TABLE adresse (
     id        CHAR(36) PRIMARY KEY CONSTRAINT adresse_id CHECK (REGEXP_LIKE(id, '^[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}$')),
     plz       CHAR(5) NOT NULL CONSTRAINT adresse_plz CHECK (REGEXP_LIKE(plz, '\d{5}')),
     ort       VARCHAR2(40) NOT NULL
);
CREATE INDEX adresse_plz_idx ON adresse(plz);

CREATE TABLE gast (
    id            CHAR(36) PRIMARY KEY CONSTRAINT kunde_id CHECK (REGEXP_LIKE(id, '^[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}$')),
    version       NUMBER(10,0) NOT NULL,
    nachname      VARCHAR2(40) NOT NULL,
                  -- impliziter Index als B-Baum durch UNIQUE
    email         VARCHAR2(40) UNIQUE NOT NULL,
    kategorie     NUMBER(1,0) NOT NULL CONSTRAINT kunde_kategorie CHECK (kategorie >= 0 AND kategorie <= 9),
                  -- https://www.postgresql.org/docs/current/datatype-boolean.html
    has_newsletter NUMBER(1,0) NOT NULL CONSTRAINT kunde_has_newsletter CHECK (has_newsletter = 0 OR has_newsletter = 1),
    geburtsdatum  DATE,
    homepage      VARCHAR2(40),
    geschlecht    VARCHAR2(9) CONSTRAINT kunde_geschlecht CHECK (REGEXP_LIKE(geschlecht, 'MAENNLICH|WEIBLICH|DIVERS')),
    familienstand VARCHAR2(11) CONSTRAINT kunde_familienstand CHECK (REGEXP_LIKE(familienstand, 'LEDIG|VERHEIRATET|GESCHIEDEN|VERWITWET')),
    adresse_id    CHAR(36) NOT NULL UNIQUE REFERENCES adresse,
    interessen    VARCHAR2(32),
    username      VARCHAR2(20) NOT NULL,
    erzeugt       TIMESTAMP NOT NULL,
    aktualisiert  TIMESTAMP NOT NULL
);

-- https://docs.oracle.com/en/database/oracle/oracle-database/21/sqlrf/CREATE-INDEX.html
CREATE INDEX kunde_nachname_idx ON gast(nachname);

CREATE TABLE umsatz (
    id        CHAR(36) PRIMARY KEY CONSTRAINT umsatz_id CHECK (REGEXP_LIKE(id, '^[0-9a-f]{8}-([0-9a-f]{4}-){3}[0-9a-f]{12}$')),
              -- 10 Stellen, davon 2 Nachkommastellen
    betrag    NUMBER(10,2) NOT NULL,
    waehrung  CHAR(3) NOT NULL CONSTRAINT umsatz_waehrung  CHECK (REGEXP_LIKE(waehrung, '[A-Z]{3}')),
    kunde_id  CHAR(36) REFERENCES gast,
    idx       NUMBER(4,0)
);
CREATE INDEX umsatz_kunde_id_idx ON umsatz(kunde_id);

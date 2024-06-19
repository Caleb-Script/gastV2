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

-- docker compose exec oracle sqlplus gast/p@FREEPDB1 '@/sql/...'

INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000000','00000','Aachen');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000001','11111','Augsburg');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000020','22222','Aalen');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000030','33333','Ahlen');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000040','44444','Dortmund');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000050','55555','Essen');
INSERT INTO adresse (id, plz, ort)
VALUES ('20000000-0000-0000-0000-000000000060','66666','Freiburg');

-- admin
INSERT INTO gast (id, version, nachname, email, kategorie, has_newsletter, geburtsdatum, homepage, geschlecht, familienstand, adresse_id, interessen, username, erzeugt, aktualisiert)
    VALUES ('00000000-0000-0000-0000-000000000000',0,'Admin','admin@acme.com',0,1,TO_DATE('2022-01-31 12:00:00', 'yyyy-MM-dd hh:mi:ss'),'https://www.acme.com','WEIBLICH','VERHEIRATET','20000000-0000-0000-0000-000000000000','LESEN','admin',TO_TIMESTAMP('2022-01-31 12:00:00', 'yyyy-MM-dd hh:mi:ss'),TO_TIMESTAMP('2022-01-31 12:00:00', 'yyyy-MM-dd hh:mi:ss'));
-- HTTP GET
INSERT INTO gast (id, version, nachname, email, kategorie, has_newsletter, geburtsdatum, homepage, geschlecht, familienstand, adresse_id, interessen, username, erzeugt, aktualisiert)
    VALUES ('00000000-0000-0000-0000-000000000001',0,'Alpha','alpha@acme.de',1,1,TO_DATE('2022-01-01 12:00:00', 'yyyy-MM-dd hh:mi:ss'),'https://www.acme.de','MAENNLICH','LEDIG','20000000-0000-0000-0000-000000000001','SPORT,LESEN','user',TO_TIMESTAMP('2022-01-01 12:00:00', 'yyyy-MM-dd hh:mi:ss'),TO_TIMESTAMP('2022-01-01 12:00:00', 'yyyy-MM-dd hh:mi:ss'));
INSERT INTO gast (id, version, nachname, email, kategorie, has_newsletter, geburtsdatum, homepage, geschlecht, familienstand, adresse_id, interessen, username, erzeugt, aktualisiert)
    VALUES ('00000000-0000-0000-0000-000000000020',0,'Alpha','alpha@acme.edu',2,1,TO_DATE('2022-01-02 12:00:00', 'yyyy-MM-dd hh:mi:ss'),'https://www.acme.edu','WEIBLICH','GESCHIEDEN','20000000-0000-0000-0000-000000000020',null,'user',TO_TIMESTAMP('2022-01-02 12:00:00', 'yyyy-MM-dd hh:mi:ss'),TO_TIMESTAMP('2022-01-02 12:00:00', 'yyyy-MM-dd hh:mi:ss'));
-- HTTP PUT
INSERT INTO gast (id, version, nachname, email, kategorie, has_newsletter, geburtsdatum, homepage, geschlecht, familienstand, adresse_id, interessen, username, erzeugt, aktualisiert)
    VALUES ('00000000-0000-0000-0000-000000000030',0,'Alpha','alpha@acme.ch',3,1,TO_DATE('2022-01-03 12:00:00', 'yyyy-MM-dd hh:mi:ss'),'https://www.acme.ch','MAENNLICH','VERWITWET','20000000-0000-0000-0000-000000000030','SPORT,REISEN','user',TO_TIMESTAMP('2022-01-03 12:00:00', 'yyyy-MM-dd hh:mi:ss'),TO_TIMESTAMP('2022-01-03 12:00:00', 'yyyy-MM-dd hh:mi:ss'));
-- HTTP PATCH
INSERT INTO gast (id, version, nachname, email, kategorie, has_newsletter, geburtsdatum, homepage, geschlecht, familienstand, adresse_id, interessen, username, erzeugt, aktualisiert)
    VALUES ('00000000-0000-0000-0000-000000000040',0,'Delta','delta@acme.uk',4,1,TO_DATE('2022-01-04 12:00:00', 'yyyy-MM-dd hh:mi:ss'),'https://www.acme.uk','WEIBLICH','VERHEIRATET','20000000-0000-0000-0000-000000000040','LESEN,REISEN','user',TO_TIMESTAMP('2022-01-04 12:00:00', 'yyyy-MM-dd hh:mi:ss'),TO_TIMESTAMP('2022-01-04 12:00:00', 'yyyy-MM-dd hh:mi:ss'));
-- HTTP DELETE
INSERT INTO gast (id, version, nachname, email, kategorie, has_newsletter, geburtsdatum, homepage, geschlecht, familienstand, adresse_id, interessen, username, erzeugt, aktualisiert)
    VALUES ('00000000-0000-0000-0000-000000000050',0,'Epsilon','epsilon@acme.jp',5,1,TO_DATE('2022-01-05 12:00:00', 'yyyy-MM-dd hh:mi:ss'),'https://www.acme.jp','MAENNLICH','LEDIG','20000000-0000-0000-0000-000000000050',null,'user',TO_TIMESTAMP('2022-01-05 12:00:00', 'yyyy-MM-dd hh:mi:ss'),TO_TIMESTAMP('2022-01-05 12:00:00', 'yyyy-MM-dd hh:mi:ss'));
-- zur freien Verfuegung
INSERT INTO gast (id, version, nachname, email, kategorie, has_newsletter, geburtsdatum, homepage, geschlecht, familienstand, adresse_id, interessen, username, erzeugt, aktualisiert)
    VALUES ('00000000-0000-0000-0000-000000000060',0,'Phi','phi@acme.cn',6,1,TO_DATE('2022-01-06 12:00:00', 'yyyy-MM-dd hh:mi:ss'),'https://www.acme.cn','DIVERS','LEDIG','20000000-0000-0000-0000-000000000060',null,'user',TO_TIMESTAMP('2022-01-06 12:00:00', 'yyyy-MM-dd hh:mi:ss'),TO_TIMESTAMP('2022-01-06 12:00:00', 'yyyy-MM-dd hh:mi:ss'));

INSERT INTO umsatz (id, betrag, waehrung, kunde_id, idx)
    VALUES ('10000000-0000-0000-0000-000000000000',0,'EUR','00000000-0000-0000-0000-000000000000', 0);
INSERT INTO umsatz (id, betrag, waehrung, kunde_id, idx)
    VALUES ('10000000-0000-0000-0000-000000000010',10,'EUR','00000000-0000-0000-0000-000000000001', 0);
INSERT INTO umsatz (id, betrag, waehrung, kunde_id, idx)
    VALUES ('10000000-0000-0000-0000-000000000011',11,'EUR','00000000-0000-0000-0000-000000000001', 1);
INSERT INTO umsatz (id, betrag, waehrung, kunde_id, idx)
    VALUES ('10000000-0000-0000-0000-000000000012',12,'EUR','00000000-0000-0000-0000-000000000001', 2);
INSERT INTO umsatz (id, betrag, waehrung, kunde_id, idx)
    VALUES ('10000000-0000-0000-0000-000000000020',20,'USD','00000000-0000-0000-0000-000000000020', 0);
INSERT INTO umsatz (id, betrag, waehrung, kunde_id, idx)
    VALUES ('10000000-0000-0000-0000-000000000030',30,'CHF','00000000-0000-0000-0000-000000000030', 0);
INSERT INTO umsatz (id, betrag, waehrung, kunde_id, idx)
    VALUES ('10000000-0000-0000-0000-000000000031',31,'CHF','00000000-0000-0000-0000-000000000030', 1);
INSERT INTO umsatz (id, betrag, waehrung, kunde_id, idx)
    VALUES ('10000000-0000-0000-0000-000000000040',40,'GBP','00000000-0000-0000-0000-000000000040', 0);

COMMIT;

SELECT * FROM dual;

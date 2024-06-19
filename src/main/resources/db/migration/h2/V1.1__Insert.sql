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

-- http://www.h2database.com/html/commands.html#insert
INSERT INTO adresse (id, plz, ort)
VALUES
    ('20000000-0000-0000-0000-000000000000','00000','Aachen'),
    ('20000000-0000-0000-0000-000000000001','11111','Augsburg'),
    ('20000000-0000-0000-0000-000000000020','22222','Aalen'),
    ('20000000-0000-0000-0000-000000000030','33333','Ahlen'),
    ('20000000-0000-0000-0000-000000000040','44444','Dortmund'),
    ('20000000-0000-0000-0000-000000000050','55555','Essen'),
    ('20000000-0000-0000-0000-000000000060','66666','Freiburg');

INSERT INTO gast (id, version, nachname, email, kategorie, has_newsletter, geburtsdatum, homepage, geschlecht, familienstand, adresse_id, interessen, username, erzeugt, aktualisiert)
VALUES
    -- admin
    ('00000000-0000-0000-0000-000000000000',0,'Admin','admin@acme.com',0,true,'2022-01-31','https://www.acme.com','WEIBLICH','VERHEIRATET','20000000-0000-0000-0000-000000000000','LESEN','admin','2022-01-31 00:00:00','2022-01-31 00:00:00'),
    -- HTTP GET
    ('00000000-0000-0000-0000-000000000001',0,'Alpha','alpha@acme.de',1,true,'2022-01-01','https://www.acme.de','MAENNLICH','LEDIG','20000000-0000-0000-0000-000000000001','SPORT,LESEN','user','2022-01-01 00:00:00','2022-01-01 00:00:00'),
    ('00000000-0000-0000-0000-000000000020',0,'Alpha','alpha@acme.edu',2,true,'2022-01-02','https://www.acme.edu','WEIBLICH','GESCHIEDEN','20000000-0000-0000-0000-000000000020',null,'user','2022-01-02 00:00:00','2022-01-02 00:00:00'),
    -- HTTP PUT
    ('00000000-0000-0000-0000-000000000030',0,'Alpha','alpha@acme.ch',3,true,'2022-01-03','https://www.acme.ch','MAENNLICH','VERWITWET','20000000-0000-0000-0000-000000000030','SPORT,REISEN','user','2022-01-03 00:00:00','2022-01-03 00:00:00'),
    -- HTTP PATCH
    ('00000000-0000-0000-0000-000000000040',0,'Delta','delta@acme.uk',4,true,'2022-01-04','https://www.acme.uk','WEIBLICH','VERHEIRATET','20000000-0000-0000-0000-000000000040','LESEN,REISEN','user','2022-01-04 00:00:00','2022-01-04 00:00:00'),
    -- HTTP DELETE
    ('00000000-0000-0000-0000-000000000050',0,'Epsilon','epsilon@acme.jp',5,true,'2022-01-05','https://www.acme.jp','MAENNLICH','LEDIG','20000000-0000-0000-0000-000000000050',null,'user','2022-01-05 00:00:00','2022-01-05 00:00:00'),
    -- zur freien Verfuegung
    ('00000000-0000-0000-0000-000000000060',0,'Phi','phi@acme.cn',6,true,'2022-01-06','https://www.acme.cn','DIVERS','LEDIG','20000000-0000-0000-0000-000000000060',null,'user','2022-01-06 00:00:00','2022-01-06 00:00:00');

INSERT INTO umsatz (id, betrag, waehrung, kunde_id, idx)
VALUES
  ('10000000-0000-0000-0000-000000000000',0,'EUR','00000000-0000-0000-0000-000000000000',0),
  ('10000000-0000-0000-0000-000000000010',10,'EUR','00000000-0000-0000-0000-000000000001',0),
  ('10000000-0000-0000-0000-000000000011',11,'EUR','00000000-0000-0000-0000-000000000001',1),
  ('10000000-0000-0000-0000-000000000012',12,'EUR','00000000-0000-0000-0000-000000000001',2),
  ('10000000-0000-0000-0000-000000000020',20,'USD','00000000-0000-0000-0000-000000000020',0),
  ('10000000-0000-0000-0000-000000000030',30,'CHF','00000000-0000-0000-0000-000000000030',0),
  ('10000000-0000-0000-0000-000000000031',31,'CHF','00000000-0000-0000-0000-000000000030',1),
  ('10000000-0000-0000-0000-000000000040',40,'GBP','00000000-0000-0000-0000-000000000040',0);

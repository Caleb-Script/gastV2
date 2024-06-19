CREATE TABLE IF NOT EXISTS login (
    id       uuid PRIMARY KEY USING INDEX TABLESPACE gastspace,
    username varchar(20) NOT NULL UNIQUE USING INDEX TABLESPACE gastspace,
    password varchar(180) NOT NULL,
    rollen   varchar(32)
) TABLESPACE gastspace;

CREATE TABLE IF NOT EXISTS zimmer_information (
    id            uuid PRIMARY KEY USING INDEX TABLESPACE gastspace,
    zimmer_nummer char(5) NOT NULL CHECK (zimmer_nummer ~ '\d{3}'),
    zimmer_typ    varchar(40) NOT NULL
) TABLESPACE gastspace;
CREATE INDEX IF NOT EXISTS zimmer_information_plz_idx ON zimmer_information(zimmer_nummer) TABLESPACE gastspace;

CREATE TABLE IF NOT EXISTS gast (
    id            uuid PRIMARY KEY USING INDEX TABLESPACE gastspace,
    version       integer NOT NULL DEFAULT 0,
    nachname      varchar(40) NOT NULL,
    vorname       varchar(40) NOT NULL,
    email         varchar(40) NOT NULL UNIQUE USING INDEX TABLESPACE gastspace,
    rang     integer NOT NULL CHECK (rang >= 0 AND rang <= 9),
    ist_vip  boolean NOT NULL DEFAULT FALSE,
    geburtsdatum  date CHECK (geburtsdatum < current_date),
    homepage      varchar(40),
    geschlecht    varchar(9) CHECK (geschlecht ~ 'MAENNLICH|WEIBLICH|DIVERS'),
    zimmer_info_id    uuid NOT NULL UNIQUE USING INDEX TABLESPACE gastspace REFERENCES zimmer_information,
    praeferenzen    varchar(32),
    username      varchar(20) NOT NULL REFERENCES login(username),
    erzeugt       timestamp NOT NULL,
    aktualisiert  timestamp NOT NULL
) TABLESPACE gastspace;

CREATE INDEX IF NOT EXISTS gast_nachname_idx ON gast(nachname) TABLESPACE gastspace;

CREATE TABLE IF NOT EXISTS buchung (
    id        uuid PRIMARY KEY USING INDEX TABLESPACE gastspace,
    betrag    decimal(10,2) NOT NULL,
    waehrung  char(3) NOT NULL CHECK (waehrung ~ '[A-Z]{3}'),
    gast_id  uuid REFERENCES gast,
    idx       integer NOT NULL DEFAULT 0
) TABLESPACE gastspace;
CREATE INDEX IF NOT EXISTS buchung_gast_id_idx ON buchung(gast_id) TABLESPACE gastspace;

CREATE TABLE nutzer
(
    id               INTEGER     NOT NULL PRIMARY KEY,
    vorname          varchar(64) NOT NULL,
    nachname         varchar(64) NOT NULL,
    anrede           varchar(10) Check(anrede in ("Herr", "Frau", "Professor", "Doktor")),
    mail             varchar(80) NOT NULL,
    einkaufswagen_id INTEGER     NOT NULL,
    iban varchar(22) NOT NULL,

    FOREIGN KEY (einkaufswagen_id) REFERENCES einkaufswagen (id) ON DELETE CASCADE
);

CREATE TABLE einkaufswagen
(
    id INTEGER NOT NULL PRIMARY KEY,
    anzahl INTEGER,
);

CREATE TABLE produkt
(
    id INTEGER NOT NULL PRIMARY KEY,
    name varchar(64) NOT NULL ,
    preis NUMBER(6, 2) NOT NULL,
    skin varchar(24),
    geschlecht varchar(10) Check(geschlecht in ("Männlich", "Weiblich")), 
    alter INTEGER,
    kategorie_id INTEGER NOT NULL,
    bizness_id INTEGER NOT NULL,

    FOREIGN KEY (bizness_id) REFERENCES bizness(id),
    FOREIGN KEY (kategorie_id) REFERENCES kategorie(id)
);

CREATE TABLE einkaufswagen_produkt
(
    einkaufswagen_id INTEGER NOT NULL,
    produkt_id INTEGER NOT NULL,

    PRIMARY KEY (einkaufswagen_id, produkt_id),
    FOREIGN KEY (einkaufswagen_id) REFERENCES einkaufswagen(id),
    FOREIGN KEY (produkt_id) REFERENCES produkt(id)
);

CREATE TABLE kategorie
(
    id INTEGER NOT NULL PRIMARY KEY,
    name varchar(24) NOT NULL,
    bild BLOB,
    subkategorie_id INTEGER DEFAULT NULL,

    FOREIGN KEY (subkategorie_id) REFERENCES kategorie(id) ON DELETE CASCADE;
);

CREATE TABLE bizness
(
    id INTEGER NOT NULL PRIMARY KEY,
    name varchar(24) NOT NULL ,
    addresse varchar(24) NOT NULL,
    telefonnummer varchar(24) NOT NULL,
    ceo varchar(24) NOT NOT NULL ,
);

CREATE TABLE kategorie_business
(
    kategorie_id INTEGER NOT NULL ,
    business_id INTEGER NOT NULL ,

    PRIMARY KEY (kategorie_id, business_id),
    FOREIGN KEY (kategorie_id) REFERENCES kategorie(id),
    FOREIGN KEY (business_id) REFERENCES bizness(id),
);

CREATE TABLE bestellung
(
    id INTEGER NOT NULL PRIMARY KEY,
    bizness_id INTEGER NOT NULL,
    nutzer_id INTEGER NOT NULL,
    bestellstatus varchar(10) NOT NULL,
    bestelldatum DATE NOT NULL,
    lieferaddresse varchar(24) NOT NULL,

    FOREIGN KEY (nutzer_id) REFERENCES nutzer(id) ON DELETE CASCADE,
    FOREIGN KEY (bizness_id) REFERENCES bizness(id) ON DELETE CASCADE
);
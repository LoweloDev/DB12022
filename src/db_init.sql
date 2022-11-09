DROP TABLE bizness CASCADE CONSTRAINTS;
DROP TABLE kategorie CASCADE CONSTRAINTS;
DROP TABLE einkaufswagen CASCADE CONSTRAINTS;
DROP TABLE nutzer CASCADE CONSTRAINTS;
DROP TABLE produkt CASCADE CONSTRAINTS;
DROP TABLE einkaufswagen_produkt CASCADE CONSTRAINTS;
DROP TABLE kategorie_business CASCADE CONSTRAINTS;
DROP TABLE bestellung CASCADE CONSTRAINTS;

CREATE TABLE bizness (
    id INTEGER NOT NULL PRIMARY KEY,
    name varchar(24) NOT NULL,
    addresse varchar(24) NOT NULL,
    telefonnummer varchar(24) NOT NULL UNIQUE,
    ceo varchar(24) NOT NULL,
);

CREATE TABLE kategorie (
    id INTEGER NOT NULL PRIMARY KEY,
    name varchar(24) NOT NULL,
    bild BLOB,
    subkategorie_id INTEGER DEFAULT NULL,
    -- TODO check whether delete cascade on subcategory deletes main category
    FOREIGN KEY (subkategorie_id) REFERENCES kategorie(id) ON DELETE CASCADE

);

CREATE TABLE einkaufswagen (
    id INTEGER NOT NULL PRIMARY KEY,
    anzahl INTEGER
);

CREATE TABLE nutzer (
    id INTEGER NOT NULL PRIMARY KEY,
    vorname varchar(64) NOT NULL,
    nachname varchar(64) NOT NULL,
    anrede varchar(10) CHECK (anrede IN ('Herr', 'Frau')) ENABLE,
    mail varchar(80) NOT NULL UNIQUE,
    einkaufswagen_id INTEGER DEFAULT NULL,
    iban varchar(22) NOT NULL UNIQUE,
    FOREIGN KEY (einkaufswagen_id) REFERENCES einkaufswagen (id) ON DELETE SET DEFAULT
);

CREATE TABLE produkt (
    id INTEGER NOT NULL PRIMARY KEY,
    name varchar(64) NOT NULL,
    preis NUMBER(6, 2) NOT NULL,
    skin varchar(24),
    geschlecht varchar(10) CHECK (geschlecht IN ('Männlich', 'Weiblich')) ENABLE,
    age INTEGER,
    kategorie_id INTEGER DEFAULT NULL,
    bizness_id INTEGER NOT NULL,
    FOREIGN KEY (bizness_id) REFERENCES bizness(id) ON DELETE CASCADE,
    FOREIGN KEY (kategorie_id) REFERENCES kategorie(id) ON DELETE SET DEFAULT
);

CREATE TABLE einkaufswagen_produkt (
    einkaufswagen_id INTEGER NOT NULL,
    produkt_id INTEGER NOT NULL,
    PRIMARY KEY (einkaufswagen_id, produkt_id),
    FOREIGN KEY (einkaufswagen_id) REFERENCES einkaufswagen(id) ON DELETE CASCADE,
    FOREIGN KEY (produkt_id) REFERENCES produkt(id) ON DELETE CASCADE
);

CREATE TABLE kategorie_business (
    kategorie_id INTEGER NOT NULL,
    business_id INTEGER NOT NULL,
    PRIMARY KEY (kategorie_id, business_id),
    FOREIGN KEY (kategorie_id) REFERENCES kategorie(id) ON DELETE CASCADE,
    FOREIGN KEY (business_id) REFERENCES bizness(id) ON DELETE CASCADE
);

CREATE TABLE bestellung (
    id INTEGER NOT NULL PRIMARY KEY,
    bizness_id INTEGER NOT NULL,
    nutzer_id INTEGER NOT NULL,
    bestellstatus varchar(10) NOT NULL,
    bestelldatum DATE NOT NULL,
    lieferaddresse varchar(24) NOT NULL,
    FOREIGN KEY (nutzer_id) REFERENCES nutzer(id) ON DELETE CASCADE,
    FOREIGN KEY (bizness_id) REFERENCES bizness(id) ON DELETE CASCADE
);
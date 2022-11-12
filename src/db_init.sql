/* Online-Marktplatz
    Mehmet Özer         7206358 
    Tobias Barthold     7209370
    Yusuf Bas           7209349 */

DROP TABLE bizness CASCADE CONSTRAINTS  PURGE;
DROP TABLE kategorie CASCADE CONSTRAINTS  PURGE;
DROP TABLE einkaufswagen CASCADE CONSTRAINTS  PURGE;
DROP TABLE nutzer CASCADE CONSTRAINTS  PURGE;
DROP TABLE produkt CASCADE CONSTRAINTS  PURGE;
DROP TABLE einkaufswagen_produkt CASCADE CONSTRAINTS  PURGE;
DROP TABLE kategorie_business CASCADE CONSTRAINTS  PURGE;
DROP TABLE bestellung CASCADE CONSTRAINTS  PURGE;
DROP TABLE business_bestellung CASCADE CONSTRAINTS  PURGE;
DROP TABLE bestellung_produkt CASCADE CONSTRAINTS  PURGE;

CREATE TABLE bizness (
    id INTEGER NOT NULL PRIMARY KEY,
    name varchar(24) NOT NULL,
    addresse varchar(24) NOT NULL,
    telefonnummer varchar(24) NOT NULL UNIQUE,
    ceo varchar(24) NOT NULL,
    iban varchar(22) NOT NULL UNIQUE
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
    anzahl INTEGER,
    nutzer_id INTEGER NOT NULL,

    FOREIGN KEY (nutzer_id) REFERENCES nutzer(id) ON DELETE CASCADE
);

CREATE TABLE nutzer (
    id INTEGER NOT NULL PRIMARY KEY,
    vorname varchar(64) NOT NULL,
    nachname varchar(64) NOT NULL,
    anrede varchar(10) CHECK (anrede IN ('Herr', 'Frau')) ENABLE,
    mail varchar(80) NOT NULL UNIQUE,
    einkaufswagen_id INTEGER DEFAULT NULL,
    iban varchar(22) NOT NULL UNIQUE,
    FOREIGN KEY (einkaufswagen_id) REFERENCES einkaufswagen (id) ON DELETE SET NULL
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
    FOREIGN KEY (kategorie_id) REFERENCES kategorie(id) ON DELETE SET NULL
);

CREATE TABLE einkaufswagen_produkt (
    id INTEGER NOT NULL PRIMARY KEY,
    einkaufswagen_id INTEGER NOT NULL,
    produkt_id INTEGER NOT NULL,

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

CREATE TABLE business_bestellung (
    business_id INTEGER NOT NULL,
    bestellung_id INTEGER NOT NULL,

    PRIMARY KEY (business_id, bestellung_id),
    FOREIGN KEY (business_id) REFERENCES bizness(id) ON DELETE CASCADE,
    FOREIGN KEY (bestellung_id) REFERENCES bestellung(id) ON DELETE CASCADE
)

CREATE TABLE bestellung_produkt (
    id INTEGER NOT NULL PRIMARY KEY,
    bestellung_id INTEGER NOT NULL,
    produkt_id INTEGER NOT NULL,

    FOREIGN KEY (bestellung_id) REFERENCES bestellung(id),
    FOREIGN KEY (produkt_id) REFERENCES produkt(id)
)
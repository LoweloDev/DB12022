/* Online-Marktplatz
    Mehmet Özer         7206358
    Tobias Barthold     7209370
    Yusuf Bas           7209349 */

DROP TABLE unternehmen CASCADE CONSTRAINTS  PURGE;
DROP TABLE kategorie CASCADE CONSTRAINTS  PURGE;
DROP TABLE einkaufswagen CASCADE CONSTRAINTS  PURGE;
DROP TABLE nutzer CASCADE CONSTRAINTS  PURGE;
DROP TABLE produkt CASCADE CONSTRAINTS  PURGE;
DROP TABLE einkaufswagen_produkt CASCADE CONSTRAINTS  PURGE;
DROP TABLE nutzer_einkaufswagen CASCADE CONSTRAINTS  PURGE;
DROP TABLE kategorie_business CASCADE CONSTRAINTS  PURGE;
DROP TABLE bestellung CASCADE CONSTRAINTS  PURGE;
DROP TABLE bestellung_produkt CASCADE CONSTRAINTS  PURGE;

CREATE TABLE unternehmen
(
    id            INTEGER     NOT NULL PRIMARY KEY,
    name          varchar(24) NOT NULL,
    addresse      varchar(24) NOT NULL,
    telefonnummer varchar(24) NOT NULL UNIQUE,
    ceo           varchar(24) NOT NULL,
    iban          varchar(22) NOT NULL UNIQUE
);

CREATE TABLE kategorie
(
    id              INTEGER     NOT NULL PRIMARY KEY,
    name            varchar(24) NOT NULL,
    bild            varchar(64),
    subkategorie_id INTEGER DEFAULT NULL,

    FOREIGN KEY (subkategorie_id) REFERENCES kategorie (id) ON DELETE SET NULL
);

CREATE TABLE einkaufswagen
(
    id     INTEGER NOT NULL PRIMARY KEY
--     Anzahl Deprecated weil belangloses Attribut, da man in hypothetischem Frontend sowieso ein Array von Objekten haben würde um die Liste von Produkten im Warenkorb anzuzeigen.
--     Somit könnte man für die Anzahl einfach Array.length oder ArrayList.size() oder Javascript Equivalent verwenden
--     anzahl INTEGER
);

CREATE TABLE nutzer
(
    id       INTEGER     NOT NULL PRIMARY KEY,
    vorname  varchar(64) NOT NULL,
    nachname varchar(64) NOT NULL,
    anrede   varchar(10) CHECK (anrede IN ('Herr', 'Frau')) ENABLE,
    mail     varchar(80) NOT NULL UNIQUE,
    iban     varchar(22) NOT NULL UNIQUE
);

CREATE TABLE nutzer_einkaufswagen
(
    nutzer_id        INTEGER NOT NULL PRIMARY KEY,
    einkaufswagen_id INTEGER NOT NULL UNIQUE,

    FOREIGN KEY (nutzer_id) REFERENCES nutzer (id) ON DELETE CASCADE,
    FOREIGN KEY (einkaufswagen_id) REFERENCES einkaufswagen (id) ON DELETE CASCADE
);

CREATE TABLE produkt
(
    id             INTEGER     NOT NULL PRIMARY KEY,
    name           varchar(64) NOT NULL,
    preis          NUMBER(6, 2) NOT NULL,
    skin           varchar(24),
    geschlecht     varchar(10) CHECK (geschlecht IN ('Männlich', 'Weiblich')) ENABLE,
    age            INTEGER,
    kategorie_id   INTEGER DEFAULT NULL,
    unternehmen_id INTEGER     NOT NULL,


    FOREIGN KEY (unternehmen_id) REFERENCES unternehmen (id) ON DELETE CASCADE,
    FOREIGN KEY (kategorie_id) REFERENCES kategorie (id) ON DELETE SET NULL
);

CREATE TABLE einkaufswagen_produkt
(
    einkaufswagen_id INTEGER NOT NULL,
    produkt_id       INTEGER NOT NULL,

    PRIMARY KEY (einkaufswagen_id, produkt_id),
    FOREIGN KEY (einkaufswagen_id) REFERENCES einkaufswagen (id) ON DELETE CASCADE,
    FOREIGN KEY (produkt_id) REFERENCES produkt (id) ON DELETE CASCADE
);

CREATE TABLE kategorie_business
(
    kategorie_id INTEGER NOT NULL,
    business_id  INTEGER NOT NULL,
    PRIMARY KEY (kategorie_id, business_id),
    FOREIGN KEY (kategorie_id) REFERENCES kategorie (id) ON DELETE CASCADE,
    FOREIGN KEY (business_id) REFERENCES unternehmen (id) ON DELETE CASCADE
);

CREATE TABLE bestellung
(
    id             INTEGER     NOT NULL PRIMARY KEY,
    unternehmen_id INTEGER     NOT NULL,
    nutzer_id      INTEGER     NOT NULL,
    bestellstatus  varchar(10) NOT NULL,
    bestelldatum   DATE        NOT NULL,
    lieferaddresse varchar(24) NOT NULL,
    FOREIGN KEY (nutzer_id) REFERENCES nutzer (id) ON DELETE CASCADE,
    FOREIGN KEY (unternehmen_id) REFERENCES unternehmen (id) ON DELETE CASCADE
);

CREATE TABLE bestellung_produkt
(
    bestellung_id INTEGER NOT NULL,
    produkt_id    INTEGER NOT NULL,

    PRIMARY KEY (bestellung_id, produkt_id),
    FOREIGN KEY (bestellung_id) REFERENCES bestellung (id) ON DELETE CASCADE,
    FOREIGN KEY (produkt_id) REFERENCES produkt (id) ON DELETE CASCADE
);

INSERT INTO unternehmen(id, name, addresse, telefonnummer, ceo, iban)
values (1, 'Max Musterfrau', 'Sesamstraße', '+493029360122', 'Max Musterfrau', 'DE5044050199350280');
INSERT INTO kategorie(id, name, bild, subkategorie_id)
values (1, 'Hunde', 'C:\Bilder\', NULL);
INSERT INTO einkaufswagen(id)
values (1);
INSERT INTO nutzer(id, vorname, nachname, anrede, mail, iban)
values (1, 'Chris', 'Aufsmaul', 'Herr', 'chris.aufsmaul@hotmail.com', 'DE5044050199350280');
INSERT INTO produkt(id, name, preis, skin, geschlecht, age, kategorie_id, unternehmen_id)
values (1, 'Schäferhund', 650, 'Braun', 'Männlich', 6, 1, 1);
INSERT INTO einkaufswagen_produkt(einkaufswagen_id, produkt_id)
values (1, 1);
INSERT INTO nutzer_einkaufswagen(nutzer_id, einkaufswagen_id)
values (1, 1);
INSERT INTO kategorie_business(kategorie_id, business_id)
values (1, 1);
INSERT INTO bestellung(id, unternehmen_id, nutzer_id, bestellstatus, bestelldatum, lieferaddresse)
values (1, 1, 1, 'Auf Lager', '16-11-22', 'Nordstraße');
INSERT INTO bestellung_produkt(bestellung_id, produkt_id)
values (1, 1);
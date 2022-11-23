# Onlinemarktplatz
#### Mehmet Özer - 7206358
#### Yusuf Bas - 7209349
#### Tobias Barthold - 7209370

<br><br>
Unser Anwendungsszenario behandelt einen Online-Marktplatz auf dem man alle möglichen Tiere und Insekten kaufen kann. Nutzer können sich einen Account machen und sich entsprechend in den Webshop einloggen und dann sofort Tiere kaufen oder an Versteigerungen teilnehmen. Wir nutzen insgesamt 6 Integritätsbedingungen in diesem Kontext, zum einen NOT NULL, weil gewisse Felder wie z. B. primary keys oder auch der Name bei einem Kunden oder bspw. Die IBAN immer angegeben werden müssen. Wir nutzen auch UNIQUE was dafür sorgt, dass Felder wie z.B. die IBAN oder die Telefonnummer immer eindeutige Werte haben.
Bei dem Geschlecht der Tiere haben wir einen CHECK eingebaut der überprüft ob die Eingabe Männlich oder Weiblich ist, wenn nicht wird sie nicht angenommen.
Beim löschen von Einträgen verwenden wir auch Foreign Key Constraints z. B. ON DELETE CASCADE oder ON DELETE SET DEFAULT, sodass u.a. bei unserem Produkt, also dem Tier, dieses Tier gelöscht wird wenn das Unternehmen gelöscht wird was selbiges verkauft, oder aber die kategorie_id auf ihren DEFAULT Wert zurückgesetzt wird (in dem Fall NULL) wenn eine Kategorie gelöscht wird in der das Tier zu finden war. Für unser wiederholt ausführbares Installationsskript verwenden wir zum löschen der Tabellen auch CASCADE CONSTRAINTS, da sonst beim Löschen von Tabellen die auf Primary Keys von anderen Tabellen referenzieren um einen Foreign Key zu befüllen ein fehler geschmissen wird. Diese Integritätsbedingung sorgt also dafür dass die CASCADE CONSTRAINTS die wir angelegt haben ebenfalls gelöscht werden dürfen.

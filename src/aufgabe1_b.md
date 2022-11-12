Wir haben zum einen ein Unternehmen, ein Unternehmen kann natürlich mehrere Produkte also in dem Fall Tiere haben. In unserem Fall haben wir so entschieden,
dass ein Produkt in unserem Fall einzigartig ist weil es ein Lebewesen ist und man ein Tier in der Regel ja nicht einfach nach Rasse und Fell auswählt, sodnern auch nach
Namen und/oder Persönlichkeit. Entsprechend eine 1:N Beziehung von Unternehmen zu Produkt/Tier.
Ein Unternehmen kann mehrere Kategorien von Tieren/Produkten bewirtschaften. Aber eine Kategorie wie z.B.
Raubtiere kann natürlich auch von mehreren Unternehmen befüllt werden.
Dementsprechend ist dies eine N:M (Many-to-Many) Beziehung.
Bestellungen können natürlich viele verschiedene Produkte enthalten.
Trotzdem gehört eine Bestellung immer genau zu einem Unternehmen, da wir beim Kauf
die Produkte aus dem Warenkorb nehmen, über das Produkt dann herausfinden zu welchem Unternehmen es gehört und dies dann in eine Bestellung packen, die zu dem Nutzer und dem Unternehmen gehört.
Entsprechend ist dies eine 1:N Beziehung von Unternehmen zu Bestellung.
Produkte also Tiere müssen nicht unbedingt eine Kategorie haben, das macht es auch einfacher Kategorien zu bearbeiten/verwalten. Im Frontend würde dann einfach für kategory_id == NULL "other" angezeigt werden.
Eine Kategorie kann natürlich mehrere Produkte haben, ansonsten wäre Kategoriesierung nutzlos.
Bei einem Produkt würde man annehmen es könne in mehreren Einkaufswagen vorkommen, in dem man 10x das gleiche Produkt kauft.
In unserem Fall ist das nicht möglich, weil unsere Produkte ja einzigartige Tiere sind, entsprechend kann man natürlich nicht
10x den exakt selben Hund in den Einkaufswagen legen, somit ist Produkt zu Einkaufswagen eine N:1 (Many-to-One) Beziehung anstatt eine N:M.
Ein Nutzer hat immer einen Einkaufswagen der Befüllt und beim Kauf geleert wird.
Nutzer können auch mehrere Bestellungen aufgeben, allerdings kann eine Bestellung nur von jeweils einem einzelnen Nutzer aufgegeben werden.
Eine Kategorie kann eine rekursive Beziehung zu einer anderen Kategorie haben, da eine Kategorie auch beliebig viele Subkategorien haben kann.
Z.B. können Hunde Subkategorien wie Dalmatiner oder Deutscher Schäferhund haben.


# Wichtig wegen Fremdschlüssel Erklärungen anpassen
# Neues RElationales Modell
# Logikfehler fixen siehe Todo comments
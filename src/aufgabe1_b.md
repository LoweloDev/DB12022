![Modell](assets/Relationales_Modell.png)

Wir haben zum einen ein Unternehmen, ein Unternehmen kann natürlich mehrere Produkte also in dem Fall Tiere haben. In unserem Fall haben wir so entschieden,
dass ein Produkt in unserem Fall einzigartig ist weil es ein Lebewesen ist und man ein Tier in der Regel ja nicht einfach nach Rasse und Fell auswählt, sodnern auch nach
Namen und/oder Persönlichkeit. Entsprechend eine 1:N Beziehung von Unternehmen zu Produkt/Tier. Somit ist bei Produkt ein Fremdschlüssel der auf den Primary-Key von einem Business referenziert.
Ein Unternehmen kann mehrere Kategorien von Tieren/Produkten bewirtschaften. Aber eine Kategorie wie z.B.
Raubtiere kann natürlich auch von mehreren Unternehmen befüllt werden. 
Dementsprechend ist dies eine N:M (Many-to-Many) Beziehung und wir brauchen einen Junction-Table in dem 2 Fremdschlüssel sind die jeweils auf den PK der betroffenen Tabelleneinträge referenzieren.
Bestellungen können natürlich viele verschiedene Produkte enthalten.
Trotzdem gehört eine Bestellung immer genau zu einem Unternehmen, da wir beim Kauf
die Produkte aus dem Warenkorb nehmen, über das Produkt dann herausfinden zu welchem Unternehmen es gehört und dies dann in eine Bestellung packen, die zu dem Nutzer und dem Unternehmen gehört.
Entsprechend ist dies eine 1:N Beziehung von Unternehmen zu Bestellung und eine Bestellung hat einen Fremdschlüssel der auf die ID des Business referenziert.
Produkte also Tiere müssen nicht unbedingt eine Kategorie haben, das macht es auch einfacher Kategorien zu bearbeiten/verwalten. Im Frontend würde dann einfach für kategory_id == NULL z.B. "other" angezeigt werden.
Eine Kategorie kann natürlich mehrere Produkte haben, ansonsten wäre Kategoriesierung nutzlos. Somit exisitert ein Fremdschlüssel im Produkt der auf die Kategorie verweist und auch NULLABLE ist.
Ein Produkt kann in mehreren Einkaufswagen vorkommen aber es dürfen keine Duplikate vorkommen weil man nicht 10x den selben Hund kaufen kann, unsere Produkte sind mehr oder weniger einzigartig. Daher wird hier
abgesehen von den Fremdschlüsseln im Junction-Table der Many-to-Many Beziehung auch ein Composite (Primary) Key aus den beiden Referenzen gebaut.   
Ein Nutzer hat immer einen Einkaufswagen der Befüllt und beim Kauf geleert wird. Dies ist eine 1:1 Beziehung. In dem Fall haben wir dies auch mit einem Junction Table und einem Primary-Key aus einem Foreign-Key sowie einem Foreign-Key mit separatem UNIQUE Constraint realisiert.
Nutzer können auch mehrere Bestellungen aufgeben, allerdings kann eine Bestellung nur von jeweils einem einzelnen Nutzer aufgegeben werden. Daher hat die Bestellung einen Fremdschlüssel der auf den Nutzer verweist der sie auf gab.
Eine Kategorie kann eine rekursive Beziehung zu einer anderen Kategorie haben, da eine Kategorie auch beliebig viele Subkategorien haben kann.
Z.B. können Hunde Subkategorien wie Dalmatiner oder Deutscher Schäferhund haben. Somit kann eine Kategorie einen Fremdschlüssel haben der auf eine andere Kategorie verweist.


# Neues RElationales Modell
# Logikfehler fixen siehe Todo comments
# VSA-test-2 (2018)

## Zadanie

POZOR! Pri vytvaraní servisu je potrebné dodržať:
- Resource package: rest
- Path: skuska

Servis bude poskytovať prístup k stromu resoursov s hodnoteniami študentov v piatich predmetoch.
- Každý z predmetov je reprezentovaný ako subresours s URI skuska/{pid}, kde pid je identifikátor
predmetu. Platné identifikátory predmetov, ktoré sprístupňuje servis sú reťazce: “VSA”, “DBS”, “AZA”, “SWI” a “RAL”.

Hodnotenie študenta v danom predmete je dostupné pod URI skuska/{pid}/{sid}, kde pid je
identifikátor predmetu a sid je identifikátor študenta.
- Hodnotenie je neprázdny reťazec.
- Identifikátor študenta je neprázdny reťazec.


Pre koreňové URL skuska implementujte metódu pre GET, pričom sa predpokladá, že v požiadavke GET
bude zadaný buď identifikátor predmetu ako hodnota parametra predmet alebo identifikátor študenta ako
hodnota parametra student (dva @QueryParam).
- Ak je zadaný identifikátor predmetu, metóda a vráti počet študentov, ktorí majú vložené hodnotenie z daného predmetu. Pozn. Ak je okrem identifikátora predmetu zadaný aj identifikátor študenta, identifikátor študenta sa ignoruje.
- Ak je zadaný len identifikátor študenta, metóda a vráti počet predmetov, pre ktoré má študent vložené hodnotenie.
- Ak hodnota parametra predmet neudáva platný identifikátor predmetu, vráti reťazec “NEPLATNY PREDMET”
- Ak požiadavka nemá zadaný ani identifikátor predmetu ani identifikátor študenta, vráti reťazec “CHYBA”

Pre URL skuska/{pid}/{sid} implementujte metódy:

- GET: vyhľadá a vráti hodnotenie študenta s identifikátorom sid z predmetu s identifikátorm pid (MIME: text/plain).
	- Ak študent nemá ešte hodnotenie z predmetu, vráti reťazec “NEMA”.
	- Ak zadaný identifikátor predmetu nie je platný vráti buď reťazec “NEPLATNY PREDMET” (alebo chybu HTTP 404 príp. HTTP 204).

- PUT: Zadá resp. zmení hodnotenie študenta sid v predmete pid. Hodnotenie študenta dostane v obsahu požiadavky (MIME: text/plain).
	- Ak študent už má hodnotenie v predmete, zmení ho.
	- Ak študent ešte nemá hodnotenie v predmete, vytvorí resourse s hodnotením.

- DELETE: odstráni hodnotenie študenta sid v predmete pid
	- Ak študent v danom predmete ešte hodnotenie alebo identifikátor predmetu nie je platný, nerobí nič.

---
## Implementácia

```java

```
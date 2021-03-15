## Zápočet z roku (asi) 2017 

Vytvorte nový java application projekt **Zadanie** a v ňom program, ktorý prečíta údaje o publikáciách z csv-súboru **[data1.csv](http://www.kaivt.elf.stuba.sk/Predmety/B-VSA/CV2?action=AttachFile&do=view&target=data1.csv "data1.csv")** a vloží ich do tabulky **KNIHA** so stĺpcami:

-   **nazov** VARCHAR(40) - _klúč_
    
-   **autor** VARCHAR(80)
    
-   **pocet** INTEGER
    

Tabuľku môžete vytvoriť manuálne a z nej vygenerovať odpovedajúcu entitnú triedu alebo naopak implementovať najprv entitnú triedu a z nej nechať vygenerovať tabuľku.

-   Pri importe údajov z csv-súboru použite ako
    -   názov údaje z 1. stĺpca vstupného súboru,
    -   autor údaje 2. a
    -   pocet údaje z 3. stĺca.
-   Keďže nazov je klúč, nesmie byť prázdny a musí byť jedinečný. Kontrolu toho, či sa v 1. stĺpci vstupného súboru nenachádzajú duplicity urobí program ešte pred tým než záznam vloží do databázy. **Návod:** pre kontrolu duplicít použite kontainer **java.util.set**.
    
-   Pred vložením textových údajov odstráňte z reťazcov prázdne znaky pred a za. Prázdne reťazce do databázy nevkladajte.

Pomôcka pre čítanie a parsovanie súboru:
```java
String line;
BufferedReader br = new BufferedReader(new FileReader("/home/igor/Downloads/data1.csv"));
while ((line = br.readLine()) != null) {
	String s\[\] = line.split(";");
	if (s.length < 3) {
		System.out.println("kratky riadok");
		return;
	}
    String  nazov = s\[0\].trim();
```
## Otestované unit testami 
```
Results :

Tests run: 1, Failures: 0, Errors: 0, Skipped: 0
```

---

## Zápočet z roku 2018 
-dostupný na: http://www.kaivt.elf.stuba.sk/Predmety/B-VSA/CV3testA

Príklad dat na testovanie: http://www.kaivt.elf.stuba.sk/Predmety/B-VSA/CV3testA?action=AttachFile&do=view&target=data.csv

```
Implementačná úloha

Vytvorte nový java aplication projekt s názvom uloha1.
Pozn. Netbeans vám v projekte vytvorí balík uloha1 a v ňom triedu Uloha1.
 
V balíku úloha vytvorte  v entitnú triedu Tovar s atribútmi:
	Long id; 
	String nazov;
	double cena;
Klúčový atribút id  môže ale nemusí byť autogenerovaný.

Pre entitnú triedu Tovar vytvorte v databáze tabuľku TOVAR, 
t.j. tabuľka bude mať stĺpce:
	ID 	PK
	NAZOV
	CENA

Pozn. Najprv môžete vytvoriť manuálne entitnú triedu a databázovu tabuľku potom automaticky vygenerovať programom pomocou JPA, alebo naopak najprv namuálne vytvoríte v databázovú tabuľku a entitnú triedu z nej vygenerovať wizardom.

V triede Uloha1 vytvorte  metódu:

public static void load(String csv)

ktorá ako parameter dostane meno csv-súboru, obsahujúceho údaje o tovaroch, ktoré treba vložiť do databázy.  Csv súbor má dva stĺpce oddelené bodkočiarkou. 
	V prvom stĺpci je názov tovaru, 
	v druhom číslo udávajúce cenu tovaru.

Funkcia load číta súbor po riadkoch a vkladá nové záznamy s názvom a cenou tovaru do databázy. 

Pred vložením textových údajov odstráňte z reťazcov prázdne znaky pred a za. Prázdne reťazce do databázy nevkladajte.
Hoci názov tovaru neslúži ako klúč do tabuľky, chceme tiež aby bol jedinečný, preto funkcia pri vkladaní tiež kontroluje, či tovar s rovnakým názvom už nebol do databázy vložený skôr, Ak už bol, druhý krát ho ignoruje. Môžete predpokladať, že v tabuľka je na začiatku prázdna, teda  stačí kontrolovať, či sa duplicitné názvy nenachádzajú vo vstupnom súbore.

Nezabudnite:
•	pridať do projektu library: Java DB driver
•	spustiť databázový server
•	začať a ukončiť (commit) transakcie

Odporúčania a návody: 
•	Ukážka čítania parsovania csv-súboru:
	String line;
	BufferedReader br = new BufferedReader(new FileReader(filename));
while ((line = br.readLine()) != null) {
	   String s[] = line.split(";");
                ...
•	Pre odstránenie prázdnych znakov použite reťazcovú metódu str.trim().
•	Pre konverziu reťazca na hodnotu double použite: 
double d = Double.parseDouble(str);
•	pre kontrolu duplicitných tovarov môžete použiť napr. kontainer java.util.set.
•	pre vlastné otestovanie funkčnosti môžete použiť funkciu main:
    	public static void main(String[] args) throws IOException {
        	    load("/home/data.csv");
    	}
Vzorový csv-súbor data.csv  pre otestovanie je pripojený k zadaniu.


Pokyny pre odovzdanie.
Odovzdávajú sa do AISu dva zdrojové súbory:
•	Uloha1.java
•	Tovar.java
```

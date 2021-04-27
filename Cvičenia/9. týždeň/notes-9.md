## RESTful servis s dátovými objektami
---
### RESTful servis jedálne – application/xml

Vytvorte RESTful servis, ktorý bude poskytovať informácie o jedál ponúkaných v jedálni. Informacie o jedle bude
poskytovať formáte xml so štruktúrou:

```xml
<jedlo>
<cena>3.5</cena>
<nazov>gulas</nazov>
</jedlo>
```

- Jednoltivé jedlá v ponuke dňa budú prístupné ako subresoursy s URL menu/{n} kde n je poradové číslo jedla v ponuke, pričom jedlá sú číslované za sebou od 0.

- Pre URL ponuky dňa menu implementujte metódy:
- GET pre MIME text/plain: vráti aktuálny počet jedál v menu .

- POST MIME application/xml. Očakáva xml s informáciami o -novom jedle, a zaradí ho do menu.
- Vráti poradové číslo (MIME text/plain)

- Pre URL jedla menu/{n} implementujte metódy:
	- GET MIME application/xml: vráti xml s informaciami o jedle
	- DELETE: odstráni jedlo s poradovým číslom n. (Pozn. Po odstránení jedla sa poradové čisla jedál za ním znížia)

#### Web App

```java
@Singleton
@Path("menu")
public class MenuResource {
    
    @Context
    private UriInfo context;
    
    private Menu menu;
    
    public MenuResource() {

        this.menu = new Menu();
          menu.getJedla().add(new Jedlo ("Vyprazany syr", 3.5));
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPocet() {
        
//        return "" + jedla.size();
            return "" + menu.getJedla().size();
    }
    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Menu getMenu() {
        
        return menu;
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String pridajJedlo(Jedlo content) {
        
        menu.getJedla().add(content);
        return "" + (menu.getJedla().size()-1);
    }
    
    @GET
    @Path("{cislo}")
    @Produces(MediaType.APPLICATION_XML)
    public Jedlo getJedlo(@PathParam("cislo") int n) {
        
        if (n < 0 || n >= menu.getJedla().size()) {
            return null;
        }
        return menu.getJedla().get(n);
    }
    
    @DELETE
    @Path("{cislo}")
    public void odstranJedlo(@PathParam("cislo") int n) {
        
        if (n < 0 || n >= menu.getJedla().size()) {
            return;
        }
        menu.getJedla().remove(n);
    }
}
```

#### Package Data
##### Jedlo

```java
package data;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement         
// hovori o tom, ze tuto datovu triedu chceme serializovat do xml
public class Jedlo {
    
    private String nazov;
    private double cena;

    public Jedlo() {
    }

    public Jedlo(String nazov, double cena) {
        this.nazov = nazov;
        this.cena = cena;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public double getCena() {
        return cena;
    }

    public void setCena(double cena) {
        this.cena = cena;
    }
    
}
```

##### Menu

```java
package data;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Menu {
    
    private List<Jedlo> jedla;

    public Menu(List<Jedlo> jedla) {
        this.jedla = jedla;
    }
    
     public Menu() {
        this.jedla = new ArrayList<>();
    }
    
    public List<Jedlo> getJedla() {
        return jedla;
    }

    public void setJedla(List<Jedlo> jedla) {
        this.jedla = jedla;
    }
}
```


---
### Úloha 1.

Definujte triedu **Jedlo** s atribútmi:

-   String nazov;
-   double cena;

A implementujte RESTful servis pre strom resoursov poskytujúcich informácie o jedlách v ponuke:

-   root resource ma URI **menu**
    
-   jednotlivé položky (jedlá) v ponuke majú URI **menu/{index}**, kde index je poradové číslo (od 1) položky.
    

**Implementujte nasledujúce metódy:** Pre root resource

-   GET: Vráti primerane skrátený textový obsah ponuky.
-   POST: Pridá nové jedlo do ponuky a **vráti jeho index**.
    

Pre jednotlivé položky v ponuke:

-   GET: Vyhľadá položku podľa indexu zadaného v URI a vráti ju (ako objekt triedy Jedlo).
-   PUT: Ako vstup dostane objekt triedy Jedlo, vyhľadá položku podľa indexu zadaného v URI a aktualizuje informácie jedle na základe vstupu.
-   DELETE: odstráni položku (jedlo) z menu, pričom po odstránení jedla:
    
    -   variata a) sa URI (indexy) jedál s vyšším indexom posunú (znížia o 1).
    -   variata b) sa URI (indexy) ostatných jedál nezmenia
        -   Pozn. Implementáciu varianty b) nájdete [TU](http://www.kaivt.elf.stuba.sk/Predmety/B-VSA/P10-RESTaJPA)
            
    
    zvážte, ktorá varianta je logickejšia resp. jednoduchšia z hladiska implementácie

**Vytvorte java-klienta servisu**, ktorý overí funkčnosť servisu.

```java

```

---
### Úloha 2.

V úlohe 1. URI jedla obsahuje index, ktorý určuje pozíciu jedla v ponuke. Servis poskytujúci informácie o jedlách by sme však mohli navrhnúť aj inak: URI jedla by namiesto pozície jedla v menu obsahovalo priamo jeho názov.

Definujte triedu _Jedlo_ rovnako ako v úlohe 1 a implementujte RESTful servis poskytujúci informácie o jedlách:

-   root resource ma URI **menu**
    
-   jednotlivé jedlá v ponuke majú URI **menu/{name}**, kde name je názov jedla.
    

**Implementujte nasledujúce metódy:**

Pre jednotlivé jedlá v ponuke:

-   GET: Vyhľadá jedlo podľa názvu zadaného v URI a vráti objekt triedy Jedlo
-   PUT: Vyhľadá jedlo podľa názvu zadaného v URI aktualizuje informácie jeho dáta. Ak jedlo s daným názvom ešte neexistuje môžete ho vytvoriť (ako alternatíva pre POST) alebo nerobiť nič.
    -   **POZOR!** Názov jedla je PUT-requeste teraz uvedený 2x, v URI aj v dátovom objekte. Zvážte ako by bolo vhodné riešiť situáciu ak by boli tieto názvy rôzne.
        
-   DELETE: Vyhľadá jedlo podľa názvu zadaného v URI a ostráni ho. (všimnite si, že teraz žiadny problém s URI ostatných jedál nenastane)

Pre root resource

-   GET: Vráti primerane skrátený textový obsah ponuky.
-   POST: Pridá nové jedlo do ponuky. Zvážte ako vhodne riešiť situáciu ak jedlo s daným názvom už v ponuke je.

```java
// TODO
```
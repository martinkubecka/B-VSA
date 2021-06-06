## RESTful servis jedálne

- Vytvorte RESTful servis, ktorý bude poskytovať informácie o jedál v aktuálne ponuke jedálne.

- Jednoltivé jedlá v ponuke dňa budú prístupné ako subresoursy s URL `menu/{n}`
kde n je poradové číslo jedla v ponuke, pričom jedlá sú číslované za sebou od 0.

### Prvá časť

- Pre URL ponuky dňa menu implementujte metódy:
	- **GET** pre MIME text/plain: vráti aktuálny počet jedál v menu.
	- **POST** pre MIME text/plain: pridá nové jedlo do menu.

- Pre URL jedla `menu/{n}` implementujte metódy:
	- **GET** pre MIME text/plain: vráti názov jedla s poradovým číslom n.
	- **DELETE**: odstráni jedlo s poradovým číslom n. (Pozn. Po odstránení jedla sa poradové čisla jedál za ním znížia)

- Implementujte metódu **POST** tak aby vrátila poradové číslo nového jedla.

#### Web App

```java
@Singleton
@Path("menu")
public class GenericResource {
    
    @Context
    private UriInfo context;
    
    private List<String> jedla;
    
    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() {
        
        jedla = new ArrayList<>();
        jedla.add("Losos");
        jedla.add("Pizza");
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getPocet(){
        
        return "" + jedla.size();
    }
    
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String pridajJedlo(String content) {
        
        jedla.add(content);
        
        return "" + (jedla.size()-1);
    }
    
    @GET
    @Path("{cislo}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getJedlo(@PathParam("cislo") int n){
        
        if (n < 0 || n >= jedla.size()) {
            return null;
        }
        
        return "" + jedla.get(n);
    }
    
    @DELETE
    @Path("{cislo}")
    public void odstranJedlo(@PathParam("cislo") int n) {
        
        if (n < 0 || n >= jedla.size()) {
            return;
        }
        
        jedla.remove(n);
    }
}
```

#### Client

```java
 public static void main(String[] args) {
        
        NewJerseyClient client = new NewJerseyClient();
        
        client.pridajJedlo("Gulas");
        client.pridajJedlo("Rezen");
        String pocet = client.getPocet();
        System.out.println("  pocet jedal: " + pocet);

        int n = Integer.parseInt(pocet);
        for (int i=0; i<n; i++) {
            System.out.println(client.getJedlo("" + i));
        }
        System.out.println("  odstranim jedlo 0...");
        client.odstranJedlo("0");
        System.out.println(client.getJedlo("0")); 
    }
```

---

### Druhá časť

- Vytvorte RESTful servis, ktorý bude poskytovať popis jedál v aktuálnej ponuke jedálne.
- (Popis jedla je textový reťazec obsahujúcie bližšie informácie o jedle, jeho cenou, alergény....)
- Popis jedla bude prístupný ako subresource s URL `menu/{nazov}` kde nazov je názov jedla.

- Pre URL ponuky dňa menu implementujte metódy:
	- **GET** pre MIME text/plain: vráti reťazec obsahujuci názvy jedál oddelené medzerou.
	- Pozn. Namiesto **POST** pre menu pridavame nový resource s **PUT** pre subresource.

- Pre URL jedla `menu/{nazov}` implementujte metódy:
	- **GET** pre MIME text/plain: vráti popis jedla s daným názvom .
	- **PUT** pre MIME text/plain: zmeni popis jedla s daným názvom alebo pridá nove jedlo - namiesto POST
	- **DELETE**: odstráni z menu jedlo s daným názvom.

#### Web App

```java
@Singleton
@Path("menu")
public class MenuResource {
    
    @Context
    private UriInfo context;
    
    private Map<String, String> jedla;
    
    public MenuResource() {
        
        jedla = new HashMap<>();
        jedla.put("Syr", "Vyprazany syr, 3.50");
    }
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMenu() {
        
        String menu = "";
        
        for (String jedlo : jedla.keySet()) {
            
            menu += jedlo + " ";
        }
        return menu;
    }
    
    @GET
    @Path("{nazov}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getJedlo(@PathParam("nazov") String n) {
        
        return jedla.get(n);
    }
    
    @PUT
    @Path("{nazov}")
    @Consumes(MediaType.TEXT_PLAIN)
    public void pridajJedlo(@PathParam("nazov") String n, String content) {
        
        jedla.put(n, content);
    }
    
    @DELETE
    @Path("{nazov}")
    public void odstranJedlo(@PathParam("nazov") String n) {
        
        jedla.remove(n);
    }
}
```

---
### Tretia časť

- Rozšírte tretiu časť na servis, ktorý bude poskytovať informácie o jedál ponúkaných v jedálni na celý týždeň.
- Menu s ponukou pre jeden deň bude pristupné ako resource s URL `menu/{den}`
kde den je meno dňa v týždni. 
- Platné mená dní sú reťazce: pondelok, utorok, streda, stvrtok, piatok
- Jednoltivé jedlá v ponuke dňa budú prístupné ako subresoursy s URL `menu/{den}/{nazov}` kde nazov je názov jedla.
- Okrem toho pre koreňový resource menu implementujte **GET** metódu, ktorá dostane názov jedla ako parameter dotazu nazov (`@QueryParam(“nazov”)`) a vráti reťazec s menami dní kedy je dané jedlo v ponuke.

- Implementujte servis tak aby, jedlá s rovnakým názvom :
	1. museli mať ten aj rovnaký popis vo všetkých dňoch, keď sú v ponuke. (T.j ak popis zmeniíte v jeden deň bude zmenený aj v ostatných dňoch.)
	2. mohli mať v každý deň vlastný špecifický popis a cenu


#### Web App

```java
@Singleton
@Path("menu")
public class MenuResource {
    
    @Context
    private UriInfo context;
    
    private Map<String, Map<Integer, String>> dni;
    
    public MenuResource() {
        
        dni = new HashMap<>();
        
        Map<Integer, String> pondelokMenu = new HashMap<>();
        pondelokMenu.put(1, "Salat");
        pondelokMenu.put(2, "Gulas");
        Map<Integer, String> utorokMenu = new HashMap<>();
        utorokMenu.put(1, "Salat");
        utorokMenu.put(2, "Cestoviny");
        Map<Integer, String> stredaMenu = new HashMap<>();
        stredaMenu.put(1, "Salat");
        stredaMenu.put(2, "Ryba");
        Map<Integer, String> stvrtokMenu = new HashMap<>();
        stvrtokMenu.put(1, "Salat");
        stvrtokMenu.put(2, "Syr");
        Map<Integer, String> piatokMenu = new HashMap<>();
        piatokMenu.put(1, "Salat");
        piatokMenu.put(2, "Palacinky");
        piatokMenu.put(3, "Syr");
        
        dni.put("pondelok", pondelokMenu);
        dni.put("utorok", utorokMenu);
        dni.put("streda", stredaMenu);
        dni.put("stvrtok", stvrtokMenu);
        dni.put("piatok", piatokMenu);
    }
    
    // Menu s ponukou pre jeden deň bude pristupné 
	// ako resource s URL menu/{den} kde den je meno dňa v týždni. 
	// Platné mená dní sú reťazce: pondelok, utorok, streda, stvrtok, piatok
    @GET
    @Path("{den}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDenneMenu(@PathParam("den") String den) {
        
        if (dni.containsKey(den)){
            
            Map<Integer, String> denneMenu = dni.get(den);
            
            String menuString = "";
            
            for(Map.Entry<Integer, String> jedlo : denneMenu.entrySet()){
                
                menuString += jedlo.getValue() + ", ";
            }
            
            return menuString;
        }
        
        return null;
    }
    
    // Jednoltivé jedlá v ponuke dňa budú prístupné
	// ako subresoursy s URL menu/{den}/{nazov} kde nazov je názov jedla.
    @GET
    @Path("{den}/{nazov}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getJedlo(@PathParam("den") String den, @PathParam("nazov") String jedlo) {
        
        if (den != null && jedlo != null){
            
            if (dni.containsKey(den)){
                
                Map<Integer, String> denneMenu = dni.get(den);
                
                for(Map.Entry<Integer, String> jedloMenu : denneMenu.entrySet()){
                    
                    if (jedloMenu.getValue().equals(jedlo)){
                        
                        return jedloMenu.getValue();
                    }
                }
            }
        }
        
        return null;
    }
    
    // Okrem toho pre koreňový resource menu implementujte GET metódu, 
	// ktorá dostane názov jedla ako parameter
    // dotazu nazov (@QueryParam(“nazov”)) a vráti reťazec 
	// s menami dní kedy je dané jedlo v ponuke.
    // http://localhost:8080/Jedalen2/webresources/menu/par?nazov=Salat
    @GET
    @Path("par")
    @Produces(MediaType.TEXT_PLAIN)
    public String findJedlo(@QueryParam("nazov") String jedlo){
        
        if (jedlo != null){
            
            String ponuka = "";
            
            for (Map.Entry<String, Map<Integer, String>> den : dni.entrySet()){
                
                Map<Integer, String> denneMenu = den.getValue();
                
                for (Entry<Integer, String> jedla : denneMenu.entrySet()){
                    
                    if (jedla.getValue().equals(jedlo)){
                        
                        ponuka += den.getKey() + " ";
                    }
                }
            }
            
            return ponuka;
        }
        
        return null;
    }
	
	// DELETE pre URI jedla - odstráni jedlo z menu.
    // Pozor! odstánenie implementujte tak aby sa URI ostatných jedál nezmenilo.
    @DELETE
    @Path("{den}/{index}")
    public void deleteJedlo(@PathParam("den") String den, @PathParam("index") String index){
        
        if ((den != null) && (index != null)){
            Map<Integer, String> denneMenu = dni.get(den);
            
            if (denneMenu != null){
                
                String jedlo = denneMenu.get(Integer.parseInt(index));
                
                if(jedlo != null){
                    
                    denneMenu.remove(Integer.parseInt(index));
                }
                
                dni.put(den, denneMenu);
            }
        }
    }
	
	// PUT pre URI jedla - modifikuje názov jedla. Pozor! ak také jedlo neexistuje neurobí nič.
    @PUT
    @Path("{den}/{index}")
    @Consumes(MediaType.TEXT_PLAIN)
    public void updateJedlo(@PathParam("den") String den, @PathParam("index") String index, String content){
        
        if ((den != null) && (index != null)){
            
            Map<Integer, String> denneMenu = dni.get(den);
            
            if (denneMenu != null){
                
                String jedlo = denneMenu.get(Integer.parseInt(index));
                
                if(jedlo != null){
                    
                    denneMenu.put(Integer.parseInt(index), content);
                }
                
                dni.put(den, denneMenu);
                
            }
        }
    }
}
```

---
## RESTful servis slovníka

Implementujte RESTful servis ktorý funguje ako prekladový slovník z angličtiny do viacerých jazykov:

-   URI **slovnik/{lang}**, kde lang je meno jazyka, reprezentuje prekladový slovnik z angličtiny do tohto jazyka.
    
-   URI **slovnik/{lang}/{word}**, kde word je anglický výraz, reprezentuje preklad tohto výrazu do daného jazyka.
    

Implementujte metódy:

-   GET pre koreňové URI slovnik - vráti reťazec obsahujúci zoznam podporovaných jazykov
-   GET pre URI slovnik/{lang}/{word} - vráti preklad slova word do jazyka lang.
-   PUT pre URI slovnik/{lang}/{word} - modifikuje preklad slova word do jazyka lang.
    -   Ak preklad daného slova ešte v slovníku nie je pridáme ho tam.
    -   Ak pre daný jazyk ešte nemáme vytvorený slovník vytvoríme ho.
-   DELETE pre URI slovnik/{lang}- odstráni celý prekladový slovník pre daný jazyk
-   DELETE pre URI slovnik/{lang}/{word}- odstráni preklad slova zo slovníka daného jazyka

Vypracoval [Viliam](https://github.com/V1l1am). 

```java
@Singleton
@Path("slovnik")
public class GenericResource {

    @Context
    private UriInfo context;

    private Map<String, Map<String,String>> slovnik;
    
    public GenericResource() {
        Map<String,String> svk = new HashMap<>();
        svk.put("dog", "pes");
        svk.put("cat", "macka");
        Map<String,String> pl = new HashMap<>();
        pl.put("exorcist", "egzorcysta");
        slovnik = new HashMap<>();
        slovnik.put("PL", pl);
        slovnik.put("SVK", svk);
    }

    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getAll() {
        String ret = "";
        for (Map.Entry<String, Map<String, String>> entry : slovnik.entrySet()) {
            ret =ret+ entry.getKey()+", ";
            
        }
        return ret;
    }
    
    
    @GET
    @Path("{lang}/{word}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getWord(@PathParam("lang") String lang, @PathParam("word") String word) {
        if(!slovnik.containsKey(lang))
            return null;
        Map<String, String> mapa = slovnik.get(lang);
        if(!mapa.containsKey(word))
            return null;
        return mapa.get(word);
    }
    
    @PUT
    @Path("{lang}/{word}")
    @Consumes(MediaType.TEXT_PLAIN)
    public void putWord(@PathParam("lang") String lang, @PathParam("word") String word, String content) {
        if(!slovnik.containsKey(lang)){
            Map<String, String> tmp = new HashMap<>();
            slovnik.put(lang, tmp);
        }
        Map<String, String> mapa = slovnik.get(lang);
        mapa.put(word, content);
    }
    
    @DELETE
    @Path("{lang}")
    public void removeSlovnik(@PathParam("lang") String lang) {
        slovnik.remove(lang);
    }
    
    @DELETE
    @Path("{lang}/{word}")
    public void removePreklad(@PathParam("lang") String lang, @PathParam("word") String word) {
        if(!slovnik.containsKey(lang))
            return;
        Map<String, String> mapa = slovnik.get(lang);
        mapa.remove(word);
    }
}
```
## 2. Zápočet

### MenuResource.java

```java
package rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("menu")
public class MenuResource {
    
    private Map<String, Ponuka> menu;
    
    public MenuResource() {
        
        menu = new HashMap<>();
        
        Ponuka pondelok = new Ponuka("pondelok");
        Ponuka utorok = new Ponuka("utorok");
        Ponuka streda = new Ponuka("streda");
        Ponuka stvrtok = new Ponuka("stvrtok");
        Ponuka piatok = new Ponuka("piatok");
        Ponuka sobota = new Ponuka("sobota");
        Ponuka nedela = new Ponuka("nedela");
        
        // PONDELOK
        pondelok.getJedlo().add(new Jedlo(3.5 , "gulas"));
        
        menu.put("pondelok", pondelok);
        menu.put("utorok", utorok);
        menu.put("streda", streda);
        menu.put("stvrtok", stvrtok);
        menu.put("piatok", piatok);
        menu.put("sobota", sobota);
        menu.put("nedela", nedela);
    }
    
    /* ------------------------------------------------------- */
    // Pre URL ponuky dňa  menu/{den}implementujte metódy:
    
    // GET pre MIME text/plain: vráti aktuálny počet jedál v menu pre daný deň
    @GET
    @Path("{den: pondelok|utorok|streda|stvrtok|piatok|sobota|nedela}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getDenneMenuPocet(@PathParam("den") String den) {
        
        Ponuka denneMenu = menu.get(den);
        int pocetJedal = denneMenu.getJedlo().size();
        
        return Integer.toString(pocetJedal);
    }
    
    // GET pre MIME application/xml: vráti  kompletné menu ako xml s horeuvedenou štruktúrou
    @GET
    @Path("{den: pondelok|utorok|streda|stvrtok|piatok|sobota|nedela}")
    @Produces(MediaType.APPLICATION_XML)
    public Ponuka getDenneMenu(@PathParam("den") String den) {
        
        Ponuka denneMenu = menu.get(den);
        
        return denneMenu;
    }
    
    // POST pre MIME application/xml : pridá nové jedlo do ponuky pre daný deň
    @POST
    @Path("{den: pondelok|utorok|streda|stvrtok|piatok|sobota|nedela}")
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String pridajJedlo(@PathParam("den") String den, Jedlo jedlo){
        
        if (jedlo == null){
            
            return "0";
        }
        
        Ponuka denneMenu = menu.get(den);
        
        if (denneMenu == null){
            
            return "0";
        }
        else {
            
            for (Jedlo j : denneMenu.getJedlo()){
                
                if (j.getNazov().equals(jedlo.getNazov())){
                    
                    return "0";
                }
            }
        }
        
        denneMenu.getJedlo().add(jedlo);
        
        return "" + denneMenu.getJedlo().size();
    }
    
    /* ------------------------------------------------------- */
    // Pre URL jedla menu/{den}/{n} implementujte metódy:
    
    // GET pre MIME application/xml : vráti xml so všetkými informáciami o jedle s poradovým číslom
    @GET
    @Path("{den: pondelok|utorok|streda|stvrtok|piatok|sobota|nedela}/{n}")
    @Produces(MediaType.APPLICATION_XML)
    public Jedlo getJedlo(@PathParam("den") String den, @PathParam("n") int n){
        
        Ponuka denneMenu = menu.get(den);
        
        if(denneMenu == null){
            
            return null;
        }
        else if((denneMenu.getJedlo().size() < 1) || (denneMenu.getJedlo().size() < n)){
            
            return null;
        }
        
        return denneMenu.getJedlo().get(n - 1);
    }
    
    // DELETE : odstráni jedlo s poradovým číslom n z menu pre daný deň
    @DELETE
    @Path("{den: pondelok|utorok|streda|stvrtok|piatok|sobota|nedela}/{n}")
    public void deleteJedlo(@PathParam("den") String den, @PathParam("n") int n){
        
        Ponuka denneMenu = menu.get(den);
        
        if(denneMenu == null){
            
            return;
        }
        else if((denneMenu.getJedlo().size() < 1) || (denneMenu.getJedlo().size() < n)){
            
            return;
        }
        
        denneMenu.getJedlo().remove(n - 1);
    }
    
    /* ------------------------------------------------------- */
    // Pre pre koreňový resource menu implementujte metódu:
    
    // GET : pre MIME text/plain umožňuje zistiť, ktorý deň je jedlo v ponuke
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String vyhladajVyber(@QueryParam("nazov") String nazov){
        
        Boolean vyber = false;
        String menuVyber = "";
        
        for (String den : menu.keySet()){
            
            List<Jedlo> dennaPonuka = menu.get(den).getJedlo();
            
            if (dennaPonuka.size() > 0){
                
                for(Jedlo jedlo : dennaPonuka){
                    
                    if (jedlo.getNazov().equals(nazov)){
                        
                        menuVyber += den + " ";
                        vyber = true;
                    }
                    
                }
            }
        }
        
        if (vyber){
            
            return menuVyber;
        } else {
            
            return "NEMAME";
        }
    }
    
}

```

---

### Ponuka.java

```java
package rest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Ponuka {
    
    private List<Jedlo> jedlo;
    private String den;

    public Ponuka() {
        
        jedlo = new ArrayList<>();
    }

    public Ponuka(String den) {
        
        this.den = den;
        jedlo = new ArrayList<>();
    }
    
    // GETTERS & SETTERS
    public void setDen(String den) {
        this.den = den;
    }
    
     public String getDen() {
        return den;
    }

    public List<Jedlo> getJedlo() {
        return jedlo;
    }

    public void setJedlo(List<Jedlo> jedlo) {
        this.jedlo = jedlo;
    }
}

```

### Jedlo.java

```java
package rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement 
public class Jedlo {

    private String nazov;
    private double cena;

    public Jedlo() {
    }

    public Jedlo(double cena, String nazov) {
        
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
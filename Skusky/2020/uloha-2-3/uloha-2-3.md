## Úloha 2 : RESTful servis

### SkuskaResource.java

```java
package rest;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Singleton
@Path("skuska")
public class SkuskaResource {
    
    private List<Skuska> skusky;
    
    public SkuskaResource() {
        this.skusky = new ArrayList<>();
        
        Skuska s = new Skuska();
        s.setPredmet("TZI");
        s.setDen("piatok");
        
        this.skusky.add(s);
    }
    
    // Pre koreňový resource skuska implementujete metódu
    // POST, ktorá slúži pre vytvorenie nového resoursu s informáciami o skúške.
    // Metóda musí najprv preveriť, či resource s danou skratkou predmetu už neexistuje.
    // Ak exsituje, neurobí nič a vráti reťazec NIC.
    // POZER !!! predmet obsahuje skratku predmetu,
    // ktorá je zároveň jednoznačným identifikátorom skúšky, teda musí byť zadaná.
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public String pridajNovuSkusku(Skuska content) {
        
        if ((skusky != null) && (content != null) && (content.getPredmet() != null)) {
            
            for (Skuska s : skusky){
                if (s.getPredmet().equals(content.getPredmet()))
                    return "NIC";
            }
            
            skusky.add(content);
        }
        
        return "NIC";
    }
    
    // Zisti skúšky, na ktoré je študent prihlásený. Implementujte pomocou metódy GET pre koreňový resource skuska.
    // Meno študenta je zadané ako parameter požiadavky student @QueryParam(“student”)
    // Metóda zistí, na ktoré skúšky je študent prihlásený a vráti reťazec, zložený zo skratiek predmetov,
    // na ktoré je prihlásený, oddelených medzerou.
    // Ak meno študenta nie je zadané ako parameter požiadavky nevráti nič, príp. prázdny reťazec.
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String prihlaseneSkuskyStudenta(@QueryParam("student") String student) {
        
        String prihlaseneSkusky = "";
        
        if ((skusky != null) && (student != null)){
            
            for (Skuska s : skusky) {
                
                 for (String name : s.getStudent()) {
                     
                     if (name.equals(student)){
                     
                     prihlaseneSkusky += s.getPredmet() + " ";
                     }
                 }
            }
        }
        
        return prihlaseneSkusky;
    }
    
    // -------------------------------------------------------------------------------
    // Pre subresource s URI skuska/{predmet}, kde reťazec predmet je skratka predmetu
    
    // Vráti počet študentov prihlásených na skúšku. Ak resource neexistuje, malo
    // by sa vrátiť 0 prípadne HTTP 204 alebo HTTP 404 .
    @GET
    @Path("{predmet}")
    @Produces(MediaType.TEXT_PLAIN)
    public String pocetPrihlasenychStudent(@PathParam("predmet") String predmet){
        
        int pocet = 0;
        
        if ((skusky != null) && (predmet != null)){
            
            for (Skuska s : skusky){
                
                if (s.getPredmet().equals(predmet)){
                    
                    for (String name : s.getStudent()){
                        
                        pocet++;
                    }
                }
            }
        }
        
        return Integer.toString(pocet);
    }
    
    // Vyhľadá skúšku podľa skratky predmetu zadaného v URL a vráti
    // informácie ako XML dokument s horeuvedenou štrukrúrou. Ak resource pre skúšku z daného predmet
    // neexistuje, malo by sa vrátiť HTTP 204 alebo 404 (Pomôcka, implementacia vráti jednoducho null)
    @GET
    @Path("{predmet}")
    @Produces(MediaType.APPLICATION_XML)
    public Skuska infoSkuska(@PathParam("predmet") String predmet){
        
        if ((skusky != null) && (predmet != null)){
            
            for (Skuska s : skusky){
                
                if (s.getPredmet().equals(predmet)){
                    
                    return s;
                }
            }
        }
        
        return null;
    }
    
    // Prihlásenie študenta na skúšku. Očakáva reťazec s menom študenta (MIME: TEXT_PLAIN). Ak
    // je študent s daným menom ešte nie je prihlásený, pridá ho medzi prihlásených študentov. Metóda nevracia nič.
    // Ak je už študent prihlásený nerobí nič.
    @POST
    @Path("{predmet}")
    @Consumes(MediaType.TEXT_PLAIN)
    public void prihlasStudenta(@PathParam("predmet") String predmet, String meno){
        
        
        if ((skusky != null) && (predmet != null)){
            
            for (Skuska s : skusky){
                
                if (s.getPredmet().equals(predmet)){
                    
                    for (String name : s.getStudent()){
                        
                        if (name.equals(meno)){
                            return;
                        }
                    }
                    
                    s.getStudent().add(meno);
                }
            }
        }
    }
}
```

### Skuska.java

```java
package rest;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Skuska {
    
    private String predmet = "";
    private String den = "";
    private List<String> student;

    public Skuska() {
        this.student = new ArrayList<>();
    }

    public String getPredmet() {
        return predmet;
    }

    public void setPredmet(String predmet) {
        this.predmet = predmet;
    }

    public String getDen() {
        return den;
    }

    public void setDen(String den) {
        this.den = den;
    }

    public List<String> getStudent() {
        return student;
    }

    public void setStudent(List<String> student) {
        this.student = student;
    }
}
```

---
## Úloha 3 : RESTful servis klient

```java
package restclient;

public class RestClient {
    
    public static void main(String[] args) {
        
        NewJerseyClient client = new NewJerseyClient();
        
        Skuska s = new Skuska();
        
        s = client.infoSkuska(Skuska.class, "TZI");
        System.out.println(s.getDen());
        
        System.out.println(client.pocetPrihlasenychStudent("TZI"));
        
        client.prihlasStudenta("TZI", "Martin");
        
        System.out.println(client.pocetPrihlasenychStudent("TZI"));
    }
}
```
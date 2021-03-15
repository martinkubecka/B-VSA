## Named query

Celé znenie úloh [tu](http://www.kaivt.elf.stuba.sk/Predmety/B-VSA/CV3)

---
### Úloha 1.

- Z tabuľky Osoba (cvičenie 2. týždeň) vygenerujeme entitnú triedu pomocou **nb.new.other.Persistance.Entity class from database**

> Implementujte a otestujete metódu, ktorá pomocou namedQuery **findAll** vyselektuje všetky osoby z DB.

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("Projekt3PU");
EntityManager em = emf.createEntityManager();
        
TypedQuery<Osoba> all = em.createNamedQuery("Osoba.findAll", Osoba.class);
for (Osoba o: all.getResultList()) {
            
    System.out.println("" + o.toString());
}
```

> Implementujte a otestujete metódu, ktorá pomocou namedQuery **findByMeno** vyhľadá osobu podľa mena.

```java
EntityManagerFactory emf = Persistence.createEntityManagerFactory("Projekt3PU");
EntityManager em = emf.createEntityManager();
        
TypedQuery<Osoba> q = em.createNamedQuery("Osoba.findByMeno", Osoba.class);
q.setParameter("meno", "Jozko");
        
for (Osoba o : q.getResultList()){
        
	System.out.println("" + o);
}
```

> Implementujte a otestujete metódu, ktorá pomocou namedQuery vyhľadá všetky osoby z DB ktoré nemajú zadanú váhu, nastaví ich váhu na 80.0 a aktualizuje DB.

```java
TypedQuery<Osoba> q = em.createQuery("SELECT o FROM Osoba o WHERE o.vaha is null", Osoba.class);
   
em.getTransaction().begin();
for (Osoba o: q.getResultList()){
            
	o.setVaha(80.0);
}
em.getTransaction().commit();
```

### Úloha 2.

[V úlohe 6 na cvičení 2](http://www.kaivt.elf.stuba.sk/Predmety/B-VSA/CV2#A.2BANo-loha6.) sme implementovali viaceré metódy, ktoré vyhľadávajú rôzne informácie o osobách pomocou **NativeQuery** (SQL). 

> Modifikujte **Projekt2** tak, že tieto metódy implementujete s použitím **JPQLQuery**.

*Hlavný rozdiel medzi SQL (NativeQuery) a JPQL je, že JPQL používa názvy entitných tried a atribútov namiesto názvov tabuliek a stĺpcov.*

```java
public Osoba findPerson(Long id){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Projekt3PU");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Osoba> q = em.createNamedQuery("Osoba.findById", Osoba.class);
        q.setParameter("id", id);
        
        return q.getSingleResult();
}
```

```java
public void showAllPersons(){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Projekt3PU");
        EntityManager em = emf.createEntityManager();
        
        TypedQuery<Osoba> all = em.createNamedQuery("Osoba.findAll", Osoba.class);
        for (Osoba o: all.getResultList()) {
            
            System.out.println("" + o.toString());
        }
}
```

---
## Metódy entity managera

### Opakované volanie persist
-   Začnite transakciu
-   Vytvorte inštanciu triedy Osoba - zadajte jej meno. 

> **Id nie je potrebné zadávať, prečo?**
> 
> Pretože primárny kľúč id má anotáciu, kt autogeneruje jeho hodnoty :
> 
> `@GeneratedValue(strategy = GenerationType.AUTO)`

-   Vytvorte druhú inštanciu triedy Osoba - zadajte rovnaké jej meno.
-   Pre obe inštancie zavolajte **em.persist** a ukončite transakciu.

```java
public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MetodyEMPU");
        EntityManager em = emf.createEntityManager();
        
        Osoba osoba1 = new Osoba();
        Osoba osoba2 = new Osoba();
        
        osoba1.setId(5L);
        osoba1.setMeno("Xavier");
        osoba1.setVaha(120.0);
        
        osoba2.setId(5L);
        osoba2.setMeno("Stefan");
        osoba2.setVaha(95.0);
        
        em.getTransaction().begin();
        em.persist(osoba1);
        em.persist(osoba2);
        em.getTransaction().commit();
}
```

>** Koľko záznamov bude vytvorených v databáze?**
> 
> V databáze OSOBA budú dva záznamy a to Xavier s ID=1 a Xavier s ID=2.


### Opakované volanie persist - bez autogenerovaného klúča

-   Z entitnej triedy Osoba odstráňte anotáciu `@GeneratedValue`. 
-   Osoba musí mať teraz zadanú aj hodnotu klúča id.
-   Vytvorte inštanciu triedy Osoba - zadajte id, meno a váhu
-   Vytvorte druhú inštanciu triedy Osoba - zadajte rovnaké id ale odlišné meno
-   Pre obe inštancie zavolajte **em.persist** a ukončite transakciu.

```java
public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MetodyEMPU");
        EntityManager em = emf.createEntityManager();
        
        Osoba osoba1 = new Osoba();
        Osoba osoba2 = new Osoba();
        
        osoba1.setId(5L);
        osoba1.setMeno("Xavier");
        osoba1.setVaha(120.0);
        
        osoba2.setId(5L);
        osoba2.setMeno("Stefan");
        osoba2.setVaha(95.0);
        
        em.getTransaction().begin();
        em.persist(osoba1);
        em.persist(osoba2);
        em.getTransaction().commit();
}
```


> **Čo spraví program po spustení?**
> 
> **ERROR :** *The statement was aborted because it would have caused a duplicate key value in a unique or primary key.*
> 
> Dvom záznamom v databáze sme priradili rovnakú hodnotu primárneho kľúča.  

### persit a merge

-   Vytvorte inštanciu triedy Osoba - zadajte id, meno a váhu
-   Vytvorte druhú inštanciu triedy Osoba - zadajte rovnaké id ale odlišné meno alebo váhu
-   Pre prvú inštanciu zavolajte **em.persist**
-   Pre druhú inštanciu zavolajte **em.merge**
-   Ukončite transakciu

```java
public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MetodyEMPU");
        EntityManager em = emf.createEntityManager();
        
        Osoba osoba1 = new Osoba();
        Osoba osoba2 = new Osoba();
        
        osoba1.setId(5L);
        osoba1.setMeno("Xavier");
        osoba1.setVaha(120.0);
        
        osoba2.setId(5L);
        osoba2.setMeno("Stefan");
        osoba2.setVaha(95.0);
        
        em.getTransaction().begin();
        em.persist(osoba1);
        em.merge(osoba2);
        em.getTransaction().commit();
}
```

> **Čo spraví program po spustení?**
>
>V databaze mame jeden zaznam s hodnotami :  
>*ID=5, NAME=Stefan, NARODENA=NULL, VAHA=95.0*
>
> Metoda *merge()* nam modifikuje hodnoty zaznamu v databaze. Da sa povedat, ze spaja metody *find()* a *commit()*.
> 
> Definícia z prednášky hovorí, že metóda *merge()* načíta osobu z DB (ak treba aj vytvorí v DB) do nového objektu a zmení jeho stav podla objektu zadaného ako argument

### clear

-   Vytvorte inštanciu triedy Osoba - zadajte id a meno
-   Vytvorte druhú inštanciu triedy Osoba - zadajte rovnaké id ale odlišné meno
-   Pre prvú inštanciu zavolajte **em.persist**
-   Zavolajte **em.clear**
-   Pre druhú inštanciu zavolajte **em.persist**
-   Ukončite transakciu

```java
public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MetodyEMPU");
        EntityManager em = emf.createEntityManager();
        
        Osoba osoba1 = new Osoba();
        Osoba osoba2 = new Osoba();
        
        osoba1.setId(5L);
        osoba1.setMeno("Xavier");
        
        osoba2.setId(5L);
        osoba2.setMeno("Stefan");
        
        em.getTransaction().begin();
        em.persist(osoba1);
        em.clear();
        em.persist(osoba2);
        em.getTransaction().commit();
}
```

> **Čo spraví program po spustení?**
> 
>V databaze mame jeden zaznam s hodnotami :  
>*ID=5, NAME=Stefan, NARODENA=NULL, VAHA=NULL*
>
>Metoda *clear()* odstáni všetky objekty z persistance kontextu

### detach

-   Vytvorte inštanciu triedy Osoba - zadajte id a meno
-   Vytvorte druhú inštanciu triedy Osoba - zadajte rovnaké id ale odlišný meno
-   Pre obe inštancie zavolajte **em.persist**
-   Pre druhú inštanciu zavolajte **em.detach**
-   Ukončite transakciu

```java
public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MetodyEMPU");
        EntityManager em = emf.createEntityManager();
        
        Osoba osoba1 = new Osoba();
        Osoba osoba2 = new Osoba();
        
        osoba1.setId(5L);
        osoba1.setMeno("Xavier");
        
        osoba2.setId(5L);
        osoba2.setMeno("Stefan");
        
        em.getTransaction().begin();
        em.persist(osoba1);
        em.persist(osoba2);
        em.detach(osoba2);
        em.getTransaction().commit();
}
```

> **Čo spraví program po spustení?**
> 
> V databaze mame jeden zaznam s hodnotami :  
>*ID=5, NAME=Xavier, NARODENA=NULL, VAHA=NULL*
> 
> Metóda *detach()* odstráni zadaný objekt z persistance kontextu

### Opakované volanie find

-   Pomocou **em.find** načítajte z DB inštanciu osoby podľa zadaného klúča.
-   Zopakujte volanie **em.find** s tým istým klúčom.


```java
public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MetodyEMPU");
        EntityManager em = emf.createEntityManager();
        
        Osoba osoba1 = em.find(Osoba.class, 5L);
        Osoba osoba2 = em.find(Osoba.class, 5L);
        
        if ((osoba1 != null) && (osoba2 != null)){
            
            if (osoba1 == osoba2){
                System.out.println("Objekty su rovnake");
            } else {
                System.out.println("Objekty nie su rovnake");
            }
        }
}
```

*NOTE : `==` compares object references, it checks to see if the two operands point to the same object (not equivalent objects, the **same** object).*

> **Overte si** či tieto dve volania vrátili ten istý objekt alebo dva rôzne objekty. (Porovnávajte referencie nie klúče)
> 
> Ano, obe volania vratili ten isty objekt.

### find a detach

-   Pomocou **em.find** načítajte z DB inštanciu osoby podľa zadaného klúča.
-   Zavolajte **em.detach** na načítanú inšanciu osoby.
-   Zopakujte volanie **em.find** s tým istým klúčom.

```java
public static void main(String[] args) {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("MetodyEMPU");
        EntityManager em = emf.createEntityManager();
        
        Osoba osoba1 = em.find(Osoba.class, 5L);
        System.out.println("" + osoba1.getId());
        
        em.detach(osoba1);
        
        Osoba osoba2 = em.find(Osoba.class, 5L);
        System.out.println("" + osoba2.getId());
}
```

> **Overte si** či tieto dve volania vrátili ten istý objekt alebo dva rôzne objekty.
> 
> Obe volania nam vratili rovnaky objekt, pretoze *detach()* odstranuje z **persist**.

### Ďalšie užitočné metódy entity managera

-   **refresh()** obnoví stav managovaného objektu z databázy
-   **flush()** zapíše zmeny v managovaných objektov do DB, ale bez ukončenia databázovej transakcie.

---
## JPA kontroler

- Pomocou netbeans wizardu **new.other.persitance.JPA Controller Classes from Entity Classes** vytvorte JPA-kontroler (data access object) pre entitu **Osoba**

```java
public class OsobaJpaController implements Serializable {

    public OsobaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Osoba osoba) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(osoba);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Osoba osoba) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            osoba = em.merge(osoba);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = osoba.getId();
                if (findOsoba(id) == null) {
                    throw new NonexistentEntityException("The osoba with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Osoba osoba;
            try {
                osoba = em.getReference(Osoba.class, id);
                osoba.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The osoba with id " + id + " no longer exists.", enfe);
            }
            em.remove(osoba);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Osoba> findOsobaEntities() {
        return findOsobaEntities(true, -1, -1);
    }

    public List<Osoba> findOsobaEntities(int maxResults, int firstResult) {
        return findOsobaEntities(false, maxResults, firstResult);
    }

    private List<Osoba> findOsobaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Osoba.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Osoba findOsoba(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Osoba.class, id);
        } finally {
            em.close();
        }
    }

    public int getOsobaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Osoba> rt = cq.from(Osoba.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
```

---
### Entititná trieda používaná v úlohách

```java
public class Osoba implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "MENO")
    private String meno;
    @Column(name = "NARODENA")
    @Temporal(TemporalType.DATE)
    private Date narodena;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "VAHA")
    private Double vaha;
	
	. . .
```
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


```java

```


### Opakované volanie persist - bez autogenerovaného klúča


```java

```


### persit a merge

```java

```


### clear


```java

```


### detach


```java

```


### Opakované volanie find


```java

```


### find a detach


```java

```


---
## JPA kontroler


```java

```
## JPA anotácie, generovanie DB tabuľky z entitnej triedy a naopak

Celé znenie úloh [tu](http://www.kaivt.elf.stuba.sk/Predmety/B-VSA/CV2).

---

**How to generate Entity Class from the Database?**

> Click on **APP** -> Right click on  **Tables**  -> Create Table
> 
> Right click on your project folder -> Other -> Persistence -> Entity Classes from Database -> choose from Available Tables -> click 2x on **Next** button and then **Finish** 

**What is @Transient annotation?**

> The @Transient annotation tells the JPA provider to not persist any (non-transient) attribute. This can be used when there is no column in the table for this class attribute.
> 
> Example :
> `@Transient`
> `private int count;`

**How to use Date attribute as a table column?**

> Add @Temportal annotation with type DATE
> 
> Example :
> 
> `@Temporal(javax.persistence.TemporalType.DATE)`
> `private Date dateOfBirth;`

---
### Vytvorenie DB tabuľky z entitnej triedy

#### Poznámky k úlohám 1. a 2.
- Novú enitnú triedu vytvoríme pomocou _**nb.new.other.Persistance.Entity Class**_
-  Entitné triedy vytvárame v samostatnom balíčku **entities**
-  V **persistence.xml** nastavujeme **table generation strategy** na **create** alebo **drop-and-create**

#### Poznámky k úlohe 3.
- Anotácia `@Table(name = "T_OSOBA")` v entitnej triede nám vytvorí tabuľku s názvom **T_OSOBA**
- Anotácia `@Column(nullable=false)` spôsobí, že hodnoty v danom stĺpci nesmú byť nulové a teda sú "povinné". 
- Novú databázu vytvoríme pomocou _**services.Databases.JavaDB**_ -> _**Create Database**_
- V **persistence.xml** zadáme url novovytvorenej databázy
- Po spustení programu nám vytvorí tabuľku v novovytvorenej databáze 

---

### Namapovanie existujúcej entitnej triedy na existujúcu databazovú tabuľku

#### Úloha 4.

> Implementujte metódu **showAllPersons(EntityManager em)**,ktorá pomocou **nativeQuery** vyhľadá všetky osoby a vypíše údaje o nich na štandardný výstup. Entity-manager dostane metóda ako argument. Na výpis údajov o osobe využite metódu toString.

```java
public class Projekt2 {

    public static void main(String[] args) {
        
        Projekt2 p = new Projekt2();
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Projekt2PU");
        EntityManager em = emf.createEntityManager();
        
        p.showAllPersons(em);
    }
    
    public void showAllPersons(EntityManager em){
    
        Query query = em.createNativeQuery("SELECT * FROM T_OSOBA", Person.class);
        List <Person> list = query.getResultList();
        
        for (Person person : list){
        
            System.out.println("" + person.toString());
        }
    }
}
```

####  Úloha 6.

> Do triedy **Projekt2** pridajte atribút **EntityManager em**, môže byť private a inicializujte ho v konštruktore. (EntityManager z hlavého programu môžete odstrániť)
> 
> Metodu **showAllPersons()** upravte tak, aby použivala atribút **em**.

> Do triedy **Projekt2** pridajte metódu **Long addPerson(String meno)** , ktorá vytvorí novú osobu so zadaným menom, vloží ju do databázy a vráti vygenerovaný kľúč.

> Do triedy **Projekt2** pridajte metódu **Long addPerson(String meno)** , ktorá vytvorí novú osobu so zadaným menom, vloží ju do databázy a vráti vygenerovaný kľúč.

> Do triedy **Projekt2** pridajte metódu **Person findPerson(Long id)** , ktorá pomocou metódy entity-managera **find** v DB vyhľadá osobu podľa id.


```java
package projekt2;

import entities.Person;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Projekt2 {
    
    EntityManager em;
    
    public Projekt2() {
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Projekt2PU");
        this.em = emf.createEntityManager();
    }
    
    public Person findPerson(Long id){
        
        return em.find(Person.class, id);
    }
    
    public Long addPerson(String meno, Float vaha){
        
        Person p = new Person();
        p.setName(meno);
        p.setBorn(new Date());
        p.setWeight(vaha);
        
        em.getTransaction().begin();
        em.persist(p);
        em.getTransaction().commit();
        
        return p.getId();
    }
    
    public void showAllPersons(){
        
        Query query = em.createNativeQuery("SELECT * FROM T_OSOBA", Person.class);
        List <Person> list = query.getResultList();
        
        for (Person person : list){
            
            System.out.println("" + person.toString());
        }
    }
}
```

```java
package projekt2;

import entities.Person;

public class Main {
    
    public static void main(String[] args) {
        
        Projekt2 p = new Projekt2();
        
        p.showAllPersons();
        
        Long newID = p.addPerson("Michal", new Float(0));
        System.out.println("" + newID);
        
        Person person = p.findPerson(5L);
        
        if (person == null){
            System.out.println("Dana osoba neexistuje.");
        } else {
            System.out.println("Uspesne sme nasli osobu s menom " + person.getName());
        }
    }
}
```

---
### Entititná trieda používaná v úlohách 4 a 6

```java
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

@Entity
@Table(name = "T_OSOBA")
public class Person implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;
    @Column(name = "MENO")
    @Basic(optional = false)
    private String name;
    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "NORODENA")
    @Basic(optional = false)
    private Date born;
    @Column(name = "VAHA")
    @Basic(optional = false)
    private Float weight;
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Date getBorn() {
        return born;
    }
    
    public void setBorn(Date born) {
        this.born = born;
    }
    
    public Float getWeight() {
        return weight;
    }
    
    public void setWeight(Float weight) {
        this.weight = weight;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Person)) {
            return false;
        }
        Person other = (Person) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return "Person{" + "id=" + id + ", name=" + name + ", born=" + born + ", weight=" + weight + '}';
    }
}
```
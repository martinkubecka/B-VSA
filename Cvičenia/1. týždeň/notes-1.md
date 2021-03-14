##  Práca s prostredím NetBeans

---

**How to create a new project?**
 
> File -> New Project -> Java - Java Application
> 
> Click **Next** -> Set Project name -> Click **Finish** 

**How to start a database service and create a new table?**

> Navigate to Services -> Databases -> Double click on *jdbc*
> 
> Authenticate with app:app
> 
> Click on **APP** -> Right click on  **Tables**  -> Create Table

*NOTE : After editing table values inside **View Table**, press **Commit Record(s)** button*

**How to add missing ClientDriver?**
> Inside your project right click on the **Libraries**
> 
> Choose **Add Library**
> 
> Find ***Java DB Driver*** and press **Add Library**

**How to add Persistence Unit?**
> Right click on the **Source Packages**
> 
> New -> Other
> 
> Persistence ->  Persistence Unit
> 
> Database Connection : jdbc:derby://
> 
> Choose **Table Generation Strategy**

**How to view the SQL queries issued by JPA?**

> Add the line below to your *persistance.xml*
> 
> `<property name="eclipselink.logging.level" value="FINE"/>`

---
### Modifikácia údajov DB

1. Create a new project
2. Start a database service and create a new table
3. Add ClientDriver 
4. Add Persistence Unit

> **IMPORTANT !!!**
> 
> Don't forget to add the line below to *persistence.xml*
> 
> `<class>firstproject.Kniha</class>`
> 
> This will set relation between your database and your entity class

---
**TASKS :**

Vytvoriť nový projekt a v ňom implementujte nasledujúce metódy:

1.  **double cenaKnihy(String meno)**
	- nájde v DB knihu so zadaným menom a vráti jej cenu. Ak neexistuje taká kniha vráti -1 a vypíše spávu "Knihu nemáme"
    
```java
public class FirstProject {
	public static void main(String[] args) {
        
        FirstProject firstProject = new FirstProject();
        double result = firstProject.cenaKnihy("MacOs");
        
        if (result == -1){
        
            System.out.println("Knihu nemáme");
        } else {
            System.out.println("" + result);
        }
    }

    private double cenaKnihy(String nazov) {
        
       EntityManagerFactory emf = Persistence.createEntityManagerFactory("FirstProjectPU");
       EntityManager em = emf.createEntityManager();
            
       Query q = em.createNativeQuery("select * from KNIHA", Kniha.class);
       List<Kniha> knihy = (List<Kniha>) q.getResultList();
       
       double isBookPresent = -1;
       
       for (Kniha k : knihy){
           
           int compareTo = nazov.compareTo(k.getNazov());
           
           if (compareTo == 0){
               return k.getCena();
           }
       }
        
        return isBookPresent;
    }
}	
```
	

---
2.  **boolean pridajKnihu(String meno, double cena)**
	- pridá do DB knihu s daným menom a cenou. Ak kniha s daným menom v DB už existuje, vráti false.
    
	
```java
public class FirstProject {
	public static void main(String[] args) {
        
        FirstProject firstProject = new FirstProject();
        
        boolean resultPridaj = firstProject.pridajKnihu("MacOs", 99);
        
        if (resultPridaj == true){
            System.out.println("Kniha uspesne pridana");
        } else {
            System.out.println("Kniha uz existuje");
        }
    }


    private boolean pridajKnihu(String meno, double cena){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FirstProjectPU");
        EntityManager em = emf.createEntityManager();

        Kniha kniha = em.find(Kniha.class, meno);

        if (kniha != null){

            return false;

        } else {

            Kniha k = new Kniha();
            em.getTransaction().begin();
            k.setNazov(meno);
            k.setCena(cena);
            em.persist(k);
            em.getTransaction().commit();
        }

        return true;
    }
}
```


	
---
3.  **void zlava(String meno)** 
	- nájde v DB knihu so zadaným menom a zníži jej cenu o o 20% (v databáze). Ak neexistuje taká kniha neurobí nič.


```java
public class FirstProject {
	public static void main(String[] args) {
        
        FirstProject firstProject = new FirstProject();
        
        firstProject.zlava("Linux");
    }

    private void zlava(String meno){
    
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FirstProjectPU");
        EntityManager em = emf.createEntityManager();

        Kniha kniha = em.find(Kniha.class, meno);

        if (kniha != null){

            em.getTransaction().begin();
            kniha.setCena(kniha.getCena() * 0.8);
            em.getTransaction().commit();
        }
    }
}
```

---

### Entity Class Kniha

```java
package firstproject;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Kniha {
    @Id
    private String nazov;
    private double cena;

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

    @Override
    public String toString() {
        return "Kniha{" + "nazov=" + nazov + ", cena=" + cena + '}';
    }
}
```

### Persistance xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1" xmlns="http://xmlns.jcp.org/xml/ns/persistence" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
  <persistence-unit name="FirstProjectPU" transaction-type="RESOURCE_LOCAL">
    <provider>org.eclipse.persistence.jpa.PersistenceProvider</provider>
    <properties>
        <class>firstproject.Kniha</class>
      <property name="javax.persistence.jdbc.url" value="jdbc:derby://localhost:1527/sample"/>
      <property name="javax.persistence.jdbc.user" value="app"/>
      <property name="javax.persistence.jdbc.driver" value="org.apache.derby.jdbc.ClientDriver"/>
      <property name="javax.persistence.jdbc.password" value="app"/>
      <property name="javax.persistence.schema-generation.database.action" value="create"/>
    </properties>
  </persistence-unit>
</persistence>
```
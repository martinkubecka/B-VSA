## 1. Zápočet

### Projekt.java

```java
package projekt;

import entities.Book;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Projekt {
    
    // Metóda vyhľadá v databáze záznam o knihe podľa zadaného isbn (klúč) a aktualizuje údaje
    public boolean updateBook( String isbn, String title, Double price){
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
        EntityManager em = emf.createEntityManager();
        
        Book hladanaKniha = em.find(Book.class, isbn);
        
        // Ak kniha v databáze neexistuje, vytvorí novú knihu so zadanými údajmi a vráti true
        if (hladanaKniha == null){
            
            Book novaKniha = new Book();
            novaKniha.setIsbn(isbn);
            novaKniha.setTitle(title);
            novaKniha.setPrice(price);
            
            em.getTransaction().begin();
            em.persist(novaKniha);
            em.getTransaction().commit();
            
            return true;
            
        } else {
            
            if (title != null){
                
                // Ak argument metódy titleje zadaný (nenulový)
                // ale v stĺpci TITLE je NULL, zapíše hodnotu do stĺpca
                if (hladanaKniha.getTitle() == null){
                    
                    em.getTransaction().begin();
                    hladanaKniha.setTitle(title);
                    em.getTransaction().commit();
                    
                    // ak argument metódy titleje zadaný
                    // ale stĺpec TITLE obsahuje inú hodnotu, vráti false
                } else if ((title.equals(hladanaKniha.getTitle()) == false)){
                    
                    return false;
                }
            }
            
            // ak je argument metódy price zadaný,
            // aktualizuje hodnotu v stlpci PRICE.
            if (price != null){
                
                em.getTransaction().begin();
                hladanaKniha.setPrice(price);
                em.getTransaction().commit();
            }
        }
        
        return true;
    }
    
    
    // Na aktualizáciu použite metódu updateBook, pričom názov knihy nezadáte
    public void updatePriceList(Map<String, Double> priceList){
        
        if(priceList != null){
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("projektPU");
            EntityManager em = emf.createEntityManager();
            
            for (Map.Entry<String, Double> entry: priceList.entrySet()){
                
                String entryMapKey = entry.getKey();
                Double entryMapValue = entry.getValue();
                
                updateBook(entryMapKey, null ,entryMapValue);
            }
        }
    }
}
```

---

### Book.java

```java
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "BOOK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Book.findAll", query = "SELECT b FROM Book b"),
    @NamedQuery(name = "Book.findByIsbn", query = "SELECT b FROM Book b WHERE b.isbn = :isbn"),
    @NamedQuery(name = "Book.findByTitle", query = "SELECT b FROM Book b WHERE b.title = :title"),
    @NamedQuery(name = "Book.findByPrice", query = "SELECT b FROM Book b WHERE b.price = :price")})
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ISBN")
    private String isbn;
    @Column(name = "TITLE")
    private String title;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PRICE")
    private Double price;

    public Book() {
    }

    public Book(String isbn) {
        this.isbn = isbn;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (isbn != null ? isbn.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Book)) {
            return false;
        }
        Book other = (Book) object;
        if ((this.isbn == null && other.isbn != null) || (this.isbn != null && !this.isbn.equals(other.isbn))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Book[ isbn=" + isbn + " ]";
    }
    
}
```
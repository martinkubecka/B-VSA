package zadanie2017;

import entities.TKniha;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Zadanie2017 {
    
    public static void main(String[] args) {
        
        BufferedReader br = null;
        try {
            String line;
            br = new BufferedReader(new FileReader("/home/vsa/Downloads/data1.csv"));
            while ((line = br.readLine()) != null) {
                
                String s[] = line.split(";");
                if (s.length < 3) {
                    System.out.println("kratky riadok");
                    return;
                }
                String nazov = s[0].trim();
                String autor = s[1].trim();
                String pocet = s[2].trim();
                
                EntityManagerFactory emf = Persistence.createEntityManagerFactory("Zadanie2017PU");
                EntityManager em = emf.createEntityManager();
                
                TKniha tempKniha = em.find(TKniha.class, nazov);
                if (tempKniha == null){
                    
                    TKniha kniha = new TKniha();
                    if (!nazov.isEmpty()){
                        kniha.setNazov(nazov);
                    }
                    if (!autor.isEmpty()){
                        kniha.setAutor(autor);
                    }
                    if (!pocet.isEmpty()){
                        kniha.setPocet(Integer.parseInt(pocet));
                    }
                    
                    em.getTransaction().begin();
                    em.persist(kniha);
                    em.getTransaction().commit();
                }
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Zadanie2017.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Zadanie2017.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(Zadanie2017.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
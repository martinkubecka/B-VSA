/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uloha1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

/**
 *
 * @author vsa
 */
public class Uloha1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        // TODO code application logic here
        load("/home/vsa/Downloads/data.csv");
    }

    public static void load(String csv) throws FileNotFoundException, IOException {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("uloha1PU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        String line;
        BufferedReader br = new BufferedReader(new FileReader(csv));
        while ((line = br.readLine()) != null) {
            try{
                String s[] = line.split(";");
                String name = s[0].trim();
                double cena = Double.parseDouble(s[1].trim());

                TypedQuery<Tovar> namedQuery = em.createNamedQuery("Tovar.findByName", Tovar.class);
                namedQuery.setParameter("name", name);
                if(namedQuery.getResultList().isEmpty()){
                    
                    transaction.begin();
                    Tovar t = new Tovar();
                    t.setName(name);
                    t.setCena(cena);
                    em.persist(t);
                    transaction.commit();
                }
            }catch(ArrayIndexOutOfBoundsException e){
                continue;
            }
        }
    }
}

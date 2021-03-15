/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uloha1;

import java.io.BufferedReader;
import java.io.FileReader;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 *
 * @author vsa
 */
public class Uloha1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args){
        // TODO code application logic here
        load("/home/vsa/Downloads/Zadanie2018B/data1.csv");
        load("/home/vsa/Downloads/Zadanie2018B/data2.csv");
    }
    
    public static void load(String csv){
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("uloha1PU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction t = em.getTransaction();
        String line;
        try{
            BufferedReader br = new BufferedReader(new FileReader(csv));
            while ((line = br.readLine()) != null) {
                try{
                    String s[] = line.split(";");
                    String name = s[0].trim();
                    Integer mnozstvo =  Integer.parseInt(s[1].trim());
                    Tovar fromDB = (Tovar) em.find(Tovar.class, name);
                    if(fromDB == null){
                        t.begin();
                        Tovar tovar = new Tovar();
                        tovar.setNazov(name);
                        tovar.setMnozstvo(mnozstvo);
                        em.persist(tovar);
                        t.commit();
                    }else{
                        t.begin();
                        fromDB.setMnozstvo(fromDB.getMnozstvo() + mnozstvo);
                        em.persist(fromDB);
                        t.commit();

                    }
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}

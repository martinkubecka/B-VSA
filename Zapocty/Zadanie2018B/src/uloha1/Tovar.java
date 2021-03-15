/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uloha1;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author vsa
 */
@Entity
@Table(name="TOVAR")
public class Tovar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name="NAZOV")
    private String nazov;
    
    @Column(name="MNOZSTVO", nullable=false)
    private Integer mnozstvo;

    @Column(name="CENA", nullable=true)
    private Double cena;
    
    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public Integer getMnozstvo() {
        return mnozstvo;
    }

    public void setMnozstvo(Integer mnozstvo) {
        this.mnozstvo = mnozstvo;
    }

    public Double getCena() {
        return cena;
    }

    public void setCena(Double cena) {
        this.cena = cena;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nazov != null ? nazov.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tovar)) {
            return false;
        }
        Tovar other = (Tovar) object;
        if ((this.nazov == null && other.nazov != null) || (this.nazov != null && !this.nazov.equals(other.nazov))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Tovar[ nazov=" + nazov + " ]";
    }
    
}

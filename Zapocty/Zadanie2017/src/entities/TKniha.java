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
@Table(name = "T_KNIHA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TKniha.findAll", query = "SELECT t FROM TKniha t"),
    @NamedQuery(name = "TKniha.findByNazov", query = "SELECT t FROM TKniha t WHERE t.nazov = :nazov"),
    @NamedQuery(name = "TKniha.findByAutor", query = "SELECT t FROM TKniha t WHERE t.autor = :autor"),
    @NamedQuery(name = "TKniha.findByPocet", query = "SELECT t FROM TKniha t WHERE t.pocet = :pocet")})
public class TKniha implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "NAZOV")
    private String nazov;
    @Column(name = "AUTOR")
    private String autor;
    @Column(name = "POCET")
    private Integer pocet;

    public TKniha() {
    }

    public TKniha(String nazov) {
        this.nazov = nazov;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Integer getPocet() {
        return pocet;
    }

    public void setPocet(Integer pocet) {
        this.pocet = pocet;
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
        if (!(object instanceof TKniha)) {
            return false;
        }
        TKniha other = (TKniha) object;
        if ((this.nazov == null && other.nazov != null) || (this.nazov != null && !this.nazov.equals(other.nazov))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.TKniha[ nazov=" + nazov + " ]";
    }
    
}

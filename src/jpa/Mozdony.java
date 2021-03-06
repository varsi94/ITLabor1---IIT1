package jpa;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Mozdony {

    @Id
    private int id;
    private int futottkm;
    @ManyToOne
    private Tipus tipus;
    @OneToMany(mappedBy = "mozdony", fetch = FetchType.EAGER)
    private Collection<Vonat> vonatok;
    
    public Mozdony(int id, int futottkm, Tipus tipus) {
	this.id = id;
	this.futottkm = futottkm;
	this.tipus = tipus;
    }

    public Mozdony() {
    }

    public int getFutottkm() {
	return futottkm;
    }

    public void setFutottkm(int futottkm) {
	this.futottkm = futottkm;
    }

    public int getId() {
	return id;
    }

    public Tipus getTipus() {
        return tipus;
    }

    public void setTipus(Tipus tipus) {
        this.tipus = tipus;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return id + " " + tipus.getAzonosito() + " " + futottkm;
    }

    public Collection<Vonat> getVonatok() {
        return vonatok;
    }

    public void setVonatok(Collection<Vonat> vonatok) {
        this.vonatok = vonatok;
    }
}

package jpa;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Mozdony {

    @Id
    private int id;
    private long futottkm;
    @ManyToOne
    private Tipus tipus;
    @OneToMany(mappedBy = "mozdony")
    private List<Vonat> vonatok;
    
    public Mozdony(int id, int futottkm, Tipus tipus) {
	this.id = id;
	this.futottkm = futottkm;
	this.tipus = tipus;
    }

    public Mozdony() {
    }

    public long getFutottkm() {
	return futottkm;
    }

    public void setFutottkm(long futottkm) {
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

    public List<Vonat> getVonatok() {
        return vonatok;
    }

    public void setVonatok(List<Vonat> vonatok) {
        this.vonatok = vonatok;
    }
}

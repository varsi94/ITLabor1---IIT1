package jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Mozdony {

    @Id
    private int id;
    private int futottkm;
    @ManyToOne
    private Tipus tipus;
    
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
}

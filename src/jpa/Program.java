package jpa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

public class Program {

    private EntityManagerFactory factory;
    private EntityManager em;

    public void initDB() {
	factory = Persistence.createEntityManagerFactory(CommonData.getUnit());
	em = factory.createEntityManager();
    }

    void closeDB() {

	em.close();
    }

    public Program(EntityManager em) {
	this.em = em;
    }

    public Program() {
    }

    public static void main(String[] args) {
	Program app = new Program();
	app.initDB();
	app.startControl();
	app.closeDB();

    }

    public void startControl() {
	// InputStream input = System.in;
	BufferedReader instream = new BufferedReader(new InputStreamReader(
		System.in));
	while (true) {
	    try {
		System.out.print(">");
		String inputLine = instream.readLine();
		StringTokenizer tokenizer = new StringTokenizer(inputLine,
			"   ");
		String command = tokenizer.nextToken();

		if ("t".startsWith(command)) {
		    ujTipus(readString(tokenizer), readString(tokenizer));
		} else if ("m".startsWith(command)) {
		    ujMozdony(readString(tokenizer), readString(tokenizer),
			    readString(tokenizer));
		} else if ("s".startsWith(command)) {
		    ujVonatszam(readString(tokenizer), readString(tokenizer));
		} else if ("v".startsWith(command)) {
		    ujVonat(readString(tokenizer), readString(tokenizer),
			    readString(tokenizer), readString(tokenizer));
		} else if ("l".startsWith(command)) {
		    String targy = readString(tokenizer);
		    if ("t".startsWith(targy)) {
			listazTipus();
		    } else if ("m".startsWith(targy)) {
			listazMozdony();
		    } else if ("s".startsWith(targy)) {
			listazVonatszam();
		    } else if ("v".startsWith(targy)) {
			listazVonat();
		    }
		} else if ("x".startsWith(command)) {
		    lekerdezes(readString(tokenizer));
		} else if ("e".startsWith(command)) {
		    break;
		} else {
		    throw new Exception("Hibas parancs! (" + inputLine + ")");
		}
	    } catch (Exception e) {
		System.out.println("? " + e.toString());
	    }
	}

    }

    static String readString(StringTokenizer tokenizer) throws Exception {
	if (tokenizer.hasMoreElements()) {
	    return tokenizer.nextToken();
	} else {
	    throw new Exception("Keves parameter!");
	}
    }

    // Uj entitások felvetelehez kapcsolodo szolgaltat�sok
    protected void ujEntity(Object o) throws Exception {
	em.getTransaction().begin();
	try {
	    em.persist(o);
	    em.getTransaction().commit();
	} catch (Exception e) {
	    em.getTransaction().rollback();
	    throw e;
	}
    }

    // Uj tipus felvetele
    public void ujTipus(String azonosito, String fajta) throws Exception {
	Query q = em.createQuery("SELECT t FROM Tipus t WHERE t.azonosito = :azon");
	q.setParameter("azon", azonosito);
	try {
	    q.getSingleResult();
	    System.out.println("?");
	} catch (NoResultException e) {
	    ujEntity(new Tipus(azonosito, fajta));
	}
    }

    // Uj mozdony felvetele
    public void ujMozdony(String sorszam, String tipusID, String futottkm)
	    throws Exception {
	int futottkmInt, sorszamInt;
	try {
	    sorszamInt = Integer.parseInt(sorszam);
	    futottkmInt = Integer.parseInt(futottkm);
	} catch (NumberFormatException exception) {
	    System.out.println("?");
	    return;
	}
	
	Query tipusQuery = em.createQuery("SELECT t FROM Tipus t WHERE t.azonosito = :azon");
	tipusQuery.setParameter("azon", tipusID);
	
	Tipus t = null;
	try {
	    t = (Tipus) tipusQuery.getSingleResult();
	} catch (NoResultException e) {
	    System.out.println("?");
	    return;
	}
	
	Query mozdonyQuery = em.createQuery("SELECT m FROM Mozdony m WHERE m.id = :azon");
	mozdonyQuery.setParameter("azon", sorszamInt);
	try {
	    mozdonyQuery.getSingleResult();
	    System.out.println("?");
	} catch (NoResultException e) {
	    ujEntity(new Mozdony(sorszamInt, futottkmInt, t));
	}
    }

    // Uj vonatszam felvetele
    public void ujVonatszam(String sorszam, String uthossz) throws Exception {
	// TODO
	// Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket.
	// Ellen�rizze, hogy van-e m�r ilyen vonatsz�m
	// Hozza l�tre az �j "Vonatsz�m" entit�st �s r�gz�tse adatb�zisban az
	// "ujEntity" met�dussal.
    }

    // Uj vonat felvetele
    public void ujVonat(String vonatszamAzonosito, String datum,
	    String mozdonySorszam, String keses) throws Exception {
	// TODO
	// Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket.
	// Tipp: haszn�lja a SimpleDateFormat-ot
	// Form�tum: "yyyy.MM.dd"
	// Ellen�rizze, hogy �rv�nyes-e a vonatsz�m, �s l�tezik a mozdony.
	// Ellen�rizze, hogy az adott napon nincs m�sik vonat ugyanezzel a
	// vonatsz�mmal.
	// Hozza l�tre az �j "Vonat" entit�st �s r�gz�tse adatb�zisban az
	// "ujEntity" met�dussal.
	// N�velje a mozdony futottkm-�t a vonatsz�m szerinti �thosszal.
    }

    // Listazasi szolgaltatasok
    public void listazEntity(List list) {
	for (Object o : list) {
	    System.out.println(o);
	}
    }

    // Tipusok listazasa
    public void listazTipus() throws Exception {
	listazEntity(em.createQuery("SELECT t FROM Tipus t").getResultList());
    }

    // Mozdonyok listazasa
    public void listazMozdony() throws Exception {
	
	Query mozdonyQuery = em.createQuery("SELECT m FROM Mozdony m");
	listazEntity(mozdonyQuery.getResultList());
    }

    // Vonatszamok listazasa
    public void listazVonatszam() throws Exception {
	// TODO
	// K�sz�tsen lek�rdez�st, amely visszaadja az �sszes vonatsz�mot, majd
	// irassa ki a listazEntity met�dussal az eredm�nyt.
    }

    // Vonatok listazasa
    public void listazVonat() throws Exception {
	// TODO
	// K�sz�tsen lek�rdez�st, amely visszaadja az �sszes vonatot, majd
	// irassa ki a listazEntity met�dussal az eredm�nyt.
    }

    // Egyedi lekerdezes
    public void lekerdezes(String datum) throws Exception {
	// TODO
	// �rja ki a param�terk�nt kapott napra (INPUTNAP) vonatkoz�an, hogy az
	// egyes mozdony-fajt�k az adott napon �sszesen h�ny kilom�tert
	// futottak.
	// Alak�tsa �t a megfelel� t�pusokra a kapott String param�tereket.
	// Tipp: haszn�lja a SimpleDateFormat-ot
	// Tipp: N�zzen ut�na a "t�bbsz�r�s SELECT" kezel�s�nek
    }
}

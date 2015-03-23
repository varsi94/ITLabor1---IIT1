package jpa;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
	Query q = em
		.createQuery("SELECT t FROM Tipus t WHERE t.azonosito = :azon");
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

	Query tipusQuery = em
		.createQuery("SELECT t FROM Tipus t WHERE t.azonosito = :azon");
	tipusQuery.setParameter("azon", tipusID);

	Tipus t = null;
	try {
	    t = (Tipus) tipusQuery.getSingleResult();
	} catch (NoResultException e) {
	    System.out.println("?");
	    return;
	}

	Query mozdonyQuery = em
		.createQuery("SELECT m FROM Mozdony m WHERE m.id = :azon");
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
	int sorszamInt;
	long utHosszLong;
	
	try {
	    sorszamInt = Integer.parseInt(sorszam);
	    utHosszLong = Long.parseLong(uthossz);
	} catch (NumberFormatException e) {
	    System.out.println("?");
	    return;
	}
	
	Query vonatszamQuery = em.createQuery("SELECT vsz FROM Vonatszam vsz WHERE vsz.szam = :sorszam");
	vonatszamQuery.setParameter("sorszam", sorszamInt);
	try {
	    vonatszamQuery.getSingleResult();
	    System.out.println("?");
	} catch (NoResultException e) {
	    ujEntity(new Vonatszam(sorszamInt, utHosszLong));
	}
    }

    // Uj vonat felvetele
    public void ujVonat(String vonatszamAzonosito, String datum,
	    String mozdonySorszam, String keses) throws Exception {
	int vonatszam, mozdonyID, kesesInt;
	Date datumParsed;
	
	try {
	    vonatszam = Integer.parseInt(vonatszamAzonosito);
	    mozdonyID = Integer.parseInt(mozdonySorszam);
	    kesesInt = Integer.parseInt(keses);
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	    datumParsed = sdf.parse(datum);
	} catch (NumberFormatException e) {
	    System.out.println("?");
	    return;
	} catch (ParseException e) {
	    System.out.println("?");
	    return;
	}
	
	Vonatszam vsz;
	try {
	    Query vonatszamQuery = em.createQuery("SELECT vsz FROM Vonatszam vsz WHERE vsz.szam = :szam");
	    vonatszamQuery.setParameter("szam", vonatszam);
	    vsz = (Vonatszam) vonatszamQuery.getSingleResult();
	} catch (NoResultException e) {
	    System.out.println("?");
	    return;
	}
	
	Mozdony mozdony;
	try {
	    Query mozdonyQuery = em.createQuery("SELECT m FROM Mozdony m WHERE m.id = :id");
	    mozdonyQuery.setParameter("id", mozdonyID);
	    mozdony = (Mozdony) mozdonyQuery.getSingleResult();
	} catch (NoResultException e) {
	    System.out.println("?");
	    return;
	}
	
	try {
	    Query vonatQuery = em.createQuery("SELECT v FROM Vonat v WHERE v.datum = :datum AND v.vonatSzam = :vsz");
	    vonatQuery.setParameter("datum", datumParsed);
	    vonatQuery.setParameter("vsz", vsz);
	    vonatQuery.getSingleResult();
	    System.out.println("?");
	    return;
	} catch (NoResultException e) {
	    //jók vagyunk, mehetünk tovább
	    ujEntity(new Vonat(datumParsed, kesesInt, mozdony, vsz));
	    em.getTransaction().begin();
	    mozdony.setFutottkm(mozdony.getFutottkm() + vsz.getUthossz().intValue());
	    em.persist(mozdony);
	    em.getTransaction().commit();
	}
    }

    // Listazasi szolgaltatasok
    @SuppressWarnings("rawtypes")
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
	listazEntity(em.createQuery("SELECT m FROM Mozdony m").getResultList());
    }

    // Vonatszamok listazasa
    public void listazVonatszam() throws Exception {
	listazEntity(em.createQuery("SELECT vsz FROM Vonatszam vsz").getResultList());
    }

    // Vonatok listazasa
    public void listazVonat() throws Exception {
	listazEntity(em.createQuery("SELECT v FROM Vonat v").getResultList());
    }

    // Egyedi lekerdezes
    @SuppressWarnings("unchecked")
    public void lekerdezes(String datum) throws Exception {
	Date datumParsed;
	try {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
	    datumParsed = sdf.parse(datum);
	} catch (ParseException e) {
	    System.out.println("?");
	    return;
	}
	
	Query query = em.createQuery("SELECT t.fajta, SUM(vsz.uthossz) "
		+ "FROM Tipus t, Mozdony m, Vonat v, Vonatszam vsz "
		+ "WHERE v.datum = :input AND v.vonatSzam = vsz AND v.mozdony = m AND m.tipus = t "
		+ "GROUP BY t.fajta");
	query.setParameter("input", datumParsed);
	List<Object[]> result = query.getResultList();
	for (Object[] item : result) {
	    System.out.println(item[0] + " " + item[1]);
	}
    }
}

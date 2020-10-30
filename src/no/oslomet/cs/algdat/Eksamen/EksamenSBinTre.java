package no.oslomet.cs.algdat.Eksamen;


import java.util.*;

public class EksamenSBinTre<T> {
    private static final class Node<T>   // en indre nodeklasse
    {
        private T verdi;                   // nodens verdi
        private Node<T> venstre, høyre;    // venstre og høyre barn
        private Node<T> forelder;          // forelder

        // konstruktør
        private Node(T verdi, Node<T> v, Node<T> h, Node<T> forelder) {
            this.verdi = verdi;
            venstre = v;
            høyre = h;
            this.forelder = forelder;
        }

        private Node(T verdi, Node<T> forelder)  // konstruktør
        {
            this(verdi, null, null, forelder);
        }

        @Override
        public String toString() {
            return "" + verdi;
        }

    } // class Node

    private Node<T> rot;                            // peker til rotnoden
    private int antall;                             // antall noder
    private int endringer;                          // antall endringer

    private final Comparator<? super T> comp;       // komparator

    public EksamenSBinTre(Comparator<? super T> c)    // konstruktør
    {
        rot = null;
        antall = 0;
        comp = c;
    }

    public boolean inneholder(T verdi) {
        if (verdi == null) return false;

        Node<T> p = rot;

        while (p != null) {
            int cmp = comp.compare(verdi, p.verdi);
            if (cmp < 0) p = p.venstre;
            else if (cmp > 0) p = p.høyre;
            else return true;
        }

        return false;
    }

    public int antall() {
        return antall;
    }

    public String toStringPostOrder() {
        if (tom()) return "[]";

        StringJoiner s = new StringJoiner(", ", "[", "]");

        Node<T> p = førstePostorden(rot); // går til den første i postorden
        while (p != null) {
            s.add(p.verdi.toString());
            p = nestePostorden(p);
        }

        return s.toString();
    }

    public boolean tom() {
        return antall == 0;
    }

    public boolean leggInn(T verdi) {// Bruke programkode fra kompendie for å opprette nye noder 5.2.3 a)
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er nå null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi,q);                   // oppretter en ny node, lagt til q så den får tiktig peker på forelder

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // hoeyre barn til q

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    public boolean fjern(T verdi) {       // Benyttet meg av programkode 5.2.8.d)
        if (verdi == null) return false;  // treet har ingen nullverdier

        Node<T> p = rot, q = null;   // q skal være forelder til p

        while (p != null)            // leter etter verdi
        {
            int cmp = comp.compare(verdi,p.verdi);      // sammenligner
            if (cmp < 0) {
                q = p;
                p = p.venstre;
            }      // går til venstre
            else if (cmp > 0) {
                q = p;
                p = p.høyre;
            }   // går til høyre
            else break;    // den søkte verdien ligger i p
        }
        if (p == null) return false;   // finner ikke verdi

        if (p.venstre == null || p.høyre == null)  // Tilfelle 1) og 2)
        {
            Node<T> b = p.venstre != null ? p.venstre : p.høyre;  // b for barn
            if (p == rot) {
                rot = b;
            }
            else if (p == q.venstre){
                q.venstre = b;
            }
            else {
                q.høyre = b;
            }
            if (b!=null){           // skal oppdateres dersom noden hadde barn
                b.forelder=q;
            }

        }
        else  // Tilfelle 3)
        {
            Node<T> s = p, r = p.høyre;   // finner neste i inorden
            while (r.venstre != null)
            {
                s = r;    // s er forelder til r
                r = r.venstre;
            }
            p.verdi = r.verdi;   // kopierer verdien i r til p

            if (s != p) {
                s.venstre = r.høyre;
            }
            else s.høyre = r.høyre;
        }

        antall--;   // det er nå én node mindre i treet
        return true;
    }

    public int fjernAlle(T verdi) {
        Objects.requireNonNull(verdi,"Kan ikke være null"); // ha en sjekk som sjekker at input ikke er null eller treet er tomt
        if (tom()){
            return 0;
        }
        int antallFjernet=0;
        while (fjern(verdi)){       // en while loekke som kjøres saa lenge fjern metoden returnerer true
            antallFjernet++;        // inni løkken skal den telle antall ganger verdi fjernes fra treet
        }
        return antallFjernet;       // returnerer antall
    }


    public int antall(T verdi) {                       // Denne ble løst med inspirasjon fra kompendiet 5.2.6 a): Søking etter en verdi
        if (tom()){                                    // Starter ved å kalle paa tom() metoden for å sjekke om treet er tomt
            return 0;
        }

        Node<T> p = rot;                             // Oppretter hjelpenode for å løpe gjennom treet fra og med rot og initialiserer antall
        int antall= 0;

        while(p!=null){                             // Bruker while løkke for å loepe gjennom treet
            int cmp = comp.compare(verdi,p.verdi);  // Bruker comp for å sammenligne høyre og venstre
            if (cmp < 0){                           // if setninger for comp sine resultater
                p= p.venstre;
            }
            else if(cmp>0){
                p=p.høyre;
            }
            else {
                p=p.høyre;
                antall++;
            }
        }
        return antall;                              // returnerer antallet
    }

    public void nullstill() {   // løst med å få litt inspirasjon fra serialiseringsmetoden
       if (tom()){                              //først sjekk at treet ikke allerede er tomt
           return;
       }
       Queue<Node<T>> koe = new ArrayDeque<>(); // Opprette en kø
       koe.add(rot);                            // starter med å legge til roten i køen

       while(!koe.isEmpty()){                   // skal kjøres så lenge køen ikke er tom
           Node<T> p = koe.remove();            // fjerner elementer
           if(p.venstre!=null){
               koe.add(p.venstre);              // sjekker om noden har et venstre barn som skal fjernes
           }
           if (p.høyre!=null){                  // sjekker om noden har et høyre barn som skal fjernes
               koe.add(p.høyre);
           }
       }
       antall=0;                                // oppdaterer antall
       rot=null;                                // nullstiller rot
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {         //Benytter meg her av kompendiet sin programkode 5.1.7 h)
        Objects.requireNonNull(p,"Kan ikke være null");     // Bruke object sin Not Null for å forsikre meg om at parameter ikke er null

        while(true){                                                // Ettersom p er rot skal vi løpe gjennom til vi finner det ytterste bladet til venstre
            if (p.venstre!=null){                                   //if setninger som sjekker først om venstre barn er null så hoeyre
                p=p.venstre;
            }
            else if (p.høyre!=null){
                p=p.høyre;
            }
            else return p;                                          // returnerer noden når venstre og høyre barn er null.
        }
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {                  // Følger prinsippene beskrevet i kompendie 5.1.7 om Postorden
        Objects.requireNonNull(p,"Kan ikke være null");             // Bruke object sin Not Null for å forsikre meg om at parameter ikke er null

        if (p.forelder==null){                                              // returnere null om p.forelder= null
            return null;
        }
        if (p.forelder.høyre==p){                                      // Hvis det er høyrebarn saa er foreldrenode neste
                return p.forelder;
            }
        else if (p.forelder.venstre==p && p.forelder.høyre==null){     // Hvis det er et venstrebarn uten at forelder har hoeyrebarn er forelder neste
                return p.forelder;
            }
            else {
                p=p.forelder.høyre;
                while (p.venstre!=null || p.høyre!=null){                   // while loekke for å løpe gjennom for å komme lengst mulig ned mot venstre
                    if (p.venstre!=null){
                        p=p.venstre;
                    }
                    else{
                        p=p.høyre;
                    }
                }
                return p;                                                   // returnerer p
            }
    }

    public void postorden(Oppgave<? super T> oppgave) {
        Node<T> p = førstePostorden(rot);   // Opprette rotnode som skal være utgangspunkt for å kalle på nestePostorden

        while (p!=null){                    // ha løkke som kjøres gjennom hele treet
            oppgave.utførOppgave(p.verdi);  // legge til verdi i oppgaven
            p=nestePostorden(p);
        }
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        if (p.venstre!=null){                     //kalle på metoden saa lenge venstre ikke er null
            postordenRecursive(p.venstre,oppgave);
        }
        if(p.høyre!=null){                          // kalle på metoden saa lenge hoeyre ikke er null
            postordenRecursive(p.høyre,oppgave);
        }
        oppgave.utførOppgave(p.verdi);              // utfør oppgave
    }

    public ArrayList<T> serialize() {                   // Løst ved hjelp av programkode over 5.1.6 a) fra kompendiet
        if (tom()){                                     // først sjekke om treet er tomt
            return null;
        }

        Queue<Node<T>> koe = new ArrayDeque<>();        // Opprette en kø og en liste det skal legges inn i
        ArrayList<T> returner = new ArrayList<>();
        koe.add(rot);                                   // Starte med å legge rot inn i koe

        while (!koe.isEmpty()){                         // så lenge koeen ikke er tom skal loekka kjoeres
            Node<T> p = koe.poll();                     // Opprette hjelpenode fra element i koeen
            returner.add(p.verdi);
            if (p.venstre!=null){                       // if setninger som sjekker om venstre og høyre barn er null og legger inn i køen
                koe.add(p.venstre);
            }
            if (p.høyre != null){
                koe.add(p.høyre);
            }
        }
        return returner;                                // returnerer listen
    }

    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        EksamenSBinTre<K> tre = new EksamenSBinTre<>(c);    //opprette objekt av treet

        for (K p: data ){                                   // for løkke som løper gjennom listen som tas inn og legger inn elementet
            tre.leggInn(p);
        }
        return tre;                                          // returnerer treet
    }


} // ObligSBinTre

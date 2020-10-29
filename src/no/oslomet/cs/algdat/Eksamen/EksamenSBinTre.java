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

    public boolean leggInn(T verdi) {
        // Bruke programkode fra kompendie for aa opprette nye noder
        Objects.requireNonNull(verdi, "Ulovlig med nullverdier!");

        Node<T> p = rot, q = null;               // p starter i roten
        int cmp = 0;                             // hjelpevariabel

        while (p != null)       // fortsetter til p er ute av treet
        {
            q = p;                                 // q er forelder til p
            cmp = comp.compare(verdi,p.verdi);     // bruker komparatoren
            p = cmp < 0 ? p.venstre : p.høyre;     // flytter p
        }

        // p er naa null, dvs. ute av treet, q er den siste vi passerte

        p = new Node<>(verdi,q);                   // oppretter en ny node

        if (q == null) rot = p;                  // p blir rotnode
        else if (cmp < 0) q.venstre = p;         // venstre barn til q
        else q.høyre = p;                        // hoeyre barn til q

        antall++;                                // én verdi mer i treet
        return true;                             // vellykket innlegging
    }

    public boolean fjern(T verdi) {       // Benyttet meg av programkode 5.2.8.b)
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
        // ha en sjekk som sjekker at input ikke er null eller treet er tomt
        Objects.requireNonNull(verdi,"Kan ikke være null");
        if (tom()){
            return 0;
        }
        int antallFjernet=0;
        // en while loekke som kjoeres saa lenge fjern metoden returnerer true
        while (fjern(verdi)){
            antallFjernet++;
        }
        // inni loekken skal den telle antall ganger verdi fjernes fra treet
        return antallFjernet;
        // returnerer antall
    }

    // Denne ble loest med inspirasjon fra kompendiet 5.2.6: Soeking etter en verdi
    public int antall(T verdi) {
        if (tom()){                                    // Starter ved aa kalle paa tom() metoden for aa sjekke om treet er tomt
            return 0;
        }

        Node<T> p = rot;                             // Oppretter hjelpenode for aa løpe gjennom treet fra og med rot og initialiserer antall
        int antall= 0;

        while(p!=null){                             // Bruker while loekke for aa loepe gjennom treet
            int cmp = comp.compare(verdi,p.verdi);  // Bruker comp for aa sammenligne hoeyre og venstre
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

    public void nullstill() {
            // tanken er aa traversere postorden
            //først sjekk at treet ikke allerede er tomt
        if (tom()){
            return;
        }
            //opprettehjelpenode ved hjelp av metoden foerstePostorden og forelder som er roten
        Node<T> p = null;
        Node<T> q =null;
        if (antall==1){
            rot=null;
            antall=0;
            return;
        }
        if (antall>0) {
            p = førstePostorden(rot);
            q = p.forelder;
        }
            // loekke som skal kjoeres saa lenge treet ikke er tomt
            while (p != null) {
                if (p == q.venstre) {
                    q.venstre = null;
                    q.verdi = null;
                } else {
                    q.høyre = null;
                    q.verdi = null;
                }
                p = førstePostorden(rot);
            }

            // sjekke om jeg skal fjerne venstre eller hoeyrebarn
        rot=null;
        antall=0;
            // sette node til neste i postorden

            // fjerne rot og oppdatere antall

    }

    private static <T> Node<T> førstePostorden(Node<T> p) {         //Benytter meg her av kompendiet sin programkode 5.1.7 g
       // Objects.requireNonNull(p,"Kan ikke være null");     // Bruke object sin Not Null for aa forsikre meg om at parameter ikke er null

        while(true){                                                // Ettersom p er rot skal vi loope gjennom til vi finner det ytterste bladet til venstre
            if (p.venstre!=null){                                   //if setninger som sjekker foerst om venstre barn er null saa hoeyre
                p=p.venstre;
            }
            else if (p.høyre!=null){
                p=p.høyre;
            }
            else if (p==null){
                return null;
            }
            else return p;                                          // returnerer noden naar venstre og hoeyre barn er null.
        }
    }

    private static <T> Node<T> nestePostorden(Node<T> p) {
        Objects.requireNonNull(p,"Kan ikke være null");             // Bruke object sin Not Null for aa forsikre meg om at parameter ikke er null

        if (p.forelder==null){                                              // returnere null om p.forelder= null
            return null;
        }
        if (p.forelder.høyre==p){                                      // Hvis det er hoeyrebarn saa er foreldrenode neste
                return p.forelder;
            }
        else if (p.forelder.venstre==p && p.forelder.høyre==null){     // Hvis det er et venstrebarn uten at forelder har hoeyrebarn er forelder neste
                return p.forelder;
            }
            else {
                p=p.forelder.høyre;
                while (p.venstre!=null || p.høyre!=null){                   // while loekke for aa loepe gjennom for aa komme lengst mulig ned mot venstre
                    if (p.venstre!=null){
                        p=p.venstre;
                    }
                    else{
                        p=p.høyre;
                    }
                }
                return p;                                                   // returnerer p dersom den kom til else setningen
            }
    }

    public void postorden(Oppgave<? super T> oppgave) {
        Node<T> p = førstePostorden(rot);   // Opprette rotnode som skal være utgangspunkt for aa kalle paa nestePostorden

        while (p!=null){                    // ha loekke som kjoeres gjennom hele treet
            oppgave.utførOppgave(p.verdi);  // legge til verdi i oppgaven
            p=nestePostorden(p);
        }
    }

    public void postordenRecursive(Oppgave<? super T> oppgave) {
        postordenRecursive(rot, oppgave);
    }

    private void postordenRecursive(Node<T> p, Oppgave<? super T> oppgave) {
        if (p.venstre!=null){                     //kalle paa metoden saa lenge venstre ikke er null
            postordenRecursive(p.venstre,oppgave);
        }
        if(p.høyre!=null){                          // kalle paa metoden saa lenge hoeyre ikke er null
            postordenRecursive(p.høyre,oppgave);
        }
        oppgave.utførOppgave(p.verdi);              // utfoer oppgave
    }

    public ArrayList<T> serialize() {                   // Loest ved hjelp av programkode over 5.1.6 a) fra kompendiet
        if (tom()){                                     // foerst sjekke om treet er tomt
            return null;
        }

        Queue<Node<T>> koe = new ArrayDeque<>();        // Opprette en koe og en liste det skal legges inn i
        ArrayList<T> returner = new ArrayList<>();
        // Starte med å legge rot inn i koe
        koe.add(rot);

        while (!koe.isEmpty()){                         // saa lenge koeen ikke er tom skal loekka kjoeres
            Node<T> p = koe.poll();                     // Opprette hjelpenode fra element i koeen
            returner.add(p.verdi);
            if (p.venstre!=null){                       // if setninger som sjekker om venstre og hoeyre barn er null og legger inn i koeen
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

        for (K p: data ){                                   // for loekke som loeper gjennom listen som tas inn og legger inn elementet
            tre.leggInn(p);
        }
        return tre;                                          // returnerer treet
    }


} // ObligSBinTre

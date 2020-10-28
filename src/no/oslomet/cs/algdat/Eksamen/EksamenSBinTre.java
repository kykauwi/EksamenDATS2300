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

    public boolean fjern(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    public int fjernAlle(T verdi) {
        throw new UnsupportedOperationException("Ikke kodet ennå!");
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
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    private static <T> Node<T> førstePostorden(Node<T> p) {         //Benytter meg her av kompendiet sin programkode 5.1.7 g
        Objects.requireNonNull(p,"Kan ikke være null");     // Bruke object sin Not Null for aa forsikre meg om at parameter ikke er null
        while(true){                                                // Ettersom p er rot skal vi loope gjennom til vi finner det ytterste bladet til venstre
            if (p.venstre!=null){                                   //if setninger som sjekker foerst om venstre barn er null saa hoeyre
                p=p.venstre;
            }
            else if (p.høyre!=null){
                p=p.høyre;
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

    public ArrayList<T> serialize() {
        // foerst sjekke om treet er tomt

        // Opprette en koe og en liste det skal legges inn i
        // Starte med å legge rot inn i koe

        // saa lenge koeen ikke er tom skal loekka kjoeres
        // Opprette hjelpenode fra element i koeen

        // if setninger som sjekker om venstre og hoeyre barn er null og legger inn i listen

        // returnerer listen

        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }

    static <K> EksamenSBinTre<K> deserialize(ArrayList<K> data, Comparator<? super K> c) {
        //opprette objekt av treet

        // for loekke som loeper gjennom listen som tas inn og leger inn verdien til elementet

        // returnerer treet
        throw new UnsupportedOperationException("Ikke kodet ennå!");
    }


} // ObligSBinTre

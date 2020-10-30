# Mappeeksamen i Algoritmer og Datastrukturer Høst 2020

# Krav til innlevering

Se oblig-tekst for alle krav, og husk spesielt på følgende:

* Git er brukt til å dokumentere arbeid (minst 2 commits per oppgave, beskrivende commit-meldinger)	
* git bundle er levert inn
* Hovedklassen ligger i denne path'en i git: src/no/oslomet/cs/algdat/Eksamen/EksamenSBinTre.java
* Ingen debug-utskrifter
* Alle testene i test-programmet kjører og gir null feil (også spesialtilfeller)
* Readme-filen her er fyllt ut som beskrevet


# Beskrivelse av warnings
* Får en warning pga at non ascii characters blir brukt, æøå var greit å bruke og java klassen vi fikk utlevert inneholdt disse tegnene
* Return value i leggInn(T verdi) is never used- her har jeg stort sett brukt programkode pluss noen endringer, men metoden returnerer boolean
* Endringer objektet og inneholder(T verdi) is never used- Endringer så jeg vi kunne se bort ifra i discussions og gjorde derfor dette. Inneholder metoden har jeg ikke benyttet meg av for å løse oppgavene.


# Beskrivelse av oppgaveløsning (4-8 linjer/setninger per oppgave)

Vi har brukt git til å dokumentere arbeidet vårt. Jeg har 27 commits totalt, og hver logg-melding beskriver det jeg har gjort av endringer.

* Oppgave 1: Løste ved å bruke programkode 5.2.3 a) oppgitt i oppgaveteksten og gjorde endringer som trengtes for at forelder peker får 
korrekt verdi i hver node. Dette løste jeg ved å legge inn en forelder referanse i opprettelsen av noden i programkoden. Brukte også main 
koden som ble oppgitt for å sjekke at jeg fikk riktig utskrift.
* Oppgave 2: Her benyttet jeg meg litt av programkode 5.2.6 a) for hvordan jeg løste oppgaven, metoden Antall(T verdi) sjekker
først at treet ikke er tomt før den intialiserer en hjelpenode og antall variabel. Deretter løper den gjennom treet ved hjelp av comparator for å 
sjekke om den skal til venstre eller høyre. Dersom den ikke går noen av veiene la jeg til at den skulle gå til høyre og antall skulle oppdateres siden verdien er funnet. 
* Oppgave 3: I førstePostOrden(Node <T> p ) benyttet jeg meg av programkode 5.1.7 h) der p er rot. Den hadde en løkke som sjekket om den skulle gå til venstre eller høyre
til den hadde funnet ytterste bladnode til venstre,det vil si at p.venstre og p.høyre er null returnerte den riktig node. 
I nestePostorden(Node <T> p) fulgte jeg prinsippene om postorden. Dersom forelder er null så skulle den returnere null, ettersom den ikke hadde en neste node.
Dersom noden var et høyrebarn så er foreldrenode neste postorden. Og hvis det er et venstrebarn uten en søsken (foreldre har ikke høyrebarn) så er forelder neste.
Til slutt dersom ingen av disse var tilfellene hadde jeg en løkke som tok utgangspunkt i p.forelder.høyre for å komme lengst mulig ned mot venstre. 
* Oppgave 4: postorden() metoden ble løst med hjelp fra førstePostorden og nestePostorden metodene. Den skulle starte i førstepostorden med utgangspunkt i rot før den hadde 
en løkke som tok den gjennom treet postorden ved hjelp av nestePostorden(p) metoden. Denne metoden sin return ble brukt for å oppdatere p. I tillegg 
ble oppgaven som skal utføres gjort inne i løkka. postordenRecursive ble løst ved to sjekker, om den skal til venstre eller høyre så kaller den på seg selv med oppdatert node.
Dette resulterer i at når både venstre og høyre barn av node er null så skal oppgaven utføres.
* Oppgave 5: I serialize() metoden fikk jeg litt hjelp fra programkdoe 5.1.6 a), men brukte arraydeque istedenfor kø
som programkoden brukte. Den starter med å opprette en liste som skal returneses og en kø. Køen starter i rot før den 
går inn i løkka der noden henter element fra køen og legger inn i returner. så legger den til barnenodene i køen. Dette gjøres så lenge køen ikke er tom.
I deserialize()  oppretter den først et objekt av treet før den løper gjennom listen som tas inn og skal legges inn i treet og bruker leggInn()
metoden for å legge inn hver enkelt node fra listen i treet på riktig sted. Til slutt returnerer den treet som er blitt opprettet. 
* Oppgave 6: I fjern() metoden bruker jeg programkode 5.2.8 d) endringen som er blitt gjort her for å få riktige pekere er at dersom det fjernede elementet hadde barn, skal deres foreldrepeker oppdateres. Dette er løst ved en if setning.
I fjernAlle() har jeg først en sjekk på at parameter verdi ikke er null og at treet ikke er tomt, deretter initialiserer jeg antallfjernet verdi som skal returneres.
Denne verdien økes i en løkke så lenge fjern(verdi) returnerer true. I Nullstill() metoden hadde jeg noen utfordringer da jeg først prøve å løse
oppgaven uten en kø til hjelp. Jeg prøvde først å løpe gjennom den postorden ved at jeg brukte FørstePostorden metoden og alltid tok utganspunkt i rot. 
Denne oppgaven passerte som regel testene, men jeg testet flere ganger og noen ganger fikk jeg feil. Jeg vill være sikker og brukte
derfor isteden en kø med inspirasjon fra serialiseringsmetoden for å fjerne elementer. Etter dette nullstiller den rot og oppdaterer antall. 

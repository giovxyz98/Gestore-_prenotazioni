import java.util.List;
import java.util.Map;
import java.util.Random;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap; 

 class StoricoAnnuo {
    private static StoricoAnnuo instance;
    private List<StoricoSettimanale> storicoAnno;

    private StoricoAnnuo() {
        storicoAnno = new ArrayList<>();
    }
    public List<StoricoSettimanale> getStoricoAnnuo(){
        return storicoAnno;
    }
    public static StoricoAnnuo getInstance() {
        if (instance == null) {
            instance = new StoricoAnnuo();
        }
        return instance;
    }

    public void aggiungiAlloStorico(StoricoSettimanale StoricoSettimanale) {
        storicoAnno.add(StoricoSettimanale);
    }

    public void elencaSpettacoli() {
        for (StoricoSettimanale settimana : storicoAnno) {
            settimana.stampaSpettacoli();
        }
    }


}

class StoricoSettimanale {
    protected int settimana;
    protected List<Spettacolo> storicoSettimanale;

    private StoricoSettimanale(int settimana) {
        this.settimana = settimana;
        this.storicoSettimanale = new ArrayList<>();

    }

    public void stampaSpettacoli() {
        System.out.println("Spettacoli della settimana"+settimana+":\n");
        for (Spettacolo spettacolo : storicoSettimanale) {
            spettacolo.stampaDettagli();
        }
    }

    public void creaStorico(int settimana){
        if(!esisteStorico(settimana)){
            StoricoSettimanale storicoSettimanale = new StoricoSettimanale(settimana);
            StoricoAnnuo.getInstance().getStoricoAnnuo().add(storicoSettimanale);
            System.out.println("\nCreato nuovo storico settimanale e aggiunto allo storico annuo\n");
        }
    }
    public boolean esisteStorico(int settimana) {
        for(StoricoSettimanale storico : StoricoAnnuo.getInstance().getStoricoAnnuo()){
            if(storico.settimana == settimana){
                return true;
            }
        };
        return false;
    }
    public void addSpettacolo(int settimana, Spettacolo spettacolo) {
    if (!esisteStorico(settimana)) {
        creaStorico(settimana);}
    StoricoAnnuo.getInstance().getStoricoAnnuo().get(settimana).storicoSettimanale.add(spettacolo);

}
    public void rimuoviSpettacolo(int settimana, Spettacolo spettacolo) {
        if (!esisteStorico(settimana)) {
            System.out.println("\nSettimana non esistente\n");
        }
        else {
            StoricoAnnuo.getInstance().getStoricoAnnuo().get(settimana).storicoSettimanale.remove(spettacolo);
        }
    }
}

 class Spettacolo {
    String titolo, codiceSpettacolo;
    LocalDateTime datetime;
    int numeroSala;

    public Spettacolo(String titolo, String codiceSpettacolo, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        this.titolo = titolo;
        this.codiceSpettacolo = codiceSpettacolo;
        this.numeroSala = sala.numeroSala;
        this.datetime = LocalDateTime.of(anno, mese, giorno, ore, minuti);
    }

    public void stampaDettagli() {
        System.out.println("Titolo: " + this.titolo);
        System.out.println("Codice Spettacolo: " + this.codiceSpettacolo);
        System.out.println("Numero Sala: " + this.numeroSala);
        System.out.println("Orario: " + this.datetime);
    }
}

class GestoreSale{
    private static GestoreSale instance;
    private List<Sala> saleTotali;
    private GestoreSale(){
    saleTotali = new ArrayList<>();
    }
    public static GestoreSale getInstance(){
        if (instance == null) {
            instance = new GestoreSale();
        }
        return instance;
    }
    private boolean controllaDisponibilità(int numeroSala, int slot){
        Sala sala = getSala(numeroSala);
        if(sala != null && sala.slot.containsKey(slot)){
            return sala.slot.get(slot);
        }
        else return false;
    }

    public void prenotaSala(int numeroSala, int slot){
        if(!controllaDisponibilità(numeroSala, slot)){
            System.out.println("Sala non prenotabile");
        }
        else{
            Sala sala = getSala(numeroSala);
            if (sala != null && sala.slot.containsKey(slot)) {
                sala.slot.put(slot, true);
                System.out.println("Sala " + numeroSala + ", slot " + slot + " prenotato con successo.");
            } else {
                System.out.println("Errore nella prenotazione della sala.");

            }
        }
    }
    public void liberaSala(int numeroSala, int slot) {
        Sala sala = getSala(numeroSala);

        if (sala != null && sala.slot.containsKey(slot)) {
            sala.slot.put(slot, false);
            System.out.println("Sala " + numeroSala + ", slot " + slot + " liberato con successo.");
        } else {
            System.out.println("Errore nella liberazione della sala o dello slot.");
        }
    }
    private Sala getSala(int numeroSala){
        for(Sala sala : saleTotali){
            if(sala.numeroSala == numeroSala){
                return sala;
            }
        } return null;
    }
}

class Sala {
    int numeroSala;
    Map<Integer, Boolean> slot;
    List<Posto> listaPosti;

    private Sala(int numeroSala) {
        this.numeroSala = numeroSala;
        this.listaPosti = creaPosti(); 
        this.slot = inizializzaSlotDisponibili();

    }
    private Map<Integer, Boolean> inizializzaSlotDisponibili() {
        Map<Integer, Boolean> slotMap = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            slotMap.put(i, false);
        }
        return slotMap;
    }

    private List<Posto> creaPosti() {
        List<Posto> posti = new ArrayList<>();
        for (char fila = 'A'; fila <= 'Z'; fila++) {
            for (int numeroPosto = 1; numeroPosto <= 10; numeroPosto++) {
                posti.add(new Posto(fila, numeroPosto));
            }
        }
        return posti;
    }

}

class Posto {
    char fila;
    int numeroPosto;
    boolean prenotato;

    public Posto(char fila, int numeroPosto) {
        this.fila = fila;
        this.numeroPosto = numeroPosto;
        this.prenotato = false;
    }

    public boolean isPrenotato() {
        return prenotato;
    }

    public void setPrenotato(boolean prenotato) {
        this.prenotato = prenotato;
    }
}

class Prenotazione{
    String idPrenotazione;
    LocalTime datetime;
    Prenotazione(String idPrenotazione){
        this.idPrenotazione = idPrenotazione;
        datetime = LocalTime.now();

    }
}

class GestorePrenotazioni {
    private static GestorePrenotazioni instance;
    private int numeroPrenotazioni;
    private List<Prenotazione> prenotazioni;

    private GestorePrenotazioni() {
        numeroPrenotazioni = 0;
        prenotazioni = new ArrayList<>();
    }

    public static GestorePrenotazioni getInstance() {
        if (instance == null) {
            instance = new GestorePrenotazioni();
        }
        return instance;
    }

    private static String generaNumeroPrenotazione() {
        Random random = new Random();
        int numeroCasuale = 100000 + random.nextInt(900000);
        String numeroPrenotazione = "P" + numeroCasuale;
        return numeroPrenotazione;
    }
    public void effettuaPrenotazione(Spettacolo spettacolo, Posto posto) {
        if (!posto.prenotato){
        Prenotazione prenotazione = new Prenotazione(generaNumeroPrenotazione());
        prenotazioni.add(prenotazione);
        numeroPrenotazioni++;
        posto.setPrenotato(true);
        Biglietto biglietto = Biglietto.creaBiglietto(spettacolo, posto);
        biglietto.stampaBiglietto(prenotazione.datetime, prenotazione.idPrenotazione);
        System.out.println("Prenotazione effettuata con successo.");
        } else {
            System.out.println("Il posto è già prenotato. Scegli un altro posto.");
        }
    }


    public int getNumeroPrenotazioni() {
        return numeroPrenotazioni;
    }
}

class Biglietto {
    Spettacolo spettacolo;
    Posto posto;
    private Biglietto(Spettacolo spettacolo, Posto posto) {
        this.spettacolo= spettacolo;
        this.posto = posto;
    }
    public static Biglietto creaBiglietto(Spettacolo spettacolo, Posto posto) {
        return new Biglietto(spettacolo, posto);
    }
    public void stampaBiglietto(LocalTime ora, String numeroPrenotazione ){
        System.out.println("Ora prenotazione: "+ ora+"\nNumero Prenotazione: "+ numeroPrenotazione + "\nNumero posto: " + posto.numeroPosto+"\nSpettacolo: \n"+ spettacolo.titolo+"\n"+spettacolo.codiceSpettacolo+"\n"+spettacolo);
    }
}
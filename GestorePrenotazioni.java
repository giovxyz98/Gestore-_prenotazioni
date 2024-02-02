import java.util.List;
import java.util.Map;
import java.util.Random;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap; 

public class GestorePrenotazioni{
        public static void main(String[] args) {

        }
    }

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
    private int settimana;
    private List<Spettacolo> storicoSettimanale;

    private StoricoSettimanale(int settimana) {
        this.settimana = settimana;
        this.storicoSettimanale = new ArrayList<>();
    }

    public void stampaSpettacoli() {
        System.out.println("Spettacoli della settimana " + settimana + ":\n");
        for (Spettacolo spettacolo : storicoSettimanale) {
            spettacolo.stampaDettagli();
        }
    }
    public Spettacolo getSpettacolo(String codiceSpettacolo) {
        for (Spettacolo spettacolo : storicoSettimanale) {
            if (spettacolo.getCodiceSpettacolo().equals(codiceSpettacolo)) {
                return spettacolo;
            }
        }
        return null;
    }
    private void creaStorico(int settimana) {
        if (settimana < 1 || settimana > 52) {
            System.out.println("\nNumero settimana non valido. Deve essere compreso tra 1 e 52.\n");
            return;
        }
        if (!esisteStorico(settimana)) {
            StoricoSettimanale nuovoStorico = new StoricoSettimanale(settimana);
            StoricoAnnuo.getInstance().getStoricoAnnuo().add(nuovoStorico);
            System.out.println("\nCreato nuovo storico settimanale e aggiunto allo storico annuo\n");
        }
    }

    public boolean esisteStorico(int settimana) {
        for (StoricoSettimanale storico : StoricoAnnuo.getInstance().getStoricoAnnuo()) {
            if (storico.settimana == settimana) {
                return true;
            }
        }
        return false;
    }

    public void addSpettacolo(int settimana, Spettacolo spettacolo) {
        if (!esisteStorico(settimana)) {
            creaStorico(settimana);
        }
        StoricoAnnuo.getInstance().getStoricoAnnuo().get(settimana).storicoSettimanale.add(spettacolo);
    }

    public void rimuoviSpettacolo(int settimana, Spettacolo spettacolo) {
        if (!esisteStorico(settimana)) {
            System.out.println("\nSettimana non esistente\n");
        } else {
            StoricoAnnuo.getInstance().getStoricoAnnuo().get(settimana).storicoSettimanale.remove(spettacolo);
        }
    }

    public int getSettimana() {
        return settimana;
    }

    public List<Spettacolo> getStoricoSettimanale() {
        return storicoSettimanale;
    }
}

class Spettacolo {
    private String titolo, codiceSpettacolo, tipo;
    private LocalDateTime datetime;
    private int numeroSala;

    protected Spettacolo(String titolo, String codiceSpettacolo, String tipo, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        this.titolo = titolo;
        this.codiceSpettacolo = codiceSpettacolo;
        this.tipo = tipo;
        this.numeroSala = sala.getNumeroSala();
        this.datetime = LocalDateTime.of(anno, mese, giorno, ore, minuti);
    }

    public static Spettacolo creaSpettacolo(String titolo, String codiceSpettacolo, String tipo, String dettaglio, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        switch (tipo.toLowerCase()) {
            case "film":
                return new Film(titolo, codiceSpettacolo, tipo, dettaglio, sala, anno, mese, giorno, ore, minuti);
            case "concerto":
                return new Concerto(titolo, codiceSpettacolo, tipo, dettaglio, sala, anno, mese, giorno, ore, minuti);
            case "opera":
                return new Opera(titolo, codiceSpettacolo, tipo, dettaglio, sala, anno, mese, giorno, ore, minuti);
            default:
                throw new IllegalArgumentException("Tipo di spettacolo non valido");
        }
    }
    public void stampaDettagli() {
        System.out.println(this.tipo);
        System.out.println("Titolo: " + this.titolo);
        System.out.println("Codice Spettacolo: " + this.codiceSpettacolo);
        System.out.println("Numero Sala: " + this.numeroSala);
        System.out.println("Data e Orario: " + this.datetime);
    }

    public String getTitolo() {
        return titolo;
    }

    public String getCodiceSpettacolo() {
        return codiceSpettacolo;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public int getNumeroSala() {
        return numeroSala;
    }
    
}
class Film extends Spettacolo {
    private String genere;

    public Film(String titolo, String codiceSpettacolo,String tipo, String genere, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        super(titolo, codiceSpettacolo,tipo, sala, anno, mese, giorno, ore, minuti);
        this.genere = genere;
    }
    public String getGenere(){
        return genere;
    }

}

class Concerto extends Spettacolo {
    private String artista;

    public Concerto(String titolo, String codiceSpettacolo,String tipo, String artista, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        super(titolo, codiceSpettacolo,tipo,sala, anno, mese, giorno, ore, minuti);
        this.artista = artista;
    }
    public String getArtista(){
        return artista;
    }

}

class Opera extends Spettacolo {
    private String compositore;

    public Opera(String titolo, String codiceSpettacolo, String tipo, String compositore, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        super(titolo, codiceSpettacolo, tipo, sala, anno, mese, giorno, ore, minuti);
        this.compositore = compositore;
    }
    public String getCompositore(){
        return compositore;
    }
}

class GestoreSale {
    private static GestoreSale instance;
    private List<Sala> saleTotali;
    private boolean saleInizializzate = false;

    private GestoreSale() {
        saleTotali = new ArrayList<>();
        inizializzaSale();
        
    }

    public static GestoreSale getInstance() {
        if (instance == null) {
            instance = new GestoreSale();
        }
        return instance;
    }

    private boolean controllaDisponibilita(int numeroSala, int slot) {
        Sala sala = getSala(numeroSala);
        return sala != null && sala.getSlot().containsKey(slot) && sala.getSlot().get(slot);
    }

    public void prenotaSala(int numeroSala, int slot) {
        if (!controllaDisponibilita(numeroSala, slot)) {
            System.out.println("Sala non prenotabile");
        } else {
            Sala sala = getSala(numeroSala);
            if (sala != null && sala.getSlot().containsKey(slot)) {
                sala.getSlot().put(slot, true);
                System.out.println("Sala " + numeroSala + ", slot " + slot + " prenotato con successo.");
            } else {
                System.out.println("Errore nella prenotazione della sala.");
            }
        }
    }
    private void inizializzaSale() {
        if(saleInizializzate == false){
            for (int numeroSala = 1; numeroSala <= 6; numeroSala++) {
                Sala sala = new Sala(numeroSala);
                saleTotali.add(sala);
                saleInizializzate = true;
            }
        }
    }

    public void liberaSala(int numeroSala, int slot) {
        Sala sala = getSala(numeroSala);

        if (sala != null && sala.getSlot().containsKey(slot)) {
            sala.getSlot().put(slot, false);
            System.out.println("Sala " + numeroSala + ", slot " + slot + " liberato con successo.");
        } else {
            System.out.println("Errore nella liberazione della sala o dello slot.");
        }
    }

    public Sala getSala(int numeroSala) {
        for (Sala sala : saleTotali) {
            if (sala.getNumeroSala() == numeroSala) {
                return sala;
            }
        }
        return null;
    }
    

}

class Sala {
    private int numeroSala;
    private Map<Integer, Boolean> slot;
    private List<Posto> listaPosti;

    protected Sala(int numeroSala) {

        this.numeroSala = numeroSala;
        this.listaPosti = creaPosti();
        this.slot = inizializzaSlotDisponibili();
    }
  
    private Map<Integer, Boolean> inizializzaSlotDisponibili() {
        Map<Integer, Boolean> slotDisponibili = new HashMap<>();
        for (int i = 1; i <= 4; i++) {
            slotDisponibili.put(i, false);
        }
        return slotDisponibili;
    }

    private List<Posto> creaPosti() {
        List<Posto> posti = new ArrayList<>();
        for (char fila = 'A'; fila <= 'G'; fila++) {
            for (int numeroPosto = 1; numeroPosto <= 15; numeroPosto++) {
                posti.add(new Posto(fila, numeroPosto));
            }
        }
        return posti;
    }

    public int getNumeroSala() {
        return numeroSala;
    }

    public Map<Integer, Boolean> getSlot() {
        return slot;
    }

    public List<Posto> getListaPosti() {
        return listaPosti;
    }
}

class Posto {
    private char fila;
    private int numeroPosto;
    private boolean prenotato;

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
    public int getNumeroPosto(){
        return numeroPosto;
    }
    public char getFila(){
        return fila;
    }

}

class Prenotazione {
    private String idPrenotazione;
    private LocalTime datetime;

    Prenotazione(String idPrenotazione) {
        this.idPrenotazione = idPrenotazione;
        datetime = LocalTime.now();
    }

    public String getIdPrenotazione() {
        return idPrenotazione;
    }

    public LocalTime getDatetime() {
        return datetime;
    }
}

class Gestore{
    private static Gestore instance;
    private int numeroPrenotazioni;
    private List<Prenotazione> prenotazioni;

    private Gestore() {
        numeroPrenotazioni = 0;
        prenotazioni = new ArrayList<>();
    }

    public static Gestore getInstance() {
        if (instance == null) {
            instance = new Gestore();
        }
        return instance;
    }

    private static String generaNumeroPrenotazione() {
        Random random = new Random();
        int numeroCasuale = 100000 + random.nextInt(900000);
        return "P" + numeroCasuale;
    }

    public void effettuaPrenotazione(Spettacolo spettacolo, Posto posto) {
        if (!posto.isPrenotato()) {
            Prenotazione prenotazione = new Prenotazione(generaNumeroPrenotazione());
            prenotazioni.add(prenotazione);
            numeroPrenotazioni++;
            posto.setPrenotato(true);
            Biglietto biglietto = Biglietto.creaBiglietto(spettacolo, posto);
            biglietto.stampaBiglietto(prenotazione.getDatetime(), prenotazione.getIdPrenotazione());
            System.out.println("Prenotazione effettuata con successo.");
        } else {
            System.out.println("Il posto è già prenotato. Scegli un altro posto.");
        }
    }

    public int getNumeroPrenotazioni() {
        return numeroPrenotazioni;
    }
    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

}

class Biglietto {
    private Spettacolo spettacolo;
    private Posto posto;

    private Biglietto(Spettacolo spettacolo, Posto posto) {
        this.spettacolo = spettacolo;
        this.posto = posto;
    }

    public static Biglietto creaBiglietto(Spettacolo spettacolo, Posto posto) {
        return new Biglietto(spettacolo, posto);
    }

    public void stampaBiglietto(LocalTime ora, String numeroPrenotazione) {
        System.out.println("Ora prenotazione: " + ora + "\nNumero Prenotazione: " + numeroPrenotazione +
                "\nNumero posto: " + posto.getNumeroPosto()+posto.getFila() + "\nSpettacolo: \n" + spettacolo.getTitolo() +
                "\n" + spettacolo.getCodiceSpettacolo() + "\n" + spettacolo.getDatetime());
    }
}
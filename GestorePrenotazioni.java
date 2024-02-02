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
            settimana.stampaSpettacoliSettimana();
        }
    }

}

class StoricoSettimanale {
    private int settimana;
    private Map<String, List<Spettacolo>> storicoSettimanale;

    protected StoricoSettimanale(int settimana) {
        this.settimana = settimana;
        this.storicoSettimanale = new HashMap<>();
        initializzaGiorni();
    }

    public boolean esisteStorico(int settimana) {
        return storicoSettimanale.containsKey(Integer.toString(settimana));
    }

    public void creaStorico(int settimana) {
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

    public StoricoSettimanale creaStoricoSettimanale(int settimana){
        StoricoSettimanale nuovoStorico = new StoricoSettimanale(settimana);
        return nuovoStorico;
        }

    private void initializzaGiorni() {
        storicoSettimanale.put("Lunedi", new ArrayList<>());
        storicoSettimanale.put("Martedi", new ArrayList<>());
        storicoSettimanale.put("Mercoledi", new ArrayList<>());
        storicoSettimanale.put("Giovedi", new ArrayList<>());
        storicoSettimanale.put("Venerdi", new ArrayList<>());
        storicoSettimanale.put("Sabato", new ArrayList<>());
        storicoSettimanale.put("Domenica", new ArrayList<>());
    }
    public void stampaSpettacoliSettimana() {
        System.out.println("Spettacoli della settimana " + settimana + ":\n");
        for (Map.Entry<String, List<Spettacolo>> entry : storicoSettimanale.entrySet()) {
            String giorno = entry.getKey();
            List<Spettacolo> spettacoliGiorno = entry.getValue();
    
            System.out.println("Giorno: " + giorno);
            for (Spettacolo spettacolo : spettacoliGiorno) {
                spettacolo.stampaDettagli();
            }
            System.out.println(); 
        }
    }
    public void stampaSpettacoliDelGiorno(String giorno) {
        if (storicoSettimanale.containsKey(giorno)) {
            System.out.println("Spettacoli del giorno " + giorno + " della settimana " + settimana + ":\n");
            List<Spettacolo> spettacoliGiorno = storicoSettimanale.get(giorno);
    
            for (Spettacolo spettacolo : spettacoliGiorno) {
                spettacolo.stampaDettagli();
            }
        } else {
            System.out.println("Nessuno spettacolo per il giorno " + giorno + " della settimana " + settimana + ".\n");
        }
    }
    public Spettacolo getSpettacolo(String codiceSpettacolo) {
        for (List<Spettacolo> spettacoliGiorno : storicoSettimanale.values()) {
            for (Spettacolo spettacolo : spettacoliGiorno) {
                if (spettacolo.getCodiceSpettacolo().equals(codiceSpettacolo)) {
                    return spettacolo;
                }
            }
        }
        return null;
    }

    public void addSpettacolo(int settimana, String giorno, Spettacolo spettacolo) {
        if (sonoParametriValidi(settimana, giorno, spettacolo)) {
            StoricoSettimanale storicoSettimanale = getStoricoSettimanale(settimana);
            storicoSettimanale.storicoSettimanale.get(giorno).add(spettacolo);
            System.out.println("Spettacolo aggiunto con successo!");
        } else {
            System.out.println("Settimana non valida: " + settimana);
        }
    }

    public void rimuoviSpettacolo(int settimana, String giorno, Spettacolo spettacolo) {
        if (sonoParametriValidi(settimana, giorno, spettacolo)) {
            StoricoSettimanale storicoSettimanale = getStoricoSettimanale(settimana);
            List<Spettacolo> spettacoliGiorno = storicoSettimanale.storicoSettimanale.get(giorno);
            if(spettacoliGiorno.contains(spettacolo)){
                spettacoliGiorno.remove(spettacolo);
                System.out.println("Spettacolo rimosso con successo!");
            }
            else System.out.println("Il giorno selezionato non contiene lo spettacolo immesso");
        } else {
            System.out.println("Paramateri non validi");
        }
    }
    private boolean sonoParametriValidi(int settimana, String giorno, Spettacolo spettacolo) {
        if (settimana < 1 || settimana > 52) {
            System.out.println("\nNumero settimana non valido. Deve essere compreso tra 1 e 52.\n");
            return false;
        }
        List<String> giorniValidi = List.of("Lunedi", "Martedi", "Mercoledi", "Giovedi", "Venerdi", "Sabato", "Domenica");
        if (!giorniValidi.contains(giorno)) {
            System.out.println("Giorno non valido: " + giorno);
            return false;
        }
        if (spettacolo == null) {
            System.out.println("Lo spettacolo non può essere nullo.");
            return false;
        }
        return true;
    }

    public int getSettimana() {
        return settimana;
    }

    private StoricoSettimanale getStoricoSettimanale(int settimana) {
        for (StoricoSettimanale storico : StoricoAnnuo.getInstance().getStoricoAnnuo()) {
            if (storico.getSettimana() == settimana) {
                return storico;
            }
        }
        return null;
    }
}

class Spettacolo {
    private String titolo, codiceSpettacolo, tipo;
    private LocalDateTime datetime;
    private int numeroSala;

    protected Spettacolo(String titolo, String codiceSpettacolo, String tipo, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        if (titolo == null || titolo.isEmpty() || codiceSpettacolo == null || codiceSpettacolo.isEmpty() || tipo == null || tipo.isEmpty() || sala == null) {
            throw new IllegalArgumentException("Parametri non validi per la creazione di uno spettacolo.");
        }
    
        if (anno < 0 || mese < 1 || mese > 12 || giorno < 1 || giorno > 31 || ore < 0 || ore > 23 || minuti < 0 || minuti > 59) {
            throw new IllegalArgumentException("Valori di data e ora non validi.");
        }
        
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
            System.out.println("La sala " + numeroSala + " allo slot " + slot + " non è disponibile per la prenotazione.");
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
        if(saleTotali.isEmpty()){
            for (int numeroSala = 1; numeroSala <= 6; numeroSala++) {
                Sala sala = new Sala(numeroSala);
                saleTotali.add(sala);
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
        if (numeroSala <= 0) {
            throw new IllegalArgumentException("Il numero della sala deve essere maggiore di zero.");
        }    
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
    public Posto getPrenotabile() {
        for (Posto posto : listaPosti) {
            if (!posto.isPrenotato()) {
                return posto;
            }
        }
        System.out.println("Nessun posto disponibile");
        return null; 
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
    public boolean prenota() {
        if (!prenotato) {
            prenotato = true;
            System.out.println("Prenotazione avvenuta con successo");
            return true; 
        } else {
            System.out.println("Il posto è già prenotato");
            return false;
        }
    }

    public boolean isPrenotato() {
        return prenotato;
    }
    public void libera() {
        prenotato = false;
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



class Prenotazioni{
    private static Prenotazioni instance;
    private int numeroPrenotazioni;
    private Map<String, Biglietto> prenotazioni;
   

    private Prenotazioni() {
        numeroPrenotazioni = 0;
        prenotazioni = new HashMap<>();
        
    }

    public static Prenotazioni getInstance() {
        if (instance == null) {
            instance = new Prenotazioni();
        }
        return instance;
    }

    private String generaNumeroPrenotazione() {
        Random random = new Random();
        String numeroPrenotazione;
    
        do {
            int numeroCasuale = 100000 + random.nextInt(900000);
            numeroPrenotazione = "P" + numeroCasuale;
        } while (prenotazioni.containsKey(numeroPrenotazione));
    
        return numeroPrenotazione;
    }

    public void effettuaPrenotazione(Spettacolo spettacolo, Posto posto) {
        if (!posto.isPrenotato()) {
            String numeroPrenotazione = generaNumeroPrenotazione();
            Biglietto biglietto = Biglietto.creaBiglietto(spettacolo, posto,numeroPrenotazione);
            numeroPrenotazioni++;
            posto.setPrenotato(true);
            prenotazioni.put(numeroPrenotazione, biglietto);
            System.out.println("Prenotazione effettuata con successo con codice: "+ numeroPrenotazione);
        } else {
            System.out.println("Il posto è già prenotato. Scegli un altro posto.");
        }
    }

    public int getNumeroPrenotazioni() {
        return numeroPrenotazioni;
    }
    public Map<String, Biglietto> getPrenotazioni() {
        return prenotazioni;
    }
    public Biglietto getBiglietto(String idPrenotazione) {
        Biglietto biglietto = prenotazioni.get(idPrenotazione);
        if (biglietto != null) {
            System.out.println("Biglietto associato alla prenotazione " + idPrenotazione + ":");
            System.out.println(biglietto.toString());
        } else {
            System.out.println("Nessun biglietto trovato per la prenotazione con ID: " + idPrenotazione);
        }
        return biglietto;
    }
}

class Biglietto {
    private Spettacolo spettacolo;
    private Posto posto;
    private String numeroPrenotazione;
    private LocalDateTime datetime;
    

    private Biglietto(Spettacolo spettacolo, Posto posto,String numeroPrenotazione) {
        this.spettacolo = spettacolo;
        this.posto = posto;
        this.numeroPrenotazione = numeroPrenotazione;
        this.datetime = LocalDateTime.now();
    }

    public static Biglietto creaBiglietto(Spettacolo spettacolo, Posto posto, String numeroPrenotazione) {
        return new Biglietto(spettacolo, posto, numeroPrenotazione);
    }

    public void stampaBiglietto() {
        System.out.println("Data e ora prenotazione: " + this.datetime.toString() + "\nNumero Prenotazione: " + numeroPrenotazione +
                "\nNumero posto: " + posto.getNumeroPosto()+posto.getFila() + "\nSpettacolo: \n" + spettacolo.getTitolo() +
                "\n" + spettacolo.getCodiceSpettacolo() + "\n" + spettacolo.getDatetime().toString());
    }
    @Override
    public String toString() {
        return "Data e ora prenotazione: " + this.datetime.toString() + "\nNumero Prenotazione: " + numeroPrenotazione +
        "\nNumero posto: " + posto.getNumeroPosto()+posto.getFila() + "\nSpettacolo: \n" + spettacolo.getTitolo() +
        "\n" + spettacolo.getCodiceSpettacolo() + "\n" + spettacolo.getDatetime().toString();
    }
}
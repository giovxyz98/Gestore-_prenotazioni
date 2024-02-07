import java.util.List;
import java.util.Map;
import java.util.Random;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap; 
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.LinkedHashMap;


public class GestorePrenotazioni{
        public static void main(String[] args) {

            GestoreSale gestoreSale = GestoreSale.getInstance();
            Storico storico = Storico.getInstance();
            storico.creaStoricoAnnuo(storico.getAnnoCorrente());
            Prenotazioni prenotazioni = Prenotazioni.getInstance();
   
            Sala sala1 = gestoreSale.creaSala(1);
            Sala sala2 = gestoreSale.creaSala(2);
            Sala sala3 = gestoreSale.creaSala(3);
            System.out.println();
            Spettacolo interstellar = storico.aggiungiNuovoSpettacolo("Interstellar", "film", "Fantascienza", sala1, 2024, 2, 14, 18, 30);
            Spettacolo titanic = storico.aggiungiNuovoSpettacolo("Titanic", "film", "Azione", sala2, 2024, 2, 15, 20, 0);
            Spettacolo il_padrino = storico.aggiungiNuovoSpettacolo("Il padrino", "film", "Musical", sala3, 2024, 2, 16, 15, 0);
            System.out.println();
            prenotazioni.effettuaPrenotazione(il_padrino, gestoreSale.getSala(il_padrino.getNumeroSala()).getPrenotabile());
            prenotazioni.effettuaPrenotazione(titanic, gestoreSale.getSala(titanic.getNumeroSala()).getPrenotabile());
            prenotazioni.effettuaPrenotazione(interstellar, gestoreSale.getSala(interstellar.getNumeroSala()).getPrenotabile());
            System.out.println();
            
            //Stampiamo tutti gli spettacoli relativi alla settimana da noi indicata
            storico.getStoricoAnnuo(storico.getAnnoCorrente()).getStoricoAnnuo().get(LocalDate.of(2024, 2, 15).get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())).stampaSpettacoliSettimana();
            System.out.println("Prenotazioni effettuate:");
            for (Biglietto biglietto : prenotazioni.getPrenotazioni().values()) {
                System.out.println("\n"+biglietto.toString()+"\n");
                
            }
        }
    }

class Storico{
    private static Storico instance;
    private Map<Integer, StoricoAnnuo> storico;
    private int annoCorrente;
    public static List<String> listaIDSpettacoli; 
    
    private Storico(){
        storico = new HashMap<>();
        listaIDSpettacoli = new ArrayList<>();
        annoCorrente = LocalDate.now().getYear();
    }

    public static synchronized Storico getInstance() {
        if (instance == null) {
            instance = new Storico();
        }
        return instance;
    }

    public StoricoAnnuo getStoricoAnnuo(int anno){
        return storico.get(anno);
    }

    public void setAnno(int anno){
        annoCorrente = anno;
    }

    public int getAnnoCorrente(){
        return annoCorrente;
    }

    public void creaStoricoAnnuo(int nuovoAnno){
        if (!storico.containsKey(nuovoAnno)) {
            StoricoAnnuo nuovoStoricoAnnuo = new StoricoAnnuo();
            storico.put(nuovoAnno, nuovoStoricoAnnuo);
        }        
    }

    public StoricoSettimana creaStoricoSettimanale(int nuovaSettimana){
        if (nuovaSettimana < 1 || nuovaSettimana > 52) {
            System.out.println("\nNumero settimana non valido. Deve essere compreso tra 1 e 52.\n");
            return null;
        }
        
        StoricoAnnuo storicoAnnuo = getStoricoAnnuo(getAnnoCorrente());
        if (storicoAnnuo == null) {
            creaStoricoAnnuo(getAnnoCorrente());
            storicoAnnuo = getStoricoAnnuo(getAnnoCorrente());
        }
        Map<Integer, StoricoSettimana> storicoSettimanaleMap = storicoAnnuo.getStoricoAnnuo();
        if (!storicoSettimanaleMap.containsKey(nuovaSettimana)) {
            StoricoSettimana nuovoStorico = new StoricoSettimana(nuovaSettimana);
            storicoSettimanaleMap.put(nuovaSettimana, nuovoStorico);
            System.out.println("\nCreato nuovo storico settimanale e aggiunto allo storico annuo\n");
            return nuovoStorico;
        }
    
        return storicoSettimanaleMap.get(nuovaSettimana);
    }

    public StoricoSettimana getStoricoSettimanale(int anno, int settimana){
        StoricoAnnuo storicoAnnuo = getStoricoAnnuo(anno);

        if (storicoAnnuo == null) {
            System.out.println("Lo storico annuo per l'anno " + anno + " non esiste.");
            return null;
        }
    
        Map<Integer, StoricoSettimana> storicoAnnuoMap = storicoAnnuo.getStoricoAnnuo();
    
        if (!storicoAnnuoMap.containsKey(settimana)) {
            StoricoSettimana storicoSettimana = creaStoricoSettimanale(settimana);
            return storicoSettimana;
        } else {
            return storicoAnnuoMap.get(settimana);
        }
    }

    public Spettacolo getSpettacolo(String codiceSpettacolo){
        Map<Integer, StoricoSettimana> storicoAnnuo = getStoricoAnnuo(getAnnoCorrente()).getStoricoAnnuo();
        for(StoricoSettimana storicoSettimana : storicoAnnuo.values()){
            for(List<Spettacolo> giorno : storicoSettimana.getLista().values()){
                for(Spettacolo spettacolo : giorno){
                    if(spettacolo.getCodiceSpettacolo().equals(codiceSpettacolo)){
                        return spettacolo;
                    }
                }
            }
        }
        System.out.println("Spettacolo non trovato nel sistema: " + codiceSpettacolo);
        return null;
    }
    public Spettacolo aggiungiNuovoSpettacolo(String titolo, String tipo, String dettaglio, Sala sala, int anno, int mese, int giorno, int ore, int minuti){
        int settimana = LocalDate.of(anno, mese,giorno).get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
        DayOfWeek giornoSettimana = LocalDate.of(anno, mese, giorno).getDayOfWeek();
        String giornoDellaSettimana = giornoSettimana.getDisplayName(TextStyle.SHORT, Locale.ITALIAN);
        StoricoSettimana storicoSettimanale = getStoricoSettimanale(anno, settimana);
        Spettacolo spettacolo = Spettacolo.creaSpettacolo(titolo, tipo, dettaglio, sala, anno, mese, giorno, ore, minuti);
        storicoSettimanale.getLista().get(giornoDellaSettimana).add(Spettacolo.creaSpettacolo(titolo, tipo, dettaglio, sala, anno, mese, giorno, ore, minuti));
        listaIDSpettacoli.add(spettacolo.getCodiceSpettacolo());
        System.out.println("Aggiunto nuovo spettacolo con codice: "+ spettacolo.getCodiceSpettacolo());
        return spettacolo;
    }
}


class StoricoAnnuo {
    private Map<Integer, StoricoSettimana> storicoAnnuo;
    
    protected StoricoAnnuo() {
        storicoAnnuo = new HashMap<>();
    }
    
    
    public Map<Integer, StoricoSettimana> getStoricoAnnuo() {
        return storicoAnnuo;
    }

}


class StoricoSettimana {
    private int settimana;
    private Map<String, List<Spettacolo>> storicoSettimanale;
    private final Map<String, String> giorniCompleti = new HashMap<String, String>() {{
        put("lun", "Lunedì");
        put("mar", "Martedì");
        put("mer", "Mercoledì");
        put("gio", "Giovedì");
        put("ven", "Venerdì");
        put("sab", "Sabato");
        put("dom", "Domenica");
    }};

    
    public Map<String, List<Spettacolo>> getLista(){
        return this.storicoSettimanale;
    }

    protected StoricoSettimana(int settimana) {
        this.settimana = settimana;
        this.storicoSettimanale = new LinkedHashMap<>();
        initializzaGiorni();
    }

    private void initializzaGiorni() {
        storicoSettimanale.put("lun", new ArrayList<>());
        storicoSettimanale.put("mar", new ArrayList<>());
        storicoSettimanale.put("mer", new ArrayList<>());
        storicoSettimanale.put("gio", new ArrayList<>());
        storicoSettimanale.put("ven", new ArrayList<>());
        storicoSettimanale.put("sab", new ArrayList<>());
        storicoSettimanale.put("dom", new ArrayList<>());
    }
    
    
    public void stampaSpettacoliSettimana() {
        System.out.println("___________________________________________________");
        System.out.println("|Spettacoli della settimana " + settimana + ":\n");
        for (Map.Entry<String, List<Spettacolo>> entry : storicoSettimanale.entrySet()) {
            String giorno = entry.getKey();
            List<Spettacolo> spettacoliGiorno = entry.getValue();
            System.out.println("Giorno: " + giorniCompleti.get(giorno));
            if(spettacoliGiorno.isEmpty()){System.out.println("     Non sono presenti spettacoli");}
            for (Spettacolo spettacolo : spettacoliGiorno) {
                spettacolo.stampaDettagli();
                System.out.println();
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
    
    public void addSpettacolo(int settimana, String giorno, Spettacolo spettacolo) {
        if (sonoParametriValidi(settimana, giorno, spettacolo)) {
            Storico storico = Storico.getInstance();
            StoricoSettimana storicoSettimanale = storico.getStoricoSettimanale(storico.getAnnoCorrente(),settimana);
            storicoSettimanale.storicoSettimanale.get(giorno).add(spettacolo);
            System.out.println("Spettacolo aggiunto con successo!");
        } else {
            System.out.println("Settimana non valida: " + settimana);
        }
    }

    public List<Spettacolo> getSpettacoliGiorno(String giorno){
        return storicoSettimanale.get(giorno.toLowerCase());
    }

    public void rimuoviSpettacolo(int settimana, String giorno, Spettacolo spettacolo) {
        if (sonoParametriValidi(settimana, giorno, spettacolo)) {
            Storico storico = Storico.getInstance();
            StoricoSettimana storicoSettimanale = storico.getStoricoSettimanale(storico.getAnnoCorrente(),settimana);
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
        List<String> giorniValidi = List.of("lun", "mar", "mer", "gio", "ven", "sab", "dom");
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
    

}


class Spettacolo {
    private String titolo, codiceSpettacolo, tipo;
    private LocalDateTime datetime;
    private int numeroSala;

    protected Spettacolo(String titolo,  String tipo, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        if (titolo == null || titolo.isEmpty()  || tipo == null || tipo.isEmpty() || sala == null) {
            throw new IllegalArgumentException("Parametri non validi per la creazione di uno spettacolo.");
        }
    
        if (anno < 0 || mese < 1 || mese > 12 || giorno < 1 || giorno > 31 || ore < 0 || ore > 23 || minuti < 0 || minuti > 59) {
            throw new IllegalArgumentException("Valori di data e ora non validi.");
        }
        
        this.titolo = titolo;
        this.codiceSpettacolo = generaNumeroSpettacolo();
        this.tipo = tipo;
        this.numeroSala = sala.getNumeroSala();
        this.datetime = LocalDateTime.of(anno, mese, giorno, ore, minuti);
    }

    public static Spettacolo creaSpettacolo(String titolo, String tipo, String dettaglio, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        switch (tipo.toLowerCase()) {
            case "film":
                return new Film(titolo, tipo, dettaglio, sala, anno, mese, giorno, ore, minuti);
            case "concerto":
                return new Concerto(titolo, tipo, dettaglio, sala, anno, mese, giorno, ore, minuti);
            case "opera":
                return new Opera(titolo, tipo, dettaglio, sala, anno, mese, giorno, ore, minuti);
            default:
                throw new IllegalArgumentException("Tipo di spettacolo non valido");
        }
    }

    private String generaNumeroSpettacolo() {
        Random random = new Random();
        String numeroSpettacolo;
    
        do {
            int numeroCasuale = 100000 + random.nextInt(900000);
            numeroSpettacolo = "S" + numeroCasuale;
        } while (Storico.listaIDSpettacoli.contains(numeroSpettacolo));
    
        return numeroSpettacolo;
    }
    
    public String getTipo(){
        return this.tipo;
    }
    @Override
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDatetime = this.datetime.format(formatter);
        return "    ___________________________________________________\n" +
                "   | Tipo: " + this.tipo + "\n" +
                "   | Titolo: " + this.titolo + "\n" +
                "   | Codice Spettacolo: " + this.codiceSpettacolo + "\n" +
                "   | Numero Sala: " + Integer.toString(this.numeroSala) + "\n" +
                "   | Data e ora dello spettacolo:  " + formattedDatetime + "\n" +
                "   |__________________________________________________";
    }
   
    public void stampaDettagli() {
        System.out.println(this.toString());
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

    public Film(String titolo, String tipo, String genere, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        super(titolo, tipo, sala, anno, mese, giorno, ore, minuti);
        this.genere = genere;
    }
    public String getGenere(){
        return genere;
    }

}

class Concerto extends Spettacolo {
    private String artista;

    public Concerto(String titolo, String tipo, String artista, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        super(titolo, tipo,sala, anno, mese, giorno, ore, minuti);
        this.artista = artista;
    }
    public String getArtista(){
        return artista;
    }

}

class Opera extends Spettacolo {
    private String compositore;

    public Opera(String titolo, String tipo, String compositore, Sala sala, int anno, int mese, int giorno, int ore, int minuti) {
        super(titolo, tipo, sala, anno, mese, giorno, ore, minuti);
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
    public Sala creaSala(int numeroSala){
        Sala sala = new Sala(numeroSala);
        saleTotali.add(sala);
        return sala;

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
        System.out.println( this.toString());
    }
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDatetime = this.datetime.format(formatter);
        String postoFila = posto.getNumeroPosto() + "" + posto.getFila();
        return "___________________________________________________\n" +
        "| Biglietto                                        |\n|‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾|\n" +
        "| Tipo: " + String.format("%-43s", spettacolo.getTipo()) + "|\n" +
        "| Spettacolo: " + String.format("%-37s", spettacolo.getTitolo()) + "|\n" +
        "| Codice Spettacolo: " + String.format("%-30s", spettacolo.getCodiceSpettacolo()) + "|\n" +
        "| Data e ora prenotazione: " + String.format("%-24s", formattedDatetime) + "|\n" +
        "| Numero Prenotazione: " + String.format("%-28s", numeroPrenotazione) + "|\n" +
        "| Sala " + String.format("%-11s", spettacolo.getNumeroSala()) + "                                 |\n" +
        "| Numero posto: " + String.format("%-35s",postoFila)+ "|\n" +
        "| Data e ora dello spettacolo:  " + String.format("%-10s", spettacolo.getDatetime().format(formatter)) + "|\n" +
        " ‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾‾ ";
          

    }
}
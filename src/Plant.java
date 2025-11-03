// Třída Plant představuje jednu pokojovou rostlinu.
// Ukládá si základní informace: název, poznámku, datum zasazení,
// datum poslední zálivky a jak často se má zalévat.

import java.time.LocalDate;                 // pro práci s daty
import java.time.format.DateTimeFormatter;  // pro převod datumu na text

public class Plant implements Comparable<Plant> {

    // ====== Atributy (vlastnosti) ======
    private String name;             // název rostliny
    private String notes;            // poznámky o rostlině
    private LocalDate planted;       // kdy byla zasazena
    private LocalDate watering;      // kdy byla naposledy zalitá
    private int frequencyOfWatering; // jak často se zalévá (ve dnech)

    // Výchozí hodnota pro frekvenci (7 dnů)
    private static final int DEFAULT_FREQUENCY_DAYS = 7;

    // Pomocná proměnná pro formátování datumu do textu (např. 2025-11-03)
    private static final DateTimeFormatter DF = DateTimeFormatter.ISO_LOCAL_DATE;


    // ====== KONSTRUKTORY ======
    // Konstruktor = speciální metoda, která se spustí při vytvoření objektu (new Plant(...))

    // 1️⃣ Konstruktor, který nastaví všechno ručně
    public Plant(String name,
                 String notes,
                 LocalDate planted,
                 LocalDate watering,
                 int frequencyOfWatering) throws PlantException {

        // Používáme metody set..., abychom hned při vytvoření ověřili správnost hodnot
        setName(name);
        setNotes(notes);
        setPlanted(planted);
        setWatering(watering);
        setFrequencyOfWatering(frequencyOfWatering);
    }

    // 2️⃣ Konstruktor, který doplní chybějící hodnoty:
    // poznámka je prázdná, datum zasazení i zalití je dnešní den,
    // frekvenci si uživatel zadá sám.
    public Plant(String name, int frequencyOfWatering) throws PlantException {
        this(name, "", LocalDate.now(), LocalDate.now(), frequencyOfWatering);
    }

    // 3️⃣ Konstruktor, kde uživatel zadá jen název.
    // Ostatní se nastaví automaticky – zalévá se každých 7 dnů.
    public Plant(String name) throws PlantException {
        this(name, "", LocalDate.now(), LocalDate.now(), DEFAULT_FREQUENCY_DAYS);
    }


    // ====== PŘÍSTUPOVÉ METODY (gettery a settery) ======

    // Každý atribut má metodu pro čtení (get) a zápis (set)

    public String getName() {
        return name;
    }

    // Setter s kontrolou – název nesmí být prázdný
    public void setName(String name) throws PlantException {
        if (name == null || name.isBlank()) {
            throw new PlantException("Název rostliny nesmí být prázdný.");
        }
        this.name = name.trim(); // odstraní případné mezery okolo
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        // Pokud uživatel zadá null (žádný text), uložíme prázdný řetězec ""
        this.notes = (notes == null) ? "" : notes;
    }

    public LocalDate getPlanted() {
        return planted;
    }

    public void setPlanted(LocalDate planted) throws PlantException {
        if (planted == null) {
            throw new PlantException("Datum zasazení nesmí být prázdné (null).");
        }
        this.planted = planted;

        // Pokud už máme nastavené datum poslední zálivky, musí být stejné nebo novější než zasazení
        if (watering != null && watering.isBefore(this.planted)) {
            throw new PlantException("Datum zálivky nesmí být dříve než datum zasazení.");
        }
    }

    public LocalDate getWatering() {
        return watering;
    }

    public void setWatering(LocalDate watering) throws PlantException {
        if (watering == null) {
            throw new PlantException("Datum zálivky nesmí být prázdné (null).");
        }
        if (planted != null && watering.isBefore(planted)) {
            throw new PlantException("Datum zálivky nesmí být dříve než datum zasazení.");
        }
        this.watering = watering;
    }

    public int getFrequencyOfWatering() {
        return frequencyOfWatering;
    }

    public void setFrequencyOfWatering(int frequencyOfWatering) throws PlantException {
        if (frequencyOfWatering <= 0) {
            throw new PlantException("Frekvence zálivky musí být kladné číslo (1 nebo více).");
        }
        this.frequencyOfWatering = frequencyOfWatering;
    }


    // ====== DALŠÍ METODY ======

    // Vrátí text s informací o zálivce – pro přehledný výpis
    public String getWateringInfo() {
        LocalDate next = watering.plusDays(frequencyOfWatering);
        return "Rostlina: " + name
                + " | Naposledy zalita: " + DF.format(watering)
                + " | Další zálivka: " + DF.format(next);
    }

    // Nastaví datum poslední zálivky na dnešní den
    public void doWateringNow() throws PlantException {
        setWatering(LocalDate.now());
    }


    // ====== POROVNÁVÁNÍ A VÝPIS ======

    // Řazení podle názvu (A-Z)
    @Override
    public int compareTo(Plant other) {
        return this.name.compareToIgnoreCase(other.name);
    }

    // Jak se má rostlina vypsat, když ji dáme do System.out.println()
    @Override
    public String toString() {
        return getWateringInfo();
    }
}

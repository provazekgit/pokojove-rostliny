// Třída PlantManager se stará o seznam rostlin a práci se soubory.
// - přidání / získání / odebrání / kopie
// - kdo je k zalití dnes
// - řazení podle názvu (výchozí) a podle data zálivky
// - načtení a uložení jednoho seznamu ze/do souboru (TAB jako oddělovač)

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlantManager {

    // Vnitřní seznam, se kterým pracujeme
    private final List<Plant> plants = new ArrayList<>();

    // Pokud bys chtěl chybné řádky PŘESKAKOVAT a zbytek načíst,
    // přepni na true. Výchozí false = na první chybě skončí (zadání).
    private static final boolean SKIP_BAD_LINES = false;

    // ========= ZÁKLADNÍ OPERACE SE SEZNAMEM =========

    public void add(Plant plant) {
        if (plant != null) plants.add(plant);
    }

    public Plant get(int index) {
        return plants.get(index); // může vyhodit IndexOutOfBoundsException – OK
    }

    public void remove(int index) {
        plants.remove(index);
    }

    public List<Plant> getAllCopy() {
        return new ArrayList<>(plants); // ochrana proti nechtěným zásahům zvenku
    }

    public int size() { return plants.size(); }

    public void clear() { plants.clear(); }

    // ========= KDO JE K ZALITÍ DNES =========
    // Rostlina je k zalití, pokud (poslední_zálivka + frekvence) <= today
    public List<Plant> getPlantsToWater(LocalDate today) {
        List<Plant> result = new ArrayList<>();
        for (Plant p : plants) {
            LocalDate next = p.getWatering().plusDays(p.getFrequencyOfWatering());
            if (!next.isAfter(today)) { // je dnes nebo už prošel
                result.add(p);
            }
        }
        return result;
    }

    // ========= ŘAZENÍ =========

    // Výchozí řazení – podle názvu (Comparable v Plant)
    public void sortByName() {
        plants.sort(null);
    }

    // Podle data poslední zálivky (od nejstaršího po nejnovější)
    public void sortByLastWateringAsc() {
        plants.sort(Comparator.comparing(Plant::getWatering));
    }

    // ========= I/O: NAČTENÍ A ULOŽENÍ JEDNOHO SEZNAMU =========

    // Načte seznam ze souboru pomocí TAB oddělovače. Každý řádek má 5 sloupců:
    // name \t notes \t planted(YYYY-MM-DD) \t watering(YYYY-MM-DD) \t frequency(int)
    public void loadFromFile(String fileName) throws PlantException {
        clear(); // vždy začneme s prázdným seznamem
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {

            String line;
            int lineNo = 0;

            while ((line = br.readLine()) != null) {
                lineNo++;
                if (line.isBlank()) continue;

                String[] parts = line.split(Settings.DELIMITER, -1); // -1 = zachová prázdné sloupce
                if (parts.length != 5) {
                    if (SKIP_BAD_LINES) {
                        System.err.println("[VAROVÁNÍ] " + fileName + ":" + lineNo +
                                " – špatný počet sloupců, řádek přeskočen.");
                        continue;
                    } else {
                        throw new PlantException("Neplatný počet sloupců na řádku " + lineNo + " v souboru '" + fileName + "'");
                    }
                }

                try {
                    String name = parts[0];
                    String notes = parts[1];
                    LocalDate planted = LocalDate.parse(parts[2]);
                    LocalDate watering = LocalDate.parse(parts[3]);
                    int frequency = Integer.parseInt(parts[4]);

                    add(new Plant(name, notes, planted, watering, frequency));

                } catch (Exception e) {
                    if (SKIP_BAD_LINES) {
                        System.err.println("[VAROVÁNÍ] " + fileName + ":" + lineNo +
                                " – " + e.getMessage() + " (řádek přeskočen)");
                        // přeskoč jen tuto položku
                    } else {
                        throw new PlantException("Chyba při parsování řádku " + lineNo +
                                " v souboru '" + fileName + "': " + e.getMessage(), e);
                    }
                }
            }

        } catch (PlantException pe) {
            throw pe; // přeposlat dál do Main
        } catch (Exception io) {
            throw new PlantException("Chyba při čtení souboru '" + fileName + "': " + io.getMessage(), io);
        }
    }

    // Uloží aktuální seznam do souboru (TAB oddělovač, stejné pořadí sloupců)
    public void saveToFile(String fileName) throws PlantException {
        try (PrintWriter pw = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8)))) {

            for (Plant p : plants) {
                String line = String.join(Settings.DELIMITER,
                        safe(p.getName()),
                        safe(p.getNotes()),
                        p.getPlanted().toString(),   // LocalDate → "YYYY-MM-DD"
                        p.getWatering().toString(),
                        String.valueOf(p.getFrequencyOfWatering())
                );
                pw.println(line);
            }

        } catch (Exception io) {
            throw new PlantException("Chyba při zápisu do souboru '" + fileName + "': " + io.getMessage(), io);
        }
    }

    // Pomocná – nahradí null za prázdný text při ukládání
    private static String safe(String s) { return (s == null) ? "" : s; }
}

// Třída PlantManager se stará o seznam (kolekci) rostlin.
// Umožní nám: přidat/odebrat/získat rostlinu, vrátit kopii seznamu,
// najít rostliny k zalití, seřadit seznam, a také načíst/uložit seznam ze/do souboru.
//
// Používáme jednoduchý formát souboru s oddělovačem TAB ('\t'):
//   name \t notes \t planted(YYYY-MM-DD) \t watering(YYYY-MM-DD) \t frequency(int)
// Pozor: opravdu TAB, ne mezery.

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

    // Vnitřní seznam rostlin, se kterými pracujeme
    private final List<Plant> plants = new ArrayList<>();

    // Oddělovač do/ze souboru – tabulátor
    private static final String DELIMITER = "\t";

    // ========== ZÁKLADNÍ PRÁCE SE SEZNAMEM ==========

    // Přidá novou rostlinu na konec seznamu
    public void add(Plant plant) {
        if (plant == null) return;  // pro jistotu – nic nepřidávej, pokud je null
        plants.add(plant);
    }

    // Vrátí rostlinu na daném indexu (0 = první)
    public Plant get(int index) {
        return plants.get(index); // může vyhodit IndexOutOfBoundsException – to je OK (programátorská chyba)
    }

    // Odebere rostlinu na daném indexu
    public void remove(int index) {
        plants.remove(index);
    }

    // Vrátí KOPII seznamu (aby si volající „nerozbil“ náš vnitřní seznam)
    public List<Plant> getAllCopy() {
        return new ArrayList<>(plants);
    }

    // Vrátí počet rostlin v seznamu
    public int size() {
        return plants.size();
    }

    // Vymaže všechny rostliny ze seznamu
    public void clear() {
        plants.clear();
    }

    // ========== (ÚKOL 4) ROSTLINY K ZALITÍ ==========

    // Vrátí rostliny, které je třeba zalít k zadanému „dnešku“.
    // Logika: spočítáme datum „další zálivky“ = poslední zalití + frekvence.
    // Pokud už je ten den dnes nebo dokonce minul, je potřeba zalít (next <= today).
    public List<Plant> getPlantsToWater(LocalDate today) {
        List<Plant> result = new ArrayList<>();
        for (Plant p : plants) {
            LocalDate next = p.getWatering().plusDays(p.getFrequencyOfWatering());
            // isAfter ⇒ je později než today; !isAfter ⇒ je dnes nebo dříve
            if (!next.isAfter(today)) {
                result.add(p);
            }
        }
        return result;
    }

    // ========== (ÚKOL 5) ŘAZENÍ ==========

    // Výchozí řazení podle názvu už máme v Plant (implements Comparable<Plant>).
    // Tady jen zavoláme sort(null), což použije Comparable.
    public void sortByName() {
        plants.sort(null); // stejné jako Collections.sort(plants)
    }

    // Seřadí podle DATA POSLEDNÍ ZÁLIVKY (od nejstarší po nejnovější)
    public void sortByLastWateringAsc() {
        plants.sort(Comparator.comparing(Plant::getWatering));
    }

    // ========== (ÚKOL 6) NAČTENÍ ZE SOUBORU A ULOŽENÍ DO SOUBORU ==========

    // Načte JEDEN seznam rostlin z daného souboru.
    // Při chybě vyhodí PlantException s popisem (aplikace nemá spadnout).
    public void loadFromFile(String fileName) throws PlantException {
        clear(); // vždy začneme „načisto“

        // try-with-resources: čte soubor a po skončení ho automaticky zavře
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {

            String line;
            int lineNo = 0;

            while ((line = br.readLine()) != null) {
                lineNo++;

                // Přeskoč prázdné řádky (bez dat)
                if (line.isBlank()) continue;

                // Rozděl řádek podle TAB
                String[] parts = line.split(DELIMITER, -1); // -1 = zachovej i prázdné sloupce
                if (parts.length != 5) {
                    throw new PlantException("Neplatný počet sloupců na řádku " + lineNo + " (soubor: " + fileName + ")");
                }

                try {
                    // OČEKÁVANÝ POŘAD: name, notes, planted, watering, frequency
                    String name = parts[0];
                    String notes = parts[1];
                    LocalDate planted = LocalDate.parse(parts[2]);   // ve formátu YYYY-MM-DD
                    LocalDate watering = LocalDate.parse(parts[3]);
                    int frequency = Integer.parseInt(parts[4]);

                    // Vytvoř objekt Plant – v konstruktoru proběhne validace
                    Plant p = new Plant(name, notes, planted, watering, frequency);
                    add(p);

                } catch (Exception e) {
                    // Zabalíme chybu do PlantException s informací, kde přesně se stala
                    throw new PlantException(
                            "Chyba při parsování řádku " + lineNo + " v souboru '" + fileName + "': " + e.getMessage(),
                            e
                    );
                }
            }

        } catch (PlantException pe) {
            // Propustíme dál – volající (Main) si rozhodne, co s tím (vypsat chybu a pokračovat s prázdným seznamem)
            throw pe;
        } catch (Exception io) {
            // Obecná chyba čtení (soubor neexistuje, práva, …)
            throw new PlantException("Chyba při čtení souboru '" + fileName + "': " + io.getMessage(), io);
        }
    }

    // Uloží AKTUÁLNÍ seznam rostlin do souboru (každá rostlina = jeden řádek).
    public void saveToFile(String fileName) throws PlantException {
        // PrintWriter nám usnadní zápis řádků; OutputStreamWriter zajistí UTF-8
        try (PrintWriter pw = new PrintWriter(
                new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8)))) {

            for (Plant p : plants) {
                // Zapíšeme přesně v pořadí: name, notes, planted, watering, frequency
                String line = String.join(DELIMITER,
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

    // Malá pomocná metoda: kdyby byl nějaký text null, uložíme raději prázdný řetězec
    private static String safe(String s) {
        return (s == null) ? "" : s;
    }
}

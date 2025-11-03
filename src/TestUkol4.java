import java.time.LocalDate;
import java.util.List;

public class TestUkol4 {
    public static void main(String[] args) {
        System.out.println("=== TEST ÚKOLU 4/5/6 – správa seznamu, řazení, I/O ===");

        PlantManager manager = new PlantManager();

        // ---------- 1) add + size + get ----------
        try {
            Plant a = new Plant("Aloe", "Na slunci", LocalDate.now().minusDays(60), LocalDate.now().minusDays(10), 14);
            Plant m = new Plant("Monstera", "Velký list", LocalDate.now().minusDays(40), LocalDate.now().minusDays(8), 7);
            Plant f = new Plant("Ficus", "U okna", LocalDate.now().minusDays(20), LocalDate.now().minusDays(2), 5);
            Plant t = new Plant("Tulipán", "", LocalDate.now().minusDays(1), LocalDate.now().minusDays(1), 14);

            manager.add(a);
            manager.add(m);
            manager.add(f);
            manager.add(t);

            System.out.println("\n[1] Po přidání má seznam: " + manager.size() + " položky (očekávám 4)");
            System.out.println("   get(0) = " + manager.get(0).getName() + " (očekávám Aloe)");
        } catch (PlantException e) {
            System.err.println("[CHYBA] " + e.getMessage());
        }

        // ---------- 2) getAllCopy (nemění originál) ----------
        List<Plant> copy = manager.getAllCopy();
        copy.clear(); // vyčistíme KOPII
        System.out.println("\n[2] Originál po vyčištění kopie: " + manager.size() + " položek (očekávám stále 4)");

        // ---------- 3) getPlantsToWater ----------
        // K zalití dnes patří ti, u kterých (watering + frequency) <= today
        System.out.println("\n[3] K zalití dnes (" + LocalDate.now() + "):");
        List<Plant> toWater = manager.getPlantsToWater(LocalDate.now());
        if (toWater.isEmpty()) {
            System.out.println("   – nikdo");
        } else {
            for (Plant p : toWater) {
                System.out.println("   – " + p.getName() + " | naposledy: " + p.getWatering()
                        + " | frekvence: " + p.getFrequencyOfWatering());
            }
        }

        // ---------- 4) Řazení ----------
        manager.sortByName();
        System.out.println("\n[4] Seřazeno podle názvu (A–Z):");
        for (Plant p : manager.getAllCopy()) {
            System.out.println("   – " + p.getName());
        }

        manager.sortByLastWateringAsc();
        System.out.println("\n[4] Seřazeno podle data poslední zálivky (od nejstarší):");
        for (Plant p : manager.getAllCopy()) {
            System.out.println("   – " + p.getName() + " | " + p.getWatering());
        }

        // ---------- 5) remove ----------
        if (manager.size() >= 3) {
            Plant third = manager.get(2);
            manager.remove(2);
            System.out.println("\n[5] Odebrána položka na indexu 2: " + third.getName());
            System.out.println("   Nová velikost: " + manager.size());
        }

        // ---------- 6) save + load (I/O) ----------
        // Uložíme do vlastního testovacího souboru, ať nám nezaclání s hlavním Settings.OUTPUT_FILE
        String testOutput = "test-vystup.txt";
        try {
            manager.saveToFile(testOutput);
            System.out.println("\n[6] Uloženo do souboru: " + testOutput);

            PlantManager reload = new PlantManager();
            reload.loadFromFile(testOutput);
            System.out.println("   Znovu načteno položek: " + reload.size() + " (očekávám stejný počet jako před uložením)");

            System.out.println("   První položka po znovunačtení: " + (reload.size() > 0 ? reload.get(0).getName() : "-"));

        } catch (PlantException e) {
            System.err.println("[CHYBA I/O] " + e.getMessage());
        }

        System.out.println("\nHotovo ✅");
    }
}

import java.time.LocalDate;

public class Main {

    public static void main(String[] args) {
        // Vytvoříme správce seznamu rostlin
        PlantManager manager = new PlantManager();

        // ============= 1) Načtení ze souboru =============
        // Pokusíme se načíst rostliny z "kvetiny.txt".
        // Když soubor chybí nebo je špatný, vypíšeme chybu a pokračujeme s prázdným seznamem.
        try {
            manager.loadFromFile(Settings.INPUT_FILE);
            System.out.println("Načteno rostlin: " + manager.size());
        } catch (PlantException ex) {
            System.err.println("[CHYBA] " + ex.getMessage());
            System.err.println("Pokračuji s prázdným seznamem…");
            manager.clear();
        }

        // ============= 2) Výpis informací o zálivce =============
        // Vypíšeme pro každou rostlinu informaci: název, naposledy, další zálivka
        if (manager.size() > 0) {
            System.out.println("\nInformace o zálivce pro všechny rostliny:");
            for (Plant p : manager.getAllCopy()) {
                System.out.println(" - " + p.getWateringInfo());
            }
        } else {
            System.out.println("\nSeznam je prázdný.");
        }

        // ============= 3) Přidání jedné nové rostliny =============
        try {
            Plant ficus = new Plant(
                    "Ficus benjamina",                      // název
                    "Stojí u okna, nemá rád průvan",       // poznámka
                    LocalDate.now().minusDays(30),          // zasazen před 30 dny
                    LocalDate.now().minusDays(2),           // naposledy zalito před 2 dny
                    5                                       // zalévat každých 5 dnů
            );
            manager.add(ficus);
            System.out.println("\nPřidána rostlina: " + ficus.getWateringInfo());
        } catch (PlantException e) {
            System.err.println("[CHYBA] " + e.getMessage());
        }

        // ============= 4) Přidání 10 tulipánů =============
        // Názvy: "Tulipán na prodej 1" až "Tulipán na prodej 10"
        // Zasazeny i zality dnes, frekvence 14 dnů
        for (int i = 1; i <= 10; i++) {
            try {
                Plant t = new Plant("Tulipán na prodej " + i, "", LocalDate.now(), LocalDate.now(), 14);
                manager.add(t);
            } catch (PlantException e) {
                System.err.println("[CHYBA] " + e.getMessage());
            }
        }
        System.out.println("\nPo přidání tulipánů má seznam: " + manager.size() + " položek.");

        // ============= 5) Odebrání třetí položky (index 2) =============
        if (manager.size() >= 3) {
            Plant removed = manager.get(2);
            manager.remove(2);
            System.out.println("Odebrána položka na třetí pozici: " + removed.getName());
        } else {
            System.out.println("Nelze odebrat třetí položku – seznam má méně než 3 položky.");
        }

        // ============= 6) Uložení do souboru a znovunačtení =============
        try {
            manager.saveToFile(Settings.OUTPUT_FILE);
            System.out.println("\nUloženo do souboru: " + Settings.OUTPUT_FILE);

            // Ověření: načteme do nového správce
            PlantManager reload = new PlantManager();
            reload.loadFromFile(Settings.OUTPUT_FILE);
            System.out.println("Znovunačteno rostlin: " + reload.size());
        } catch (PlantException e) {
            System.err.println("[CHYBA] " + e.getMessage());
        }

        // ============= 7) Řazení a výpis =============
        // Výchozí řazení je podle názvu (Comparable v Plant)
        manager.sortByName();
        System.out.println("\nSeřazeno podle názvu:");
        for (Plant p : manager.getAllCopy()) {
            System.out.println(" - " + p.getName());
        }

        // Řazení podle data poslední zálivky (od nejstarší po nejnovější)
        manager.sortByLastWateringAsc();
        System.out.println("\nSeřazeno podle data poslední zálivky:");
        for (Plant p : manager.getAllCopy()) {
            System.out.println(" - " + p.getName() + " | poslední zálivka: " + p.getWatering());
        }

        // ============= 8) Které je potřeba zalít dnes =============
        System.out.println("\nK zalití dnes (" + LocalDate.now() + "):");
        for (Plant p : manager.getPlantsToWater(LocalDate.now())) {
            System.out.println(" - " + p.getName());
        }

        System.out.println("\nHotovo ✅");
    }
}

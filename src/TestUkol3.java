import java.time.LocalDate;

public class TestUkol3 {
    public static void main(String[] args) {
        System.out.println("=== TEST ÚKOLU 3 – validace a výjimky PlantException ===");

        // 1) Frekvence zálivky <= 0  → musí vyhodit PlantException
        try {
            System.out.println("\n[TEST 1] Frekvence = 0 (očekávám CHYBU)");
            Plant p = new Plant("Test 1", 0);
            System.out.println("NEČEKANÝ VÝSLEDEK: chyba se nevyhodila -> " + p);
        } catch (PlantException e) {
            System.out.println("OK – zachyceno: " + e.getMessage());
        }

        // 2) Zálivka dřív než zasazení  → musí vyhodit PlantException
        try {
            System.out.println("\n[TEST 2] watering < planted (očekávám CHYBU)");
            LocalDate planted  = LocalDate.now();
            LocalDate watering = LocalDate.now().minusDays(1); // dříve než zasazení
            Plant p = new Plant("Test 2", "x", planted, watering, 7);
            System.out.println("NEČEKANÝ VÝSLEDEK: chyba se nevyhodila -> " + p);
        } catch (PlantException e) {
            System.out.println("OK – zachyceno: " + e.getMessage());
        }

        // 3) Prázdný název  → musí vyhodit PlantException
        try {
            System.out.println("\n[TEST 3] prázdný název (očekávám CHYBU)");
            Plant p = new Plant("   "); // jen mezery
            System.out.println("NEČEKANÝ VÝSLEDEK: chyba se nevyhodila -> " + p);
        } catch (PlantException e) {
            System.out.println("OK – zachyceno: " + e.getMessage());
        }

        // 4) Setter frekvence (dodatečná validace)  → musí vyhodit PlantException
        try {
            System.out.println("\n[TEST 4] setter frekvence = -5 (očekávám CHYBU)");
            Plant p = new Plant("Validní", 7);
            p.setFrequencyOfWatering(-5);
            System.out.println("NEČEKANÝ VÝSLEDEK: chyba se nevyhodila -> " + p);
        } catch (PlantException e) {
            System.out.println("OK – zachyceno: " + e.getMessage());
        }

        // 5) Setter watering před planted  → musí vyhodit PlantException
        try {
            System.out.println("\n[TEST 5] setter watering před planted (očekávám CHYBU)");
            Plant p = new Plant("Validní", 7);
            p.setPlanted(LocalDate.now());
            p.setWatering(LocalDate.now().minusDays(1));
            System.out.println("NEČEKANÝ VÝSLEDEK: chyba se nevyhodila -> " + p);
        } catch (PlantException e) {
            System.out.println("OK – zachyceno: " + e.getMessage());
        }

        // 6) Pozitivní scénář – všechno validní  → bez výjimky
        try {
            System.out.println("\n[TEST 6] validní zadání (očekávám BEZ CHYBY)");
            Plant p = new Plant("Monstera", "ok", LocalDate.now().minusDays(10), LocalDate.now().minusDays(2), 3);
            System.out.println("OK – vytvořeno: " + p.getWateringInfo());
        } catch (PlantException e) {
            System.out.println("NEČEKANÝ PÁD: " + e.getMessage());
        }

        System.out.println("\nHotovo ✅");
    }
}

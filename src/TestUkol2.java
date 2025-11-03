import java.time.LocalDate;

public class TestUkol2 {
    public static void main(String[] args) {
        System.out.println("=== TEST ÚKOLU 2 – getWateringInfo() a doWateringNow() ===");

        try {
            // vytvoříme testovací rostlinu
            Plant testPlant = new Plant(
                    "Monstera",
                    "Testovací rostlina",
                    LocalDate.now().minusDays(10),
                    LocalDate.now().minusDays(5),
                    3
            );

            // výpis informací (ověření getWateringInfo)
            System.out.println("Před zalitím: " + testPlant.getWateringInfo());

            // zavoláme zalití teď
            testPlant.doWateringNow();

            // výpis po zalití
            System.out.println("Po zalití:   " + testPlant.getWateringInfo());
        }
        catch (PlantException e) {
            System.err.println("[CHYBA] " + e.getMessage());
        }

        System.out.println("\nHotovo ✅");
    }
}


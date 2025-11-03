// Třída Settings slouží pro uložení nastavení aplikace na jedno místo.
// Když budeš chtít změnit název souboru nebo oddělovač,
// stačí to udělat tady a ne ve všech třídách zvlášť.

public class Settings {

    // Název souboru, ze kterého se načítají květiny (vstupní soubor)
    public static final String INPUT_FILE = "kvetiny.txt";

    // Název souboru, kam se výsledek uloží (výstupní soubor)
    public static final String OUTPUT_FILE = "kvetiny-vystup.txt";

    // Soubor se špatnými daty (pro test chyb)
    public static final String BAD_DATE_FILE = "kvetiny-spatne-datum.txt";
    public static final String BAD_FREQUENCY_FILE = "kvetiny-spatne-frekvence.txt";

    // Oddělovač hodnot v souboru – tabulátor (\t)
    public static final String DELIMITER = "\t";
}

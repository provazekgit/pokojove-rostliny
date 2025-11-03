// Třída PlantException slouží k hlášení chyb při práci s rostlinami.
// Například když uživatel zadá špatné datum nebo zápornou frekvenci zálivky.
//
// "extends Exception" znamená, že tato třída vychází z obecné třídy Exception,
// takže ji můžeme používat s příkazem "throw new PlantException(...)".

public class PlantException extends Exception {

    // Konstruktor – umožní vytvořit novou chybu s textovou zprávou
    public PlantException(String message) {
        super(message);  // předá text rodičovské třídě Exception
    }

    // Druhý konstruktor – kdybychom chtěli připojit i původní chybu (méně časté)
    public PlantException(String message, Throwable cause) {
        super(message, cause);
    }
}

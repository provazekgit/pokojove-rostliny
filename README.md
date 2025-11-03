# Pokojové rostliny – Engeto Projekt 1 (Java)

Jednoduchá aplikace pro správu pokojových rostlin: evidence názvu, poznámky, data zasazení, poslední zálivky a frekvence zálivky.  
Podporuje přidávání, odebírání, řazení, zjištění „k zalití dnes“, a ukládání/načítání seznamu ze souboru.

## Struktura projektu

src/
├─ Main.java # test dle zadání (Úkol 7)
├─ Plant.java # model + validace + metody (Úkol 1–2)
├─ PlantException.java # vlastní výjimka (Úkol 3)
├─ PlantManager.java # správa seznamu + I/O + řazení (Úkol 4–6)
├─ Settings.java # názvy souborů, oddělovač (TAB)
├─ TestUkol2.java # extra ruční test Úkolu 2
├─ TestUkol3.java # extra ruční test Úkolu 3
└─ TestUkol4.java # extra ruční test Úkolů 4/5/6

## Požadavky
- Java 17+ (projekt testován s JDK 21)
- IntelliJ IDEA (Community stačí)

## Jak spustit v IntelliJ
1. Otevři projekt → `Main.java` → zelený ▶ **Run**.
2. Pro samostatné testy spouštěj `TestUkol2.java`, `TestUkol3.java`, `TestUkol4.java`.

## Formát souboru se seznamem (`kvetiny.txt`)
- **Oddělovač: TAB** (`\t`) – nikoli mezery.
- Každý řádek:  
  `name<TAB>notes<TAB>planted(YYYY-MM-DD)<TAB>watering(YYYY-MM-DD)<TAB>frequency(int)`
- Příklad:
  Monstera\tVelký list\t2025-09-01\t2025-11-01\t7
  Aloe\tNa slunci\t2025-08-15\t2025-10-30\t14

> V editoru zapiš skutečný **TAB** (zobrazení: View → Show Whitespaces).  
> Soubory umísti **do kořene projektu** (vedle `src`).

## Chování při chybě vstupu
- Výchozí varianta: **all-or-nothing** – při první chybné řádce načítání skončí a aplikace pokračuje s prázdným seznamem.
- Volitelně lze v `PlantManager` přepnout `SKIP_BAD_LINES = true`, aby se **chybné řádky přeskočily** a zbytek načetl.

## Splnění zadání (rychlá mapa)
- **Úkol 1 – Model, 3 konstruktory, get/set:** `Plant.java`
- **Úkol 2 – getWateringInfo(), doWateringNow():** `Plant.java` (+ test `TestUkol2.java`)
- **Úkol 3 – PlantException + validace:** `PlantException.java`, settery/konstruktory v `Plant.java` (+ test `TestUkol3.java`)
- **Úkol 4 – Správa seznamu:** `PlantManager.java` (add/get/remove/getAllCopy/getPlantsToWater)
- **Úkol 5 – Řazení:** `Plant` (`Comparable` podle názvu), `PlantManager.sortByLastWateringAsc()`
- **Úkol 6 – I/O:** `PlantManager.loadFromFile()` / `saveToFile()` (TAB, UTF-8)
- **Úkol 7 – Test v mainu:** `Main.java`
- **Úkol 8 – Ověření funkčnosti:** výstupy z `Main` + extra testy `TestUkol2/3/4`

## Nejčastější problémy
- **„Neplatný počet sloupců“** → nejsou TABy, ale mezery; nebo TAB navíc.
- Zapni si zobrazení bílých znaků a nahraď mezery za `\t` (Ctrl+R, regex ` +` → `\t`).
- **„watering < planted“** → oprav data, poslední zálivka nesmí být dřív než zasazení.
- **„frequency <= 0“** → frekvence musí být `>= 1`.
- **Neshoda názvů soubor/třída** → `public class X` musí být v `X.java` (velikost písmen se rozlišuje).

## Bonus – nápady na rozšíření
- „Lidské“ řazení „Tulipán 1, 2, …, 10“ (číslo v názvu) – speciální komparátor.
- Lokalizace dat – formát `dd.MM.yyyy`.
- Přeskočení chybných řádků při načítání + sběr chyb do reportu.
- Jednotkové testy (JUnit 5) – viz níže skeleton.
- 

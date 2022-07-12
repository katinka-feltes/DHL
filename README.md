# DHL - Dire Horror Land

<br>

# Anforderungsdokumentation

Die Anforderungen des Produkts sollen übersichtlich in einem **Anwendungsfalldiagramm** dargestellt werden. Die einzelnen Anforderungen sollen detailliert auf **Story-Cards** festgehalten werden, auf die während des Praktikums zurückgegriffen werden soll.

## Anwendungsfalldiagramm

https://md.isp.uni-luebeck.de/CmioeQyMRYS00nuryhVgZg#

<br>

# Bedienungsanleitung

In diesem Dokument sollen alle Funktionen des Systems kurz vorgestellt werden und erklärt sein, wie diese angesteuert werden können.

<br>

# Architekturdokumentation

In diesem Dokument sollen die grundsätzlichen Ideen hinter der gewählten Architektur dargelegt werden. Es sollte erklärt werden, in welche Komponenten das System eingeteilt wurde, welche Klassen zur Umsetzung der einzelnen Komponenten dienen und welche Kommunikation zwischen den einzelnen Klassen stattfindet. Außerdem sollte klar werden, in wie fern die gewählte Architektur eine Erweiterbarkeit und einfache Wartbarkeit des Programms gewährleistet.

## Klassendiagramm

https://md.isp.uni-luebeck.de/NtSg37ifQ2yYPsb06Ve10Q?both

## Zustandsdiagramm

https://md.isp.uni-luebeck.de/Y3-Xzz5kTii3JAN-Yl34tA?view

<br>

# Maven

Kurzübersicht nützlicher Maven-Befehle. Weitere Informationen finden sich im Tutorial:

* `mvn clean` löscht alle generierten Dateien
* `mvn compile` übersetzt den Code
* `mvn javafx:jlink` packt den gebauten Code als modulare Laufzeit-Image. Das Projekt kann danach gestartet werden mit `target/dhl/bin/dhl`
* `mvn test` führt die Tests aus
* `mvn compile site` baut den Code, die Dokumentation und die Tests und führt alle Tests, sowie JaCoCo und PMD inklusive CPD aus. Die Datei `target/site/index.html` bietet eine Übersicht über alle Reports.
* `mvn javafx:run` führt das Projekt aus
* `mvn javafx:run -Dargs="--no-gui"` führt das Projekt mit Command-Line-Parameter `--no-gui` aus.

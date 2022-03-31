---
Author: "Gruppe 3: CodeBrewery"
Header:
  Center: "{{ChangeDate}}"
---

# Projekt Skizze

- [Projekt Skizze](#projekt-skizze)
  - [Ausgangslage](#ausgangslage)
  - [Idee  von Ikitomo](#idee--von-ikitomo)
  - [Kundennutzen](#kundennutzen)
  - [Konkurrenzanalyse und abgrenzung zu Bestehendem](#konkurrenzanalyse-und-abgrenzung-zu-bestehendem)
  - [Hauptablauf](#hauptablauf)
    - [Anwendungsbeispiel](#anwendungsbeispiel)
  - [Weitere Anforderungen](#weitere-anforderungen)
  - [Ressourcen](#ressourcen)
  - [Risiken](#risiken)
  - [Wirtschaftlichkeit](#wirtschaftlichkeit)
  - [Bibliographie](#bibliographie)

- nicht auf katzen spezifizieren
- nach IEEE zitieren
## Ausgangslage
Gerade in den Zeiten der Pandemie sehen sich wohl viele Leute nach einem eigenen Haustier,
was sich schon alleine anhand der erhöhten Nachfrage nach Hauskatzen und Hunden feststellen lässt.

Leider haben nicht alle Personen die finanzielle Möglichkeit,
die Pflege, das Futter und die Behandlung eines Haustiers zu bezahlen.

Es besteht also ein erhöhter Bedarf einer Möglichkeit,
das preiswerte Beschaffen von Haustieren zu ermöglichen.

## Idee  von Ikitomo
- die Idee genauer formulieren

Ikitomo will genau diese genannten Punkte aufgreifen und es einem ermöglichen,
für die ersten 30 Tage kostenlos Haustiere auf den eigenen Bildschirm einzuladen.

Haustiere sollen hierbei so natürlich wie möglich agieren.

So sind Katzen mit den klassischen "Schläft durchgehend", "Ist der König der Welt" (aka. "Braucht jetzt Aufmerksamkeit") und "Katz- und Maus-Spiel" Modi ausgestattet.

Haustiere bewegen sich frei auf dem Bildschirm, interagieren mit dem Nutzer,
zeigen Wertschätzung, Freude, Hass und weitere Emotionen auf nachvollziehbarer Weise.

Zusätzlich sollen über eine Online-Plattform Tauschhandel möglich sein,
um neue Kostüme, Spezies, Verhaltensweisen und weiteres erleben zu können.

## Kundennutzen
Wie auch bereits Tamagotchis soll Ikitomo dem Nutzer ein virtuelles Haustier haben. Das *Tomodachi*, ein virtuelles Haustier, soll mit dem Nutzer interagieren ohne dabei nervend zu werden.
Dies soll dem Nutzer regelmässig ein Lächeln auf das Gesicht zaubern. Da das *Tomodachi* einen gewissen Pflegeaufwand mit sich bringt, soll sich der Nutzer
emotional an sein *Tomodachi* binden und eine Beziehung mit seinem Freund auf dem Bildschirm führen, sei dies eine Katze, ein Hund oder eine anderes Tier.

Für das *Tomodachi* soll ein kleines Zuhause gebaut werden können. Der Nutzer kann sich dabei kreativ ausleben und kann danach zusehen, wie sein *Tomodachi* in seinen neuen 
vier Wänden spielt oder schläft. 

Ikitomo soll viel Konfigurationsmöglichkeiten besitzen, damit das *Tomodachi* und sein Verhalten so angepasst werden kann, dass es den Nutzer unterhält und ihm Spass
bringt.

Wenn der Nutzer das App heruntergeladen hat, kann sein *Tomodachi* ihn zusätzlich überal begleiten und ihm so immer emotionalen Support leisten.

## Konkurrenzanalyse und abgrenzung zu Bestehendem
- Alleinstellungsmerkmal schreiben
 
Es gibt einige Programme, in welchen ein animiertes Tier oder Figur dem Mauszeiger folgen oder alternativ auch zum aktiven Fenster laufen. 
Das bekannteste davon ist Oneko, welches Open-Source ist und kostenlos auf Unix Betriebsystemen installiert werden kann [^1]. Es gibt mehrere Windows Ports, kostenlose, wie auch kostenpflichtige.
Eine weitere Applikation ist Desktop Goose, welche ein Gans auf dem Dekstop anzeigt, welche den Nutzer nerft [^2] oder eSheep, welche nach einer Windows Alternative aussieht[^3].

Keine der oben genannten Software hat den geplanten Umfang und sind alles Fan-Projekte, welche nicht kommerzialisiert sind. Unsere Software
soll eine höhere Qualität aufweissen. Sie soll auch mehr Funktionen implementieren, wie das Bauen von einem Zuhause für das *Tomodachi* oder 
ein App, welches mit dem Desktop Client synchronisiert, so dass der Nutzer nie sein *Tomdachi* am Rechner zurück lassen muss. All dies
soll dazu führen, dass Ikitomo mehr als nur eine Software ist, sondern dass das *Tomodachi* zu einem Freund wird. 

Um den Nutzer über eine lange Zeit zu fesseln, können über einen Store Erweiterungen gekauft werden, um neuen Inhalt, wie neue Skins, Gebäude oder Minigames, freizuschalten.
## Hauptablauf
- noch schreiben, was für den Prototyp genau realisiert werden soll
 
*Ikitomo* als Ganzes besteht aus zwei Komponenten, welche in weitere Unterkomponenten aufgeteilt sind. Die erste Komponente ist die *Ikitomo* Benutzerapplikation. Diese läuft lokal auf dem Gerät des **Endbenutzers**. Es besteht aus dem *Tomodoachi*, das gewählte Tier des **Bentuzers**, und einer Konfigurationsschaltfläche, worin **Benutzer** ihren *Tomodachi* zu Belieben konfigurieren können. **Fortgeschrittene Benutzer** haben die möglichkeit direkt auf die Dateien des *Tomodachis* zuzugreifen, um erweiterte Funktionalität zu kreieren.  
Die zweite Komponente ist der *Tomodachi Plaza*, ein digitaler Marktplatz worin offizielle *Tomodachis* verkauft werden und **Benutzer** ihre eigenen *Tomodachis* hochladen und teilen können. Im Plaza können **Benutzer** auch ihre hochgeladenen *Tomodachis* verwalten und organisieren. Für **Adminstratoren** gibt es einen exklusiven Bereich zur Moderation der Plattform.  

### Anwendungsbeispiel
Mark verwendet nun seit einer Woche *Ikitomo* auf seinem Hauptgerät. Auch heute startet er seinen PC auf und *Ikitomo* startet automatisch mit. Nach ein paar Sekunden taucht eine kleine Katze auf Marks Bildschirm auf. Dies ist sein aktueller *Tomodachi*. Für eine kurze Weile beobachtet er wie die Katze seiner Maus folgt und beginnt anschliessen mit seiner Arbeit. Im fällt ein, dass er vergessen hat seinen *Tomodachi* zu füttern und klickt auf die Katze mit der rechten Maustaste. Eine kurze Animation wird abgespielt und die Katze geht zum oberen Bildschirmrand ruht sich dort aus.  
Während der Mittagspause möchte Mark seinen *Tomodachi* austauschen. Er öffnet das Bedienfeld von *Ikitomo* und sieht sich die vorinstallierten *Tomodachis* an. Keine der Optionen gefallen ihm aber und er navigiert über das Bedienfeld zum *Tomodachi Plaza*. Dort meldet er sich mit seinem Benutzerkonto and sieht sich die beliebtesten *Tomodachis* der Woche an. Mark findet einen Tiger *Tomodachi* und fügt ihn zu seinem Profil hinzu. *Ikitomo* lädt den neuen *Tomodachi* innert Sekunden herunter und wird in der Applikation angezeigt. Mark wählt den neuen *Tomodachi* aus und beobachtet das neue Verhalten. Der Tiger ist deutlich schneller als die Katze und das Futter sieht ebenfalls anders aus. Mark ist zufrieden mit dem Tiger und wendet sich wieder seiner Arbeit.

<p style="page-break-after: always;">&nbsp;</p>
<p style="page-break-before: always;">&nbsp;</p>


## Weitere Anforderungen
- was Nice to have wäre
Um die Benutzererfahrung zu erhöhen


### Funktionale Anforderungen
Bei Funktionalen Anforderungen wird spezifisch zusätzliche Features im Programm selber aufgezählt, die für den Endnutzer zur Verfügung gestellt werden wird und und vom Endnutzer manipuliarbar ist 

- Beim Klick der Katze soll eine Interaktion gestartet werden, um die Benutzererfahrung zu erweitern. Die Interaktionen sind verschieden und können zufällig angezeigt werden. Beispielsweise würde die Katze ein Herz ausgeben oder auf dem Boden liegen.
- Katze ist per Drag-and-Drop verschiebbar, um so nicht die Interaktion zwischen Benutzern und anderen Programmen bzw. Software zu beeinträchtigen.
- Es gibt ein Korb, bei der die Katze schlafen kann. 

### Nicht-Funktionale Anforderung
Bei Nicht-Funktionalen Anforderungen 

- Performance beziehungsweise Resourcenleichte Software.
- Die Software soll so wenig wie möglich mit der Kontrolle des Benutzers schneiden. Beispielsweise soll die Katze nicht Elemente zu stark verdecken können.
- Security des Softwares

## Ressourcen

| Komponente                           | Benötigtes Know-How                                                    | Geschätzter Aufwand |
| ------------------------------------ | ---------------------------------------------------------------------- | ------------------- |
| Benutzerapplikation                  | Plattformunabhängige Entwicklung, Dateiformate kreiern, Rudimentäre KI | 640 h               |
| *Tomodachis*                         | Pixel-Art & Animation                                                  | 50 h                |
| *Tomodachi Plaza* Grundgerüst        | Datenbankmodell krieren, Netzwerksicherheit, Hosting                   | 200 h               |
| *Tomodachi Plaza* Benutzeroberfläche | Webdesign, JavaScript, UX-Design                                       | 200 h               |
| **Total**                            |                                                                        | 1090 h              |

## Risiken
Ein wichtiger Aspekt von *Ikitomo* ist die Art des Produktes. *Ikitomo* ist eine 
sogenannte "Virtual Pet" Applikation, die in den 2000-er Jahren sehr beliebt waren 
aber wegen Marktübersättigung ausgestorben sind. *Ikitomo* versucht diesen Markt 
wiederzubeleben. Es ist durchaus möglich, dass *Ikitomo* nicht genug Traffic oder 
Onlinediskussion generiert, um relevant zu bleiben. Aus diesem Grund ist es notwendig Social-Media intelligent anzuwenden, um *Ikitomo* ausserordentlich zu vermarkten.  
Ein weiterer Punkt ist das fehlende Know-How für Animation, Netzwerksicherheit und 
Webdesign. Wenn diese Fähigkeiten nicht bis zu Projektstart angeschafft werden, kommt 
es zu Verschiebungen und Verzögerungen. Somit würden auch die Kosten steigen.

## Wirtschaftlichkeit
Für das Endprodukt schätzen wir einen Aufwand von 1'090h, für was wir mit einem
Stundenanstatz von 90.- pro Stunde 98'100.- benötigt, um das Produkt zu verwirklichen.
Zu diesen Kosten kommen wiederkehrende Kosten für die Server-Infrastruktur, für was wir 5000.- 
pro Jahr rechnen.

Die Haupteinnahmequelle werden verkaufte *Tomodachis* und weitere Accessoirs vom *Ikitomo Plaza* sein. 
Ein *Tomodachi* wird 5.- kosten; Accessiors zwischen 1.- bis 15.- für grössere interaktive Objekte. 
Wir erwarten 100'000 Nutzer nach 2 Jahren, davon werden 30% drei Käufe im *Ikitomo Plaza* tätigen und somit über 
diese zwei Jahre 630'000.- einbringen. 

## Bibliographie
[^1]: "oneko-1.3", [http://www.daidouji.com/oneko/](http://www.daidouji.com/oneko/) (Zugegriffen: 30.03.2022)
[^2]: "Desktop Goose by samperson", [https://samperson.itch.io/desktop-goose](https://samperson.itch.io/desktop-goose) (Zugegriffen: 30.03.2022)
[^3]: "Desktop Pet (eSheep 64bit) | Add a screen mate to your desktop", [https://adrianotiger.github.io/desktopPet/](https://adrianotiger.github.io/desktopPet/) (Zugegriffen: 30.03.2022)
# Praktikum Softwaretechnik 2 (ST2) im SoSe 2024

Sofortiges Feedback zu Ihres Lösung finden Sie wie immer auf Ihrer 
[individuellen Testseite](http://students.pages.archi-lab.io/st2/ss24/m1/test/ST2M1_tests_group_498e1de7-4b77-485c-9fb8-36edaa97812d).


## Milestone 1 (M1) - Refactor Your Code

_Da wir aus der Aufgabenbeschreibung direkt Coding-Aufgaben ableiten, ist die komplette Meilenstein-Beschreibung 
in Englisch gehalten. Wir brauchen immer mal wieder Englisch, weil fachliche Beschreibungen dazukommen. Damit nicht
ein deutsch-englisches Sprach-Mischmasch entsteht, ist der gesamte Meilenstein-Pflichtteil auf Englisch._

In this milestone, you will refactor your code from M0. You will refactor the code to adhere to the
DDD conventions, Clean Code rules and SOLID principles. Compliance with these rules will be automatically 
tested by additional tests (compared to M0). 

There are no new features yet (that will come later). However, you will notice that the `ShoppingBasketUseCases`
interface has been split into two interfaces. This is because the interface was too large and violated 
the [Single Responsibility Principle (SRP)](https://www.youtube.com/watch?v=BFVLXYFlNXg). There are now two interfaces:

* `ShoppingBasketUseCases`  
* `OrderUseCases`

The methods have just been split between the two interfaces, with one exception: In the old, "combined" interface,
`deleteAllOrders()` was supposed to delete all orders **and** shopping baskets. This, of course, now doesn't make sense
anymore. Therefore: 

* `deleteAllOrders()` just deletes all orders for all customers,  
* a new method `emptyAllShoppingBaskets()` empties all shopping baskets for all customers, and
* a new method `isEmpty()` checks if a shopping baskets is empty.

You will have to update your code accordingly. The task description and the rules (your own packages and classes
located below `thkoeln.archilab.ecommerce.solution`) remain the same as in M0.


### E1: Keep DDD Conventions, and Avoid Anti-Patterns

![Bloom-Level](./images/5-filled-32.png)
[Level 5 (Evaluate) in Bloom's Taxonomy](https://www.archi-lab.io/infopages/didactics/blooms_taxonomy.html#level5)

In this milestone, you need to keep Domain-Driven Design (DDD) conventions, and avoid anti-patterns.
We will especially check the following aspects.

#### General Do's and Don'ts

There are a number of general do's and don'ts that you should follow when implementing entities and value objects.
Amongst the most relevant ones (which we will check) are:

* Value Objects are immutable, and are considered equal if all their attributes are equal
* Entities have relationships via object reference, not via ID
* References between entities are NEVER bidirectional, ALWAYS unidirectional

More details can be found in the ArchiLab 
[Anti-Patterns bei "DDD mit Spring JPA"](https://www.archi-lab.io/infopages/st2/antipatterns.html)
infopage.


#### Use Case Implementations 

You should implement the use cases using **adapters**. These adapters must be located in the `application` package
of the fitting aggregate. More details on the adapter pattern can be found in in the ArchiLab
[Adapter-Pattern (a.k.a. Anti-Corruption-Layer)](https://www.archi-lab.io/infopages/st2/adapter-pattern.html) infopage.

**Hint:** The required packages are obvious for most use case interfaces. One notable exception is
`InventoryManagementUseCases`, which specifies inventory (Warenbestand) use cases. 
This interface can be implemented either as part of the `product` aggregate, or as its 
own aggregate in package `inventory`.


#### DDD Package Conventions

* Your code follows the **naming conventions** usually used in DDD
* Your code follows the **package and layer conventions** we have defined in this course 
* The relevant domain classes from the LDM are implemented, and located in the correct packages

More information and a brief motivation you can find in the ArchiLab
[DDD-konforme Package-Konvention](https://www.archi-lab.io/infopages/st2/package-convention.html) infopage.



### E2: Refactoring according to Clean Code and SOLID Principles

![Bloom-Level](./images/5-filled-32.png)
[Level 5 (Evaluate) in Bloom's Taxonomy](https://www.archi-lab.io/infopages/didactics/blooms_taxonomy.html#level5)

When you refactor and enhance your code from M0, you need to apply the Clean Code rules and the SOLID
principles. We will check the following aspects:

* Clean Code
    * Meaningful names for variables, classes, packages, ...
    * Methods should be small, not longer than max. 30 lines of code, and if possible should be much smaller.
    * Code lines should not extend 120 characters - add a linebreak otherwise, or try re-writing the code.
* SOLID
    * Single Responsibility Principle
        * Classes should serve one purpose only
        * No domain business logic in application services or controllers
    * Open-Closed Principle
        * No public access to member variables
        * States (see above for ShoppingBasket) must be encapsulated - no possibility of directly changing the status from outside
    * Dependency Inversion Principle
        * No cyclic dependencies - avoid them by applying the DIP

Besides the videos to Clean Code and SOLID
(see [M1 description in the ArchiLab ST2 page](https://www.archi-lab.io/regularModules/ss24/st2_ss24.html#milestoneM1)),
there is additional material you can use:

* [Checkliste für Clean Code und SOLID-Prinzipien](https://www.archi-lab.io/infopages/material/checklist-clean-code-and-solid.html)
* [Zykel auflösen mittels Dependency Inversion Principle](https://www.archi-lab.io/infopages/st2/zykel-aufloesen-mit-dip.html)


## Zusatzaufgabe (Optional): Nutzung von KI-Tools beim Refactoring

_(Ab jetzt wieder auf Deutsch, weil Sie die Videos für die Zusatzaufgabe auf Deutsch erstellen können.)_

Wie in [der ST2-Seite beschrieben](https://www.archi-lab.io/regularModules/ss24/st2_ss24.html#ki-tools-im-praktikum), 
können Sie KI-Tools einsetzen und Ihre Erfahrungen damit reflektieren. Sie können damit bis zu 6 
Klausurbonuspunkte sammeln.

In diesem Milestone schauen wir besonders auf das Thema **KI-Tools und Refactoring**. Ihr maximal 5-minütiges Video 
soll bitte folgende Leitfragen beantworten:

1. Bei welchem Teil des Refactorings sollten die KI-Tools Ihnen helfen?
2. Welche(s) KI-Tool(s) haben Sie verwendet? Wenn in Kombination, welches Tool an welcher Stelle?
3. Was hat gut funktioniert? 
4. Was hat nicht so gut funktioniert?
5. Was ist Ihr Fazit, wenn Sie Ihren Kommilitonen eine Empfehlung geben wollen?


### Anmeldung und Abgabe

Sie müssen sich für die Zusatzaufgabe anmelden, durch Beitritt 
[zu dieser ILU-Gruppe](https://ilu.th-koeln.de/ilias.php?baseClass=ilrepositorygui&ref_id=380318).
Der Beitritt wird im M1-Workshop am 6.5. freigeschaltet. Es gibt nur eine begrenzte Anzahl Plätze. Es gilt: 
- First come, first serve
- Jede:r hat nur eine Anmeldung zu einem Video frei. Wenn man also einen Slot bekommt und dann verfallen lässt, 
  dann gibt es keinen weiteren Versuch.

Die Abgabe erfolgt auch über ILU. Sie laden Ihr Video in die Übung "KI-Reflektions-Video" hoch. (Achtung: 
maximale Dateigröße 350 MB - das sollte für 5 min 1080p leicht ausreichen.) Abgabedatum ist dasselbe wie der 
Pflichtteil des Meilensteins. Feedback gibt es dann von uns über eine persönliche Nachricht. Je nach 
Qualität der Reflektion im Video geben wir Ihnen zwischen 0 und 6 Klausur-Bonuspunkte.

<b>Mit der Abgabe des Videos stimmen Sie zu, dass wir Ihr Video für nicht-kommerzielle Zwecke im
Rahmen von Forschung und Lehre auswerten dürfen, in Teilen oder ganz in Lehrveranstaltungen zeigen können, 
sowie in weiteren Lehrmitteln (z.B. Lehrvideos) verwenden dürfen. Wenn von Ihnen gewünscht, wird Ihre
Urheberschaft dabei anonymisiert.</b>


### Am Schluss nochmal Disclaimers ...

- KI-Tool-Nutzung im Praktikum ist **NICHT** Cheating! Egal, ob Sie dazu ein Bonus-Video machen oder 
  nicht. (Copy-Paste von Kommilitonen ist aber immer Cheating.) 
- Sie dürfen die KI-Tools auch nutzen, ohne dass Sie ein Bonus-Video machen. Dann gibt es halt keine Bonuspunkte.
- In der Klausur gibts aber keine KI-Tools! Da müssen Sie dann schon selbst coden :-). Sie müssen es also selbst können.

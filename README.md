# Mars Rover

Implémentation du kata **Mars Rover** en Java 21 : un programme en ligne de commande lit un fichier d'instructions, déplace des rovers sur un plateau rectangulaire et affiche leur position finale.

## Règles

- La première ligne du fichier donne le coin supérieur droit du plateau, `xMax yMax`. Le plateau va de `(0,0)` à `(xMax,yMax)`, **bornes incluses**.
- Chaque rover est décrit par deux lignes :
  - sa position de départ, `x y D`, où `D` est l'orientation : `N`, `E`, `S` ou `W` ;
  - ses commandes, par exemple `LMLMLMLMM`.
- Commandes :

| Commande | Effet |
|---|---|
| `L` | Tourne de 90° à gauche |
| `R` | Tourne de 90° à droite |
| `M` | Avance d'une case : `N` → y+1, `E` → x+1, `S` → y−1, `W` → x−1 |

- Les rovers se déplacent **l'un après l'autre**, dans l'ordre du fichier.
- **Sortie du plateau :** si une commande `M` ferait sortir un rover, le mouvement est refusé (la position n'est pas modifiée), une erreur est signalée et l'exécution s'arrête.
- **Erreurs :** fichier absent ou illisible, chemin invalide, format invalide, direction ou commande inconnue, départ hors du plateau. Le message va sur la sortie d'erreur, rien n'est écrit sur la sortie standard, et le code de retour vaut `1`.

### Exemple

Entrée (`input.txt`) :

```
5 5
1 2 N
LMLMLMLMM
3 3 E
MMRMMRMRRM
```

Sortie :

```
1 3 N
5 1 E
```

## Prérequis

- JDK 21
- Gradle n'a pas besoin d'être installé : le projet contient le wrapper (`gradlew`, `gradlew.bat`).

## Tester

```powershell
.\gradlew.bat test
```

Le rapport est généré dans `build\reports\tests\test\index.html`.

## Construire

```powershell
.\gradlew.bat bootJar
```

Le jar exécutable est produit dans `build\libs\rover.jar`.

## Lancer

```powershell
java -jar build\libs\rover.jar input.txt
```

Ou, après avoir copié le jar à la racine :

```powershell
Copy-Item build\libs\rover.jar .
java -jar rover.jar input.txt
```

Sous Linux, macOS ou Git Bash, utilise `./gradlew` à la place de `.\gradlew.bat`, et `/` dans les chemins.

## Structure

```
src/main/java/com/maguette/rover/
├── Direction.java          # N, E, S, W : rotations et déplacement (dx, dy)
├── Command.java            # L, R, M
├── Position.java           # coordonnées x, y
├── Plateau.java            # limites et vérification d'une position
├── Rover.java              # position, direction, exécution des commandes
├── RoverException.java     # erreur prévue, avec un message lisible
├── InstructionReader.java  # lecture et interprétation du fichier
└── RoverApplication.java   # lancement, enchaînement, affichage
```

Spring Boot sert uniquement au lancement : démarrage, injection de `InstructionReader`, code de retour et jar exécutable.

## Limites

- Les collisions entre rovers ne sont pas gérées.
- Une erreur arrête toute l'exécution, et aucune position partielle n'est affichée.
- Le fichier doit être en ASCII ou en UTF-8 **sans** BOM.

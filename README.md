# Programmierprojekt

Programmierprojekt WS 21/22 Schindler &amp; Tran &amp; Redecker

## Requirements

- Maven
- Java JDK 16

## Compiling

Im Root-Folder vom Projekt (Bei pom.xml)

```
mvn compile
```

## Running

Im Root-Folder vom Projekt (Bei pom.xml)

```
java -cp ./target/classes de.redeckertranschindler.Benchmark -graph /home/felix/germany.fmi -lon 9.098 -lat 48.746 -que /home/felix/germany.que -s 638394
```

- Bei _-graph_ argument den Pfad zur Graph-Datei angeben
- Bei _-lon_ argument den Längengrad angeben
- Bei _-lat_ argument den Breitengrad angeben
- Bei _-que_ argument den Pfad zur .que-Datei für den one-to-one-dijkstra angeben
- Bei _-s_ argument den Startknoten für den one-to-all-dijkstra angeben

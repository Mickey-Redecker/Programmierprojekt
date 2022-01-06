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

 - Bei *-graph* argument den Pfad zur Graph-Datei angeben,  
 - Bei *-lon* argument den Längengrad angeben  
 - Bei *-lat* argument den Breitengrad angeben,  
 - Bei *-que* argument den Pfad zur .que-Datei für den one-to-one-dijkstra angeben,  
 - Bei *-s* argument den Startknoten für den one-to-all-dijkstra angeben.  

# MiniJava-Compiler

## Setup IntelliJ
* IntelliJ installieren, see https://www.jetbrains.com/idea/
* JavaCC-Plugin für Syntax-Highlighting installieren, see https://plugins.jetbrains.com/plugin/11431-javacc

## Setup MiniJavaC-Projekt
* JavaCC : Laden Sie sich die JavaCC-Bibliothek über http://javacc.org/ herunter.
* Jasmin : Laden Sie sich die Jasmin-Bibliothek über http://jasmin.sourceforge.net/ herunter.
* Legen Sie die jar-Dateien <code>javacc.jar</code> und <code>jasmin.jar</code> im MiniJavaC-Projekt im Verzeichnis <code>tools/</code> ab.

## Erstellen der Spezifikation des MiniJavaParsers  
Erstellen Sie die Spezifikation des MiniJavaParsers in der Datei Spezifikation des <code>MiniJavaParser.jj</code>.
Die Grammatik für JavaCC finden sie unter https://javacc.github.io/javacc/documentation/grammar.html

In der main-Methode des MiniJavaParsers können Sie zum Testen das Blatt und das zu testende Beispiel setzen 
und variieren.
<pre>
blatt = 4;
sample = "Factorial";
</pre>

## Prozessschritte 
Die folgenden Schritte werden im Terminal der IDE ausgeführt. Bei den Anweisungen wird davon ausgegangen,
dass man sich im Terminal im Hauptverzeichnis des Projekts befindet:
<pre>
...\MiniJavaC
</pre>
Alle im Prozess erzeugten Dateien werden im Verzeichnis <code>out/</code> abgelegt. Das Verzeichnis muss existieren. 
Die im Verzeichnis liegenden Dateien können Sie jedoch löschen. 
<pre>
rm .\samples\out\*
</pre>

### 1. MiniJava-Parser aus MiniJavaParser.jj erzeugen
Die folgende Anweisung erzeugt den Parser im selben Verzeichnis, in der sich auch die jj-Datei
befindet.
<pre>
java -cp .\tools\javacc.jar javacc .\src\parser\MiniJavaParser.jj
</pre> 

### 2. MiniJava-Parser kompilieren 
Build Project MiniJavaC in IDE

### 3. MiniJava-Parser ausführen 
Run Project MiniJavaC in IDE

### 4. Jasmin-Dateien in Class-Dateien übersetzen
<pre>
java -jar .\tools\jasmin.jar -d .\samples\out .\samples\out\*.j
</pre>

### 5. Class-Dateien ausführen, bspw. Factorial
Die Ausführung erfolgt im Verzeichnis <code>out/</code>.
<pre>
cd .\samples\out
java Factorial .\Factorial
</pre>
Die Ausführung von <code>Factorial</code>liefert <code>3628800</code> als Ergebnisausgabe.
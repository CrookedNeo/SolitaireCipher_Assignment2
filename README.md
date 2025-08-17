# Solitaire Cipher (Assignment 2) — Java

This version patches `Deck.shuffle()` to use a safe index wrap and rebuild of prev/next pointers.

## Build & Run
```powershell
javac -d out (Get-ChildItem -Recurse -Filter *.java).FullName
java -cp out assignment2.Main
```

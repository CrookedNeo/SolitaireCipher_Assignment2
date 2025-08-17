# Solitaire Cipher — Java

A standalone implementation of the **Solitaire (Pontifex) cipher** using a circular doubly-linked list deck (playing cards + red/black jokers). Includes a small CLI demo. Built 03/2025.

## What this project does

* Builds a deck of cards (1–13 for each suit used) plus **two jokers**.
* Generates a **keystream** via the classical Solitaire steps:

  1. Move red joker down 1
  2. Move black joker down 2
  3. Triple cut (everything above the first joker swaps with everything below the second)
  4. Count cut (by bottom card value)
  5. Look-up the output card (skip if it’s a joker; repeat)
* **Encode/Decode**:

  * Strip non-letters, uppercase
  * Map A–Z → 1–26
  * Add/Subtract keystream mod 26
  * Map back to letters

## Project structure

```
SolitaireCipher_Assignment2/
  └─ src/
     └─ assignment2/
        ├─ Deck.java             # Deck + Card/PlayingCard/Joker (circular DLL)
        ├─ SolitaireCipher.java  # Encode/Decode using keystream from Deck
        └─ Main.java             # Simple demo (encode then decode)
```

## Build & run

### Windows (PowerShell)

```powershell
# From project root
javac -d out (Get-ChildItem -Recurse -Filter *.java).FullName
java -cp out assignment2.Main
```

### macOS / Linux (bash/zsh)

```bash
# From project root
mkdir -p out
javac -d out $(find src -name "*.java")
java -cp out assignment2.Main
```

## Demo

`Main.java`:

* Creates a standard deck (13×4 + 2 jokers), shuffles, builds the cipher, and
* Encodes then decodes: `"Meet me at the west gate at 9pm!"`

You should see three lines:

```
Original: Meet me at the west gate at 9pm!
Encoded : <ciphertext>
Decoded : MEETMEATTHEWESTGATEATPM
```

> (Punctuation/spaces are removed and text is uppercased by design.)

### Customize the message

Edit `src/assignment2/Main.java` and change the `message` string. Rebuild and run.

## Notes

* The implementation follows the classical Solitaire algorithm and skips any joker looked up in step 5 (repeats until a non-joker is found).
* The deck uses a **circular doubly linked list** to model cuts and moves in O(1) pointer updates.

## License

MIT

---

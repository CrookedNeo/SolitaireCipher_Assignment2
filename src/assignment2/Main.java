package assignment2;

public class Main {
    public static void main(String[] args) {
        Deck deck = new Deck(13, 4);
        Deck.gen.setSeed(42L);
        deck.shuffle();

        SolitaireCipher cipher = new SolitaireCipher(deck);

        String message = "Meet me at the west gate at 9pm!";
        String encoded = cipher.encode(message);
        String decoded = cipher.decode(encoded);

        System.out.println("Original: " + message);
        System.out.println("Encoded : " + encoded);
        System.out.println("Decoded : " + decoded);
    }
}

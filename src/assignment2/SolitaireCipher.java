package assignment2;

public class SolitaireCipher {
    public Deck key;

    public SolitaireCipher(Deck key) {
        this.key = new Deck(key);
    }

    public int[] getKeystream(int size) {
        int[] keystream = new int[size];
        for (int i = 0; i < size; i++) {
            keystream[i] = key.generateNextKeystreamValue();
        }
        return keystream;
    }

    public String encode(String msg) {
        String cleanMsg = msg.replaceAll("[^A-Za-z]", "").toUpperCase();
        int[] keystream = getKeystream(cleanMsg.length());
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < cleanMsg.length(); i++) {
            char ch = cleanMsg.charAt(i);
            int value = ch - 'A' + 1;
            int shifted = (value + keystream[i] - 1) % 26 + 1;
            char newCh = (char) ('A' + shifted - 1);
            ciphertext.append(newCh);
        }
        return ciphertext.toString();
    }

    public String decode(String msg) {
        String cleanMsg = msg.replaceAll("[^A-Za-z]", "").toUpperCase();
        int[] keystream = getKeystream(cleanMsg.length());
        StringBuilder plaintext = new StringBuilder();
        for (int i = 0; i < cleanMsg.length(); i++) {
            char ch = cleanMsg.charAt(i);
            int value = ch - 'A' + 1;
            int shifted = (value - keystream[i] - 1);
            shifted = ((shifted % 26) + 26) % 26 + 1;
            char newCh = (char) ('A' + shifted - 1);
            plaintext.append(newCh);
        }
        return plaintext.toString();
    }
}

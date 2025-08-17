package assignment2;

import java.util.Random;

public class Deck {
    public static String[] suitsInOrder = {"clubs", "diamonds", "hearts", "spades"};
    public static Random gen = new Random();

    public int numOfCards; // Total number of cards in the deck
    public Card head;      // Pointer to the card at the top of the deck

    public Deck(int numOfCardsPerSuit, int numOfSuits) {
        if (numOfCardsPerSuit < 1 || numOfCardsPerSuit > 13) {
            throw new IllegalArgumentException("Input must be between 1 and 13");
        }
        if (numOfSuits < 1 || numOfSuits > suitsInOrder.length) {
            throw new IllegalArgumentException("Input must be between 1 and the size of suitsInOrder");
        }
        head = null;
        numOfCards = 0;

        for (int j = 0; j < numOfSuits; j++) {
            for (int i = 1; i <= numOfCardsPerSuit; i++) {
                PlayingCard card = new PlayingCard(suitsInOrder[j], i);
                addCard(card);
            }
        }
        Joker redJoker = new Joker("red");
        addCard(redJoker);
        Joker blackJoker = new Joker("black");
        addCard(blackJoker);
    }

    public Deck(Deck d) {
        if (d.head == null) {
            this.head = null;
            this.numOfCards = 0;
            return;
        }
        this.head = null;
        this.numOfCards = 0;
        Card currentOriginal = d.head;
        do {
            Card newCard = currentOriginal.getCopy();
            addCard(newCard);
            currentOriginal = currentOriginal.next;
        } while (currentOriginal != d.head);
    }

    public Deck() {}

    public void addCard(Card c) {
        if (head == null) {
            head = c;
            c.next = c;
            c.prev = c;
        } else {
            Card tail = head.prev;
            tail.next = c;
            c.prev = tail;
            c.next = head;
            head.prev = c;
        }
        numOfCards++;
    }

    /**
     * Safer shuffle: convert to array, Fisher–Yates shuffle, then rebuild circular links
     * with modular indexing for next/prev. Avoids any off-by-one access.
     */
    public void shuffle() {
        if (numOfCards <= 1) return;

        Card[] cards = new Card[numOfCards];
        Card current = head;
        for (int i = 0; i < numOfCards; i++) {
            cards[i] = current;
            current = current.next;
        }
        for (int i = numOfCards - 1; i > 0; i--) {
            int j = gen.nextInt(i + 1);
            Card tmp = cards[i];
            cards[i] = cards[j];
            cards[j] = tmp;
        }

        // Rebuild pointers using modulo (no out-of-bounds)
        for (int i = 0; i < numOfCards; i++) {
            int nextIndex = (i + 1) % numOfCards;
            int prevIndex = (i - 1 + numOfCards) % numOfCards;
            cards[i].next = cards[nextIndex];
            cards[i].prev = cards[prevIndex];
        }
        head = cards[0];
    }

    public Joker locateJoker(String color) {
        if (head == null) return null;
        Card current = head;
        do {
            if (current instanceof Joker) {
                Joker joker = (Joker) current;
                if (joker.getColor().equalsIgnoreCase(color)) return joker;
            }
            current = current.next;
        } while (current != head);
        return null;
    }

    public void moveCard(Card c, int p) {
        c.prev.next = c.next;
        c.next.prev = c.prev;

        Card target = c;
        for (int i = 0; i < p; i++) {
            target = target.next;
        }

        c.next = target.next;
        c.prev = target;
        target.next.prev = c;
        target.next = c;

        if (c == head) head = c.next;
    }

    public void tripleCut(Card firstCard, Card secondCard) {
        if (numOfCards <= 1) return;

        Card firstSectionHead = head;
        Card firstSectionTail = firstCard.prev;
        Card middleSectionHead = firstCard;
        Card middleSectionTail = secondCard;
        Card thirdSectionHead = secondCard.next;
        Card thirdSectionTail = head.prev;

        if (firstCard == head && secondCard == thirdSectionTail) return;

        if (firstCard == head) {
            head = thirdSectionHead;
            thirdSectionTail.next = middleSectionHead;
            middleSectionHead.prev = thirdSectionTail;
            middleSectionTail.next = head;
            head.prev = middleSectionTail;
        } else if (secondCard == thirdSectionTail) {
            head = middleSectionHead;
            middleSectionTail.next = firstSectionHead;
            firstSectionHead.prev = middleSectionTail;
            firstSectionTail.next = head;
            head.prev = firstSectionTail;
        } else {
            head = thirdSectionHead;
            thirdSectionTail.next = middleSectionHead;
            middleSectionHead.prev = thirdSectionTail;
            middleSectionTail.next = firstSectionHead;
            firstSectionHead.prev = middleSectionTail;
            firstSectionTail.next = head;
            head.prev = firstSectionTail;
        }
    }

    public void countCut() {
        Card bottomCard = head.prev;
        int value = bottomCard.getValue();
        int cutValue = value % numOfCards;
        if (cutValue == 0) return;

        Card cutEnd = head;
        for (int i = 1; i < cutValue; i++) {
            cutEnd = cutEnd.next;
        }
        Card newHead = cutEnd.next;

        head.prev.next = newHead;
        newHead.prev = head.prev;

        Card beforeBottom = bottomCard.prev;
        beforeBottom.next = head;
        head.prev = beforeBottom;
        cutEnd.next = bottomCard;
        bottomCard.prev = cutEnd;

        head = newHead;
    }

    public Card lookUpCard() {
        Card topCard = head;
        int value = topCard.getValue();
        Card current = topCard;
        for (int i = 0; i < value; i++) current = current.next;
        if (current instanceof Joker) return null;
        return current;
    }

    public int generateNextKeystreamValue() {
        while (true) {
            Joker redJoker = locateJoker("red");
            moveCard(redJoker, 1);

            Joker blackJoker = locateJoker("black");
            moveCard(blackJoker, 2);

            Card current = head;
            Joker firstJoker = null, secondJoker = null;
            do {
                if (current instanceof Joker) {
                    if (firstJoker == null) firstJoker = (Joker) current;
                    else { secondJoker = (Joker) current; break; }
                }
                current = current.next;
            } while (current != head);

            tripleCut(firstJoker, secondJoker);
            countCut();

            Card lookup = lookUpCard();
            if (lookup != null) return lookup.getValue();
        }
    }

    public abstract class Card {
        public Card next;
        public Card prev;
        public abstract Card getCopy();
        public abstract int getValue();
    }

    public class PlayingCard extends Card {
        public String suit;
        public int rank;

        public PlayingCard(String s, int r) {
            this.suit = s.toLowerCase();
            this.rank = r;
        }
        public String toString() {
            String info = "";
            if (this.rank == 1) info += "A";
            else if (this.rank > 10) {
                String[] cards = {"J", "Q", "K"};
                info += cards[this.rank - 11];
            } else info += this.rank;
            info = (info + this.suit.charAt(0)).toUpperCase();
            return info;
        }
        public PlayingCard getCopy() { return new PlayingCard(this.suit, this.rank); }
        public int getValue() {
            int i;
            for (i = 0; i < suitsInOrder.length; i++) {
                if (this.suit.equals(suitsInOrder[i])) break;
            }
            return this.rank + 13 * i;
        }
    }

    public class Joker extends Card {
        public String redOrBlack;
        public Joker(String c) {
            if (!c.equalsIgnoreCase("red") && !c.equalsIgnoreCase("black"))
                throw new IllegalArgumentException("Jokers can only be red or black");
            this.redOrBlack = c.toLowerCase();
        }
        public String toString() { return (this.redOrBlack.charAt(0) + "J").toUpperCase(); }
        public Joker getCopy() { return new Joker(this.redOrBlack); }
        public int getValue() { return numOfCards - 1; }
        public String getColor() { return this.redOrBlack; }
    }
}

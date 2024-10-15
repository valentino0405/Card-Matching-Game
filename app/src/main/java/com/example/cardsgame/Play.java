package com.example.cardsgame;
import java.util.ArrayList;
import java.util.List;

public class Play {
    private String name;
    private List<Card2> hand;
    private int score;

    public Play(String name) {
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public void addPoint() {
        score++;
    }

    public void addCard(Card2 card) {
        hand.add(card);
    }

    public List<Card2> getHand() {
        return hand;
    }

    public Card2 playCard() {
        if (!hand.isEmpty()) {
            return hand.remove(0);
        }
        return null;
    }

    public int getHandSize() {
        return hand.size();
    }
}

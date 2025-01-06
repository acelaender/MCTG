package models;

import java.util.ArrayList;

public class Player {
    private String username;
    private ArrayList<Card> deck;

    public Player(String username, ArrayList<Card> deck) {
        this.username = username;
        this.deck = deck;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public void setDeck(ArrayList<Card> deck) {
        this.deck = deck;
    }
}

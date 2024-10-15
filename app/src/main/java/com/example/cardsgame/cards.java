package com.example.cardsgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class cards extends AppCompatActivity {

    private List<Card> deck;
    private List<Player> players;
    private Card lastPlayedCard;
    private int currentPlayerIndex = 0;
    private TextView currentPlayerLabel, scoreLabel;
    private ImageView currentCardImage, previousCardImage;
    private Button playCardButton, exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game);

        // Initialize the game deck, players, and hands
        initializeDeck();
        initializePlayers();
        dealCards();

        // Bind UI elements
        currentPlayerLabel = findViewById(R.id.currentPlayerLabel);
        currentCardImage = findViewById(R.id.currentCardImage);
        previousCardImage = findViewById(R.id.previousCardImage); // New ImageView for previous card
        playCardButton = findViewById(R.id.playButton);
        exitButton = findViewById(R.id.exitButton);
        scoreLabel = findViewById(R.id.scoreLabel);

        updateUI();

        // Set the play card button's functionality
        playCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard();
            }
        });

        // Set the exit button's functionality
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Exit the game
            }
        });
    }

    private void initializeDeck() {
        deck = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] values = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

        // Create the deck with all combinations of suits and values
        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card(suit, value));
            }
        }
        Collections.shuffle(deck);
    }

    private void initializePlayers() {
        players = new ArrayList<>();
        players.add(new Player("Player 1"));
        players.add(new Player("Player 2"));
    }

    private void dealCards() {
        int cardsPerPlayer = deck.size() / players.size();
        for (Player player : players) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                player.drawCard(deck.remove(0));
            }
        }
    }

    private void playCard() {
        Player currentPlayer = players.get(currentPlayerIndex);
        if (currentPlayer.getHandSize() == 0) {
            Toast.makeText(this, currentPlayer.getName() + " has no cards left!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get and remove the top card from the player's hand
        Card playedCard = currentPlayer.playCard();
        if (playedCard == null) {
            Toast.makeText(this, "No cards to play!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the played card matches the last played card
        if (lastPlayedCard != null && playedCard.getValue().equals(lastPlayedCard.getValue())) {
            Toast.makeText(this, currentPlayer.getName() + " won this round!", Toast.LENGTH_SHORT).show();
            currentPlayer.addPoint(); // Award a point for matching cards
        }

        // Update the previous card image before replacing the current card
        if (lastPlayedCard != null) {
            previousCardImage.setImageResource(getCardImage(lastPlayedCard));
        }

        lastPlayedCard = playedCard;
        currentCardImage.setImageResource(getCardImage(playedCard)); // Update the current card image

        // Switch to the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        updateUI();
    }

    private void updateUI() {
        // Update the current player label and scores
        currentPlayerLabel.setText("Current Player: " + players.get(currentPlayerIndex).getName());
        scoreLabel.setText("Scores:\nPlayer 1: " + players.get(0).getScore() + "\nPlayer 2: " + players.get(1).getScore());
    }

    private int getCardImage(Card card) {
        String imageName;

        // Check if the card is a face card (i.e., King, Queen, Jack, etc.)
        if (card.getValue().equalsIgnoreCase("king") ||
                card.getValue().equalsIgnoreCase("queen") ||
                card.getValue().equalsIgnoreCase("jack") ||
                card.getValue().equalsIgnoreCase("ace")) {

            // For face cards, use the format: "queen_of_spades"
            imageName = card.getValue().toLowerCase() + "_of_" + card.getSuit().toLowerCase();

        } else {
            // For number cards, use the format: "spades_of_9"
            imageName = card.getSuit().toLowerCase() + "_of_" + card.getValue().toLowerCase();
        }

        // Get the drawable resource ID based on the imageName
        return getResources().getIdentifier(imageName, "drawable", getPackageName());
    }
}

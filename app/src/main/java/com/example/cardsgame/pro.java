package com.example.cardsgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class pro extends AppCompatActivity {

    private List<Card> deck;
    private List<Player> players;
    private Card lastPlayedCard;
    private int currentPlayerIndex = 0;
    private TextView currentPlayerLabel, scoreLabel, historyLabel, player1DeckLabel, player2DeckLabel;
    private ImageView currentCardImage, previousCardImage;
    private Button playCardButton, exitButton;
    private LinearLayout nameInputLayout;
    private EditText player1NameInput, player2NameInput;
    private Button startGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pro);

        currentPlayerLabel = findViewById(R.id.currentPlayerLabel);
        currentCardImage = findViewById(R.id.currentCardImage);
        previousCardImage = findViewById(R.id.previousCardImage);
        playCardButton = findViewById(R.id.playButton);
        exitButton = findViewById(R.id.exitButton);
        scoreLabel = findViewById(R.id.scoreLabel);
        historyLabel = findViewById(R.id.historyLabel);
        player1DeckLabel = findViewById(R.id.player1DeckLabel);
        player2DeckLabel = findViewById(R.id.player2DeckLabel);
        nameInputLayout = findViewById(R.id.nameInputLayout);
        player1NameInput = findViewById(R.id.player1NameInput);
        player2NameInput = findViewById(R.id.player2NameInput);
        startGameButton = findViewById(R.id.startGameButton);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        playCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard();
            }
        });
    }

    private void startGame() {
        String player1Name = player1NameInput.getText().toString().trim();
        String player2Name = player2NameInput.getText().toString().trim();

        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            Toast.makeText(this, "Please enter both player names!", Toast.LENGTH_SHORT).show();
            return;
        }

        players = new ArrayList<>();
        players.add(new Player(player1Name));
        players.add(new Player(player2Name));

        nameInputLayout.setVisibility(View.GONE);
        initializeDeck();
        dealCards();

        updateUI();
        playCardButton.setEnabled(true);
    }

    private void initializeDeck() {
        deck = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] values = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card(suit, value));
            }
        }
        Collections.shuffle(deck);
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

        Card playedCard = currentPlayer.playCard();
        if (playedCard == null) {
            Toast.makeText(this, "No cards to play!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lastPlayedCard != null && playedCard.getValue().equals(lastPlayedCard.getValue())) {
            Toast.makeText(this, currentPlayer.getName() + " won this round!", Toast.LENGTH_SHORT).show();
            currentPlayer.addPoint();
        }

        if (lastPlayedCard != null) {
            previousCardImage.setImageResource(getCardImage(lastPlayedCard));
        }

        lastPlayedCard = playedCard;
        currentCardImage.setImageResource(getCardImage(playedCard));

        String history = currentPlayer.getName() + " played " + playedCard.getValue() + " of " + playedCard.getSuit();
        historyLabel.append(history + "\n");

        player1DeckLabel.setText("Player 1 Deck: " + players.get(0).getHandSize() + " cards left");
        player2DeckLabel.setText("Player 2 Deck: " + players.get(1).getHandSize() + " cards left");

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        updateUI();
    }

    private void updateUI() {
        currentPlayerLabel.setText("Current Player: " + players.get(currentPlayerIndex).getName());
        scoreLabel.setText("Scores:\n" + players.get(0).getName() + ": " + players.get(0).getScore() +
                "\n" + players.get(1).getName() + ": " + players.get(1).getScore());

        player1DeckLabel.setText("Player 1 Deck: " + players.get(0).getHandSize() + " cards left");
        player2DeckLabel.setText("Player 2 Deck: " + players.get(1).getHandSize() + " cards left");
    }

    private int getCardImage(Card card) {
        String imageName;

        if (card.getValue().equalsIgnoreCase("king") ||
                card.getValue().equalsIgnoreCase("queen") ||
                card.getValue().equalsIgnoreCase("jack") ||
                card.getValue().equalsIgnoreCase("ace")) {

            imageName = card.getValue().toLowerCase() + "_of_" + card.getSuit().toLowerCase();

        } else {
            imageName = card.getSuit().toLowerCase() + "_of_" + card.getValue().toLowerCase();
        }

        return getResources().getIdentifier(imageName, "drawable", getPackageName());
    }
}

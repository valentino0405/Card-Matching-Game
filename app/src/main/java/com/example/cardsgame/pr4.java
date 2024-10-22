package com.example.cardsgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class pr4 extends AppCompatActivity {

    private List<Card2> deck;
    private List<Play> players;
    private Card2 lastPlayedCard;
    private int currentPlayerIndex = 0;
    private TextView currentPlayerLabel, scoreLabel, historyLabel, player1DeckLabel, player2DeckLabel;
    private ImageView currentCardImage, previousCardImage;
    private Button playCardButton, exitButton;
    private LinearLayout nameInputLayout;
    private EditText player1NameInput, player2NameInput;
    private Button startGameButton;
    private GridLayout player1DeckGrid, player2DeckGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pr4);

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
        player1DeckGrid = findViewById(R.id.player1DeckGrid);
        player2DeckGrid = findViewById(R.id.player2DeckGrid);

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        playCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
        players.add(new Play(player1Name));
        players.add(new Play(player2Name));

        nameInputLayout.setVisibility(View.GONE);
        initializeDeck();
        dealCards();

        displayPlayerDecks();
        updateUI();
        playCardButton.setEnabled(true);
    }

    private void initializeDeck() {
        deck = new ArrayList<>();
        String[] suits = {"Hearts", "Diamonds", "Clubs", "Spades"};
        String[] values = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

        for (String suit : suits) {
            for (String value : values) {
                deck.add(new Card2(suit, value));
            }
        }
        Collections.shuffle(deck);
    }

    private void dealCards() {
        int cardsPerPlayer = deck.size() / players.size();
        for (Play player : players) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                player.addCard(deck.remove(0));
            }
        }
    }

    private void playCard() {
        Play currentPlayer = players.get(currentPlayerIndex);
        if (currentPlayer.getHandSize() == 0) {
            Toast.makeText(this, currentPlayer.getName() + " has no more cards!", Toast.LENGTH_SHORT).show();
            return;
        }

        Card2 playedCard = currentPlayer.playCard();
        lastPlayedCard = playedCard;
        updateUI();

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    private void updateUI() {
        Play currentPlayer = players.get(currentPlayerIndex);
        currentPlayerLabel.setText(currentPlayer.getName() + "'s Turn");

        if (lastPlayedCard != null) {
            String cardText = lastPlayedCard.getValue() + " of " + lastPlayedCard.getSuit();
            historyLabel.setText("Last played: " + cardText);
        }

        displayPlayerDecks();
    }

    private void displayPlayerDecks() {
        Play player1 = players.get(0);
        Play player2 = players.get(1);

        player1DeckLabel.setText(player1.getName() + "'s Cards: " + player1.getHandSize());
        player2DeckLabel.setText(player2.getName() + "'s Cards: " + player2.getHandSize());
    }
}

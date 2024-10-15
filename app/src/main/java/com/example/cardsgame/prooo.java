package com.example.cardsgame; // Adjust the package name as necessary

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class prooo extends AppCompatActivity {

    private EditText player1NameInput, player2NameInput;
    private Button startGameButton, playButton, exitButton;
    private TextView currentPlayerLabel, scoreLabel, historyLabel, player1DeckLabel, player2DeckLabel;
    private ImageView currentCardImage, previousCardImage;

    private String player1Name, player2Name;
    private int player1Score = 0, player2Score = 0;
    private StringBuilder gameHistory = new StringBuilder();
    private boolean isPlayer1Turn = true; // Track whose turn it is

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prooo); // Ensure this matches your layout filename

        // Initialize UI components
        player1NameInput = findViewById(R.id.player1NameInput);
        player2NameInput = findViewById(R.id.player2NameInput);
        startGameButton = findViewById(R.id.startGameButton);
        playButton = findViewById(R.id.playButton);
        exitButton = findViewById(R.id.exitButton);
        currentPlayerLabel = findViewById(R.id.currentPlayerLabel);
        scoreLabel = findViewById(R.id.scoreLabel);
        historyLabel = findViewById(R.id.historyLabel);
        player1DeckLabel = findViewById(R.id.player1DeckLabel);
        player2DeckLabel = findViewById(R.id.player2DeckLabel);
        currentCardImage = findViewById(R.id.currentCardImage);
        previousCardImage = findViewById(R.id.previousCardImage);

        // Start Game Button click listener
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGame();
            }
        });

        // Play Card Button click listener
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playCard();
            }
        });

        // Exit Game Button click listener
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Close the app
            }
        });
    }

    private void startGame() {
        player1Name = player1NameInput.getText().toString().trim();
        player2Name = player2NameInput.getText().toString().trim();

        if (player1Name.isEmpty() || player2Name.isEmpty()) {
            Toast.makeText(this, "Please enter both player names.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Enable the Play Card button
        playButton.setEnabled(true);
        currentPlayerLabel.setText("Current Player: " + player1Name);
        updateDeckLabels();
    }

    private void playCard() {
        // Logic for playing a card (random card generation example)
        int cardValue = (int) (Math.random() * 10 + 1); // Random card value between 1 and 10

        if (isPlayer1Turn) {
            player1Score += cardValue;
            gameHistory.append(player1Name).append(" played a card worth ").append(cardValue).append("\n");
            isPlayer1Turn = false;
            currentPlayerLabel.setText("Current Player: " + player2Name);
        } else {
            player2Score += cardValue;
            gameHistory.append(player2Name).append(" played a card worth ").append(cardValue).append("\n");
            isPlayer1Turn = true;
            currentPlayerLabel.setText("Current Player: " + player1Name);
        }

        // Update UI components
        scoreLabel.setText("Scores: " + player1Name + " - " + player1Score + " | " + player2Name + " - " + player2Score);
        historyLabel.setText(gameHistory.toString());
        updateDeckLabels();

        // Logic to update the card images can be added here (currently omitted)
    }

    private void updateDeckLabels() {
        // Placeholder for deck sizes; adjust according to your game logic
        player1DeckLabel.setText(player1Name + " Deck: " + (10 - player1Score) + " cards left");
        player2DeckLabel.setText(player2Name + " Deck: " + (10 - player2Score) + " cards left");
    }
}

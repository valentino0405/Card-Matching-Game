package com.example.cardsgame;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class newmega extends AppCompatActivity {

    private List<Card> deck;
    private List<Player> players;
    private Card lastPlayedCard;
    private int currentPlayerIndex = 0;
    private TextView player1DeckLabel, player2DeckLabel, player1NameText, player2NameText, player1ScoreText, player2ScoreText;
    private ImageButton player1CardsButton, player2CardsButton, middleDeckButton, historyButton, leaderboardButton;
    private EditText player1NameInput, player2NameInput;
    private Button startGameButton;
    private StringBuilder gameHistory; // StringBuilder to store game history

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerinput);

        // Bind UI elements from playerinput.xml
        player1NameInput = findViewById(R.id.p1); // Player 1 input field
        player2NameInput = findViewById(R.id.p2); // Player 2 input field
        startGameButton = findViewById(R.id.button); // Start button

        // Initialize the game history StringBuilder
        gameHistory = new StringBuilder();

        // Set the start button's functionality to transition to themega.xml
        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
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

        // Transition to themega.xml layout
        setContentView(R.layout.themega);

        // Bind UI elements from themega.xml
        player1NameText = findViewById(R.id.textView3); // Player 1 textView
        player2NameText = findViewById(R.id.textView4); // Player 2 textView
        player1CardsButton = findViewById(R.id.Player1cards); // Player 1 deck ImageButton
        player2CardsButton = findViewById(R.id.Player2cards); // Player 2 deck ImageButton
        middleDeckButton = findViewById(R.id.middledeck); // Middle deck for cards played
        historyButton = findViewById(R.id.history); // History ImageButton
        leaderboardButton = findViewById(R.id.imageButton5); // Leaderboard ImageButton

        // Score TextViews
        player1ScoreText = findViewById(R.id.player1Score); // Player 1 score TextView
        player2ScoreText = findViewById(R.id.player2Score); // Player 2 score TextView

        // Initialize player names
        players = new ArrayList<>();
        players.add(new Player(player1Name));
        players.add(new Player(player2Name));

        // Display player names on the themega.xml layout
        player1NameText.setText(player1Name);
        player2NameText.setText(player2Name);

        // Initialize the deck and deal cards
        initializeDeck();
        dealCards();

        // Setup card click listeners and update UI
        updateUI();

        // Set click listeners for player card buttons
        player1CardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard(0); // Call playCard for Player 1
            }
        });

        player2CardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard(1); // Call playCard for Player 2
            }
        });

        // Set the history button's functionality
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistory();
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

    private void dealCards() {
        int cardsPerPlayer = deck.size() / players.size();
        for (Player player : players) {
            for (int i = 0; i < cardsPerPlayer; i++) {
                player.drawCard(deck.remove(0));
            }
        }
    }

    private void playCard(int playerIndex) {
        Player currentPlayer = players.get(playerIndex);
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
        if (lastPlayedCard != null && (playedCard.getValue().equals(lastPlayedCard.getValue()) ||
                playedCard.getSuit().equals(lastPlayedCard.getSuit()))) {
            Toast.makeText(this, currentPlayer.getName() + " won this round!", Toast.LENGTH_SHORT).show();
            currentPlayer.addPoint(); // Award a point for matching cards
            updateScoreUI(); // Update score display
        }

        // Update middle deck ImageButton
        lastPlayedCard = playedCard;
        middleDeckButton.setImageResource(getCardImage(playedCard)); // Display card in middle deck

        // Update game history
        String historyEntry = currentPlayer.getName() + " played " + playedCard.getValue() + " of " + playedCard.getSuit();
        gameHistory.append(historyEntry).append("\n"); // Append to the game history
        Toast.makeText(this, historyEntry, Toast.LENGTH_SHORT).show();// Optional: Show the move as a toast

        // Update deck display
        player1CardsButton.setImageResource(R.drawable.card_back); // Update Player 1's card back
        player2CardsButton.setImageResource(R.drawable.card_back); // Update Player 2's card back

        // Switch to the next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        updateUI();
    }

    private void updateScoreUI() {
        player1ScoreText.setText("Score: " + players.get(0).getScore());
        player2ScoreText.setText("Score: " + players.get(1).getScore());
    }

    private void updateUI() {
        // Update deck and scores
        player1CardsButton.setContentDescription("Player 1 Deck: " + players.get(0).getHandSize() + " cards left");
        player2CardsButton.setContentDescription("Player 2 Deck: " + players.get(1).getHandSize() + " cards left");
    }

    // Method to get the image resource for a specific card
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

    // Method to display the game history
    private void showHistory() {
        // Transition to history.xml layout
        setContentView(R.layout.history);

        // Bind the history TextView in history.xml
        TextView historyTextView = findViewById(R.id.historydis);

        // Set the game history text
        historyTextView.setText(gameHistory.toString());

        // Set up the back button
        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToGameLayout(); // Go back to the game layout
            }
        });
    }

    // Method to return to the game layout and restore state
    private void returnToGameLayout() {
        // Switch back to the game layout (themega.xml)
        setContentView(R.layout.themega);

        // Bind the game UI elements again (text views, buttons, etc.)
        bindGameUIElements();

        // Restore player names and scores
        player1NameText.setText(players.get(0).getName());
        player2NameText.setText(players.get(1).getName());
        updateScoreUI();

        // Restore the last played card in the middle deck, if any
        if (lastPlayedCard != null) {
            middleDeckButton.setImageResource(getCardImage(lastPlayedCard));
        }

        // Restore card back images for player decks
        player1CardsButton.setImageResource(R.drawable.card_back);
        player2CardsButton.setImageResource(R.drawable.card_back);

        // Set click listeners again for card buttons
        player1CardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard(0); // Call playCard for Player 1
            }
        });

        player2CardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard(1); // Call playCard for Player 2
            }
        });

        // Set the history button's functionality again
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistory(); // Show the history view
            }
        });

        // Set click listener for leaderboard button (if needed)
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show leaderboard (implementation depends on your app)
                Toast.makeText(newmega.this, "Leaderboard clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Helper method to bind UI elements for the game layout
    private void bindGameUIElements() {
        player1NameText = findViewById(R.id.textView3);
        player2NameText = findViewById(R.id.textView4);
        player1CardsButton = findViewById(R.id.Player1cards);
        player2CardsButton = findViewById(R.id.Player2cards);
        middleDeckButton = findViewById(R.id.middledeck);
        historyButton = findViewById(R.id.history);
        leaderboardButton = findViewById(R.id.imageButton5);
        player1ScoreText = findViewById(R.id.player1Score);
        player2ScoreText = findViewById(R.id.player2Score);
    }
}

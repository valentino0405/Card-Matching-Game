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

public class mega3 extends AppCompatActivity {

    private List<Card> deck;
    private List<Player> players;
    private Card lastPlayedCard;
    private int currentPlayerIndex = 0;
    private TextView player1DeckLabel, player2DeckLabel, player1NameText, player2NameText, player1ScoreText, player2ScoreText;
    private ImageButton player1CardsButton, player2CardsButton, middleDeckButton, historyButton, leaderboardButton;
    private EditText player1NameInput, player2NameInput;
    private Button startGameButton;
    private StringBuilder gameHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.playerinput);

        player1NameInput = findViewById(R.id.p1);
        player2NameInput = findViewById(R.id.p2);
        startGameButton = findViewById(R.id.button);
        gameHistory = new StringBuilder();

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

        setContentView(R.layout.themega);
        player1NameText = findViewById(R.id.textView3);
        player2NameText = findViewById(R.id.textView4);
        player1CardsButton = findViewById(R.id.Player1cards);
        player2CardsButton = findViewById(R.id.Player2cards);
        middleDeckButton = findViewById(R.id.middledeck);
        historyButton = findViewById(R.id.history);
        leaderboardButton = findViewById(R.id.imageButton5);
        player1ScoreText = findViewById(R.id.player1Score);
        player2ScoreText = findViewById(R.id.player2Score);

        players = new ArrayList<>();
        players.add(new Player(player1Name));
        players.add(new Player(player2Name));

        player1NameText.setText(player1Name);
        player2NameText.setText(player2Name);
        initializeDeck();
        dealCards();
        updateUI();

        player1CardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard(0);
            }
        });

        player2CardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard(1);
            }
        });

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

        if (playerIndex != currentPlayerIndex) {
            Toast.makeText(this, "It's not " + currentPlayer.getName() + "'s turn!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (currentPlayer.getHandSize() == 0) {
            Toast.makeText(this, currentPlayer.getName() + " has no cards left!", Toast.LENGTH_SHORT).show();
            return;
        }

        Card playedCard = currentPlayer.playCard();
        if (playedCard == null) {
            Toast.makeText(this, "No cards to play!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (lastPlayedCard != null && (playedCard.getValue().equals(lastPlayedCard.getValue()) ||
                playedCard.getSuit().equals(lastPlayedCard.getSuit()))) {
            Toast.makeText(this, currentPlayer.getName() + " won this round!", Toast.LENGTH_SHORT).show();
            currentPlayer.addPoint();
            updateScoreUI();
        }

        lastPlayedCard = playedCard;
        middleDeckButton.setImageResource(getCardImage(playedCard));
        String historyEntry = currentPlayer.getName() + " played " + playedCard.getValue() + " of " + playedCard.getSuit();
        gameHistory.append(historyEntry).append("\n");
        Toast.makeText(this, historyEntry, Toast.LENGTH_SHORT).show();

        if (playerIndex == 0) {
            player1CardsButton.setEnabled(false);
            player2CardsButton.setEnabled(true);
        } else {
            player1CardsButton.setEnabled(true);
            player2CardsButton.setEnabled(false);
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        updateUI();
    }

    private void updateScoreUI() {
        player1ScoreText.setText("Score: " + players.get(0).getScore());
        player2ScoreText.setText("Score: " + players.get(1).getScore());
    }

    private void updateUI() {
        player1CardsButton.setContentDescription("Player 1 Deck: " + players.get(0).getHandSize() + " cards left");
        player2CardsButton.setContentDescription("Player 2 Deck: " + players.get(1).getHandSize() + " cards left");
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

    private void showHistory() {
        setContentView(R.layout.history);
        TextView historyTextView = findViewById(R.id.historydis);
        historyTextView.setText(gameHistory.toString());

        ImageButton backButton = findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnToGameLayout();
            }
        });
    }

    private void returnToGameLayout() {
        setContentView(R.layout.themega);
        bindGameUIElements();
        player1NameText.setText(players.get(0).getName());
        player2NameText.setText(players.get(1).getName());
        updateScoreUI();

        if (lastPlayedCard != null) {
            middleDeckButton.setImageResource(getCardImage(lastPlayedCard));
        }

        player1CardsButton.setImageResource(R.drawable.card_back);
        player2CardsButton.setImageResource(R.drawable.card_back);

        player1CardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard(0);
            }
        });

        player2CardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playCard(1);
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHistory();
            }
        });

        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mega3.this, "Leaderboard clicked!", Toast.LENGTH_SHORT).show();
            }
        });
    }

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

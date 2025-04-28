package com.ddiop.tennis.application.impl;

import com.ddiop.tennis.domain.PlayerId;
import com.ddiop.tennis.domain.TennisGame;
import com.ddiop.tennis.domain.event.PointWon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

class TennisGameServiceTest {

  private TennisGame game;

  @BeforeEach
  void setUp() {
    game = new TennisGame();
  }

  @Test
  @DisplayName("Player A should win the game after 4 consecutive points")
  void testPlayerAWins() {
    // Player A wins the game
    game.pointWonBy(PlayerId.A);
    game.pointWonBy(PlayerId.A);
    game.pointWonBy(PlayerId.A);
    game.pointWonBy(PlayerId.A);

    assertThat(game.displayScore()).isEqualTo("Player A wins the game");
  }

  @Test
  @DisplayName("Player B should win the game after 4 consecutive points")
  void testPlayerBWins() {
    // Player B wins the game
    game.pointWonBy(PlayerId.B);
    game.pointWonBy(PlayerId.B);
    game.pointWonBy(PlayerId.B);
    game.pointWonBy(PlayerId.B);

    assertThat(game.displayScore()).isEqualTo("Player B wins the game");
  }

  @Test
  @DisplayName("The game should reach Deuce when both players have 40 points")
  void testDeuce() {
    // Both players reach deuce
    game.pointWonBy(PlayerId.A);
    game.pointWonBy(PlayerId.B);
    game.pointWonBy(PlayerId.A);
    game.pointWonBy(PlayerId.B);
    game.pointWonBy(PlayerId.A);
    game.pointWonBy(PlayerId.B);

    assertThat(game.displayScore()).isEqualTo("Deuce");
  }

  @Test
  @DisplayName("Player A should gain advantage and win the game")
  void testAdvantage() {
    // Player A and Player B reach deuce (40-40)
    game.pointWonBy(PlayerId.A); // 15-0
    game.pointWonBy(PlayerId.A); // 30-0
    game.pointWonBy(PlayerId.A); // 40-0
    game.pointWonBy(PlayerId.B); // 40-15
    game.pointWonBy(PlayerId.B); // 40-30
    game.pointWonBy(PlayerId.B); // 40-40 (Deuce)

    // Player A gains advantage
    game.pointWonBy(PlayerId.A);
    assertThat(game.displayScore()).isEqualTo("Advantage A");

    // Player A wins the game
    game.pointWonBy(PlayerId.A);
    assertThat(game.displayScore()).isEqualTo("Player A wins the game");
  }

  @Test
  @DisplayName("The game should return to Deuce after Player A gains advantage")
  void testBackToDeuce() {
    // Player A has advantage, but Player B wins the point and back to deuce
    game.pointWonBy(PlayerId.A);
    game.pointWonBy(PlayerId.B);
    game.pointWonBy(PlayerId.A);
    game.pointWonBy(PlayerId.B);
    game.pointWonBy(PlayerId.A);
    game.pointWonBy(PlayerId.B);

    assertThat(game.displayScore()).isEqualTo("Deuce");
  }

  @Test
  @DisplayName("The complete match should follow deuce and advantage phases")
  void testCompleteMatch() {
    // Test full sequence
    game.pointWonBy(PlayerId.A); // A: 15
    game.pointWonBy(PlayerId.B); // B: 15
    game.pointWonBy(PlayerId.A); // A: 30
    game.pointWonBy(PlayerId.B); // B: 30
    game.pointWonBy(PlayerId.A); // A: 40
    game.pointWonBy(PlayerId.B); // B: 40

    assertThat(game.displayScore()).isEqualTo("Deuce");

    game.pointWonBy(PlayerId.A); // Advantage A
    assertThat(game.displayScore()).isEqualTo("Advantage A");

    game.pointWonBy(PlayerId.A); // A wins the game
    assertThat(game.displayScore()).isEqualTo("Player A wins the game");
  }

  @Test
  @DisplayName("The sequence ABABAA should result in Player A winning the game")
  void testSequence_ABABAA() {
    // A marque
    game.pointWonBy(PlayerId.A);
    assertThat(game.displayScore()).isEqualTo("Player A : 15 / Player B : 0");

    // B marque
    game.pointWonBy(PlayerId.B);
    assertThat(game.displayScore()).isEqualTo("Player A : 15 / Player B : 15");

    // A marque
    game.pointWonBy(PlayerId.A);
    assertThat(game.displayScore()).isEqualTo("Player A : 30 / Player B : 15");

    // B marque
    game.pointWonBy(PlayerId.B);
    assertThat(game.displayScore()).isEqualTo("Player A : 30 / Player B : 30");

    // A marque
    game.pointWonBy(PlayerId.A);
    assertThat(game.displayScore()).isEqualTo("Player A : 40 / Player B : 30");

    // A marque
    game.pointWonBy(PlayerId.A);
    assertThat(game.displayScore()).isEqualTo("Player A wins the game");
  }

  @Test
  @DisplayName("The game events should include PointWon when points are won")
  void testGameEvents() {
    game.pointWonBy(PlayerId.A);
    game.pointWonBy(PlayerId.B);

    var events = game.popEvents();
    assertThat(events).anyMatch(e -> e instanceof PointWon);
    assertThat(events).anyMatch(e -> e instanceof PointWon);
  }
}

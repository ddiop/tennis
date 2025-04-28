package com.ddiop.tennis.domain;


import com.ddiop.tennis.domain.event.DomainEvent;
import com.ddiop.tennis.domain.event.GameWon;
import com.ddiop.tennis.domain.event.PointWon;


import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TennisGame {

  private final Score scoreA = new Score();
  private final Score scoreB = new Score();
  private PlayerId advantage = null;
  private GameState state = GameState.IN_PROGRESS;
  private final List<DomainEvent> events = new ArrayList<>();

  public void pointWonBy(PlayerId player) {
    if (state == GameState.FINISHED) {
      return;
    }

    PlayerId opponent = (player == PlayerId.A) ? PlayerId.B : PlayerId.A;
    Score playerScore = getScore(player);
    Score opponentScore = getScore(opponent);

    if (playerScore.getValue().getValue() < 40) {
      playerScore.increment();
    } else if (playerScore.isForty()) {
      if (opponentScore.getValue().getValue() < 40) {
        playerScore.win();
        state = GameState.FINISHED;
        events.add(new GameWon(player));
      } else if (state == GameState.DEUCE) {
        advantage = player;
        state = GameState.ADVANTAGE;
      } else if (state == GameState.ADVANTAGE && advantage == player) {
        playerScore.win();
        state = GameState.FINISHED;
        events.add(new GameWon(player));
      } else if (state == GameState.ADVANTAGE && advantage != player) {
        advantage = null;
        state = GameState.DEUCE;
      }
    }

    if (scoreA.getValue() == ScoreValue.FORTY && scoreB.getValue() == ScoreValue.FORTY
        && state != GameState.ADVANTAGE) {
      state = GameState.DEUCE;
    }

    // Traitement d'événements ,Vidage de la liste des événements , Utilisation du domaine événementiel
    events.add(new PointWon(player));
    if (state == GameState.FINISHED) {
      List<DomainEvent> gameEvents = popEvents();
      log.info(gameEvents.toString());
    }
  }

  private Score getScore(PlayerId player) {
    return (player == PlayerId.A) ? scoreA : scoreB;
  }

  public String displayScore() {
    if (state == GameState.DEUCE) {
      return "Deuce";
    }
    if (state == GameState.ADVANTAGE) {
      return "Advantage " + advantage;
    }
    if (scoreA.hasWon()) {
      return "Player A wins the game";
    }
    if (scoreB.hasWon()) {
      return "Player B wins the game";
    }

    return String.format("Player A : %s / Player B : %s",
        scoreA.getValue().getValue(),
        scoreB.getValue().getValue());
  }

  public List<DomainEvent> popEvents() {
    List<DomainEvent> copy = new ArrayList<>(events);
    events.clear();
    return copy;
  }
}

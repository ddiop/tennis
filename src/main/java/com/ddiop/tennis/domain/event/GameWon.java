package com.ddiop.tennis.domain.event;

import com.ddiop.tennis.domain.PlayerId;

public record GameWon(PlayerId winner) implements DomainEvent {

  @Override
  public String description() {
    return "Game won by player " + winner;
  }
}
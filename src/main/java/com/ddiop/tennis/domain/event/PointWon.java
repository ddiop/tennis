package com.ddiop.tennis.domain.event;

import com.ddiop.tennis.domain.PlayerId;

public record PointWon(PlayerId player) implements DomainEvent {
  @Override
  public String description() {
    return "Point won by player " + player;
  }
}
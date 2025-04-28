package com.ddiop.tennis.application.impl;

import com.ddiop.tennis.application.TennisGameService;
import com.ddiop.tennis.domain.PlayerId;
import com.ddiop.tennis.domain.TennisGame;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class TennisGameServiceImpl implements TennisGameService {

  private final TennisGame game = new TennisGame();

  @Override
  public void play(String sequence) {
    sequence.chars()
        .mapToObj(c -> (c == 'A') ? PlayerId.A : PlayerId.B)
        .forEach(player -> {
          game.pointWonBy(player);
         log.info(game.displayScore());
        });
  }
}

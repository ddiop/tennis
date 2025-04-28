package com.ddiop.tennis.domain;

import java.util.concurrent.atomic.AtomicReference;

public class Score {

  private final AtomicReference<ScoreValue> value = new AtomicReference<>(ScoreValue.LOVE);

  public ScoreValue getValue() {
    return value.get();
  }

  public void increment() {
    value.updateAndGet(ScoreValue::next);
  }

  public void win() {
    value.set(ScoreValue.WON);
  }

  public boolean isForty() {
    return value.get() == ScoreValue.FORTY;
  }

  public boolean hasWon() {
    return value.get() == ScoreValue.WON;
  }
}
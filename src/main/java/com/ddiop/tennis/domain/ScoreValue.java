package com.ddiop.tennis.domain;


public enum ScoreValue {
  LOVE(0), FIFTEEN(15), THIRTY(30), FORTY(40), WON(100);

  private final int value;

  ScoreValue(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  public ScoreValue next() {
    return switch (this) {
      case LOVE -> FIFTEEN;
      case FIFTEEN -> THIRTY;
      case THIRTY -> FORTY;
      default -> this;
    };
  }
}

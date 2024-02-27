package main.pattern.enam;

public enum TypeSignalArrow {

  ASK(200),
  BID(201),
  NO_PATTERN(404),
  ERROR(400);

  private final int responseCode;

  TypeSignalArrow(int responseCode) {
    this.responseCode = responseCode;
  }

  public int getResponseCode() {
    return responseCode;
  }

}

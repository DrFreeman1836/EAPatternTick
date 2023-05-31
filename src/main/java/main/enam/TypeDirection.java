package main.enam;

public enum TypeDirection {

  UP(200),//цена пошла вверх
  DOWN(201),// цена пошла вниз
  NO_PATTERN(404),
  ERROR(400);

  private final int responseCode;

  TypeDirection(int responseCode) {
    this.responseCode = responseCode;
  }

  public int getResponseCode() {
    return responseCode;
  }

}

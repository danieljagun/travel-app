package com.travel.app;

public class TripNotFoundException extends RuntimeException {

  public TripNotFoundException(String tripID) {
    super("Trip not found with id: " + tripID);
  }
}

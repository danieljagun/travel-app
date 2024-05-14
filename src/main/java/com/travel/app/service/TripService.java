package com.travel.app.service;

import com.travel.app.Trip;

public interface TripService {

  String createTrip(Trip trip);

  Trip getTrip(String tripID);

  Trip updateTrip(Trip updatedTrip);

  void deleteTrip(String tripID);
}

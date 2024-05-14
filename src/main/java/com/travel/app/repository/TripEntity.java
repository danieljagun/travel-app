package com.travel.app.repository;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("trips")
public class TripEntity {

  @Id
  private final String tripId;

  private final String owner;

  private final String origin;

  private final String destination;

  private final LocalDateTime tripDate;

  public TripEntity(String tripId, String owner, String origin, String destination, LocalDateTime tripDate) {
    this.tripId = tripId;
    this.owner = owner;
    this.origin = origin;
    this.destination = destination;
    this.tripDate = tripDate;
  }

  public String getTripId() {
    return tripId;
  }

  public String getOwner() {
    return owner;
  }

  public String getOrigin() {
    return origin;
  }

  public String getDestination() {
    return destination;
  }

  public LocalDateTime getTripDate() {
    return tripDate;
  }

  @Override
  public String toString() {
    return "TripEntity{" +
      "tripId='" + tripId + '\'' +
      ", owner='" + owner + '\'' +
      ", origin='" + origin + '\'' +
      ", destination='" + destination + '\'' +
      ", tripDate=" + tripDate +
      '}';
  }
}


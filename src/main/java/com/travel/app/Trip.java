package com.travel.app;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;

public class Trip {

  private final String tripID;

  private final String owner;

  private final String origin;

  private final String destination;

  private final LocalDateTime tripDate;

  @JsonCreator
  public Trip(@JsonProperty("tripID") String tripID,
              @JsonProperty("owner") String owner,
              @JsonProperty("origin") String origin,
              @JsonProperty("destination") String destination,
              @JsonProperty("tripDate") LocalDateTime tripDate) {
    this.tripID = tripID;
    this.owner = owner;
    this.origin = origin;
    this.destination = destination;
    this.tripDate = tripDate;
  }

  public String getTripID() {
    return tripID;
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
    return "Trip{" +
      "tripID='" + tripID + '\'' +
      ", owner='" + owner + '\'' +
      ", origin='" + origin + '\'' +
      ", destination='" + destination + '\'' +
      ", tripDate=" + tripDate +
      '}';
  }
}

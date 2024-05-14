package com.travel.app.service;

import com.travel.app.Trip;
import com.travel.app.TripNotFoundException;
import com.travel.app.repository.TripEntity;
import com.travel.app.repository.TripRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TripServiceImpl implements TripService {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TripRepository tripRepository;

  public TripServiceImpl(TripRepository tripRepository) {
    this.tripRepository = tripRepository;
  }

  public String createTrip(Trip trip) {
    String tripId = UUID.randomUUID().toString();
    TripEntity newTrip = new TripEntity(tripId, trip.getOwner(), trip.getOrigin(), trip.getDestination(), trip.getTripDate());
    TripEntity savedTripEntity = tripRepository.save(newTrip);
    logger.info("Created new trip with ID: {}", savedTripEntity.getTripId());
    return savedTripEntity.getTripId();
  }

  public Trip getTrip(String tripID) {
    Optional<TripEntity> tripEntity = tripRepository.findById(tripID);
    if (tripEntity.isPresent()) {
      TripEntity savedTrip = tripEntity.get();
      logger.info("Retrieved trip with ID: {}", savedTrip.getTripId());
      return new Trip(savedTrip.getTripId(), savedTrip.getOwner(), savedTrip.getOrigin(), savedTrip.getDestination(), savedTrip.getTripDate());
    }
    throw new TripNotFoundException("Trip not found with ID: " + tripID);
  }

  public Trip updateTrip(Trip updatedTrip) {
    Optional<TripEntity> tripEntity = tripRepository.findById(updatedTrip.getTripID());
    if (tripEntity.isPresent()) {
      TripEntity savedTrip = tripEntity.get();
      TripEntity updatedTripEntity = new TripEntity(
        savedTrip.getTripId(),
        updatedTrip.getOwner(),
        updatedTrip.getOrigin(),
        updatedTrip.getDestination(),
        updatedTrip.getTripDate()
      );
      TripEntity savedTripEntity = tripRepository.save(updatedTripEntity);
      logger.info("Updated trip with ID: {} ", savedTripEntity.getTripId());
      return new Trip(savedTripEntity.getTripId(), savedTripEntity.getOwner(), savedTripEntity.getOrigin(), savedTripEntity.getDestination(), savedTripEntity.getTripDate());
    }
    throw new TripNotFoundException("Trip not found with ID: " + updatedTrip.getTripID());
  }

  public void deleteTrip(String tripID) {
    Optional<TripEntity> tripEntity = tripRepository.findById(tripID);
    if (tripEntity.isPresent()) {
      tripRepository.deleteById(tripID);
      logger.info("Deleted trip with ID: {} ", tripID);
    } else {
      throw new TripNotFoundException("Trip not found with ID: " + tripID);
    }
  }
}


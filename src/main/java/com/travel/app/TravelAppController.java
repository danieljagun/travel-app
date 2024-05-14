package com.travel.app;

import com.travel.app.service.TripService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/trips")
public class TravelAppController {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final TripService tripService;

  public TravelAppController(TripService tripService) {
    this.tripService = tripService;
  }

  @GetMapping("/{tripID}")
  public Trip getTrip(@PathVariable String tripID) {
    logger.info("Received request to get Trip, tripId {}", tripID);
    return tripService.getTrip(tripID);
  }

  @PostMapping
  public String createTrip(@RequestBody Trip trip) {
    logger.info("Received request to create Trip {} ", trip);
    return tripService.createTrip(trip);
  }

  @PutMapping("/update/{tripID}")
  public Trip updateTrip(@RequestBody Trip updatedTrip) {
    logger.info("Received request to update Trip, tripId {}", updatedTrip.getTripID());
    return tripService.updateTrip(updatedTrip);
  }

  @DeleteMapping("/delete/{tripID}")
  public void deleteTrip(@PathVariable String tripID) {
    logger.info("Received request to delete Trip, tripId {}", tripID);
    tripService.deleteTrip(tripID);
  }

  @ExceptionHandler(TripNotFoundException.class)
  @ResponseStatus(value = HttpStatus.NOT_FOUND)
  public String handleTripNotFoundException(TripNotFoundException exception) {
    return exception.getMessage();
  }
}

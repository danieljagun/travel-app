package com.travel.app.service;

import com.travel.app.Trip;
import com.travel.app.repository.TripEntity;
import com.travel.app.repository.TripRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TripServiceImplUnitTest {

  @Mock
  private TripRepository tripRepository;

  @InjectMocks
  private TripServiceImpl tripService;

  @Test
  @DisplayName("Should Create Trip")
  void shouldCreateTrip() {
    //given
    String tripID = UUID.randomUUID().toString();
    LocalDateTime tripDate = LocalDateTime.of(2023, 4, 3, 12, 0);
    Trip trip = new Trip(tripID, "Daniel", "Dublin", "Italy", tripDate);
    when(tripRepository.save(any(TripEntity.class))).thenReturn(new TripEntity(tripID, "Daniel", "Dublin", "Italy", tripDate));

    //when
    String createdTripID = tripService.createTrip(trip);

    //then
    assertEquals(tripID, createdTripID);

    verify(tripRepository).save(any(TripEntity.class));
  }

  @Test
  @DisplayName("Should Get Trip")
  void shouldGetTrip() {
    //given
    String tripID = "1111";
    LocalDateTime tripDate = LocalDateTime.of(2023, 4, 3, 12, 0);
    TripEntity trip = new TripEntity(tripID, "Daniel", "Dublin", "Italy", tripDate);
    when(tripRepository.findById(tripID)).thenReturn(Optional.of(trip));

    Trip expectedTrip = new Trip(tripID, "Daniel", "Dublin", "Italy", tripDate);

    //when
    Trip retrievedTrip = tripService.getTrip(tripID);

    //then
    assertEquals(expectedTrip.getTripID(), retrievedTrip.getTripID());
    assertEquals(expectedTrip.getOwner(), retrievedTrip.getOwner());
    assertEquals(expectedTrip.getOrigin(), retrievedTrip.getOrigin());
    assertEquals(expectedTrip.getDestination(), retrievedTrip.getDestination());
    assertEquals(expectedTrip.getTripDate(), retrievedTrip.getTripDate());

    verify(tripRepository).findById(tripID);
  }

  @Test
  @DisplayName("Should Update Trip")
  void shouldUpdateTrip() {
    //given
    String tripID = "1111";
    LocalDateTime tripDate = LocalDateTime.of(2023, 4, 3, 12, 0);
    TripEntity existingTrip = new TripEntity(tripID, "Daniel", "Dublin", "Italy", tripDate);
    when(tripRepository.findById(tripID)).thenReturn(Optional.of(existingTrip));

    Trip updatedTrip = new Trip(tripID, "Sean", "France", "Spain", tripDate);
    TripEntity updatedTripEntity = new TripEntity(
      tripID,
      updatedTrip.getOwner(),
      updatedTrip.getOrigin(),
      updatedTrip.getDestination(),
      updatedTrip.getTripDate()
    );

    when(tripRepository.save(any(TripEntity.class))).thenReturn(updatedTripEntity);

    //when
    Trip result = tripService.updateTrip(updatedTrip);

    //then
    assertEquals(updatedTrip.getTripID(), result.getTripID());
    assertEquals(updatedTrip.getOwner(), result.getOwner());
    assertEquals(updatedTrip.getOrigin(), result.getOrigin());
    assertEquals(updatedTrip.getDestination(), result.getDestination());
    assertEquals(updatedTrip.getTripDate(), result.getTripDate());

    verify(tripRepository).findById(tripID);
    verify(tripRepository).save(any(TripEntity.class));
  }

  @Test
  @DisplayName("Should Delete Trip")
  void shouldDeleteTrip() {
    //given
    String tripID = "1111";
    TripEntity trip = new TripEntity(tripID, "Daniel", "Dublin", "Italy", LocalDateTime.of(2023, 4, 3, 12, 0));
    when(tripRepository.findById(tripID)).thenReturn(Optional.of(trip));

    //when
    tripService.deleteTrip(tripID);

    //then
    verify(tripRepository).findById(tripID);
    verify(tripRepository).deleteById(tripID);
  }

  @Test
  @DisplayName("Should Throw Exception When Trip Not Found")
  void shouldThrowExceptionWhenTripIsNotFound() {
    //given
    String tripID = "1000";
    when(tripRepository.findById(tripID)).thenReturn(Optional.empty());

    //when
    Exception exception = assertThrows(RuntimeException.class, () ->
      tripService.getTrip(tripID)
    );

    //then
    String expectedMessage = "Trip not found with ID: " + tripID;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    verify(tripRepository).findById(tripID);
  }

  @Test
  @DisplayName("Should Throw Exception When Trip Not Found During Delete")
  void shouldThrowExceptionWhenTripNotFoundDuringDelete() {
    //given
    String tripID = "1000";
    when(tripRepository.findById(tripID)).thenReturn(Optional.empty());

    //when
    Exception exception = assertThrows(RuntimeException.class, () ->
      tripService.deleteTrip(tripID)
    );

    //then
    String expectedMessage = "Trip not found with ID: " + tripID;
    String actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));

    verify(tripRepository).findById(tripID);
  }
}

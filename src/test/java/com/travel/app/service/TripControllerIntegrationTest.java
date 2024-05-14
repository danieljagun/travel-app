package com.travel.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travel.app.Trip;
import com.travel.app.TripNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TripControllerIntegrationTest {

  @Autowired
  private TestRestTemplate testRestTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private TripService tripService;

  @Test
  @DisplayName("Should Create Trip")
  void shouldCreateTrip() throws Exception {
    //given
    String tripID = UUID.randomUUID().toString();
    LocalDateTime tripDate = LocalDateTime.of(2023, 4, 3, 12, 0);
    Trip trip = new Trip(tripID, "Daniel", "Dublin", "Italy", tripDate);

    String requestJson = objectMapper.writeValueAsString(trip);
    when(tripService.createTrip(any(Trip.class)))
      .thenReturn(trip.getTripID());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

    //when
    ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(
      "/trips", request, String.class);

    //then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    String response = responseEntity.getBody();
    assertEquals(trip.getTripID(), response);
    verify(tripService).createTrip(any(Trip.class));
  }

  @Test
  @DisplayName("Should Get Trip")
  void shouldGetTrip() {
    //given
    String tripID = UUID.randomUUID().toString();
    LocalDateTime tripDate = LocalDateTime.of(2023, 4, 3, 12, 0);
    Trip trip = new Trip(tripID, "Daniel", "Dublin", "Italy", tripDate);

    when(tripService.getTrip(trip.getTripID()))
      .thenReturn(trip);

    //when
    ResponseEntity<Trip> responseEntity = testRestTemplate.getForEntity(
      "/trips/" + tripID, Trip.class);

    //then
    Trip response = responseEntity.getBody();
    assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);
    assertEquals(tripID, response.getTripID());
    assertEquals(trip.getOwner(), response.getOwner());
    assertEquals(trip.getOrigin(), response.getOrigin());
    assertEquals(trip.getDestination(), response.getDestination());
    assertEquals(tripDate, response.getTripDate());
    verify(tripService).getTrip(tripID);
  }

  @Test
  @DisplayName("Should Update Trip")
  void shouldUpdateTrip() throws Exception {
    //given
    String tripID = UUID.randomUUID().toString();
    LocalDateTime tripDate = LocalDateTime.of(2023, 4, 3, 12, 0);

    Trip updatedTrip = new Trip(tripID, "Daniel", "Dublin", "Spain", tripDate);

    when(tripService.updateTrip(any(Trip.class)))
      .thenReturn(updatedTrip);

    String requestJson = objectMapper.writeValueAsString(updatedTrip);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

    //when
    ResponseEntity<Trip> responseEntity = testRestTemplate.exchange(
      "/trips/update/" + tripID,
      HttpMethod.PUT,
      request,
      Trip.class
    );

    //then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    Trip response = responseEntity.getBody();
    assertThat(response).isNotNull();
    assertEquals(tripID, response.getTripID());
    assertEquals(updatedTrip.getOwner(), response.getOwner());
    assertEquals(updatedTrip.getOrigin(), response.getOrigin());
    assertEquals(updatedTrip.getDestination(), response.getDestination());
    assertEquals(tripDate, response.getTripDate());
    verify(tripService).updateTrip(any(Trip.class));
  }

  @Test
  @DisplayName("Should Return 404 When Updating And Trip Is Not Found")
  void ShouldReturn404WhenUpdatingAndTripIsNotFound() throws Exception {
    //given
    String tripID = UUID.randomUUID().toString();
    LocalDateTime tripDate = LocalDateTime.of(2023, 4, 3, 12, 0);

    Trip updatedTrip = new Trip(tripID, "Daniel", "Dublin", "Spain", tripDate);

    when(tripService.updateTrip(any(Trip.class)))
      .thenThrow(new TripNotFoundException(tripID));

    String requestJson = objectMapper.writeValueAsString(updatedTrip);

    // when
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    HttpEntity<String> request = new HttpEntity<>(requestJson, headers);

    ResponseEntity<String> responseEntity = testRestTemplate.exchange(
      "/trips/update/" + tripID,
      HttpMethod.PUT,
      request,
      String.class
    );

    // then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    verify(tripService).updateTrip(any(Trip.class));
  }

  @Test
  @DisplayName("Should Delete Trip")
  void shouldDeleteTrip() {
    //given
    String tripID = UUID.randomUUID().toString();
    LocalDateTime tripDate = LocalDateTime.of(2023, 4, 3, 12, 0);
    Trip trip = new Trip(tripID, "Daniel", "Dublin", "Italy", tripDate);

    when(tripService.getTrip(trip.getTripID()))
      .thenReturn(trip);

    //when
    ResponseEntity<String> responseEntity = testRestTemplate.exchange(
      "/trips/delete/" + tripID,
      HttpMethod.DELETE,
      null,
      String.class
    );

    //then
    assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    verify(tripService).deleteTrip(tripID);
  }

  @Test
  @DisplayName("Should Return 404 When Trip Is Not Found")
  void ShouldReturn404WhenTripIsNotFound() {
    //given
    String tripID = UUID.randomUUID().toString();
    when(tripService.getTrip(tripID))
      .thenThrow(new TripNotFoundException(tripID));

    //when
    ResponseEntity<String> responseEntity = testRestTemplate.getForEntity(
      "/trips/" + tripID, String.class);

    //then
    assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    String response = responseEntity.getBody();
    assertTrue(response.
      contains("Trip not found with id: " + tripID));
  }
}

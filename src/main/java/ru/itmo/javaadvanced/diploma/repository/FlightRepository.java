package ru.itmo.javaadvanced.diploma.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.itmo.javaadvanced.diploma.domain.entity.Flight;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    Optional<Flight> findByFlightNumber(String flightNumber);

    List<Flight> findByScheduledDepartureBetween(OffsetDateTime start, OffsetDateTime end);
}
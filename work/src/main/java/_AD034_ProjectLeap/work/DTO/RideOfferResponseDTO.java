package _AD034_ProjectLeap.work.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideOfferResponseDTO {

    private Long rideOfferId;

    private Long driverId;

    private String driverName;

    private String origin;

    private String destination;

    private String route;

    private String rideDate;

    private String departureTime;

    private Integer availableSeats;

    private String status;
}
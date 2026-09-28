package _AD034_ProjectLeap.work.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestResponseDTO {

    private Long rideRequestId;

    private Long riderId;
    private String riderName;

    private Long rideOfferId;

    private Long driverId;
    private String driverName;

    private String origin;
    private String destination;
    private String rideDate;
    private String departureTime;

    private String status;
}
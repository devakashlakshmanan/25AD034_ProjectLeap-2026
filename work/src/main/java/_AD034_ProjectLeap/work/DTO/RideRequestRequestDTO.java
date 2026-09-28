package _AD034_ProjectLeap.work.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideRequestRequestDTO {

    private Long riderId;
    private Long rideOfferId;
    private String status;
}
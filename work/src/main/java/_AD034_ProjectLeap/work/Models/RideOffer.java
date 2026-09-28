package _AD034_ProjectLeap.work.Models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ride_offers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RideOffer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideOfferId;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    private String origin;

    private String destination;

    private String route;

    private String rideDate;

    private String departureTime;

    private Integer availableSeats;

    private String status;
}
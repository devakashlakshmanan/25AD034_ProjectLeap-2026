package _AD034_ProjectLeap.work.Services;

import _AD034_ProjectLeap.work.DTO.RideRequestRequestDTO;
import _AD034_ProjectLeap.work.DTO.RideRequestResponseDTO;
import _AD034_ProjectLeap.work.Models.RideOffer;
import _AD034_ProjectLeap.work.Models.RideRequest;
import _AD034_ProjectLeap.work.Models.User;
import _AD034_ProjectLeap.work.Repository.RideOfferRepository;
import _AD034_ProjectLeap.work.Repository.RideRequestRepository;
import _AD034_ProjectLeap.work.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideRequestServices {

    private final RideRequestRepository rideRequestRepository;
    private final UserRepository userRepository;
    private final RideOfferRepository rideOfferRepository;

    public RideRequestServices(
            RideRequestRepository rideRequestRepository,
            UserRepository userRepository,
            RideOfferRepository rideOfferRepository) {

        this.rideRequestRepository = rideRequestRepository;
        this.userRepository = userRepository;
        this.rideOfferRepository = rideOfferRepository;
    }

    // CREATE RIDE REQUEST
    public RideRequestResponseDTO createRideRequest(
            RideRequestRequestDTO request) {

        User rider = userRepository.findById(request.getRiderId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Rider not found with ID: "
                                        + request.getRiderId()));

        RideOffer rideOffer = rideOfferRepository
                .findById(request.getRideOfferId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ride offer not found with ID: "
                                        + request.getRideOfferId()));

        // User cannot request their own ride
        if (rideOffer.getDriver().getUserId()
                .equals(rider.getUserId())) {

            throw new RuntimeException(
                    "User cannot request their own ride");
        }

        // Seats must be available
        if (rideOffer.getAvailableSeats() == null
                || rideOffer.getAvailableSeats() <= 0) {

            throw new RuntimeException(
                    "No available seats for this ride");
        }

        RideRequest rideRequest = new RideRequest();

        rideRequest.setRider(rider);
        rideRequest.setRideOffer(rideOffer);
        rideRequest.setStatus("Pending");

        RideRequest savedRequest =
                rideRequestRepository.save(rideRequest);

        return convertToResponse(savedRequest);
    }

    // GET ALL RIDE REQUESTS
    public List<RideRequestResponseDTO> getAllRideRequests() {

        return rideRequestRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET RIDE REQUEST BY ID
    public RideRequestResponseDTO getRideRequestById(Long id) {

        RideRequest rideRequest =
                rideRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ride request not found with ID: "
                                                + id));

        return convertToResponse(rideRequest);
    }

    // UPDATE REQUEST STATUS
    public RideRequestResponseDTO updateRideRequestStatus(
            Long id,
            String status) {

        RideRequest rideRequest =
                rideRequestRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Ride request not found with ID: "
                                                + id));

        if (!status.equalsIgnoreCase("Accepted")
                && !status.equalsIgnoreCase("Rejected")) {

            throw new RuntimeException(
                    "Status must be Accepted or Rejected");
        }

        // Prevent changing an already processed request
        if (!rideRequest.getStatus().equalsIgnoreCase("Pending")) {

            throw new RuntimeException(
                    "Only Pending requests can be updated");
        }

        // ACCEPT REQUEST
        if (status.equalsIgnoreCase("Accepted")) {

            RideOffer rideOffer =
                    rideRequest.getRideOffer();

            if (rideOffer.getAvailableSeats() == null
                    || rideOffer.getAvailableSeats() <= 0) {

                throw new RuntimeException(
                        "Cannot accept request. No available seats");
            }

            // Reduce available seat by 1
            rideOffer.setAvailableSeats(
                    rideOffer.getAvailableSeats() - 1
            );

            rideOfferRepository.save(rideOffer);

            rideRequest.setStatus("Accepted");
        }

        // REJECT REQUEST
        else {
            rideRequest.setStatus("Rejected");
        }

        RideRequest updatedRequest =
                rideRequestRepository.save(rideRequest);

        return convertToResponse(updatedRequest);
    }

    // DELETE RIDE REQUEST
    public void deleteRideRequest(Long id) {

        if (!rideRequestRepository.existsById(id)) {

            throw new RuntimeException(
                    "Ride request not found with ID: " + id);
        }

        rideRequestRepository.deleteById(id);
    }

    // ENTITY TO RESPONSE DTO
    private RideRequestResponseDTO convertToResponse(
            RideRequest rideRequest) {

        RideOffer rideOffer =
                rideRequest.getRideOffer();

        User driver =
                rideOffer.getDriver();

        User rider =
                rideRequest.getRider();

        return new RideRequestResponseDTO(
                rideRequest.getRideRequestId(),

                rider.getUserId(),
                rider.getName(),

                rideOffer.getRideOfferId(),

                driver.getUserId(),
                driver.getName(),

                rideOffer.getOrigin(),
                rideOffer.getDestination(),
                rideOffer.getRideDate(),
                rideOffer.getDepartureTime(),

                rideRequest.getStatus()
        );
    }
}
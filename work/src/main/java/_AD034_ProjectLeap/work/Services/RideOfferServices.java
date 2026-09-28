package _AD034_ProjectLeap.work.Services;

import _AD034_ProjectLeap.work.DTO.RideOfferRequestDTO;
import _AD034_ProjectLeap.work.DTO.RideOfferResponseDTO;
import _AD034_ProjectLeap.work.Models.RideOffer;
import _AD034_ProjectLeap.work.Models.User;
import _AD034_ProjectLeap.work.Repository.RideOfferRepository;
import _AD034_ProjectLeap.work.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RideOfferServices {

    private final RideOfferRepository rideOfferRepository;
    private final UserRepository userRepository;

    public RideOfferServices(
            RideOfferRepository rideOfferRepository,
            UserRepository userRepository) {

        this.rideOfferRepository = rideOfferRepository;
        this.userRepository = userRepository;
    }

    // CREATE RIDE OFFER
    public RideOfferResponseDTO createRideOffer(
            RideOfferRequestDTO request) {

        User driver = userRepository.findById(request.getDriverId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver not found with ID: "
                                        + request.getDriverId()));

        RideOffer rideOffer = new RideOffer();

        rideOffer.setDriver(driver);
        rideOffer.setOrigin(request.getOrigin());
        rideOffer.setDestination(request.getDestination());
        rideOffer.setRoute(request.getRoute());
        rideOffer.setRideDate(request.getRideDate());
        rideOffer.setDepartureTime(request.getDepartureTime());
        rideOffer.setAvailableSeats(request.getAvailableSeats());
        rideOffer.setStatus(request.getStatus());

        RideOffer savedRideOffer =
                rideOfferRepository.save(rideOffer);

        return convertToResponse(savedRideOffer);
    }

    // GET ALL RIDE OFFERS
    public List<RideOfferResponseDTO> getAllRideOffers() {

        return rideOfferRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // GET RIDE OFFER BY ID
    public RideOfferResponseDTO getRideOfferById(Long id) {

        RideOffer rideOffer = rideOfferRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ride offer not found with ID: " + id));

        return convertToResponse(rideOffer);
    }

    // UPDATE RIDE OFFER
    public RideOfferResponseDTO updateRideOffer(
            Long id,
            RideOfferRequestDTO request) {

        RideOffer rideOffer = rideOfferRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Ride offer not found with ID: " + id));

        User driver = userRepository.findById(request.getDriverId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Driver not found with ID: "
                                        + request.getDriverId()));

        rideOffer.setDriver(driver);
        rideOffer.setOrigin(request.getOrigin());
        rideOffer.setDestination(request.getDestination());
        rideOffer.setRoute(request.getRoute());
        rideOffer.setRideDate(request.getRideDate());
        rideOffer.setDepartureTime(request.getDepartureTime());
        rideOffer.setAvailableSeats(request.getAvailableSeats());
        rideOffer.setStatus(request.getStatus());

        RideOffer updatedRideOffer =
                rideOfferRepository.save(rideOffer);

        return convertToResponse(updatedRideOffer);
    }

    // DELETE RIDE OFFER
    public void deleteRideOffer(Long id) {

        if (!rideOfferRepository.existsById(id)) {
            throw new RuntimeException(
                    "Ride offer not found with ID: " + id);
        }

        rideOfferRepository.deleteById(id);
    }

    // ENTITY TO RESPONSE DTO
    private RideOfferResponseDTO convertToResponse(
            RideOffer rideOffer) {

        return new RideOfferResponseDTO(
                rideOffer.getRideOfferId(),
                rideOffer.getDriver().getUserId(),
                rideOffer.getDriver().getName(),
                rideOffer.getOrigin(),
                rideOffer.getDestination(),
                rideOffer.getRoute(),
                rideOffer.getRideDate(),
                rideOffer.getDepartureTime(),
                rideOffer.getAvailableSeats(),
                rideOffer.getStatus()
        );
    }
}

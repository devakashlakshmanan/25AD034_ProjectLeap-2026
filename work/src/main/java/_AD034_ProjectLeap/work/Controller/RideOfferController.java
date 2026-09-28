package _AD034_ProjectLeap.work.Controller;

import _AD034_ProjectLeap.work.DTO.RideOfferRequestDTO;
import _AD034_ProjectLeap.work.DTO.RideOfferResponseDTO;
import _AD034_ProjectLeap.work.Services.RideOfferServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ride-offers")
@CrossOrigin(origins = "*")
public class RideOfferController {

    private final RideOfferServices rideOfferServices;

    public RideOfferController(RideOfferServices rideOfferServices) {
        this.rideOfferServices = rideOfferServices;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<RideOfferResponseDTO> createRideOffer(
            @RequestBody RideOfferRequestDTO request) {

        return new ResponseEntity<>(
                rideOfferServices.createRideOffer(request),
                HttpStatus.CREATED
        );
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<RideOfferResponseDTO>> getAllRideOffers() {

        return ResponseEntity.ok(
                rideOfferServices.getAllRideOffers()
        );
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RideOfferResponseDTO> getRideOfferById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                rideOfferServices.getRideOfferById(id)
        );
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<RideOfferResponseDTO> updateRideOffer(
            @PathVariable Long id,
            @RequestBody RideOfferRequestDTO request) {

        return ResponseEntity.ok(
                rideOfferServices.updateRideOffer(id, request)
        );
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRideOffer(
            @PathVariable Long id) {

        rideOfferServices.deleteRideOffer(id);

        return ResponseEntity.ok(
                "Ride offer deleted successfully"
        );
    }
}

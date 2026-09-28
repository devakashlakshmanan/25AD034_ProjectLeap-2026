package _AD034_ProjectLeap.work.Controller;

import _AD034_ProjectLeap.work.DTO.RideRequestRequestDTO;
import _AD034_ProjectLeap.work.DTO.RideRequestResponseDTO;
import _AD034_ProjectLeap.work.Services.RideRequestServices;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ride-requests")
@CrossOrigin(origins = "*")
public class RideRequestController {

    private final RideRequestServices rideRequestServices;

    public RideRequestController(
            RideRequestServices rideRequestServices) {

        this.rideRequestServices = rideRequestServices;
    }

    // CREATE RIDE REQUEST
    @PostMapping
    public ResponseEntity<RideRequestResponseDTO> createRideRequest(
            @RequestBody RideRequestRequestDTO request) {

        return new ResponseEntity<>(
                rideRequestServices.createRideRequest(request),
                HttpStatus.CREATED
        );
    }

    // GET ALL RIDE REQUESTS
    @GetMapping
    public ResponseEntity<List<RideRequestResponseDTO>> getAllRideRequests() {

        return ResponseEntity.ok(
                rideRequestServices.getAllRideRequests()
        );
    }

    // GET RIDE REQUEST BY ID
    @GetMapping("/{id}")
    public ResponseEntity<RideRequestResponseDTO> getRideRequestById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                rideRequestServices.getRideRequestById(id)
        );
    }

    // ACCEPT OR REJECT RIDE REQUEST
    @PutMapping("/{id}/status")
    public ResponseEntity<RideRequestResponseDTO> updateRideRequestStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        return ResponseEntity.ok(
                rideRequestServices.updateRideRequestStatus(
                        id,
                        status
                )
        );
    }

    // DELETE RIDE REQUEST
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRideRequest(
            @PathVariable Long id) {

        rideRequestServices.deleteRideRequest(id);

        return ResponseEntity.ok(
                "Ride request deleted successfully"
        );
    }
}
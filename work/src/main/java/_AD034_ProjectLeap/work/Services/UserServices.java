package _AD034_ProjectLeap.work.Services;

import _AD034_ProjectLeap.work.DTO.UserRequestDTO;
import _AD034_ProjectLeap.work.DTO.UserResponseDTO;
import _AD034_ProjectLeap.work.Models.User;
import _AD034_ProjectLeap.work.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {

    private final UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE
    public UserResponseDTO createUser(UserRequestDTO request) {

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUserType(request.getUserType());
        user.setOrigin(request.getOrigin());
        user.setDestination(request.getDestination());
        user.setRoute(request.getRoute());
        user.setPreferredTime(request.getPreferredTime());

        User savedUser = userRepository.save(user);

        return convertToResponse(savedUser);
    }

    // READ ALL
    public List<UserResponseDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // READ BY ID
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        return convertToResponse(user);
    }

    // UPDATE
    public UserResponseDTO updateUser(Long id, UserRequestDTO request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setUserType(request.getUserType());
        user.setOrigin(request.getOrigin());
        user.setDestination(request.getDestination());
        user.setRoute(request.getRoute());
        user.setPreferredTime(request.getPreferredTime());

        User updatedUser = userRepository.save(user);

        return convertToResponse(updatedUser);
    }

    // DELETE
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
    }

    // ENTITY → RESPONSE DTO
    private UserResponseDTO convertToResponse(User user) {

        return new UserResponseDTO(
                user.getUserId(),
                user.getName(),
                user.getEmail(),
                user.getPhone(),
                user.getUserType(),
                user.getOrigin(),
                user.getDestination(),
                user.getRoute(),
                user.getPreferredTime()
        );
    }
}
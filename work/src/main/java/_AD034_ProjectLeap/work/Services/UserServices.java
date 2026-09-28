package _AD034_ProjectLeap.work.Services;

import _AD034_ProjectLeap.work.DTO.UserRequestDTO;
import _AD034_ProjectLeap.work.DTO.UserResponseDTO;
import _AD034_ProjectLeap.work.Models.User;
import _AD034_ProjectLeap.work.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServices {
    private final UserRepository userRepository;

    public UserServices(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // CREATE USER
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

        return convertToResponseDTO(savedUser);
    }


    // GET ALL USERS
    public List<UserResponseDTO> getAllUsers() {

        List<User> users = userRepository.findAll();

        return users.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }


    // GET USER BY ID
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));

        return convertToResponseDTO(user);
    }


    // UPDATE USER
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

        return convertToResponseDTO(updatedUser);
    }


    // DELETE USER
    public void deleteUser(Long id) {

        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with ID: " + id);
        }

        userRepository.deleteById(id);
    }


    // ENTITY → RESPONSE DTO
    private UserResponseDTO convertToResponseDTO(User user) {

        UserResponseDTO response = new UserResponseDTO();

        response.setUserId(user.getUserId());
        response.setName(user.getName());
        response.setEmail(user.getEmail());
        response.setPhone(user.getPhone());
        response.setUserType(user.getUserType());
        response.setOrigin(user.getOrigin());
        response.setDestination(user.getDestination());
        response.setRoute(user.getRoute());
        response.setPreferredTime(user.getPreferredTime());

        return response;
    }
}

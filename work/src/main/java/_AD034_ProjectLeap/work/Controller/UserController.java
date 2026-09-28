package _AD034_ProjectLeap.work.Controller;

import _AD034_ProjectLeap.work.DTO.UserRequestDTO;
import _AD034_ProjectLeap.work.DTO.UserResponseDTO;
import _AD034_ProjectLeap.work.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(("/api/users"))
public class UserController {
    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }


    // CREATE USER
    @PostMapping("/create")
    public UserResponseDTO createUser(@RequestBody UserRequestDTO request) {
        return userServices.createUser(request);
    }

    // GET ALL USERS
    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userServices.getAllUsers();
    }

    // GET USER BY ID
    @GetMapping("/{id}")
    public UserResponseDTO getUserById(@PathVariable Long id) {
        return userServices.getUserById(id);
    }

    // UPDATE USER
    @PutMapping("/{id}")
    public UserResponseDTO updateUser(
            @PathVariable Long id,
            @RequestBody UserRequestDTO request) {

        return userServices.updateUser(id, request);
    }

    // DELETE USER
    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userServices.deleteUser(id);
        return "User deleted successfully";
    }

}

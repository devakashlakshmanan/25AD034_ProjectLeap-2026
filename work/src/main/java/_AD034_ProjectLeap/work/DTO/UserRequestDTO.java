package _AD034_ProjectLeap.work.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    private String name;

    private String email;

    private String phone;

    private String userType;

    private String origin;

    private String destination;

    private String route;

    private String preferredTime;
}

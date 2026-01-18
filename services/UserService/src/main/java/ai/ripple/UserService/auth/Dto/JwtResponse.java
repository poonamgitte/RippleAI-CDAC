package ai.ripple.UserService.auth.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtResponse {
     private String token;
     
     public String getToken() {
         return token;
     }
}

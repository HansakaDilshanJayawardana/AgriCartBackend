package com.agricart.app.auth;

import com.agricart.app.auth.exceptions.FieldValidationFaild;
import com.agricart.app.auth.payload.AuthRequestDto;
import com.agricart.app.auth.payload.AuthRespondDto;
import com.agricart.app.auth.payload.RegisterRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {


    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/signin")
    public ResponseEntity<AuthRespondDto> login(@RequestBody AuthRequestDto authRequestDto) throws BadCredentialsException {

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequestDto.getUserName(),
                            authRequestDto.getPassword()
                    )
            );
        }catch (BadCredentialsException e){
            //TODO add proper exception
            throw new BadCredentialsException("INVALID_USERNAME_OR_PASSWORD" , e);
        }


        return new ResponseEntity<>(userService.loginUser(authRequestDto) , HttpStatus.ACCEPTED);
    }


    @PostMapping("/signup")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterRequestDto request){
        // basic controller level validations
        if(request.getUserName().isEmpty()){
            throw new FieldValidationFaild("user name is required");
        }
        if(request.getPassword().isEmpty()){
            throw new FieldValidationFaild("password is required");
        }
        if(request.getFirstName().isEmpty()){
            throw new FieldValidationFaild("first name is required");
        }
        if(request.getEmail().isEmpty()){
            throw new FieldValidationFaild("email is required");
        }
        if(request.getPhone().isEmpty()){
            throw new FieldValidationFaild("phone is required");
        }
        if(request.getPhone().length() != 10){
            throw new FieldValidationFaild("phone number length must be 10");
        }
        if(request.getAddress().isEmpty()){
            throw new FieldValidationFaild("address is required");
        }

        return new ResponseEntity<>(userService.registerUser(request) , HttpStatus.CREATED);

    }

}

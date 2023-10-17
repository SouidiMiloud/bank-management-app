package com.example.libertybankapp.controllers;

import com.example.libertybankapp.dto.Credentials;
import com.example.libertybankapp.dto.TokenResponse;
import com.example.libertybankapp.dto.UserRequest;
import com.example.libertybankapp.security.JwtUtil;
import com.example.libertybankapp.services.UserService;
import com.example.libertybankapp.user.AppUser;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Credentials credentials){


        try{
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentials.getIdentifier(), credentials.getPassword()
                    )
            );
            AppUser appUser = (AppUser) authentication.getPrincipal();
            return ResponseEntity.ok().body(new TokenResponse(appUser, jwtUtil.generateToken(appUser)));
        }
        catch(BadCredentialsException exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }


    @PostMapping("/newuser")
    public void addUser(@RequestBody UserRequest userRequest){

        userService.addNewUser(userRequest);
    }

    //@GetMapping("/checkbalance")
    //public ResponseEntity<Long> checkBalance(@RequestParam Long id){

    //    return userService.getBalance(id);
    //}

    @GetMapping("/checkbalance")
    public ResponseEntity<Long> checkBalance(@AuthenticationPrincipal AppUser appUser){

        return userService.getBalance(appUser.getId());
    }


    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@RequestBody Map<String, Object> request){

        return userService.withdraw(request);
    }

    @GetMapping("/getCustomerInfo")
    public ResponseEntity<Map<String, String>> getCustomerInfo(@RequestParam Long id){
        return userService.getCustomerInfo(id);
    }



    @PostMapping("/putamount")
    public ResponseEntity<String> putAmount( @RequestBody Map<String,Long> request ){
        return userService.putAmount(request);
    }

}
//eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDAwMDIiLCJleHAiOjE2OTk4ODk5NTMsImlhdCI6MTY5NzI5Nzk1M30.C0nrQHLV4c1QUqMlNmBGt5WCGqoE-s2fhAzczxdnEBZW-Hb3MQOONP33d-HZ6o1eKqK223ScJoh4w2VR7zV1Dw
//eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIwMDAwMDEiLCJleHAiOjE2OTk4ODk3MzIsImlhdCI6MTY5NzI5NzczMn0.O3VvcHyjHYtjwucWvc1jh41k_94pvot2fFmJGXId-Yud_-B_161OAqaTyiHWzC2hH2smU5Swh1BFo2lYLKDpzw
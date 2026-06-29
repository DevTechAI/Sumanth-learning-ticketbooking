package com.TicketBooking.oops.controller;

import com.TicketBooking.oops.entity.UserDetails;
import com.TicketBooking.oops.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/getallUsers")
    public Flux<UserDetails> getUserDetails()
    {
        return userService.getUserDetails();
    }

    @GetMapping("/byID/{uid}")
    public Mono<UserDetails> getuserByID(@PathVariable int uid)
    {
        return userService.getuserByID(uid);
    }

    @PostMapping("/create")
    public Mono<UserDetails> createUser(@RequestBody UserDetails user)
    {
        return userService.createUser(user);
    }

    @PutMapping("/update")
    public Mono<UserDetails> modifyUser(@RequestBody UserDetails userDetails)
    {
        return userService.updateUser(userDetails);
    }

    @DeleteMapping("/delete/{uid}")
    public Mono<Void> deleteUser(@PathVariable int uid)
    {
        return userService.deleteUser(uid);
    }

}
package com.TicketBooking.oops.service;

import com.TicketBooking.oops.entity.UserDetails;
import com.TicketBooking.oops.repository.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService
{
    private final UserRepository userRepository;

    public UserService (UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    public Flux<UserDetails> getUserDetails()
   {
       return userRepository.findAll();
   }

    public Mono<UserDetails> getuserByID(int uid)
    {
        return userRepository.findById(uid);
    }


    public Mono<UserDetails> createUser(UserDetails userDetails)
    {
        return userRepository.save(userDetails);
    }

    public Mono<UserDetails> updateUser(UserDetails userDetails)
    {
       return userRepository.findById(userDetails.getUid())
               .flatMap(euser -> {
                   euser.setUserName(userDetails.getUserName());
                   euser.setPassWord(userDetails.getPassWord());
                   euser.setEmail(userDetails.getEmail());
                   euser.setTicketNumber(userDetails.getTicketNumber());

                   return userRepository.save(euser);
               });

    }

    public Mono<Void> deleteUser(int uid)
    {
        return userRepository.deleteById(uid);
    }

}

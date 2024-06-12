package com.exameple.mytube.user.service;

import com.exameple.mytube.user.data.UserRepository;
import com.exameple.mytube.user.domain.User;
import com.exameple.mytube.user.dto.Userdto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public void join(User user) {
        userRepository.save(user);
    }


    public Userdto login(User user) throws Exception{
        Optional<User> opUser = userRepository.findByEmail(user.getEmail());
            if (opUser.isPresent()){
                User loginedUser = opUser.get();
                if(loginedUser.getPassword().equals(user.getPassword())){
                Userdto userDto = new Userdto();
                userDto.setEmail(loginedUser.getEmail());
                userDto.setId(loginedUser.getId());
                userDto.setUsername(loginedUser.getUsername());
                    return  userDto;
                }
                throw new Exception();
        }
        throw new Exception();
    }
}

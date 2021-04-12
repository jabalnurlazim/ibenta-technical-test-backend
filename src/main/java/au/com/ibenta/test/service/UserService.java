package au.com.ibenta.test.service;

import au.com.ibenta.test.model.User;
import au.com.ibenta.test.persistence.UserEntity;
import au.com.ibenta.test.model.UserParams;
import au.com.ibenta.test.persistence.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private ModelMapper modelMapper = new ModelMapper();

    public ResponseEntity<String> create(UserParams userParams) {

        String response = dataValidation(userParams);
        if(!response.equals("success")) return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        UserEntity userEntity = userRepository.save(modelMapper.map(userParams, UserEntity.class));

        User user = modelMapper.map(userEntity, User.class);

        return new ResponseEntity<>(user.toString(), HttpStatus.CREATED);

    }

    public ResponseEntity<String> get (Long userId) {

        if(!userRepository.existsById(userId))
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        UserEntity userEntity = userRepository.findById(userId).get();
        User user = modelMapper.map(userEntity, User.class);

        return new ResponseEntity<>(user.toString(), HttpStatus.OK);
    }

    public ResponseEntity<String> update (UserParams userParams, Long userId) {

        if(!userRepository.existsById(userId))
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        // check if parameters are complete
        String response = dataValidation(userParams);
        if(!response.equals("success"))
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

        UserEntity userEntity = userRepository.save(mapParamsToEntity(userParams, userId));

        User user = modelMapper.map(userEntity, User.class);

        return new ResponseEntity<>(user.toString(), HttpStatus.OK);

    }

    public ResponseEntity<String> delete (Long userId) {

        if(!userRepository.existsById(userId)) return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);

        userRepository.deleteById(userId);

        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    public ResponseEntity<String> list() {

        List<User> userList = userRepository.findAll().stream()
                .map(user -> modelMapper.map(user, User.class))
                .collect(Collectors.toList());

        return new ResponseEntity<>(userList.toString(), HttpStatus.OK);
    }

    private UserEntity mapParamsToEntity(UserParams userParams, Long id) {

        UserEntity user = userRepository.findById(id).get();

        user.setFirstName(userParams.getFirstName());
        user.setLastName(userParams.getLastName());
        user.setEmail(userParams.getEmail());
        user.setPassword(userParams.getPassword());

        return user;
    }

    private String dataValidation(UserParams userParams) {
        if(userParams.getFirstName() == null || userParams.getFirstName().isEmpty())
            return "firstName is required";
        if(userParams.getLastName() == null || userParams.getLastName().isEmpty())
            return "lastname is required";
        if(userParams.getEmail() == null || userParams.getEmail().isEmpty() || !User.isValidEmail(userParams.getEmail()))
            return "email is required and should be a proper email format";
        if(userParams.getPassword() == null || userParams.getPassword().isEmpty())
            return "password is required";

        return "success";
    }
}

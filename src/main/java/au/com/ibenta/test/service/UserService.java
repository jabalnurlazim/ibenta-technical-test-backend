package au.com.ibenta.test.service;

import au.com.ibenta.test.model.User;
import au.com.ibenta.test.persistence.UserEntity;
import au.com.ibenta.test.params.UserParams;
import au.com.ibenta.test.persistence.UserRepository;
import au.com.ibenta.test.response.ResponseBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public ResponseBase create (UserParams userParams) {

        // check if parameters are complete
        ResponseBase response = dataValidation(userParams);
        if(response.getCode() != ResponseBase.OK.getCode()) return response;

        return ResponseBase.newSuccessResponse(convertEntityToDTO(userRepository.save(convertParamsToEntity(userParams, null))));

    }

    public ResponseBase get (Long userId) {
        // check user if exist
        if(!userRepository.existsById(userId)) return ResponseBase.INVALID_USER;

        return ResponseBase.newSuccessResponse(ResponseBase.OK, convertEntityToDTO(userRepository.findById(userId).get()));
    }

    public ResponseBase update (UserParams userParams, Long userId) {

        // check user if exist
        if(!userRepository.existsById(userId)) return ResponseBase.INVALID_USER;

        // check if parameters are complete
        ResponseBase response = dataValidation(userParams);
        if(response.getCode() != ResponseBase.OK.getCode()) return response;

        logger.info("User has been updated");
        return ResponseBase.newSuccessResponse(ResponseBase.OK, convertEntityToDTO(userRepository.save(convertParamsToEntity(userParams, userId))));

    }

    public ResponseBase delete (Long userId) {
        // check user if exist
        if(!userRepository.existsById(userId)) return ResponseBase.INVALID_USER;

        userRepository.deleteById(userId);
        logger.info("User has been deleted");
        return ResponseBase.SUCCESS;
    }

    public ResponseBase list() {
        return ResponseBase.newSuccessResponse(ResponseBase.OK, userRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList()));
    }

    public User convertEntityToDTO(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setEmail(userEntity.getEmail());
        return user;
    }

    private UserEntity convertParamsToEntity(UserParams userParams, Long id) {
        UserEntity user = null;

        if(id == null) user = new UserEntity();
        else user = userRepository.findById(id).get();

        user.setFirstName(userParams.getFirstName());
        user.setLastName(userParams.getLastName());
        user.setEmail(userParams.getEmail());
        user.setPassword(userParams.getPassword());

        return user;
    }

    private ResponseBase dataValidation(UserParams userParams) {
        if(userParams.getFirstName() == null || userParams.getFirstName().isEmpty())
            return ResponseBase.INVALID_FIRST_NAME;
        if(userParams.getLastName() == null || userParams.getLastName().isEmpty())
            return ResponseBase.INVALID_LAST_NAME;
        if(userParams.getEmail() == null || userParams.getEmail().isEmpty() || !User.isValidEmail(userParams.getEmail()))
            return ResponseBase.INVALID_EMAIL;
        if(userParams.getPassword() == null || userParams.getPassword().isEmpty())
            return ResponseBase.INVALID_PASSWORD;

        return ResponseBase.OK;
    }
}

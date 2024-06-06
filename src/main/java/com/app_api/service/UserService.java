package com.app_api.service;

import com.app_api.dto.request.ChangePasswordRequest;
import com.app_api.dto.response.UserResponse;
import com.app_api.resource.repo.TokenRepository;
import com.app_api.resource.repo.UserRepository;
import com.app_api.dto.request.UserRequest;
import com.app_api.resource.entity.User;
import com.app_api.resource.enums.UserRoleEnum;
import com.app_api.util.CurrentUserHolder;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService extends BaseService {

    private final UserRepository repo;
    private final TokenRepository tokenRepository;

    //private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public UserService(CurrentUserHolder currentUserHolder, UserRepository repo, TokenRepository tokenRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        super(currentUserHolder);
        this.repo = repo;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public void changePassword(ChangePasswordRequest request) {

        if (!passwordEncoder.matches(request.getCurrentPassword(), currentUser.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Password are not the same");
        }
        currentUser.setPassword(passwordEncoder.encode(request.getNewPassword()));
        currentUser.setAuditInfo(updateAudit(currentUser.getAuditInfo()));
        repo.save(currentUser);
    }

    public void changeActive(Integer id, boolean status) {
        User user = repo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_ACCEPTABLE, "Can't find user"));
        user.setActive(status);
        user.setAuditInfo(updateAudit(user.getAuditInfo()));
        repo.save(user);
        if (!status) {
            revokeAllUserTokens(user);
        }
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void updateUser(UserRequest userRequest) {
        currentUser.setName(userRequest.getName());
        currentUser.setPhone(userRequest.getPhone());

        currentUser.setCompanyName(userRequest.getCompanyName());
        currentUser.setStreet1(userRequest.getStreet1());
        currentUser.setStreet2(userRequest.getStreet2());
        currentUser.setCity(userRequest.getCity());
        currentUser.setState(userRequest.getState());
        currentUser.setZipCode(userRequest.getZipCode());

        currentUser.setAuditInfo(updateAudit(currentUser.getAuditInfo()));
        repo.save(currentUser);
    }

    public UserResponse getUserDetail() {
        UserResponse userResponse = modelMapper.map(currentUser, UserResponse.class);
        if (currentUser.getRole().getValue().equals(UserRoleEnum.whser)
                || currentUser.getRole().getValue().equals(UserRoleEnum.whserEmp)) {
            if (currentUser.getRole().getValue().equals(UserRoleEnum.whserEmp)) {
                currentUser = currentUser.getEmployer();
            }
            //userResponse.setCountryWhser(fetchCountryWhser(user));
        }
/*        if (user.getRole().getValue().equals(UserRoleEnum.sellerEmp)) {
            List<CountrySellerXrefResponse> list = user.getEmployer().getCountrySellerXrefs()
                    .stream()
                    .map((element) -> modelMapper.map(
                            element, CountrySellerXrefResponse.class)).toList();
            userResponse.setCountrySellerXrefs(list);
        }*/
        return userResponse;
    }
}

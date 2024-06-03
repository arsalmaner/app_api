package com.app_api.service;

import com.app_api.dto.request.ChangePasswordRequest;
import com.app_api.dto.response.UserResponse;
import com.app_api.resource.repo.TokenRepository;
import com.app_api.resource.repo.UserRepository;
import com.app_api.resource.repo.UserRoleRepository;
import com.app_api.dto.request.UserRequest;
import com.app_api.resource.entity.User;
import com.app_api.resource.enums.UserRoleEnum;
import com.app_api.util.CurrentUserHolder;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserService extends BaseService {

    private final UserRepository repo;
    private final TokenRepository tokenRepository;

    //private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    private final CurrentUserHolder currentUserHolder;

    public void changePassword(ChangePasswordRequest request) {
        User user = currentUserHolder.getCurrentUser();

        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setAuditInfo(updateAudit(user.getId(), user.getAuditInfo()));
        repo.save(user);
    }

    public void changeActive(Integer id, boolean status) {
        User user = repo.findById(id).orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_ACCEPTABLE, "Can't find user"));
        user.setActive(status);
        user.setAuditInfo(updateAudit(user.getId(), user.getAuditInfo()));
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
        User user = currentUserHolder.getCurrentUser();
        user.setName(userRequest.getName());
        user.setPhone(userRequest.getPhone());

        user.setCompanyName(userRequest.getCompanyName());
        user.setStreet1(userRequest.getStreet1());
        user.setStreet2(userRequest.getStreet2());
        user.setCity(userRequest.getCity());
        user.setState(userRequest.getState());
        user.setZipCode(userRequest.getZipCode());

        user.setAuditInfo(updateAudit(user.getId(), user.getAuditInfo()));
        repo.save(user);
    }

    public UserResponse getUserDetail() {
        User user = currentUserHolder.getCurrentUser();
        UserResponse userResponse = modelMapper.map(user, UserResponse.class);
        if (user.getRole().getValue().equals(UserRoleEnum.whser)
                || user.getRole().getValue().equals(UserRoleEnum.whserEmp)) {
            if (user.getRole().getValue().equals(UserRoleEnum.whserEmp)) {
                user = user.getEmployer();
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

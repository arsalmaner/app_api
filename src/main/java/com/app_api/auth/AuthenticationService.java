package com.app_api.auth;

import com.app_api.config.JwtService;
import com.app_api.resource.entity.Token;
import com.app_api.resource.entity.UserRole;
import com.app_api.resource.repo.TokenRepository;
import com.app_api.resource.repo.UserRepository;
import com.app_api.resource.repo.UserRoleRepository;
import com.app_api.service.BaseService;
import com.app_api.resource.entity.User;
import com.app_api.resource.enums.TokenType;
import com.app_api.resource.enums.UserRoleEnum;
import com.app_api.util.CurrentUserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthenticationService extends BaseService {
    private final UserRepository repository;
    private final TokenRepository tokenRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CurrentUserHolder currentUserHolder;

    @Transactional
    public void register(RegisterRequest request) {
        if (request.getRole() == UserRoleEnum.admin) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "Can't register an admin");
        }

        UserRole userRole = userRoleRepository.findByValue(UserRoleEnum.admin)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_ACCEPTABLE, "User role not found"));

/*        User employer = null;
        if(request.getRole() == UserRoleEnum.sellerEmp) {
            employer = repository.findById(request.getSellerId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_ACCEPTABLE, "Employer not found"));
        } else if (request.getRole() == UserRoleEnum.whserEmp) {
            employer = whser;
        }*/

        User whser = currentUserHolder.getCurrentUser();

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // https://bcrypt.online/
                .role(userRole)
                .name(request.getName())
                .active(true)
                //.employer(employer)
                .depot(whser)
                .phone(request.getPhone())
                .auditInfo(createAudit(whser.getId()))
                .build();

        var savedUser = repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        saveUserToken(savedUser, jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));
        var user = repository.findByEmail(request.getEmail()).orElseThrow();

        if (!user.getActive()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE, "User is waiting for approval");
        }

        var jwtToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
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

}

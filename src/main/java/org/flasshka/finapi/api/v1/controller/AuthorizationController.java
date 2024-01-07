package org.flasshka.finapi.api.v1.controller;

import org.flasshka.finapi.Security;
import org.flasshka.finapi.api.v1.model.Validator;
import org.flasshka.finapi.dao.AuthorizationDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@Controller
@RequestMapping("/api/v1/auth")
public class AuthorizationController {
    private final AuthorizationDAO dao;

    @Autowired
    public AuthorizationController(AuthorizationDAO dao) {
        this.dao = dao;
    }

    @PostMapping("/reg")
    public ResponseEntity<String> registration(@RequestHeader MultiValueMap<String, String> headers) {
        if (!headers.containsKey(Security.Http.LOGIN_HEADER_NAME)
                || !headers.containsKey(Security.Http.PASSWORD_HEADER_NAME)) {
            return new ResponseEntity<>("login or password not found", HttpStatus.BAD_REQUEST);
        }

        String login = headers.get(Security.Http.LOGIN_HEADER_NAME).stream().findAny().get();
        String password = headers.get(Security.Http.PASSWORD_HEADER_NAME).stream().findAny().get();

        if (dao.haveLogin(login) || !Validator.isValidLogin(login) || !Validator.isValidPassword(password)) {
            return new ResponseEntity<>("incorrect login or password", HttpStatus.UNAUTHORIZED);
        }

        try {
            dao.registration(login, Security.getEncryptedPassword(password));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestHeader MultiValueMap<String, String> headers) {
        if (!headers.containsKey(Security.Http.LOGIN_HEADER_NAME) || !headers.containsKey(Security.Http.PASSWORD_HEADER_NAME)) {
            return new ResponseEntity<>("login or password not found", HttpStatus.BAD_REQUEST);
        }

        String login = headers.get(Security.Http.LOGIN_HEADER_NAME).stream().findAny().get();
        String password = headers.get(Security.Http.PASSWORD_HEADER_NAME).stream().findAny().get();

        try {
            if (!dao.logIn(login, Security.getEncryptedPassword(password))) {
                return new ResponseEntity<>("incorrect login or password", HttpStatus.UNAUTHORIZED);
            }
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }

        return ResponseEntity.ok("Successful!");
    }
}

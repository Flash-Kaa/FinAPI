package org.flasshka.finapi.api.v1.controller;

import org.flasshka.finapi.Security;
import org.flasshka.finapi.dao.ExpenditureDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;


@Controller
@RequestMapping("/api/v1/expenditure")
public class ExpenditureController {
    private final ExpenditureDAO dao;

    @Autowired
    public ExpenditureController(ExpenditureDAO dao) {
        this.dao = dao;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addExpenditure(
            @RequestParam("sum") int sum,
            @RequestParam("date") long dateParam,
            @RequestHeader MultiValueMap<String, String> headers
    ) {
        if (!headers.containsKey(Security.Http.LOGIN_HEADER_NAME)) {
            return new ResponseEntity<>("user not found", HttpStatus.BAD_REQUEST);
        }

        String login = headers.get(Security.Http.LOGIN_HEADER_NAME).stream().findAny().get();
        Date date = new Date(dateParam);

        dao.addExpenditures(login, sum, date);

        return ResponseEntity.ok("Successful!");
    }
}

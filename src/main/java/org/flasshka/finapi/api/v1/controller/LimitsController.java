package org.flasshka.finapi.api.v1.controller;

import org.flasshka.finapi.Security;
import org.flasshka.finapi.dao.LimitsDAO;
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
@RequestMapping("/api/v1/limits")
public class LimitsController {
    private final LimitsDAO dao;

    @Autowired
    public LimitsController(LimitsDAO dao) {
        this.dao = dao;
    }

    @PostMapping("/addOne")
    public ResponseEntity<String> addInOneDate(
            @RequestParam("limit") int limit,
            @RequestParam("date") long dateParam,
            @RequestHeader MultiValueMap<String, String> headers
    ) {

        if (!headers.containsKey(Security.Http.LOGIN_HEADER_NAME)) {
            return new ResponseEntity<>("user not found", HttpStatus.BAD_REQUEST);
        }

        String login = headers.get(Security.Http.LOGIN_HEADER_NAME).stream().findAny().get();
        Date date = new Date(dateParam);

        dao.addLimit(login, limit, date, date);

        return ResponseEntity.ok("Successful!");
    }

    @PostMapping("/addPeriod")
    public ResponseEntity<String> addInPeriod(
            @RequestParam("limit") int limit,
            @RequestParam("firstDate") long start,
            @RequestParam("lastDate") long end,
            @RequestHeader MultiValueMap<String, String> headers
    ) {
        if (!headers.containsKey(Security.Http.LOGIN_HEADER_NAME)) {
            return new ResponseEntity<>("user not found", HttpStatus.BAD_REQUEST);
        }

        String login = headers.get(Security.Http.LOGIN_HEADER_NAME).stream().findAny().get();
        Date startDate = new Date(start);
        Date endDate = new Date(end);

        dao.addLimit(login, limit, startDate, endDate);

        return ResponseEntity.ok("Successful!");
    }
}

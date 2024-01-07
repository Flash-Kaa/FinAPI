package org.flasshka.finapi.api.v1.controller;

import org.flasshka.finapi.Security;
import org.flasshka.finapi.dao.ExpenditureDAO;
import org.flasshka.finapi.dao.LimitsDAO;
import org.flasshka.finapi.dao.model.ExpenditureModel;
import org.flasshka.finapi.dao.model.LimitModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@RequestMapping("api/v1/result")
public class DayResultController {
    private final LimitsDAO limitsDao;
    private final ExpenditureDAO expenditureDao;

    @Autowired
    public DayResultController(LimitsDAO dao, ExpenditureDAO expenditureDao) {
        this.limitsDao = dao;
        this.expenditureDao = expenditureDao;
    }

    @GetMapping("/day")
    public ResponseEntity<String> getDayResult(
            @RequestParam("date") long dateParam,
            @RequestHeader MultiValueMap<String, String> headers
    ) {
        if (!headers.containsKey(Security.Http.LOGIN_HEADER_NAME)) {
            return new ResponseEntity<>("login not found", HttpStatus.BAD_REQUEST);
        }

        String login = headers.get(Security.Http.LOGIN_HEADER_NAME).stream().findAny().get();
        Date date = new Date(dateParam);

        List<LimitModel> limits = limitsDao.getLimits(login, date);
        List<ExpenditureModel> expenditures = expenditureDao.getExpenditures(login, date);

        //TODO:  неверныый вывод дат. Из-за этого выводится не то, что ожидается
        // как исправить: либо добавляем в бд странные даты, либо получаем даты и изменяем их, либо в бд храним лонги, а не даты

        Integer[] res = calculateResults(date, expenditures, limits);

        return ResponseEntity.ok(Arrays.stream(res).map(Objects::toString).collect(Collectors.joining(" ")));
    }

    private Integer[] calculateResults(Date date, List<ExpenditureModel> expendituresFromDao, List<LimitModel> limitsFromDao) {
        List<ExpenditureModel> expenditures = expendituresFromDao
                .stream()
                .filter(model -> model.getDate().getTime() <= date.getTime())
                .sorted(Comparator.comparing(ExpenditureModel::getDate).thenComparing(ExpenditureModel::getSum))
                .collect(Collectors.toList());

        if (expenditures.isEmpty()) {
            return new Integer[0];
        }

        Date firstCalcDate = expenditures.get(0).getDate();

        int arrSize = convertToMillis(date, firstCalcDate);
        Integer[] limitsArr = setLimits(arrSize, limitsFromDao, firstCalcDate, date);

        return setExpenditures(arrSize, expenditures, firstCalcDate, limitsArr);
    }

    private int convertToMillis(Date date1, Date date2) {
        return (int) TimeUnit.DAYS.convert(
                date1.getTime() - date2.getTime(),
                TimeUnit.MILLISECONDS
        );
    }

    private Integer[] setLimits(
            int arrSize, List<LimitModel> limitsFromDao, Date firstCalcDate, Date date
    ) {
        Integer[] limitsArr = new Integer[arrSize];

        List<LimitModel> limits = limitsFromDao
                .stream()
                .filter(model -> model.getStartLim().getTime() <= date.getTime())
                .collect(Collectors.toList());

        for (int i = limits.size() - 1; i >= 0; i--) {
            LimitModel cur = limits.get(i);
            int start = Math.max(0, convertToMillis(cur.getStartLim(), firstCalcDate));
            int end = convertToMillis(cur.getEndLim(), firstCalcDate);

            for (int j = start; j <= end && j < limitsArr.length; j++) {
                if (limitsArr[j] == null) {
                    limitsArr[j] = cur.getLim();
                }
            }
        }

        return limitsArr;
    }

    private Integer[] setExpenditures(
            int arrSize, List<ExpenditureModel> expenditures, Date firstCalcDate, Integer[] limitsArr
    ) {
        Integer[] daysResults = new Integer[arrSize];

        for (ExpenditureModel cur : expenditures) {
            int ind = convertToMillis(cur.getDate(), firstCalcDate);
            int curExp = cur.getSum();
            int curLim = limitsArr[ind] == null ? 0 : limitsArr[ind];

            if (curExp < 0) {
                if (daysResults[ind] == null) {
                    daysResults[ind] = curExp - curLim;
                } else {
                    daysResults[ind] += curExp;
                }

                continue;
            }

            if (daysResults[ind] != null) {
                curExp += daysResults[ind];
                if (daysResults[ind] < 0) {
                    daysResults[ind] = curExp;
                    continue;
                }
            } else {
                curExp -= curLim;
            }


            boolean isFirst = true;
            while (ind < limitsArr.length && curExp > 0) {
                if (!isFirst) {
                    curExp -= curLim;
                }
                isFirst = false;

                if (curExp >= 0) {
                    daysResults[ind] = curExp;
                }

                ind++;
            }
        }


        for (int i = 0; i < arrSize; i++) {
            daysResults[i] = daysResults[i] == null ? (limitsArr[i] == null ? 0 : -limitsArr[i]) : daysResults[i];
        }

        return daysResults;
    }
}
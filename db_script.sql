CREATE DATABASE "FinancialService"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;


CREATE TABLE "Authorization"
(
    login VARCHAR(30) PRIMARY KEY NOT NULL,
    password VARCHAR(16) NOT NULL
);

CREATE TABLE "Limits"
(
    id SERIAL PRIMARY KEY NOT NULL,
    login VARCHAR(30) NOT NULL,
    lim INT NOT NULL,
    startLim DATE NOT NULL,
    endLim DATE NOT NULL,
    FOREIGN KEY(login) REFERENCES "Authorization"(login)
);

CREATE TABLE "Expenditure"
(
    id SERIAL PRIMARY KEY NOT NULL,
    login VARCHAR(30) NOT NULL,
    date DATE NOT NULL,
    sum INT NOT NULL,
    FOREIGN KEY(login) REFERENCES "Authorization"(login)
);

SELECT * FROM "Expenditure";
SELECT * FROM "Limits";
SELECT * FROM "Authorization";

INSERT INTO "Expenditure"(login, date, sum) VALUES ('abc', '05.07.2023', 200);
INSERT INTO "Limits"(login, lim, startLim, endLim) VALUES ('abc', 102, '01.04.2017', '10.12.2020');

SELECT *
FROM "Limits" L JOIN "Expenditure" E
    ON L.login = E.login
WHERE L.login = 'abc';
--ORDER BY L.startlim, L.endlim

SELECT *
FROM "Limits"
WHERE login = 'abc'




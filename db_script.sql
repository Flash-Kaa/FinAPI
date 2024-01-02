CREATE DATABASE "FinancialService"
    WITH
    OWNER = postgres
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;

	
CREATE TABLE "Authorization" 
(
	login VARCHAR(20) PRIMARY KEY,
	password VARCHAR(32)
)

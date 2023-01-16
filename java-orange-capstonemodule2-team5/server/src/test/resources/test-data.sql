BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transaction CASCADE;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transaction_id;

-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;

CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);

-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;

CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance decimal(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id));


    CREATE SEQUENCE seq_transaction_id
      INCREMENT BY 1
      START WITH 3001
      NO MAXVALUE;

    CREATE TABLE transaction (
    	transaction_id int NOT NULL DEFAULT nextval('seq_transaction_id'),
    	sender_id int NOT NULL,
    	receiver_id int NOT NULL,
    	amount decimal(13, 2) NOT NULL,
    	status varchar(30) DEFAULT 'APPROVED',
    	CONSTRAINT PK_transaction_id PRIMARY KEY (transaction_id),
    	CONSTRAINT FK_trans_sender_id FOREIGN KEY (sender_id) REFERENCES account (account_id),
    	CONSTRAINT FK_trans_receiver_id FOREIGN KEY (receiver_id) REFERENCES account (account_id)
    );


INSERT INTO tenmo_user (username, password_hash)
VALUES ('tom', '$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2'),
       ('dick', '$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy'),
       ('harry', '$2a$10$Ub8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy'),
       ('broke', '$2a$10$Ub8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy')
       ;

 INSERT INTO account (user_id, balance)
 VALUES (1001, 1000.00),
 (1002, 1000.00),
 (1003, 1000.00),
 (1004, 0.00)
 ;

 INSERT INTO transaction (sender_id, receiver_id, amount)
 VALUES (2001, 2002, 0.01),
 (2002, 2001, 0.01);

COMMIT;



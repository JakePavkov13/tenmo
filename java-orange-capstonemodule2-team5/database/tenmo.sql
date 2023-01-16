BEGIN TRANSACTION;

DROP TABLE IF EXISTS tenmo_user, account, transaction CASCADE;

DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id,seq_transaction_id;

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
	balance numeric(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
		
);

CREATE SEQUENCE seq_transaction_id
INCREMENT BY 1
START WITH 3001
NO MAXVALUE;

CREATE TABLE transaction (
	transaction_id int NOT null DEFAULT nextval('seq_transaction_id'),
	sender_id int NOT null, 
	receiver_id int NOT null,
	amount numeric(13,2) NOT null,
	status varchar(30) NOT null DEFAULT 'APPROVED',
	CONSTRAINT PK_transaction PRIMARY KEY (transaction_id),
	CONSTRAINT FK_sender_id FOREIGN KEY (sender_id) REFERENCES account(account_id),
	CONSTRAINT FK_receiver_id FOREIGN KEY (receiver_id) REFERENCES account(account_id)
	);

COMMIT;

select * from tenmo_user tu
JOIN account ON account.user_id = tu.user_id;

select * from transaction where sender_id = 2001 OR receiver_id = 2001;
UPDATE account SET balance = 1000;

SELECT transaction_id, (SELECT username FROM tenmo_user WHERE user_id IN(SELECT user_id from account WHERE account_id IN(select sender_id from transaction where transaction_id = 3024))) AS sender, (SELECT username FROM tenmo_user WHERE user_id IN (SELECT user_id from account WHERE account_id IN(select receiver_id from transaction where transaction_id = 3024))) AS receiver, amount, status FROM transaction where transaction_id = 3024;
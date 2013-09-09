/*
	SQL Code for Creating necessary Tables and Indices
*/
SET SCHEMA Public;

CREATE TABLE Accounts
(
	id INT NOT NULL PRIMARY KEY,
	email VARCHAR(256) NOT NULL,
	password VARCHAR(256) NOT NULL,
);
CREATE UNIQUE INDEX uk_email ON Accounts (email);

CREATE TABLE PlayerCharacters
(
	id INT NOT NULL PRIMARY KEY,
	accountId INT NOT NULL,
	gameObjectId INT NOT NULL
);

CREATE TABLE GameObjects
(
	id INT NOT NULL PRIMARY KEY,
	name VARCHAR(256),
);

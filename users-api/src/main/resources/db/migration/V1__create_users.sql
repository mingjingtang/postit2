CREATE TABLE users (
    id SERIAL,
    username VARCHAR(100) NOT NULL,
    email VARCHAR(355) UNIQUE NOT NULL,
    password VARCHAR(355) UNIQUE NOT NULL
);
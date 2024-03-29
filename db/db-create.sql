--------
-- Drop all tables
--------

-- SELECT	pg_terminate_backend (pid)
-- FROM	pg_stat_activity
-- WHERE	pg_stat_activity.datname = 'postgres';

-- DROP DATABASE hotel WITH (FORCE);
-- CREATE DATABASE hotel ENCODING 'UTF-8';

DROP SCHEMA IF EXISTS public CASCADE;

CREATE SCHEMA public;

--------
-- Creating tables
--------

CREATE TABLE users (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    password VARCHAR(100) NOT NULL, -- hashed pwd in SHA-256
                    name VARCHAR NOT NULL,
                    surname VARCHAR NOT NULL,
                    phone_number VARCHAR(15) UNIQUE NOT NULL,
                    email VARCHAR(30) UNIQUE NOT NULL,
                    role INT NOT NULL,
                    verification VARCHAR(5) NULL); -- customer(1) or manager(2)

CREATE TABLE room_types (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                         capacity INT NOT NULL,
                         beds_types VARCHAR(8), -- S - single, D - double, T - twin, K - king size, Q - Queen size
                         class INT NOT NULL, -- standard(1), upgraded(2), deluxe(3), suite(4)
                         description TEXT);

CREATE TABLE rooms (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    room_number INT UNIQUE NOT NULL ,
                    floor INT NOT NULL,
                    room_type_id INT REFERENCES room_types(id) ON DELETE CASCADE);

CREATE TABLE room_images (room_type_id INT REFERENCES room_types(id) ON DELETE CASCADE,
                    img_path VARCHAR(30) NOT NULL,
                    UNIQUE (room_type_id, img_path));

CREATE TABLE reservations (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, -- already payed up
                    user_id INT REFERENCES users(id) ON DELETE CASCADE,
                    room_id INT REFERENCES rooms(id) ON DELETE CASCADE,
                    date_from DATE NOT NULL,
                    date_to DATE NOT NULL,
                    guests_amount INT);

CREATE TABLE requests (id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                    user_id INT REFERENCES users(id) ON DELETE CASCADE,
                    room_id INT REFERENCES rooms(id) NULL,
                    date_from DATE NOT NULL,
                    date_to DATE NOT NULL,
                    date_of_manager_response DATE,
                    adults_amount INT,
                    children_amount INT,
                    class INT NOT NULL,
                    status INT NOT NULL, -- waiting for: manager response(1), customer accept(2), payment(3), canceled(4)
                    price DOUBLE PRECISION);

CREATE TABLE users_requests(user_id INT REFERENCES users(id) ON DELETE CASCADE,
                    request_id INT REFERENCES requests(id) ON DELETE CASCADE,
                    UNIQUE (user_id, request_id));

--------
-- Inserting default values
--------

INSERT INTO users  -- temporary values for dev process --
    (password, name, surname, phone_number, email, role)
VALUES
    ('824bcb1c0446d078:6ec937e670eea75bc7dd2d8d30b41807690d4955b6c902f1dc7e46e24d25670e',
     'Manager', 'Managerov', '+222222222222', 'unnamed.hotel@gmail.com', 2),
    ('ca1f23e567162130:c4987aef350c32d8720c8d97fe215cd9a2e228d9c016a61647c9dbd1abb24b03',
     'User', 'Userov', '+333333333333', 'user.unnamed.hotel@gmail.com', 1);

INSERT INTO room_types  -- temporary values for dev process --
    (capacity,    beds_types, class, description)
VALUES
    (2,           '1D',       1,     'desc_01'),
    (2,           '1T',       1,     'desc_02'),
    (3,           '1T, 1S',   2,     'desc_03'),
    (2,           '1T',       2,     'desc_04'),
    (3,           '1T, 1S',   2,     'desc_04'),
    (3,           '1D, 1S',   3,     'desc_05'),
    (4,           '1T, 2S',   3,     'desc_05'),
    (2,           '1D',       4,     'desc_06'),
    (2,           '1D',       4,     'desc_06'),
    (4,           '1D, 1T',   3,     'desc_05');

INSERT INTO rooms  -- temporary values for dev process --
    (room_number, floor, room_type_id)
VALUES
    (101,           1,     1),
    (102,           1,     2),
    (103,           1,     3),
    (104,           1,     4),
    (105,           1,     5),
    (106,           1,     6),
    (107,           1,     7),
    (108,           1,     8),
    (109,           1,     9),
    (110,           1,     10),
    (201,           2,     1),
    (202,           2,     2),
    (203,           2,     3),
    (204,           2,     4),
    (205,           2,     5),
    (206,           2,     6),
    (207,           2,     7),
    (208,           2,     8),
    (209,           2,     9),
    (210,           2,     10),
    (301,           3,     1),
    (302,           3,     2),
    (303,           3,     3),
    (304,           3,     4),
    (305,           3,     5),
    (306,           3,     6),
    (307,           3,     7),
    (308,           3,     8),
    (309,           3,     9),
    (310,           3,     10);

INSERT INTO reservations  -- temporary values for dev process --
    (user_id, room_id, date_from,    date_to,      guests_amount)
VALUES
    (2,       2,       '2021-11-29', '2021-11-30', 2),
    (2,       2,       '2021-11-16', '2021-11-25', 2);

INSERT INTO requests  -- temporary values for dev process --
    (user_id, room_id, date_from,    date_to,       date_of_manager_response,      adults_amount, children_amount, class, status, price)
VALUES
    (2,       3,       '2021-11-29', '2021-11-30',  null,                             1,             1,                1,    1,       900),
    (2,       4,       '2021-11-29', '2021-11-30',  '2021-11-28',                     1,             1,                2,    2,       900),
    (2,       5,       '2021-11-27', '2021-11-28',  '2021-11-15',                     1,             1,                1,    2,       900);

INSERT INTO room_images
    (room_type_id, img_path)
VALUES
    (1,       'img/double_standard.jpg'),
    (1,       'img/standard_01.jpg'),
    (1,       'img/standard_02.jpg'),
    (1,       'img/standard_03.jpg'),

    (2,       'img/twin_standard.jpg'),
    (2,       'img/standard_01.jpg'),
    (2,       'img/standard_02.jpg'),
    (2,       'img/standard_03.jpg'),

    (3,       'img/twin_single_upgraded.jpg'),
    (3,       'img/upgraded_01.jpg'),
    (3,       'img/standard_02.jpg'),
    (3,       'img/standard_03.jpg'),

    (4,       'img/twin_upgraded_01.jpg'),
    (4,       'img/upgraded_01.jpg'),
    (4,       'img/standard_02.jpg'),
    (4,       'img/standard_03.jpg'),

    (5,       'img/twin_upgraded_02.jpg'),
    (5,       'img/upgraded_01.jpg'),
    (5,       'img/standard_02.jpg'),
    (5,       'img/standard_03.jpg'),

    (6,       'img/double_suite.jpg'),
    (6,       'img/standard_01.jpg'),
    (6,       'img/standard_02.jpg'),
    (6,       'img/standard_03.jpg'),

    (7,       'img/twin_suite.jpg'),
    (7,       'img/standard_01.jpg'),
    (7,       'img/standard_02.jpg'),
    (7,       'img/standard_03.jpg'),

    (8,       'img/double_deluxe_01.jpg'),
    (8,       'img/deluxe_01.jpg'),
    (8,       'img/deluxe_02.jpg'),
    (8,       'img/deluxe_03.jpg'),
    (8,       'img/deluxe_04.jpg'),

    (9,       'img/double_deluxe_02.jpg'),
    (9,       'img/deluxe_01.jpg'),
    (9,       'img/deluxe_02.jpg'),
    (9,       'img/deluxe_03.jpg'),
    (9,       'img/deluxe_04.jpg'),

    (10,       'img/double_twin_suite_01.jpg'),
    (10,       'img/double_twin_suite_02.jpg'),
    (10,       'img/upgraded_01.jpg'),
    (10,       'img/standard_02.jpg'),
    (10,       'img/standard_03.jpg');
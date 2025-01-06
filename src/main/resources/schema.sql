-- le fac drop cu cascade in caz de orice
DROP TABLE IF EXISTS repair_request_spare_part CASCADE;
DROP TABLE IF EXISTS repair_request CASCADE;
DROP TABLE IF EXISTS fuel_log CASCADE;
DROP TABLE IF EXISTS maintenance_log CASCADE;
DROP TABLE IF EXISTS vehicle CASCADE;
DROP TABLE IF EXISTS garage CASCADE;

CREATE TABLE garage (
id SERIAL PRIMARY KEY, -- SERIAL, in PostGreSQL, da o valoare unica la id, incrementata mereu cand un nou rand e introdus in tabela
location VARCHAR(100) NOT NULL,
capacity INT NOT NULL,
occupied_spaces INT NOT NULL);

CREATE TABLE vehicle (
id SERIAL PRIMARY KEY,
brand VARCHAR(50) NOT NULL,
model VARCHAR(50) NOT NULL,
production_year INT NOT NULL,
type VARCHAR(50) NOT NULL,
capacity INT NOT NULL,
status VARCHAR(20) NOT NULL,
garage_id INT,
FOREIGN KEY (garage_id) REFERENCES garage(id) ON DELETE SET NULL);

CREATE TABLE maintenance_log (
id SERIAL PRIMARY KEY,
vehicle_id INT NOT NULL,
date DATE NOT NULL,
description VARCHAR(255),
cost DOUBLE PRECISION NOT NULL,
next_scheduled_date DATE,
FOREIGN KEY (vehicle_id) REFERENCES vehicle(id) ON DELETE CASCADE);

CREATE TABLE fuel_log (
id SERIAL PRIMARY KEY,
vehicle_id INT NOT NULL,
date DATE NOT NULL,
fuel_added_liters DOUBLE PRECISION NOT NULL,
cost_per_liter DOUBLE PRECISION NOT NULL,
total_cost DOUBLE PRECISION NOT NULL,
FOREIGN KEY (vehicle_id) REFERENCES vehicle(id) ON DELETE CASCADE);

CREATE TABLE spare_part (
id SERIAL PRIMARY KEY,
name VARCHAR(100) NOT NULL,
type VARCHAR(50) NOT NULL,
price DOUBLE PRECISION NOT NULL,
stock_quantity INT NOT NULL);

CREATE TABLE repair_request (
id SERIAL PRIMARY KEY,
vehicle_id INT NOT NULL,
issue_description VARCHAR(500),
request_date DATE NOT NULL,
status VARCHAR(50) NOT NULL,
total_cost DOUBLE PRECISION NOT NULL,
FOREIGN KEY (vehicle_id) REFERENCES vehicle(id) ON DELETE CASCADE);

CREATE TABLE repair_request_spare_part (
repair_request_id INT NOT NULL,
spare_part_id INT NOT NULL,
PRIMARY KEY (repair_request_id, spare_part_id),
FOREIGN KEY (repair_request_id) REFERENCES repair_request(id) ON DELETE CASCADE,
FOREIGN KEY (spare_part_id) REFERENCES spare_part(id) ON DELETE CASCADE);

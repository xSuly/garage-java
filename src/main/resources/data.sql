INSERT INTO garage (location, capacity, occupied_spaces)
VALUES ('Downtown Garage', 100, 50), ('Uptown Garage', 200, 120);

INSERT INTO vehicle (brand, model, production_year, type, capacity, status, garage_id)
VALUES
('Toyota', 'Corolla', 2020, 'Sedan', 5, 'Available', 1),
('Ford', 'Focus', 2019, 'Hatchback', 5, 'In Use', 1),
('BMW', 'X5', 2021, 'SUV', 7, 'Available', 2);

INSERT INTO maintenance_log (vehicle_id, date, description, cost, next_scheduled_date)
VALUES
(1, '2024-01-15', 'Oil change', 50.0, '2024-07-15'),
(2, '2024-02-20', 'Tire rotation', 30.0, '2024-08-20');

INSERT INTO fuel_log (vehicle_id, date, fuel_added_liters, cost_per_liter, total_cost)
VALUES
(1, '2024-01-10', 50, 1.5, 75.0),
(3, '2024-02-01', 60, 1.8, 108.0);

INSERT INTO spare_part (name, type, price, stock_quantity)
VALUES
('Brake Pads', 'Brakes', 40.0, 100),
('Oil Filter', 'Engine', 20.0, 200);

INSERT INTO repair_request (vehicle_id, issue_description, request_date, status, total_cost)
VALUES
(1, 'Brake replacement', '2024-01-12', 'Completed', 150.0),
(2, 'Engine diagnostics', '2024-02-05', 'Pending', 200.0);

INSERT INTO repair_request_spare_part (repair_request_id, spare_part_id)
VALUES
(1, 1),
(2, 2);

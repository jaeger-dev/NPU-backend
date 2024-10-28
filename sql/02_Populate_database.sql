INSERT INTO users (id, username, email, password) VALUES
    (1, 'testuser1', 'test1@example.com', 'ABC'),
    (2, 'testuser2', 'test2@example.com', 'DEF');

INSERT INTO elements (id, name, color) VALUES
    (1,'1x1 Brick', 'white'),
    (2,'1x2 Brick', 'black'),
    (3,'2x4 Plate', 'white'),
    (4,'2x2 Brick', 'red');

 INSERT INTO creations (id, title, description, user_id) VALUES
    (1,'RobotCar', 'This is a nice robot car', 1),
    (2,'Pirate ship', 'Pirate ship made out of scales', 2);

 INSERT INTO creation_elements (creation_id, element_id) VALUES
    (1,1),
    (1,3),
    (2,2),
    (2,4);





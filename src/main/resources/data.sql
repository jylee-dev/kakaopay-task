INSERT INTO room (room_id, user_id) VALUES ('R0001', 2011001);
INSERT INTO room (room_id, user_id) VALUES ('R0001', 2011002);
INSERT INTO room (room_id, user_id) VALUES ('R0001', 2011003);
INSERT INTO room (room_id, user_id) VALUES ('R0001', 2011004);

INSERT INTO room (room_id, user_id) VALUES ('R0002', 2011005);
INSERT INTO room (room_id, user_id) VALUES ('R0002', 2011006);
INSERT INTO room (room_id, user_id) VALUES ('R0002', 2011007);

INSERT INTO seed (token, amount, cnt, user_id, room_id) VALUES ('Aa0', 10000, 3, 2011001, 'R0001');

INSERT INTO receive (token, seq, amount) VALUES ('Aa0', 1, 5000);
INSERT INTO receive (token, seq, amount) VALUES ('Aa0', 2, 2000);
INSERT INTO receive (token, seq, amount) VALUES ('Aa0', 3, 3000);
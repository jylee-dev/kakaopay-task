CREATE TABLE seed -- 뿌리기 정보
(
  token			VARCHAR(3)		NOT NULL
, amount		INT				NOT NULL
, cnt			INT				NOT NULL
, user_id		INT				NOT NULL
, room_id		VARCHAR(5)		NOT NULL
, reg_date	 	DATE			DEFAULT	SYSDATE
);
ALTER TABLE seed ADD CONSTRAINT seed_pk PRIMARY KEY (token);

CREATE TABLE receive -- 분배된 금액에 대한 받기 정보
(
  token			VARCHAR(3)		NOT NULL
, seq			INT				NOT NULL
, amount		INT				NOT NULL
, user_id		INT
);
ALTER TABLE receive ADD CONSTRAINT receive_pk PRIMARY KEY (token, seq);

CREATE TABLE room -- 대화방 별 사용자 정보
(
  room_id		VARCHAR(5)		NOT NULL
, user_id		INT				NOT NULL
);
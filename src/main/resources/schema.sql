CREATE TABLE seed -- �Ѹ��� ����
(
  token			VARCHAR(3)		NOT NULL
, amount		INT				NOT NULL
, cnt			INT				NOT NULL
, user_id		INT				NOT NULL
, room_id		VARCHAR(5)		NOT NULL
, reg_date	 	DATE			DEFAULT	SYSDATE
);
ALTER TABLE seed ADD CONSTRAINT seed_pk PRIMARY KEY (token);

CREATE TABLE receive -- �й�� �ݾ׿� ���� �ޱ� ����
(
  token			VARCHAR(3)		NOT NULL
, seq			INT				NOT NULL
, amount		INT				NOT NULL
, user_id		INT
);
ALTER TABLE receive ADD CONSTRAINT receive_pk PRIMARY KEY (token, seq);

CREATE TABLE room -- ��ȭ�� �� ����� ����
(
  room_id		VARCHAR(5)		NOT NULL
, user_id		INT				NOT NULL
);
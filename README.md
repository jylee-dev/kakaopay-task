# kakaopay task - 머니 뿌리기 기능 API 개발
## 목차
- [프로젝트 소개](#프로젝트-소개)
- [개발 환경](#개발-환경)
- [빌드 및 실행](#빌드-및-실행)
- [해결 전략](#해결-전략)
  - [0. DB](#0.-DB)
  - [1. 뿌리기 API](#1.-뿌리기-API)
  - [2. 받기 API](#2.-받기-API)
  - [3. 조회 API](#3-조회-API)
  - [4. 에러 응답 처리](#4.-에러-응답-처리)
---
## 프로젝트 소개
- 카카오페이 머니 뿌리기 기능 API 개발
  - 뿌리기 API : 대화방에서 뿌릴 금액과 받아갈 대상의 숫자를 입력하여 뿌리기 요청
  - 받기 API : 뿌리기 요청을 통해 분배된 건 중 미할당된 건에 대해 받기
  - 조회 API : 뿌리기 건에 대한 현재 상태 조회 (뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보)

## 개발 환경
- JAVA 8
- Gradle
- SpringBoot 2.1.4
- Mybatis
- H2 Database
- Lombok
- JUnit
## 빌드 및 실행
- Git, Java는 설치되어 있다고 가정
  ```
  > git clone https://github.com/jylee-dev/kakaopay-task.git
  > cd kakaopay-task
  > gradlew clean build
  > java -jar build/libs/kakaopaytask-0.0.1-SNAPSHOT.jar
  ```
## 해결 전략
### 0. DB
- InMemory DB H2 사용
- MyBatis 프레임워크 사용 : JDBC 작업 시 SQL, 파라미터, 결과 값 매핑
- resouces 디렉토리에 schema.sql, data.sql 스크립트 추가 : 프로젝트 실행 시 자동 테이블 생성 및 초기 데이터 세팅
- 뿌리기 정보(seed)와 받기 정보(receive)를 master-detail 구조로 설계
- room 테이블 생성 및 초기 데이터 세팅 : 뿌리기건 받기 시 해당 대화방에 속한 사용자인지 확인용
	```sql
	-- schema.sql
	CREATE TABLE seed -- 뿌리기 정보
	(
	  token		VARCHAR(3)		NOT NULL
	, amount		INT			NOT NULL
	, cnt			INT			NOT NULL
	, user_id		INT			NOT NULL
	, room_id		VARCHAR(5)		NOT NULL
	, reg_date	 	DATE			DEFAULT	SYSDATE
	);
	ALTER TABLE seed ADD CONSTRAINT seed_pk PRIMARY KEY (token);

	CREATE TABLE receive -- 분배된 금액에 대한 받기 정보
	(
	  token		VARCHAR(3)		NOT NULL
	, seq			INT			NOT NULL
	, amount		INT			NOT NULL
	, user_id		INT
	);
	ALTER TABLE receive ADD CONSTRAINT receive_pk PRIMARY KEY (token, seq);

	CREATE TABLE room -- 대화방 별 사용자 정보
	(
	  room_id		VARCHAR(5)		NOT NULL
	, user_id		INT			NOT NULL
	);
	```
	```sql
	-- data.sql
	INSERT INTO room (room_id, user_id) VALUES ('R0001', 2011001);
	INSERT INTO room (room_id, user_id) VALUES ('R0001', 2011002);
	INSERT INTO room (room_id, user_id) VALUES ('R0001', 2011003);
	INSERT INTO room (room_id, user_id) VALUES ('R0001', 2011004);

	INSERT INTO room (room_id, user_id) VALUES ('R0002', 2011005);
	INSERT INTO room (room_id, user_id) VALUES ('R0002', 2011006);
	INSERT INTO room (room_id, user_id) VALUES ('R0002', 2011007);
	```
### 1. 뿌리기 API
- **INPUT**
	- X-USER-ID, X-ROOM-ID, 뿌릴 금액, 뿌릴 인원
- **OUTPUT**
	- TOKEN
- **TASK**
	- 고유 TOKEN 생성, 인원수 별로 금액을 분배하여 저장
- **실행 결과**
	- 사용자(ID:'2011001')가 '3'명이 속해 있는 대화방(ID:'R0001')에 '10000'원 뿌리기
	```
	POST /seed/10000/3 HTTP/1.1
	Host: localhost:8080
	Content-Type: application/json
	X-USER-ID: 2011001
	X-ROOM-ID: R0001
	```
	```
	{
	    "status": "SUCCESS",
	    "message": "seed success!",
	    "data": {
		"token": "Vk6"
	    }
	}
	```
- **TOKEN 생성 로직**
	```java
	// token 생성 (3자리 랜덤 문자열 = (영대문자) + (영소문자) + (숫자))
	public String getToken() {
		Random rand = new Random();
		char[] chrArry = new char[3];
		chrArry[0] = (char)(rand.nextInt(26) + 'A'); // 'A'~'Z' 랜덤 문자
		chrArry[1] = (char)(rand.nextInt(26) + 'a'); // 'a'~'z' 랜덤 문자
		chrArry[2] = (char)(rand.nextInt(10) + '0'); // '0'~'9' 랜덤 문자

		return String.valueOf(chrArry);		
	}
	```
- **금액 분배 로직**
	```java
	// 인원수별로 금액 분배
	public int[] distribution(int amount, int cnt) {
		Random rand = new Random();
		int[] amountArry = new int[cnt]; // 분배된 금액
		int remain = amount; // 잔액

		for (int i = 0; i < cnt; i ++) {
			if (i != cnt -1) {
				amountArry[i] = (remain) * (rand.nextInt(9) + 1) / 100 * 10; // 남은 금액에서 10~90% 중 랜덤으로 분배, 1원 단위 금액 안나오도록 처리
				remain -= amountArry[i]; // 잔액 차감
			} else {				
				amountArry[i] = remain; // 잔액이 남지 않도록 마지막 값은 남은 금액으로 저장
			}			
		}
		return amountArry;
	}
	```    
### 2. 받기 API
- **INPUT**
	- X-USER-ID, TOKEN
- **OUTPUT**
	- 받은 금액
- **TASK**
	- 금액 분배된 건 중 할당되지 않은 분배건을 해당 사용자에게 할당
- **실행 결과**
	- 사용자(ID:'2011003')가 뿌리기 건(토큰:'Vk6') 받기
	```http
	PUT /receive/Vk6 HTTP/1.1
	Host: localhost:8080
	Content-Type: application/json
	X-USER-ID: 2011003
	```
	```json
	{
	    "status": "SUCCESS",
	    "message": "receive success!",
	    "data": {
		"received amount": 3000
	    }
	}
	```	
- **받기 로직**
	```java
	// 미할당된 분배건 검색 및 할당
	int receivedAmount = -1; // 받은 금액
	for (Receive receive : receiveMapper.selectReceiveListByToken(token)) {
		if (receive.getUserId() == null || receive.getUserId().equals(null)) {
			receive.setUserId(userId);  // 할당 받은 userId 값 세팅
			receiveMapper.updateUserIdBySeq(receive); // 할당 받은 userId 정보 업데이트
			receivedAmount = receive.getAmount(); // 받은 금액 저장
			break;
		} else if (receive.getUserId() == userId) { // 이미  받은 사람일 경우 받기 불가
			throw new ApiException(ApiError.RECEIVE_DUPLICATE);
		}
	}

	// 미할당 건을 찾지 못한 경우 받기 불가 (받기 완료된 뿌리기 건)
	if (receivedAmount == -1) {
		throw new ApiException(ApiError.RECEIVE_FINISH);
	}
	```
- **에러 처리 경우**
	- 해당 사용자가 해당 뿌리기 건에 대해서 이미 받았을 경우
	- 자신이 뿌린 건을 받는 경우
	- 해당 사용자가 해당 뿌리기 대화방에 속하지 않을 경우
	- 뿌린지 10분이 지난 뿌리기 건일 경우
	- 받기가 완료된 뿌리기 건일 경우
	
### 3. 조회 API
- **INPUT**
	- X-USER-ID, TOKEN
- **OUTPUT**
	- 뿌린 시각, 뿌린 금액, 받기 완료된 금액, 받기 완료된 정보([받은 금액, 받은 사용자 아이디]리스트
- **실행 결과**
	- 사용자(ID:'2011001')가 뿌리기 건(TOKEN:'Vk6') 조회하기
	```http
	GET /list/Vk6 HTTP/1.1
	Host: localhost:8080
	Content-Type: application/json
	X-USER-ID: 2011001
	```	
	```json
  {
      "status": "SUCCESS",
      "message": "receive success!",
      "data": {
          "received amount": 3000,
          "total amount": 10000,
          "time": "2020-11-23 18:36:09",
          "receive info": [
              {
                  "amount": 3000,
                  "receivedUser": 2011003
              }
          ]
      }
  }
	```
- **에러 처리 경우**
	- 유효하지 않은 TOKEN일 경우
	- 자신이 뿌린 건이 아닌 경우
	- 뿌린지 7일이 지난 뿌리기 건일 경우

### 4. 에러 응답 처리
- **에러 코드 관리**
	```java
	public enum ApiError {
		ERROR(0,"오류"),
		SEED_ERROR(10, "뿌리기 오류"),
		RECEIVE_ERROR(20, "받기 오류"),
		RECEIVE_YOURS(21, "자신이 뿌린 건은 받을 수 없습니다."),
		RECEIVE_PERIOD_OVER(22, "뿌린지 10분이 지난 건은 받을 수 없습니다."),
		NOT_SAME_ROOM_USER(23, "해당 뿌리기 대화방에 속하지 않은 사람은 받을 수 없습니다."),
		RECEIVE_DUPLICATE(24, "이미 받은 사람은 받을 수 없습니다."),
		RECEIVE_FINISH(25, "모든 받기가 완료된 건입니다."),
		SEARCH_ERROR(30, "조회 오류"),
		INVALID_TOKEN(31, "유효하지 않은 token 입니다."),
		NOT_YOUR_SEED(32, "자신이 뿌린 건이 아니라서 조회할 수 없습니다."),
		INQUIRY_PERIOD_OVER(33, "뿌린지 7일이 지난 건은 조회할 수 없습니다.");

		@Getter
		private final int code;
		@Getter
		private final String desc;	

		ApiError(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}
	}
	```
- **Example**
	- (받기 API) 자신이 뿌린 건을 받는 경우 에러
	```http
	PUT /receive/Vk6 HTTP/1.1
	Host: localhost:8080
	Content-Type: application/json
	X-USER-ID: 2011001
	```
	```json
	{
	    "status": "ERROR",
	    "error": "RECEIVE_YOURS",
	    "message": "자신이 뿌린 건은 받을 수 없습니다."
	}
	```


--네트워크 프로그래밍 미니 프로젝트
/*
클라이언트의 대화내역을 저장할 테이블 생성
저장할 내용: 시퀀스, 대화명, 대화내용, 현재시간
테이블명: chating_tb
*/

--테이블 생성
create table chating_tb (
    idx number,
    name varchar2(100) not null,
    message varchar2(500),
    time varchar2(100) default sysdate,
    primary key(idx)
);

--테이블 삭제
drop table chating_tb;

--시퀀스 생성
create sequence seq_chat_num
    increment by 1   /* 증가치 */
    start with 0     /* 시작값 */
    nomaxvalue       /* 최대값 */
    minvalue 0       /* 최소값 */
    nocycle          /* 사이클 사용여부 */
    nocache;         /* 캐쉬 사용여부 */

--데이터사전에서 생성된 시퀀스 확인
select * from user_sequences; --데이터사전

--시퀀스 수정
alter sequence seq_chat_num
    increment by 1   /* 증가치 */
    start with 0     /* 시작값 */
    nomaxvalue       /* 최대값 */
    minvalue 0       /* 최소값 */
    nocycle          /* 사이클 사용여부 */
    nocache;         /* 캐쉬 사용여부 */

--시퀀스 시작 번호는 변경할 수 없으므로 시퀀스 삭제 후 재 생성
drop sequence seq_chat_num;

--시퀀스 생성
create sequence seq_chat_num
    increment by 1   /* 증가치 */
    start with 1     /* 시작값 */
    nomaxvalue       /* 최대값 */
    minvalue 1       /* 최소값 */
    nocycle          /* 사이클 사용여부 */
    nocache;         /* 캐쉬 사용여부 */

--전체 레코드 삭제
delete from chating_tb;


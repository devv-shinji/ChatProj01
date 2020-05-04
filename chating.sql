--��Ʈ��ũ ���α׷��� �̴� ������Ʈ
/*
Ŭ���̾�Ʈ�� ��ȭ������ ������ ���̺� ����
������ ����: ������, ��ȭ��, ��ȭ����, ����ð�
���̺��: chating_tb
*/

--���̺� ����
create table chating_tb (
    idx number,
    name varchar2(100) not null,
    message varchar2(500),
    time varchar2(100) default sysdate,
    primary key(idx)
);
-----------------------------��Ʈ��ũ ���α׷��� ��-----------------------------
--���̺� ����
create table chatting_tb (
    idx number,
    client_name varchar2(500),
    client_message varchar2(500),
    senddate date
);
--�ڸ�Ʈ �߰�
comment on column chatting_tb.idx is '�Ϸù�ȣ';
comment on column chatting_tb.client_name is '�̸�';
comment on column chatting_tb.client_message is '�޼���';
comment on column chatting_tb.senddate is '���۳�¥�ͽð�';

--������ ����
create sequence chatting_seq
    increment by 1   /* ����ġ */
    start with 1     /* ���۰� */
    nomaxvalue       /* �ִ밪 */
    minvalue 1       /* �ּҰ� */
    nocycle          /* ����Ŭ ��뿩�� */
    nocache;         /* ĳ�� ��뿩�� */
-----------------------------��Ʈ��ũ ���α׷��� ��-----------------------------



--���̺� ����
drop table chating_tb;

--������ ����
create sequence seq_chat_num
    increment by 1   /* ����ġ */
    start with 0     /* ���۰� */
    nomaxvalue       /* �ִ밪 */
    minvalue 0       /* �ּҰ� */
    nocycle          /* ����Ŭ ��뿩�� */
    nocache;         /* ĳ�� ��뿩�� */

--�����ͻ������� ������ ������ Ȯ��
select * from user_sequences; --�����ͻ���

--������ ����
alter sequence seq_chat_num
    increment by 1   /* ����ġ */
    start with 0     /* ���۰� */
    nomaxvalue       /* �ִ밪 */
    minvalue 0       /* �ּҰ� */
    nocycle          /* ����Ŭ ��뿩�� */
    nocache;         /* ĳ�� ��뿩�� */

--������ ���� ��ȣ�� ������ �� �����Ƿ� ������ ���� �� �� ����
drop sequence seq_chat_num;

--������ ����
create sequence seq_chat_num
    increment by 1   /* ����ġ */
    start with 1     /* ���۰� */
    nomaxvalue       /* �ִ밪 */
    minvalue 1       /* �ּҰ� */
    nocycle          /* ����Ŭ ��뿩�� */
    nocache;         /* ĳ�� ��뿩�� */

--��ü ���ڵ� ����
delete from chating_tb;


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


-- 운영자 정보 저장
INSERT into admin (admin_no, email, admin_id, name, phone_number, created_date, city, country, detail_address)
values (1, 'lee33397@naver.com', 'lee33397', '이동석', '010-9999-1234', now(), 'Seoul', 'KR', '강서구');

INSERT into admin (admin_no, email, admin_id, name, phone_number, created_date, city, country, detail_address)
values (2, 'lee33398@naver.com', 'lee33398', '홍길동', '010-0000-0001', now(), 'Seoul', 'KR', '동대문구');

INSERT into admin (email, admin_id, name, phone_number, created_date, city, country, detail_address)
values ('lee33399@naver.com', 'lee33399', '김아무개', '010-0000-0002', now(), 'Seoul', 'KR', '중구');

INSERT into admin (email, admin_id, name, phone_number, created_date, city, country, detail_address)
values ('lee33391@naver.com', 'lee33391', '이아무개', '010-0000-0003', now(), 'Seoul', 'KR', '강남구');

-- 쇼핑몰 정보
INSERT into mall (mall_no, admin_no, name, country_type, country, city, detail_address, created_date)
values (1, 1, 'lee33397의 쇼핑몰', 'KR', 'KR', 'Seoul', '강남구', now());

INSERT into mall (mall_no, admin_no, name, country_type, country, city, detail_address, created_date)
values (2, 1, 'lee33397의 쇼핑몰 - EN', 'EN', 'KR', 'Seoul', '강남구', now());

INSERT into mall (mall_no, admin_no, name, country_type, country, city, detail_address, created_date)
values (3, 2, 'lee33398의 쇼핑몰', 'KR', 'KR', 'Seoul', '강남구', now());

INSERT into mall (mall_no, admin_no, name, country_type, country, city, detail_address, created_date)
values (4, 2, 'lee33398의 쇼핑몰 - EN', 'EN', 'EN', 'Seoul', '강남구', now());

-- 회원 정보
insert into member (mall_no, member_id, name, email, gender, phone_number, birthdate, status, created_date)
values (1, 'custom001', '회원001', 'custom001@naver.com', 'F', '010-0000-0001', '1992-12-29', 'ACTIVE', now());

insert into member (mall_no, member_id, name, email, gender, phone_number, birthdate, status, created_date)
values (1, 'custom002', '회원002', 'custom002@naver.com', 'F', '010-0000-0002', '1992-12-30', 'ACTIVE', now());

insert into member (mall_no, member_id, name, email, gender, phone_number, birthdate, status, created_date)
values (1, 'custom003', '회원003', 'custom003@naver.com', 'F', '010-0000-0003', '1992-11-30', 'ACTIVE', now());

insert into member (mall_no, member_id, name, email, gender, phone_number, birthdate, status, created_date)
values (1, 'custom004', '회원004', 'custom004@naver.com', 'F', '010-0000-0004', '1992-11-29', 'ACTIVE', now());
-- 운영자 정보 저장
INSERT into admin (adm_email, adm_id, adm_name, adm_phone_number, created_date, city, country, detail_address)
values ('lee33397@naver.com', 'lee33397', '이동석', '010-9999-1234', now(), 'Seoul', 'KR', '강서구');

INSERT into admin (adm_email, adm_id, adm_name, adm_phone_number, created_date, city, country, detail_address)
values ('lee33398@naver.com', 'lee33398', '홍길동', '010-0000-0001', now(), 'Seoul', 'KR', '동대문구');

INSERT into admin (adm_email, adm_id, adm_name, adm_phone_number, created_date, city, country, detail_address)
values ('lee33399@naver.com', 'lee33399', '김아무개', '010-0000-0002', now(), 'Seoul', 'KR', '중구');

INSERT into admin (adm_email, adm_id, adm_name, adm_phone_number, created_date, city, country, detail_address)
values ('lee33391@naver.com', 'lee33391', '이아무개', '010-0000-0003', now(), 'Seoul', 'KR', '강남구');

-- 쇼핑몰 정보
INSERT into mall (mall_no, adm_no, mall_name, mall_country_type, country, city, detail_address, created_date)
values (1, 1, 'lee33397의 쇼핑몰', 'KR', 'KR', 'Seoul', '강남구', now());

-- 회원 정보
insert into member (mall_no, mbr_id, mbr_name, mbr_email, mbr_gender, mbr_phone_number, mbr_birthdate, mbr_status, created_date)
values (1, 'custom001', '회원001', 'custom001@naver.com', 'F', '010-0000-0001', '1992-12-29', 'ACTIVE', now());

insert into member (mall_no, mbr_id, mbr_name, mbr_email, mbr_gender, mbr_phone_number, mbr_birthdate, mbr_status, created_date)
values (1, 'custom002', '회원002', 'custom002@naver.com', 'F', '010-0000-0002', '1992-12-30', 'ACTIVE', now());

insert into member (mall_no, mbr_id, mbr_name, mbr_email, mbr_gender, mbr_phone_number, mbr_birthdate, mbr_status, created_date)
values (1, 'custom003', '회원003', 'custom003@naver.com', 'F', '010-0000-0003', '1992-11-30', 'ACTIVE', now());

insert into member (mall_no, mbr_id, mbr_name, mbr_email, mbr_gender, mbr_phone_number, mbr_birthdate, mbr_status, created_date)
values (1, 'custom004', '회원004', 'custom004@naver.com', 'F', '010-0000-0004', '1992-11-29', 'ACTIVE', now());
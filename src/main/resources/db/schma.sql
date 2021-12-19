-- 테스트 초기화 (개발기간에만 사용)
DROP TABLE IF EXISTS admin;
DROP TABLE IF EXISTS mall;
DROP TABLE IF EXISTS member;

-- 운영자(Admin)
create table admin
(
    adm_no             bigint generated by default as identity not null,
    adm_id             varchar(50)                             not null,
    adm_name           varchar(50)                             not null,
    adm_email          varchar(50)                             not null,
    adm_phone_number   varchar(14),
    country            varchar(2),
    city               varchar(50),
    street             varchar(100),
    zipcode            varchar(10),
    detail_address     varchar(100),
    created_date       timestamp,
    last_modified_date timestamp,
    primary key (adm_no)
);
ALTER TABLE admin
    ADD CONSTRAINT UQ_ADM_ID UNIQUE (adm_id);
ALTER TABLE admin
    ADD CONSTRAINT UQ_ADM_EMAIL UNIQUE (adm_email);


-- 쇼핑몰(Mall)
create table mall
(
    mall_no            bigint generated by default as identity not null,
    adm_no             bigint                                  not null,
    mall_name          varchar(50)                             not null,
    mall_country_type  varchar(2) default 'KR'                 not null,
    country            varchar(2),
    city               varchar(50),
    street             varchar(100),
    zipcode            varchar(10),
    detail_address     varchar(100),
    created_date       timestamp,
    last_modified_date timestamp,
    primary key (mall_no)
);
ALTER TABLE mall
    ADD CONSTRAINT UK_MALL_NAME UNIQUE (mall_name);
ALTER TABLE mall
    ADD CONSTRAINT FK_MALL_ADMIN FOREIGN KEY (adm_no) REFERENCES admin;

-- 회원(Member)
create table member
(
    mbr_no             bigint generated by default as identity not null,
    mall_no            bigint                                  not null,
    mbr_id             varchar(50)                             not null,
    mbr_name           varchar(10)                             not null,
    mbr_email          varchar(50)                             not null,
    mbr_gender         varchar(2)  default 'F',
    mbr_phone_number   varchar(14),
    mbr_birthdate      date,
    mbr_status         varchar(10) default 'ACTIVE',
    created_date       timestamp,
    last_modified_date timestamp,
    primary key (mbr_no)
);
ALTER TABLE member
    ADD CONSTRAINT UQ_MBR_ID UNIQUE (mbr_id);
ALTER TABLE member
    ADD CONSTRAINT UQ_MBR_EMAIL UNIQUE (mbr_email);
ALTER TABLE member
    ADD CONSTRAINT FK_MBR_MALL FOREIGN KEY (mall_no) REFERENCES mall;

package com.practice.project.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Employee")
@Getter
@Setter
@NoArgsConstructor
public class Employee {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; //고유id
    private String name; //이름
    private String number; //연락처
    private String kakaoid; //카카오톡 아이디
    private int shop_id; //샵 아이디
}

package com.test.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNo;
	
	@Column(unique =  true, nullable = false, length = 30)
    private String userId;

    @Column(nullable = false, length = 100)
    private String password;
    
    @Column(nullable = false, length = 10)
    private String name;
    
    @Column(nullable = false, length = 30)
    private String regNo;
}

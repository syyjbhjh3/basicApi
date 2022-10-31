package com.test.api.dto;

import lombok.Data;

@Data
public class UserResponseDto {
    private Long userNo;
    private String userId;
    private String name;
    private String regNo;
   
   public UserResponseDto(Long userNo, String userId, String name, String regNo){
       this.userNo = userNo;
       this.userId = userId;
       this.name = name;
       this.regNo = regNo;
   }
}
package com.whz.springboot.security.demo.model;

import lombok.*;

/** @author Strive */
@Data
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MingYueUser {
    private Long userId;
    private String username;
    private String password;
}

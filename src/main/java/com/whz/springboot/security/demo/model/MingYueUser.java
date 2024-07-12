package com.whz.springboot.security.demo.model;

import lombok.*;

import java.io.Serializable;

/** @author Strive */
@Data
public class MingYueUser implements Serializable {

    private static final long serialVersionUID = -3941399884238323953L;

    private Long userId;
    private String username;
    private String password;

}

package com.example.demo.jwt;

import com.example.demo.common.Authority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Payload {
    private String username;
    private Authority authority;
}

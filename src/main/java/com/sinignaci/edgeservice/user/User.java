package com.sinignaci.edgeservice.user;

import lombok.Builder;

import java.util.List;

@Builder
public record User(String username, String firstName, String lastName, List<String> roles) {
}

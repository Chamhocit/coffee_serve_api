package com.aptech.shop.demo.Response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CheckUserResponse {
    @JsonProperty("status")
    private boolean status;
    @JsonProperty("userName")
    private String name;
    @JsonProperty("role")
    private List<String> role;
}

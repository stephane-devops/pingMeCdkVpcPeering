package com.stephanecharron.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class VpcSetup {
    private String[] cidrs;
    private int maxAzs;
}

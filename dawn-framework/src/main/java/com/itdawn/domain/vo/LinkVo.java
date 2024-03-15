package com.itdawn.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkVo {
    private String address;

    private String description;

    private Long id;

    private String logo;

    private String name;
}

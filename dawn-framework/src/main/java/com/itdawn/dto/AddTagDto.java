package com.itdawn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//用于接收前端传过来的参数
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddTagDto {
    //备注
    private String remark;
    //标签名
    private String name;
}


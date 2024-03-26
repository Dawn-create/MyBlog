package com.itdawn.domain.vo;

import com.itdawn.domain.entity.Menu;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
//负责把指定字段返回给前端
public class RoutersVo {
    private List<Menu> menus;
}
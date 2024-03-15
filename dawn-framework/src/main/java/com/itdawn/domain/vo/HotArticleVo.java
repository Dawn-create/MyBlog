package com.itdawn.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotArticleVo {
    //封装想要展示和响应回去的字段
    private Long id;
    private String title;
    private Long viewCount;
}

package com.itdawn.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2024-03-12 14:14:07
 */

public interface LinkService extends IService<Link> {
    //查询友链
    ResponseResult getAllLink();
}

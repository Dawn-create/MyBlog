package com.itdawn.service;

import com.itdawn.domain.ResponseResult;
import com.itdawn.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}

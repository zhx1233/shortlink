/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nageoffer.shortlink.project.common.biz.user;

import cn.hutool.core.util.StrUtil;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 用户信息传输拦截器
 */
@Component
/**
 * 用户信息透传拦截器（project 端）。从请求头中解析 Gateway 或 admin Feign 注入的
 * username、userId、realName，存入 TransmittableThreadLocal。
 */
public class UserTransmitInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler) throws Exception {
        String username = decodeHeader(request.getHeader("username"));
        if (StrUtil.isNotBlank(username)) {
            String userId = request.getHeader("userId");
            String realName = decodeHeader(request.getHeader("realName"));
            UserInfoDTO userInfoDTO = new UserInfoDTO(userId, username, realName);
            UserContext.setUser(userInfoDTO);
        }
        return true;
    }

    @Override
    public void afterCompletion(@Nullable HttpServletRequest request, @Nullable HttpServletResponse response, @Nullable Object handler, Exception exception) throws Exception {
        UserContext.removeUser();
    }

    private String decodeHeader(String value) {
        return StrUtil.isBlank(value) ? value : URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}

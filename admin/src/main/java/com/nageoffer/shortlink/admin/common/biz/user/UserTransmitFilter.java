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

package com.nageoffer.shortlink.admin.common.biz.user;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 用户信息传输过滤器
 */
@RequiredArgsConstructor
/**
 * 用户信息透传过滤器——从 Gateway 注入的请求头中读取 username、userId、realName，
 * 解码后存入 TransmittableThreadLocal。使用 TTL 而非普通 ThreadLocal 是为了在
 * OpenFeign 线程池中自动传递上下文。
 */
public class UserTransmitFilter implements Filter {

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String username = decodeHeader(httpServletRequest.getHeader("username"));
        if (StrUtil.isNotBlank(username)) {
            String userId = httpServletRequest.getHeader("userId");
            String realName = decodeHeader(httpServletRequest.getHeader("realName"));
            UserInfoDTO userInfoDTO = new UserInfoDTO(userId, username, realName);
            UserContext.setUser(userInfoDTO);
        }
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.removeUser();
        }
    }

    private String decodeHeader(String value) {
        return StrUtil.isBlank(value) ? value : URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}

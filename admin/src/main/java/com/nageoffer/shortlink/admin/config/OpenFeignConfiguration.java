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

package com.nageoffer.shortlink.admin.config;

import com.nageoffer.shortlink.admin.common.biz.user.UserContext;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * openFeign 微服务调用传递用户信息配置
 */
/**
 * OpenFeign 配置——注册 RequestInterceptor，在每次 Feign 调用前自动从 ThreadLocal
 * 中读取当前用户信息（username、userId、realName）并填充到请求头，
 * 确保 project 服务能接收到完整的用户上下文。
 */
@Configuration
public class OpenFeignConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            template.header("username", encodeHeader(UserContext.getUsername()));
            template.header("userId", UserContext.getUserId());
            template.header("realName", encodeHeader(UserContext.getRealName()));
        };
    }

    private String encodeHeader(String value) {
        return value == null ? null : URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}

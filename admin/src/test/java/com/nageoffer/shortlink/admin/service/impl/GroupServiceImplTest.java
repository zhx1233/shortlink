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

package com.nageoffer.shortlink.admin.service.impl;

import com.nageoffer.shortlink.admin.common.biz.user.UserContext;
import com.nageoffer.shortlink.admin.common.biz.user.UserInfoDTO;
import com.nageoffer.shortlink.admin.dao.mapper.GroupMapper;
import com.nageoffer.shortlink.admin.dto.resp.ShortLinkGroupRespDTO;
import com.nageoffer.shortlink.admin.remote.ShortLinkActualRemoteService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GroupServiceImplTest {

    @AfterEach
    void tearDown() {
        UserContext.removeUser();
    }

    @Test
    void listGroupReturnsEmptyListWithoutRemoteCountWhenUserHasNoGroups() {
        UserContext.setUser(new UserInfoDTO("1", "test_user", "Test User"));
        GroupMapper groupMapper = mock(GroupMapper.class);
        ShortLinkActualRemoteService remoteService = mock(ShortLinkActualRemoteService.class);
        GroupServiceImpl groupService = new GroupServiceImpl(null, null, remoteService, null);
        ReflectionTestUtils.setField(groupService, "baseMapper", groupMapper);
        when(groupMapper.selectList(any())).thenReturn(List.of());

        List<ShortLinkGroupRespDTO> result = groupService.listGroup();

        assertTrue(result.isEmpty());
        verify(remoteService, never()).listGroupShortLinkCount(any());
    }
}

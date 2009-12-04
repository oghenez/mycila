/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycila.testing.plugin.spring;

import com.mycila.testing.junit.MycilaJunitRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
@RunWith(MycilaJunitRunner.class)
@SpringContext(locations = "/ctx-autowired2.xml", classes = MyJavaConfig.class)
@TransactionConfiguration(transactionManager = "myTransactionManager")
public class SpringJavaConfigTest {

    @Autowired
    Dao dao;

    @Autowired
    Autowired2Bean autowired2Bean;

    @Test
    @Transactional
    public void test() {
        assertNotNull(dao);
        assertNotNull(autowired2Bean);
        assertNotNull(autowired2Bean.dao);
        dao.save();
    }
}
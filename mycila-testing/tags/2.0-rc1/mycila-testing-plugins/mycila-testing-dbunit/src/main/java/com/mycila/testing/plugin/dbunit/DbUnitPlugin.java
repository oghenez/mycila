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
package com.mycila.testing.plugin.dbunit;

import com.mycila.testing.core.api.TestContext;
import com.mycila.testing.core.api.TestExecution;
import com.mycila.testing.core.plugin.DefaultTestPlugin;

import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
public final class DbUnitPlugin extends DefaultTestPlugin {
    @Override
    public void prepareTestInstance(TestContext context) throws Exception {
        super.prepareTestInstance(context);
    }

    @Override
    public List<String> getBefore() {
        return super.getBefore();
    }

    @Override
    public List<String> getAfter() {
        return super.getAfter();
    }

    @Override
    public void beforeTest(TestExecution testExecution) throws Exception {
        super.beforeTest(testExecution);
    }

    @Override
    public void afterTest(TestExecution testExecution) throws Exception {
        super.afterTest(testExecution);
    }

    @Override
    public void afterClass(TestContext context) throws Exception {
        super.afterClass(context);
    }
}

/*
    rassembler DB + DbUnit

    http://www.dbunit.org/faq.html#extract
    singleton DbUnit.get() ThreadLocale
    * toXmlDataSet(File out)
    * String toXmlDataSet()
    * toXmlDataSet(OutputStream out)
    * toXmlDataSet(Writer out)

    * idem pour createDtd

    * idem pour

    * getConnection

    * operations: cleanInsert, cleanUpdate, ...
    * ...

    @InjectDriver(...)
    DbUnit dbUnit

    @InjectDriver(...)
    DataSource ds

    @InjectDriver(...)
    Connection con

    @Test
    @Transactional(readonly=false, isolation=default, rollbackOn={Throwable.class}, alwaysRollback=true)


    ConnectionHolder.currentConnection();


    ThreadLocale pour Mycila.currentExecution()
 */

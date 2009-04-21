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

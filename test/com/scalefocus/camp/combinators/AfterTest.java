package com.scalefocus.camp.combinators;

import com.scalefocus.camp.combinators.usage.AfterDecorator;
import com.scalefocus.camp.combinators.usage.AfterLog;
import org.junit.Test;

public class AfterTest {
    @Test
    public void testLogAfter() {
        AfterLog.usage();
        AfterDecorator.usage();
    }
}

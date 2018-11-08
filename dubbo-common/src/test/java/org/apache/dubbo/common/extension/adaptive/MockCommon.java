package org.apache.dubbo.common.extension.adaptive;

import org.apache.dubbo.common.URL;

public class MockCommon implements Common {
    @Override
    public void method1(URL url) {
        System.out.println(url.getAbsolutePath());
    }
}

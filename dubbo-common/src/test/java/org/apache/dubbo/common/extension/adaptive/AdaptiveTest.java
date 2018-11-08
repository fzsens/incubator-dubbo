package org.apache.dubbo.common.extension.adaptive;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.junit.Test;

public class AdaptiveTest {

    @Test
    public void getCommonAdaptive() {
        URL url = URL.valueOf("mock://localhost/test");
        Common protocol =  ExtensionLoader.getExtensionLoader(Common.class).getAdaptiveExtension();
        protocol.method1(url);
    }
}

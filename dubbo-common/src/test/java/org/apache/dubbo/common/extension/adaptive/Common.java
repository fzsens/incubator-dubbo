package org.apache.dubbo.common.extension.adaptive;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

@SPI
public interface  Common {
    @Adaptive("protocol")
    void method1(URL url);
}
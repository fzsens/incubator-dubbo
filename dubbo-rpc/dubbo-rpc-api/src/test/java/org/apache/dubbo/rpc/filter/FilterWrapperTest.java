package org.apache.dubbo.rpc.filter;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Protocol;
import org.apache.dubbo.rpc.protocol.ProtocolFilterWrapper;
import org.apache.dubbo.rpc.protocol.ProtocolListenerWrapper;
import org.junit.Assert;
import org.junit.Test;

/**
 * filter wrapper test
 * Created by fzsens on 11/7/18.
 */
public class FilterWrapperTest {

    /**
     * in dubbo spi if one extension has constructor with argument X
     * and this extension implement X , then this extension will wrapper the actually implement of X
     */
    @Test
    public void getWrapperExtension() {
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension("mock");
        Assert.assertTrue(protocol instanceof ProtocolFilterWrapper || protocol instanceof ProtocolListenerWrapper);
    }
}

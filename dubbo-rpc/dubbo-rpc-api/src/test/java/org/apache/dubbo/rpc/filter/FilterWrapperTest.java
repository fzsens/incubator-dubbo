package org.apache.dubbo.rpc.filter;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.extension.SPI;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.protocol.AbstractInvoker;
import org.apache.dubbo.rpc.protocol.ProtocolFilterWrapper;
import org.apache.dubbo.rpc.protocol.ProtocolListenerWrapper;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 *
 * TODO remove
 * filter wrapper test
 * Created by fzsens on 11/7/18.
 */
public class FilterWrapperTest {

    /**
     * in dubbo spi if one extension has constructor with argument X
     * and  implement X , then it will be wrapper the actually implement of X
     */
    @Test
    public void getWrapperExtension() {
        Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension("mock");
        System.out.println(protocol);
        Assert.assertTrue(protocol instanceof ProtocolFilterWrapper || protocol instanceof ProtocolListenerWrapper);
    }

    @Test
    public void getWrapperActiveExtensions(){
        URL url = URL.valueOf("test://localhost/test");
        List<Protocol> protocols =ExtensionLoader.getExtensionLoader(Protocol.class).getActivateExtension(url,"");
        Assert.assertTrue(protocols.size() == 0);
    }

    @Test
    public void getProtocolAdaptive() {
        URL url = URL.valueOf("mock://localhost/test");
        Protocol protocol =  ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
        protocol.export(new AbstractInvoker<Object>(Object.class,url) {
            @Override
            protected Result doInvoke(Invocation invocation) {
                return null;
            }
        });
    }

    /**
     * Protocol SPI default is dubbo
     * But in this context didn't have. so throw an IllegalStateException
     */
    @Test(expected = IllegalStateException.class)
    public void getDefaultExtension() {
        Protocol protocol =  ExtensionLoader.getExtensionLoader(Protocol.class).getDefaultExtension();

    }


}

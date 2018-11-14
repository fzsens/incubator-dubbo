package org.apache.dubbo.rpc.protocl.ipc;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.Exporter;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Protocol;
import org.apache.dubbo.rpc.ProxyFactory;
import org.apache.dubbo.rpc.protocol.ipc.IpcInvoker;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IpcProtocolTest {


    private Protocol protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getAdaptiveExtension();
    private ProxyFactory proxy = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getAdaptiveExtension();


    @Test
    public void testLocalProtocol() throws Exception {
        DemoService service = new DemoServiceImpl();
        Invoker<?> invoker = proxy.getInvoker(service, DemoService.class, URL.valueOf("ipc://127.0.0.1/TestService").addParameter(Constants.INTERFACE_KEY, DemoService.class.getName()));
        assertTrue(invoker.isAvailable());
        Exporter<?> exporter = protocol.export(invoker);
       // service = proxy.getProxy(protocol.refer(DemoService.class, URL.valueOf("ipc://127.0.0.1/TestService").addParameter(Constants.INTERFACE_KEY, DemoService.class.getName())));
        // assertEquals(service.getSize(new String[]{"", "", ""}), 3);
        // service.invoke("injvm://127.0.0.1/TestService", "invoke");
        //IpcInvoker injvmInvoker = new IpcInvoker(DemoService.class, URL.valueOf("ipc://127.0.0.1/TestService"),"TestService");
        // assertFalse(injvmInvoker.isAvailable());
       // service.sayHello("ttteetetetetetetetetedddsssadaf");
        System.in.read();
    }

    @Test
    public void testInvoke() {
        DemoService service = proxy.getProxy(protocol.refer(DemoService.class, URL.valueOf("ipc://127.0.0.1/TestService").addParameter(Constants.INTERFACE_KEY, DemoService.class.getName())));
        service.sayHello("QQ爱");
    }
}

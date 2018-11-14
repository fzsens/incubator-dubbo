package org.apache.dubbo.rpc.protocol.ipc;

import org.apache.dubbo.rpc.Exporter;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.protocol.AbstractExporter;

import java.util.Map;

public class IpcExporter<T> extends AbstractExporter<T> {
    
    private final String key;

    private final Map<String, Exporter<?>> exporterMap;

    public IpcExporter(Invoker<T> invoker, String key, Map<String, Exporter<?>> exporterMap) {
        super(invoker);
        this.key = key;
        this.exporterMap = exporterMap;
    }

    @Override
    public void unexport() {
        super.unexport();
        exporterMap.remove(key);
    }
}

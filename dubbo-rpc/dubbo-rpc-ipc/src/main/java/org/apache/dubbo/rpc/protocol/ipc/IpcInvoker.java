package org.apache.dubbo.rpc.protocol.ipc;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcResult;
import org.apache.dubbo.rpc.protocol.AbstractInvoker;
import org.apache.dubbo.rpc.protocol.ipc.mmap.MemoryMappedFile;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

/**
 *  tobe complete.
 *  1. how to know last position.
 * @param <T>
 */
public class IpcInvoker<T> extends AbstractInvoker<T> {

    String serviceKey;

    public IpcInvoker(Class<T> type, URL url, String key) {
        super(type, url);
        this.serviceKey = key;
    }

    @Override
    protected Result doInvoke(Invocation invocation) throws Throwable {
        try {
            MemoryMappedFile   mem = new MemoryMappedFile("/dubbo/ipc/send/" + serviceKey, 20000);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(invocation);
            byte[] invocationBytes = baos.toByteArray();
            mem.putInt(404,invocationBytes.length);
            mem.setBytes(408,invocationBytes,0,invocationBytes.length);
            mem.unmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new RpcResult();
    }
}

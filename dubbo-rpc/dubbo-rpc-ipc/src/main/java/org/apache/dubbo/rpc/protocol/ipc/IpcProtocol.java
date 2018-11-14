package org.apache.dubbo.rpc.protocol.ipc;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.protocol.AbstractProtocol;
import org.apache.dubbo.rpc.protocol.ipc.mmap.MemoryMappedFile;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Inter Process Communication using mmap {@link MemoryMappedFile}
 *
 * mmap will create a file and map it to memory , so we can directly operation memory (not java heap memory)
 *
 * and share this memory in different process.
 */
public class IpcProtocol extends AbstractProtocol {

    private Map<String, MappedBusServer> servers = new HashMap<>();

    @Override
    public int getDefaultPort() {
        return 0;
    }

    @Override
    public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
        URL url = invoker.getUrl();
        // export key.
        String key = serviceKey(url);

        IpcExporter<T> exporter = new IpcExporter<T>(invoker, key, exporterMap);
        exporterMap.put(key, exporter);

        createReader(url);

        return exporter;
    }

    @Override
    public void destroy() {
        super.destroy();
        for (MappedBusServer mbServer : servers.values()) {
            mbServer.stop();
        }
    }

    @Override
    public <T> Invoker<T> refer(Class<T> type, URL url) throws RpcException {
        String key = serviceKey(url);
        IpcInvoker<T> invoker = new IpcInvoker<T>(type, url, key);
        invokers.add(invoker);
        return invoker;
    }

    private synchronized void createReader(URL url) {
        String key = serviceKey(url);
        if (servers.get(key) != null) return;
        MappedBusServer mbServer = new MappedBusServer(key);
        servers.put(key, mbServer);
        Thread t = new Thread(mbServer);
        t.setDaemon(true);
        t.start();
    }

    class MappedBusServer implements Runnable {
        private String key;
        private volatile boolean running = true;
        private int position = 0;

        MappedBusServer(String key) {
            this.key = key;
        }

        void stop() {
            this.running = false;
        }

        @Override
        public void run() {
            logger.info(key + " server started!");
            MemoryMappedFile mem = null;
            try {
                mem = new MemoryMappedFile("/dubbo/ipc/send/" + key , 20000);
                while (running) {
                    int length = mem.getInt(position);
                    if (length <= 0) {
                        TimeUnit.MILLISECONDS.sleep(10);
                        continue;
                    }
                    byte[] invBytes = new byte[length];
                    mem.getBytes(position + 4, invBytes, 0, length);
                    position = position + 4 + length;
                    ByteArrayInputStream byteArray = new ByteArrayInputStream(invBytes);
                    ObjectInputStream ois = new ObjectInputStream(byteArray);
                    Invocation inv = (Invocation) ois.readObject();
                    Result result = exporterMap.get(key).getInvoker().invoke(inv);
                    if (result.hasException()) {
                        result.getException().printStackTrace();
                    }
                    // process result.
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error(key + " server is running but throw exception ", e);
            } finally {
                try {
                    if (mem != null) {
                        mem.unmap();
                    }
                } catch (Exception e) {
                    logger.error(key + " server is closing but throw exception ", e);
                }
            }
            logger.info(key + " server stopped!");
        }
    }

}
package org.apache.dubbo.rpc.protocl.ipc.mmap;

import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.protocol.ipc.mmap.MemoryMappedFile;
import org.junit.Test;

import java.io.*;
import java.util.Objects;

public class MemoryMapperFileTest {

    MemoryMappedFile mem = new MemoryMappedFile("/dubbo/objects", 20000);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);

    public MemoryMapperFileTest() throws Exception {
    }

    @Test
    public void testWriteMmap() throws Exception {
        SerializableObject serializableObject =
                new SerializableObject(11, "darwin2", new SerializableObject(22,"claire",null));
        oos.writeObject(serializableObject);
        byte[] invocationBytes = baos.toByteArray();
        mem.putInt(352, invocationBytes.length);
        mem.setBytes(352+4, invocationBytes, 0, invocationBytes.length);
        System.out.println(invocationBytes.length);
        mem.unmap();
    }

    @Test
    public void testReadMmap() throws IOException, ClassNotFoundException {
        int lenght = mem.getInt(5000);
        byte[] invBytes = new byte[lenght];
        mem.getBytes(0+4, invBytes, 0, lenght);
        ByteArrayInputStream bais = new ByteArrayInputStream(invBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        SerializableObject inv = (SerializableObject) ois.readObject();
        System.out.println(inv);
    }

}


class SerializableObject implements Serializable {

    private long serialVersionUID = -1;

    private Integer id;

    private String name;

    private SerializableObject object;

    public SerializableObject(Integer id, String name, SerializableObject object) {
        this.id = id;
        this.name = name;
        this.object = object;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SerializableObject getObject() {
        return object;
    }

    public void setObject(SerializableObject object) {
        this.object = object;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SerializableObject that = (SerializableObject) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(object, that.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, object);
    }

    @Override
    public String toString() {
        return "SerializableObject{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", object=" + object +
                '}';
    }
}

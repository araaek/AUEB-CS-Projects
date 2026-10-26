package shared;

import java.io.Serializable;
import java.util.Objects;

public class WorkerInfo implements Serializable{
    private static final long serialVersionUID = 6L;
    private final int id;
    private final String host;
    private final int port;

    public WorkerInfo(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }

    public int getId() { return id; }
    public String getHost() { return host; }
    public int getPort() { return port; }

    @Override
    public String toString() {
        return "WorkerInfo{" +
               "id=" + id +
               ", host='" + host + '\'' +
               ", port=" + port +
               '}';
    }

     @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkerInfo that = (WorkerInfo) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
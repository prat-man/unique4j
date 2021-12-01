package in.pratanumandal.unique4j;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class DynamicPortSocketIpcFactory implements SocketIpcFactory, PortIpcFactory {

    private final InetAddress address;
    private final int port;
    private final Charset charset;

    private int actualPort = -1;

    public DynamicPortSocketIpcFactory(InetAddress address, int port) {
        this(address, port, Charset.defaultCharset());
    }

    public DynamicPortSocketIpcFactory(InetAddress address, int port, Charset charset) {
        this.address = address;
        this.port = port;
        this.charset = charset;
    }

    @Override
    public ServerSocket getServerSocket(OutputStream fileOutputStream) throws IOException {
        // use dynamic port policy
        ServerSocket socket;
        actualPort = port;
        while (true) {
            try {
                socket = new ServerSocket(actualPort, 0, address);
                break;
            } catch (IOException e) {
                actualPort++;
            }
        }

        try {
            final BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream, charset));
            bw.write(String.valueOf(actualPort));
            bw.flush();
            System.out.println("actualPort: " + actualPort);
            return socket;
        } catch (IOException ex) {
            try {
                socket.close();
            } catch (IOException ignored) {
            }
            throw ex;
        }
    }

    @Override
    public Socket getClientSocket(InputStream fileInputStream) throws IOException {
        final BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream, charset));
        final String line = br.readLine();
        System.out.println(line);
        return new Socket(address, Integer.parseInt(line));
    }

    @Override
    public int getPort() {
        return actualPort;
    }
}

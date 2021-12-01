package in.pratanumandal.unique4j;

import java.io.File;
import java.io.IOException;

public interface IpcFactory {

    IpcServer createIpcServer(File parentDirectory, String appId) throws IOException;

    IpcClient createIpcClient(File parentDirectory, String appId) throws IOException;
}

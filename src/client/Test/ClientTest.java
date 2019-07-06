import client.ClientThread;

import org.junit.jupiter.api.Test;

import server.FileEntry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ClientTest
{
    @Test
    public void login2()
    {
        String username = ClientThread.login2("krizu");
        assertEquals("Users", username);
    }
    @Test
    public void logout2()
    {
        String username = ClientThread.logout2("krizu");
        assertEquals("Users", username);
    }
    @Test
    public void getDownloadList2(){
        String localFolder = "test", username = "username", ip = "";
        int port = 0;
        ClientThread bg = new ClientThread(localFolder, username, ip, port, null);
        //server 4 files
        ArrayList<FileEntry> server = new ArrayList<>();
        server.add(new FileEntry("file1", 1, "file1", 60, "user1"));
        server.add(new FileEntry("file2", 1, "file2", 60, "user1"));
        server.add(new FileEntry("file5", 1, "file5", 60, "user1"));
        server.add(new FileEntry("file8", 1, "file8", 60, "user1"));
        bg.setFilesServer2(server);
        //local 3 files
        ArrayList<FileEntry> local = new ArrayList<>();
        local.add(new FileEntry("file1", 1, "file1", 60, "user1"));
        local.add(new FileEntry("file2", 1, "file2", 60, "user2"));
        local.add(new FileEntry("file5", 1, "file5", 60, "user1"));
        bg.setFilesLocal2(local);

        ArrayList<FileEntry> list = bg.getDownloadList2();
        assertEquals(2, list.size());
    }
}

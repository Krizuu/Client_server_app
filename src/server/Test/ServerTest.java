import server.ServerThread;

import org.junit.jupiter.api.Test;

import server.FileEntry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class ServerTest
{
    @Test
    void userList() {
        String s = ServerThread.UserList();
        assertEquals("Users", s);
    }

    @Test
    void saveCSVtoFile()
    {
        int a = ServerThread.SaveCSV("testfile.txt", "TestUser", 150, "TestDrive", "/TestServer");
        assertEquals(1, a);
    }

    @Test
    void saveCSVdirectoryexists() throws IOException {
        File f1 = new File("/TestServer/TestDrive/");
        f1.mkdirs();
        File f = new File("/TestServer/"+ "TestDrive"+"/"+ "TestDrive" + ".csv");
        f.createNewFile();
        int a = ServerThread.SaveCSV("testfile.txt", "TestUser", 150, "TestDrive", "/TestServer");
        assertEquals(0, a);
        f.delete();
        f1.delete();
        f1 = new File("/TestServer");
        f1.delete();
    }
}

package client;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import server.FileEntry;
import java.util.ArrayList;
import java.util.Iterator;

public class Controller
{

    private boolean syncFlag;
    public boolean isSyncFlag()
    {
        return syncFlag;
    }
    public void setSyncFlag(boolean syncFlag)
    {
        this.syncFlag = syncFlag;
    }
    private String username;
    private String localFolder;
    private String ip;
    private int port;

    private ClientThread.BackgroundTasks bg = null;
    public Controller(){}

    @FXML
    private void initialize()
    {
        ConnectB.setDisable(false);
        UserName.setDisable(false);
        FolderPath.setDisable(false);
        PortNumber.setDisable(false);
        UserName.setDisable(false);

        tableFiles.setDisable(true);
        DisconnectB.setDisable(true);
        ClearB.setDisable(true);
        Consolee.setDisable(true);

        columnNames.setCellValueFactory(new PropertyValueFactory<>("filename"));
        columnOwner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        columnShared.setCellValueFactory(new PropertyValueFactory<>("others"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @FXML
    private Button ShareB;
    @FXML
    private Button UnshareB;
    @FXML
    private TableView<FileEntry> tableFiles;
    @FXML
    private TableColumn<FileEntry, String> columnNames;
    @FXML
    private TableColumn<FileEntry, String> columnOwner;
    @FXML
    private TableColumn<FileEntry, String> columnShared;
    @FXML
    private TableColumn<FileEntry, String> columnStatus;
    @FXML
    public TextArea Consolee;
    @FXML
    private TextField UserName;
    @FXML
    private TextField FolderPath;
    @FXML
    private TextField PortNumber;
    @FXML
    private Button ConnectB;
    @FXML
    private Button DisconnectB;
    @FXML
    private Button ClearB;

    public void Connect()
    {
        boolean validFlag = false;
        try {
            username = UserName.getText();
            localFolder = FolderPath.getText();
            ip = "127.0.0.1";
            port = Integer.parseInt(PortNumber.getText());
            validFlag = true;
        } catch (Exception e)
        {
            validFlag = false;
        }
        if(validFlag)
        {
            clearFiles();
            bg = new ClientThread.BackgroundTasks(localFolder, username, ip, port, this);

            UserName.setDisable(true);
            FolderPath.setDisable(true);
            PortNumber.setDisable(true);
            UserName.setDisable(true);
            ConnectB.setDisable(true);

            //enable all else
            tableFiles.setDisable(false);
            DisconnectB.setDisable(false);
            ClearB.setDisable(false);
            ShareB.setDisable(false);
            UnshareB.setDisable(false);

            Consolee.setDisable(false);
            Consolee.setText("Connecting to " + ip + "... ");
        }
    }

    public void Disconnect()
    {
        bg.stop();
        ConnectB.setDisable(false);
        UserName.setDisable(false);
        FolderPath.setDisable(false);
        PortNumber.setDisable(false);
        UserName.setDisable(false);

        tableFiles.setDisable(true);
        DisconnectB.setDisable(true);
        ClearB.setDisable(true);
        ShareB.setDisable(true);
        UnshareB.setDisable(true);
        Consolee.setDisable(true);
    }

    public void Share()
    {
        FileEntry file = tableFiles.getItems().get(tableFiles.getFocusModel().getFocusedCell().getRow());
        //file : selected file on list
        Platform.runLater(() -> printText("share : " + file));
        if(file.getOwner().equals(username))
        {
            bg.share(file);
        }
    }

    public void Unshare()
    {
        FileEntry file = tableFiles.getItems().get(tableFiles.getFocusModel().getFocusedCell().getRow());
        //file : selected file on list
        Platform.runLater(() -> printText("unshare : " + file));
        if(file.getOwner().equals(username))
        {
            bg.unshare(file);
        }
    }

    public void Clear()
    {
        Consolee.clear();
    }

    public void printText(String a)
    {

        try
        {
            Platform.runLater(() -> {
                Consolee.setText(Consolee.getText() + "\n" + a + "\n");
                Consolee.setScrollTop(Double.MAX_VALUE);
            });
        }catch(Exception e)
        {
            System.out.println("printText Exception : " + e.getMessage());
        }
    }
    public void setFileStatus(String fname ,boolean status)
    {
        String newStatus;
        if(status)
        {
            newStatus = "on server";
        }
        else
        {
            newStatus = "ready to upload";
        }
        Iterator<FileEntry> i = tableFiles.getItems().iterator();
        while(i.hasNext())
        {
            FileEntry temp = i.next();
            if(temp.getFilename().matches(fname))
            {
                temp.setStatus(newStatus);
                printText("updated status for: " + fname);
                break;
            }
        }
    }

    public void clearFiles()
    {
        tableFiles.getItems().clear();
    }

    public void updateFiles(ArrayList<FileEntry> f)
    {
        ObservableList<FileEntry> listForGui = FXCollections.observableArrayList();
        for(FileEntry fe : f)
        {
            listForGui.add(fe);
        }

        if(tableFiles.getItems().size() > 0)
        {
            for(Iterator<FileEntry> fGui = tableFiles.getItems().iterator(); fGui.hasNext();)
            {
                FileEntry fgui = fGui.next();
                boolean found = false;
                for(Iterator<FileEntry> fList = listForGui.iterator(); fList.hasNext();)
                {
                    FileEntry flist = fList.next();
                    if(fgui.equals2(flist))
                    {
                        found = true;
                        fList.remove();
                        break;
                    }
                    else
                    {
                        //continue search
                    }
                }
                if(!found)
                {
                    fGui.remove();
                }
            }
        }
        if(listForGui.size() > 0)
            for(FileEntry fnew : listForGui)
                tableFiles.getItems().addAll(fnew);
    }
}
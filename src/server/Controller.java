package server;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Controller
{
    private int port, nThreads;
    private String path;
    private ServerThread server;
    private ServerThread.FileListUpdater updater;
    private HddController hddController = null;

    //hdd folder names
    String hdd1 = "hdd1";
    String hdd2 = "hdd2";
    String hdd3 = "hdd3";
    String hdd4 = "hdd4";
    ArrayList<String> hdd = new ArrayList<>();

    public Controller(){}

    @FXML private void initialize()
    {
        columnAllNames.setCellValueFactory(new PropertyValueFactory<>("filename"));
        columnAllOwner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        columnAllOthers.setCellValueFactory(new PropertyValueFactory<>("others"));
        updateOperations(Arrays.asList(0, 0, 0, 0));
    }

    @FXML private ListView EnableUsers;
    @FXML private TableView<FileEntry> FileTable;
    @FXML private Label HDDALLCount;
    @FXML private Label HDDALLOperation;
    @FXML private VBox boxAll;
    @FXML private TableColumn<FileEntry, String> columnAllOwner;
    @FXML private TableColumn<FileEntry, String> columnAllNames;
    @FXML private TableColumn<FileEntry, String> columnAllOthers;

    @FXML private Label HDD1Count;
    @FXML private Label HDD1Operation;
    @FXML private VBox boxHdd1;

    @FXML private Label HDD2Count;
    @FXML private Label HDD2Operation;
    @FXML private VBox boxHdd2;

    @FXML private Label HDD3Operation;
    @FXML private Label HDD3Count;
    @FXML private VBox boxHdd3;

    @FXML private Label HDD4Operation;
    @FXML private Label HDD4Count;
    @FXML private VBox boxHdd4;

    @FXML private Button StartB;
    @FXML private Button StopB;

    @FXML private TextField Port;
    @FXML private TextField serverPath;

    @FXML private List<Label> hddOperations = Arrays.asList(HDD1Operation, HDD2Operation, HDD3Operation, HDD4Operation);

    @FXML public void updateUsersOnline(ObservableList<String> users)
    {
        Platform.runLater(() -> {
            EnableUsers.getItems().clear();
            EnableUsers.getItems().setAll(users);
        });
    }
    @FXML public void updateFiles(ArrayList<FileEntry> f)
    {
        //update all files tab
        //-------------------------------------
        ObservableList<FileEntry> listForGuiAll = FXCollections.observableArrayList();
        List<Integer> numberOfFiles = Arrays.asList(0, 0, 0, 0);
        for(FileEntry fe : f)
        {
            listForGuiAll.add(fe);
            numberOfFiles.set(fe.getHddNo()-1, 1+numberOfFiles.get(fe.getHddNo()-1));
        }
        //remove from new list or update entries that are already in gui
        //for(FileEntry fold : tableAll.getItems())
        for(Iterator<FileEntry> j = FileTable.getItems().iterator(); j.hasNext();)
        {
            FileEntry fold = j.next();
            boolean fileFound = false;
            for(Iterator<FileEntry> i = listForGuiAll.iterator(); i.hasNext();)
            {
                FileEntry fnew = i.next();
                if(fnew.getFilename().equals(fold.getFilename()) && fnew.getOwner().equals(fold.getOwner()))
                {
                    //same file entry
                    if(!fold.getOthers().equals(fnew.getOthers()))
                    {
                        fold.setOthers(fnew.getOthers());
                    }
                    if(fold.getSize() != fnew.getSize())
                    {
                        fold.setSize(fnew.getSize());
                    }
                    fileFound = true;
                    //remove from new list entry that is already in gui
                    i.remove();
                    break;
                }
            }
            if(!fileFound)
            {
                //remove from gui entry that is not present in new list
                //tableAll.getItems().removeAll(fold);
                j.remove();
            }
        }
        //add new entries to gui
        if(listForGuiAll.size() > 0)
            for(FileEntry fnew : listForGuiAll)
                FileTable.getItems().addAll(fnew);
        //don't change, throws exception otherwise
        Platform.runLater(() -> HDDALLCount.setText(FileTable.getItems().size()+" files"));
        //-------------------------------------
        Platform.runLater(() -> HDD1Count.setText(numberOfFiles.get(0)+" files"));
        Platform.runLater(() -> HDD2Count.setText(numberOfFiles.get(1)+" files"));
        Platform.runLater(() -> HDD3Count.setText(numberOfFiles.get(2)+" files"));
        Platform.runLater(() -> HDD4Count.setText(numberOfFiles.get(3)+" files"));
    }

    @FXML public void updateOperations(List<Integer> hddNumberOfOperations)
    {
        int i = 0;
        int sumOfOperations = 0;
        String green = "-fx-background-color: #af7;";
        String gray = "-fx-background-color: #ddd;";
        String text1 = "" + hddNumberOfOperations.get(i);
        if(hddNumberOfOperations.get(i) > 0)
            boxHdd1.setStyle(green);    //green
        else
            boxHdd1.setStyle(gray);     //gray
        Platform.runLater(() -> HDD1Operation.setText(text1));
        i++;
        String text2 = "" + hddNumberOfOperations.get(i);
        if(hddNumberOfOperations.get(i) > 0)
            boxHdd2.setStyle(green);    //green
        else
            boxHdd2.setStyle(gray);     //gray
        Platform.runLater(() -> HDD2Operation.setText(text2));
        i++;
        String text3 = "" + hddNumberOfOperations.get(i);
        if(hddNumberOfOperations.get(i) > 0)
            boxHdd3.setStyle(green);    //green
        else
            boxHdd3.setStyle(gray);     //gray
        Platform.runLater(() -> HDD3Operation.setText(text3));
        i++;
        String text4 = "" + hddNumberOfOperations.get(i);
        if(hddNumberOfOperations.get(i) > 0)
            boxHdd4.setStyle(green);    //green
        else
            boxHdd4.setStyle(gray);     //gray
        Platform.runLater(() -> HDD4Operation.setText(text4));

        for(Integer n : hddNumberOfOperations)
            sumOfOperations += n;
        String textAll = "" + sumOfOperations;
        if(hddNumberOfOperations.get(i) > 0)
            boxAll.setStyle(green);     //green
        else
            boxAll.setStyle(gray);
        Platform.runLater(() -> HDDALLOperation.setText(textAll));
    }

    @FXML void Start(ActionEvent event)
    {
        event.consume();
        boolean validFlag = false;
        try
        {
            path = serverPath.getText();
            port = Integer.parseInt(Port.getText());
            nThreads = 100;     //default value
            validFlag = true;
        }catch(Exception e)
        {
            validFlag = false;
        }
        if(validFlag)
        {
            hdd.add(path + File.separator + File.separator + hdd1);
            hdd.add(path + File.separator + File.separator + hdd2);
            hdd.add(path + File.separator + File.separator + hdd3);
            hdd.add(path + File.separator + File.separator + hdd4);

            StopB.setDisable(false);
            serverPath.setDisable(true);
            Port.setDisable(true);
            StartB.setDisable(true);

            hddController = new HddController(this);
            server = new ServerThread(port, nThreads, path, hdd, hddController);
            updater = new ServerThread.FileListUpdater(hdd, this, server, 10);
            updater.start();
            //wait till updater finishes init
            while(!updater.initialized)
            {
                try
                {
                    Thread.sleep(100);
                }catch(InterruptedException e){}
            }
            server.start();
        }
    }

    @FXML void Stop(ActionEvent event)
    {
        event.consume();
        server.stop();
        updater.stop();
        StopB.setDisable(true);
        serverPath.setDisable(false);
        Port.setDisable(false);
        StartB.setDisable(false);
    }

    public void shutdown()
    {
        if(server != null)server.stop();
        if(updater != null)updater.stop();
    }
}
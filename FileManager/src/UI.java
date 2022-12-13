import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UI extends JFrame {

    private JPanel catalogPanel = new JPanel();
    private JList filesList = new JList<>();
    private JScrollPane filesScroll = new JScrollPane(filesList);
    private JPanel buttonsPanel = new JPanel();
    private JButton addButton = new JButton("Create");
    private JButton backButton = new JButton("Back");
    private JButton deleteButton = new JButton("Delete");
    private JButton renameButton = new JButton("Rename");
    private List<String> dirscache = new ArrayList<>();

    public UI() {
        super("Explorer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        catalogPanel.setLayout(new BorderLayout(5, 5));
        catalogPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        buttonsPanel.setLayout(new GridLayout(1, 4, 5, 5));
        JDialog createNewDialog = new JDialog(UI.this, "Create folder", true);
        JPanel createNewDirPanel = new JPanel();
        createNewDialog.add(createNewDirPanel);
        File[] discs = File.listRoots();
        filesScroll.setPreferredSize(new Dimension(400, 500));
        filesList.setListData(discs);
        filesList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        filesList.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    DefaultListModel model = new DefaultListModel();
                    String selectedObject = filesList.getSelectedValue().toString();
                    String fullpath = toFullPath(dirscache);
                    File selectedFile;
                    if (dirscache.size() > 1) {
                        selectedFile = new File(fullpath, selectedObject);
                    } else {
                        selectedFile = new File((fullpath + selectedObject));
                    }
                    if (selectedFile.isDirectory()) {
                        String[] rootStr = selectedFile.list();
                        for (String s : rootStr) {
                            File checkObject = new File(selectedFile.getPath(), s);
                            if (!checkObject.isHidden()) {
                                if (checkObject.isDirectory()) {
                                    model.addElement(s);
                                } else {
                                    model.addElement("file " + s);
                                }
                            }
                        }
                        dirscache.add(selectedObject);
                        filesList.setModel(model);
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (dirscache.size() > 1) {
                    dirscache.remove(dirscache.size() - 1);
                    String backDir = toFullPath(dirscache);
                    String[] objects = new File(backDir).list();
                    DefaultListModel backRootModel = new DefaultListModel();
                    for (String s : objects) {
                        File checkFile = new File(backDir, s);
                        if (!checkFile.isHidden()) {
                            if (checkFile.isDirectory()) {
                                backRootModel.addElement(s);
                            } else {
                                backRootModel.addElement("file " + s);
                            }
                        }
                    }
                    filesList.setModel(backRootModel);
                } else {
                    dirscache.removeAll(dirscache);
                    filesList.setListData(discs);
                }
            }
        });

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!dirscache.isEmpty()) {
                    String currentPath;
                    File newFolder;
                    CreateNewFolderJDialog newFolderJDialog = new CreateNewFolderJDialog(UI.this);
                    if (newFolderJDialog.isReady()) {
                        currentPath = toFullPath(dirscache);
                        newFolder = new File(currentPath, newFolderJDialog.getNewFolderName());
                        if (!newFolder.exists()) {
                            newFolder.mkdir();
                        }
                        File updateDir = new File(currentPath);
                        String[] updateMas = updateDir.list();
                        DefaultListModel updateModel = new DefaultListModel();
                        for (String s : updateMas) {
                            File check = new File(updateDir.getPath(), s);
                            if (!check.isHidden()) {
                                if (check.isDirectory()) {
                                    updateModel.addElement(s);
                                } else {
                                    updateModel.addElement("file " + s);
                                }
                            }
                        }
                        filesList.setModel(updateModel);
                    }
                }
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!dirscache.isEmpty()) {
                    String selectedObject = filesList.getSelectedValue().toString();
                    String currentPath = toFullPath(dirscache);
                    if (!selectedObject.isEmpty()) {
                        deleteDir(new File(currentPath, selectedObject));
                        File updateDir = new File(currentPath);
                        String[] updateMas = updateDir.list();
                        DefaultListModel updateModel = new DefaultListModel<>();
                        for (String s : updateMas) {
                            File check = new File(updateDir.getPath(), s);
                            if (!check.isHidden()) {
                                if (check.isDirectory()) {
                                    updateModel.addElement(s);
                                } else {
                                    updateModel.addElement("file " + s);
                                }
                            }
                        }
                        filesList.setModel(updateModel);
                    }
                }
            }
        });

        renameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!dirscache.isEmpty() & filesList.getSelectedValue() != null) {
                    String currentPath = toFullPath(dirscache);
                    String selectedObject = filesList.getSelectedValue().toString();
                    RenameJDialog renamer = new RenameJDialog(UI.this);
                    if (renamer.isReady()) {
                        File renameFile = new File(currentPath, selectedObject);
                        renameFile.renameTo(new File(currentPath, renamer.getNewFolderName()));

                        File updateDir = new File(currentPath);
                        String[] updateMas = updateDir.list();
                        DefaultListModel updateModel = new DefaultListModel();
                        for (String s : updateMas) {
                            File check = new File(updateDir.getPath(), s);
                            if (!check.isHidden()) {
                                if (check.isDirectory()) {
                                    updateModel.addElement(s);
                                } else {
                                    updateModel.addElement("file " + s);
                                }
                            }
                        }
                        filesList.setModel(updateModel);
                    }
                }
            }
        });
        buttonsPanel.add(backButton);
        buttonsPanel.add(addButton);
        buttonsPanel.add(deleteButton);
        buttonsPanel.add(renameButton);
        catalogPanel.setLayout(new BorderLayout());
        catalogPanel.add(filesScroll, BorderLayout.CENTER);
        catalogPanel.add(buttonsPanel, BorderLayout.SOUTH);
        getContentPane().add(catalogPanel);
        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);

    }

    private String toFullPath(List<String> file) {
        String listPath = "";
        for (String s : file) {
            listPath = listPath + s;
        }
        return listPath;
    }

    private void deleteDir(File file) {
        File[] objects = file.listFiles();
        if (objects != null) {
            for (File file1 : objects) {
                deleteDir(file1);
            }
        }
        file.delete();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RenameJDialog extends JDialog {
    private JTextField nameOfNewFolder =new JTextField(10);
    private JButton okButton=new JButton("Rename");
    private JButton cancelButton=new JButton("Cancel");
    private String newFolderName;
    private JLabel nameFolderWait=new JLabel("Name's new folder");
    private boolean ready=false;

    public RenameJDialog(JFrame jFrame){
        super(jFrame,"rename folder",true);
        setLayout(new GridLayout(2,2,5,5));
        setSize(400,200);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newFolderName= nameOfNewFolder.getText();
                setVisible(false);
                ready=true;
            }
        });
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                ready=false;
            }
        });
        getContentPane().add(nameFolderWait);
        getContentPane().add(nameOfNewFolder);
        getContentPane().add(okButton);
        getContentPane().add(cancelButton);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    public String getNewFolderName() {
        return newFolderName;
    }


    public boolean isReady() {
        return ready;
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JPanel {
    public JComboBox<String> menu, options;
    public JButton execute, ok;
    public JLabel output, label1, label2, label3;
    public JTextField input1, input2;
    public JPanel panel1, panel2, panel3;
    boolean activateOptions = false;
    Jobcreator2 jobcreator2;
    Jobcreator2 j1 = new Jobcreator2(5001);
    Jobcreator2 j2 = new Jobcreator2(5002);

    public GUI() {
        super();

        menu = new JComboBox<>();
        options = new JComboBox<>();
        execute = new JButton("Execute Job");
        ok = new JButton("OK");
        output = new JLabel("");
        label1 = new JLabel("");
        label2 = new JLabel("");
        label3 = new JLabel("");
        input1 = new JTextField("", 15);
        input2 = new JTextField("", 15);
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        jobcreator2 = new Jobcreator2();

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        createMenu();

        menu.addActionListener(new menuListener());
        options.addActionListener(new optionListener());
        execute.addActionListener(new executeListener());
        ok.addActionListener(new okListener());

        panel1.add(menu);

        setInvisible();

        panel3.add(output);
        panel3.add(ok);
        output.setVisible(false);
        ok.setVisible(false);

        this.add(panel1);
        this.add(panel2);
        this.add(panel3);
    }

    public void createMenu() {
        menu.addItem("Job Menu");
        menu.addItem("******************* One-to-One Jobs *******************");
        menu.addItem("1. Detect if a given IP Address or Host Name is online or not.");
        menu.addItem("2. Detect the status of a given port at a given IP Address.");
        menu.addItem("******************* One-to-Many Jobs *******************");
        menu.addItem("3. ICMP flood attack against a given IP or subnet");
        menu.addItem("4. TCP flood attack against a given port on a given IP");
    }

    public void job1() {
        String result = jobcreator2.job1(options.getSelectedIndex(), input1.getText());
        output.setText("Output: " + result);
        output.setVisible(true);
        ok.setVisible(true);
        panel3.revalidate();
    }

    public void job2() {
        String result = jobcreator2.job2(input1.getText(), input2.getText());
        output.setText("Output: " + result);
        output.setVisible(true);
        ok.setVisible(true);
        panel3.revalidate();
    }

    public void job3() {
        j1.multiJob = 3;
        j1.mode = options.getSelectedIndex();
        j1.IP = input1.getText();
        j2.multiJob = 3;
        j2.mode = options.getSelectedIndex();
        j2.IP = input1.getText();
        Thread t1 = new Thread(j1);
        Thread t2 = new Thread(j2);
        t1.start();
        t2.start();
//        while(j1.result == null) { }
        output.setText("Output: " + j1.result);
        output.setVisible(true);
        ok.setVisible(true);
        panel3.revalidate();
    }

    public void job4() {
        output.setText("Output: Port: " + input1.getText() + ", Who: " + input2.getText());
        output.setVisible(true);
        ok.setVisible(true);
        panel3.revalidate();
    }

    public void job1Add() {
        label1.setText("Detect by: ");
        options.removeAllItems();
        options.addItem("- select -");
        options.addItem("IP Address");
        options.addItem("Host Name");
        options.setSelectedIndex(0);
        label1.setVisible(true);
        options.setVisible(true);
        panel2.add(label1);
        panel2.add(options);
        panel2.add(label2);
        panel2.add(input1);
        panel2.add(execute);
        panel2.revalidate();
        activateOptions = true;
    }

    public void job2Add() {
        label1.setText("Port Number: ");
        label2.setText("IP Address: ");
        label1.setVisible(true);
        label2.setVisible(true);
        input1.setVisible(true);
        input2.setVisible(true);
        execute.setVisible(true);
        panel2.add(label1);
        panel2.add(input1);
        panel2.add(label2);
        panel2.add(input2);
        panel2.add(execute);
        panel2.revalidate();
    }

    public void job3Add() {
        label1.setText("Flood against: ");
        options.removeAllItems();
        options.addItem("- select -");
        options.addItem("IP Address");
        options.addItem("Subnet");
        options.addItem("Unknown");
        options.setSelectedIndex(0);
        label1.setVisible(true);
        options.setVisible(true);
        panel2.add(label1);
        panel2.add(options);
        panel2.add(label2);
        panel2.add(input1);
        panel2.add(execute);
        panel2.revalidate();
        activateOptions = true;
    }

    public void job4Add() {
        label1.setText("Port Number: ");
        label2.setText("IP Address: ");
        label1.setVisible(true);
        label2.setVisible(true);
        input1.setVisible(true);
        input2.setVisible(true);
        execute.setVisible(true);
        panel2.add(label1);
        panel2.add(input1);
        panel2.add(label2);
        panel2.add(input2);
        panel2.add(execute);
        panel2.revalidate();
    }

    public void clearInputs() {
        input1.setText("");
        input2.setText("");
        panel2.revalidate();
    }

    public void setInvisible() {
        options.setVisible(false);
        label1.setVisible(false);
        label2.setVisible(false);
        label3.setVisible(false);
        input1.setVisible(false);
        input2.setVisible(false);
        execute.setVisible(false);
        panel2.revalidate();
    }

    public void createDialog(String title, String content) {
        JFrame dialogBox = new JFrame(title);
        JLabel dialog = new JLabel(content);
        dialogBox.setLayout(new BorderLayout());
        dialogBox.add(dialog, BorderLayout.CENTER);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialogBox.setSize(screenSize.width / 4, screenSize.height / 4);
        dialogBox.setLocation(screenSize.width / 2 - screenSize.width / 8, screenSize.height / 2 - screenSize.height / 8);
        dialogBox.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dialogBox.setVisible(true);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Jobcreator");
        frame.add(new GUI());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height);
    }

    class menuListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            setInvisible();
            panel2.removeAll();
            clearInputs();
            switch (menu.getSelectedIndex()) {
                case 0:
                case 1:
                case 4:
                default:
                    break;
                case 2:
                    job1Add();
                    break;
                case 3:
                    job2Add();
                    break;
                case 5:
                    job3Add();
                    break;
                case 6:
                    job4Add();
                    break;
            }
        }
    }

    class optionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(options.getSelectedIndex() == 0 && activateOptions) {
                label2.setVisible(false);
                input1.setVisible(false);
                input1.setText("");
                execute.setVisible(false);
                panel2.revalidate();
            }

            else if(options.getSelectedIndex() == 1 && activateOptions) {
                label2.setText("IP Address: ");
                label2.setVisible(true);
                input1.setText("");
                input1.setVisible(true);
                execute.setVisible(true);
                panel2.revalidate();
            }

            else if(options.getSelectedIndex() == 2 && activateOptions) {
                if(menu.getSelectedIndex() == 2)
                    label2.setText("Host Name: ");
                else
                    label2.setText("Subnet: ");
                label2.setVisible(true);
                input1.setVisible(true);
                input1.setText("");
                execute.setVisible(true);
                panel2.revalidate();
            }

            else if(options.getSelectedIndex() == 3 && activateOptions) {
                label2.setText("Unknown: ");
                label2.setVisible(true);
                input1.setVisible(true);
                input1.setText("");
                execute.setVisible(true);
                panel2.revalidate();
            }
        }
    }

    class executeListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch(menu.getSelectedIndex()) {
                case 0:
                case 1:
                case 4:
                default:
                    break;
                case 2:
                    if(input1.getText().compareTo("") == 0 && options.getSelectedIndex() == 1)
                        createDialog("Input Error: IP Address", "Please enter an IP Address.");
                    else if(input1.getText().compareTo("") == 0 && options.getSelectedIndex() == 2)
                        createDialog("Input Error: Host Name", "Please enter a Host Name.");
                    else
                        job1();
                    break;
                case 3:
                    if(input1.getText().compareTo("") == 0 && input2.getText().compareTo("") == 0)
                        createDialog("Input Error: Port Number & IP Address", "Please enter a Port Number and an IP Address");
                    else if(input1.getText().compareTo("") == 0)
                        createDialog("Input Error: Port Number", "Please enter a Port Number.");
                    else if(input2.getText().compareTo("") == 0)
                        createDialog("Input Error: IP Address", "Please enter an IP Address.");
                    else
                        job2();
                    break;
                case 5:
                    if(input1.getText().compareTo("") == 0 && options.getSelectedIndex() == 1)
                        createDialog("Input Error: IP Address", "Please enter an IP Address.");
                    else if(input1.getText().compareTo("") == 0 && options.getSelectedIndex() == 2)
                        createDialog("Input Error: Subnet", "Please enter a Subnet.");
                    else {
                        job3();
                    }
                    break;
                case 6:
                    if(input1.getText().compareTo("") == 0 && input2.getText().compareTo("") == 0)
                        createDialog("Input Error: Port Number & IP Address", "Please enter a Port Number and an IP Address");
                    else if(input1.getText().compareTo("") == 0)
                        createDialog("Input Error: Port Number", "Please enter a Port Number.");
                    else if(input2.getText().compareTo("") == 0)
                        createDialog("Input Error: IP Address", "Please enter an IP Address.");
                    else
                        job4();
                    break;
            }
        }
    }

    class okListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            panel2.removeAll();
            panel2.revalidate();

            output.setVisible(false);
            ok.setVisible(false);
            panel3.revalidate();

            setInvisible();
            clearInputs();
            activateOptions = false;
            menu.setSelectedIndex(0);
        }
    }
}
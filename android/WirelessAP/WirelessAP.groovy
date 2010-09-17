#!/usr/bin/env groovy

import java.awt.*
import java.awt.event.*
import javax.swing.*
import java.util.concurrent.CountDownLatch

//
// UI CONFIG - GENERATED
//

panel1 = new JPanel();
panel1.setLayout(new BorderLayout(0, 0));
final JScrollPane scrollPane1 = new JScrollPane();
panel1.add(scrollPane1, BorderLayout.CENTER);
console = new JTextArea();
console.setEditable(false);
console.setFont(new Font("Monospaced", console.getFont().getStyle(), 11));
console.setRows(10);
console.setText("");
scrollPane1.setViewportView(console);
final JPanel panel2 = new JPanel();
panel2.setLayout(new GridBagLayout());
panel1.add(panel2, BorderLayout.NORTH);
final JLabel label1 = new JLabel();
label1.setFont(new Font(label1.getFont().getName(), Font.BOLD, label1.getFont().getSize()));
label1.setText("Wireless interface:");
GridBagConstraints gbc;
gbc = new GridBagConstraints();
gbc.gridx = 0;
gbc.gridy = 0;
gbc.anchor = GridBagConstraints.WEST;
panel2.add(label1, gbc);
wirelessIF = new JTextField();
wirelessIF.setText("wlan2");
gbc = new GridBagConstraints();
gbc.gridx = 1;
gbc.gridy = 0;
gbc.weightx = 2.0;
gbc.anchor = GridBagConstraints.WEST;
gbc.fill = GridBagConstraints.HORIZONTAL;
panel2.add(wirelessIF, gbc);
final JLabel label2 = new JLabel();
label2.setFont(new Font(label2.getFont().getName(), Font.BOLD, label2.getFont().getSize()));
label2.setRequestFocusEnabled(false);
label2.setText("Shared interface:");
gbc = new GridBagConstraints();
gbc.gridx = 0;
gbc.gridy = 1;
gbc.anchor = GridBagConstraints.WEST;
panel2.add(label2, gbc);
sharedIF = new JTextField();
sharedIF.setText("eth0");
gbc = new GridBagConstraints();
gbc.gridx = 1;
gbc.gridy = 1;
gbc.anchor = GridBagConstraints.WEST;
gbc.fill = GridBagConstraints.HORIZONTAL;
panel2.add(sharedIF, gbc);
final JLabel label3 = new JLabel();
label3.setFont(new Font(label3.getFont().getName(), Font.BOLD, label3.getFont().getSize()));
label3.setText("Bridge interface:");
gbc = new GridBagConstraints();
gbc.gridx = 0;
gbc.gridy = 2;
gbc.anchor = GridBagConstraints.WEST;
panel2.add(label3, gbc);
bridgeIF = new JTextField();
bridgeIF.setText("br0");
gbc = new GridBagConstraints();
gbc.gridx = 1;
gbc.gridy = 2;
gbc.anchor = GridBagConstraints.WEST;
gbc.fill = GridBagConstraints.HORIZONTAL;
panel2.add(bridgeIF, gbc);
final JPanel panel3 = new JPanel();
panel3.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
gbc = new GridBagConstraints();
gbc.gridx = 0;
gbc.gridy = 3;
gbc.gridwidth = 2;
gbc.fill = GridBagConstraints.BOTH;
panel2.add(panel3, gbc);
btnStart = new JButton();
btnStart.setFont(new Font(btnStart.getFont().getName(), Font.BOLD, btnStart.getFont().getSize()));
btnStart.setText("Start Wireless AP");
panel3.add(btnStart);
final JLabel label4 = new JLabel();
label4.setText("          ");
panel3.add(label4);
btnStop = new JButton();
btnStop.setFont(new Font(btnStop.getFont().getName(), Font.BOLD, btnStop.getFont().getSize()));
btnStop.setText("Stop Wireless AP");
btnStop.setEnabled false
panel3.add(btnStop);

//
// SCRIPT
//

def inReader
def errReader

def inConsole(proc) {
    inReader = Thread.start {
        try {
            proc.getIn().eachLine {
                console.append it + '\n'
                console.setCaretPosition console.text.length()
            }
        } catch (e) {
        }
    }
    errReader = Thread.start {
        try {
            proc.getErr().eachLine {
                console.append it + '\n'
                console.setCaretPosition console.text.length()
            }
        } catch (e) {
        }
    }
}

basedir = new File(getClass().protectionDomain.codeSource.location.path).parent
frame = new JFrame(title: 'Wireless AP', preferredSize: new Dimension(600, 400))
startable = new CountDownLatch(1)
stoppable = new CountDownLatch(1)

def startWirelessAP() {
    startable.await()
    console.text = ''
    proc = ["gksu", "/bin/bash -c ${basedir}/WirelessAP-on.sh"].execute()
    inConsole proc
    proc.waitFor()
    stoppable.countDown()
}

def stopWirelessAP() {
    stoppable.await()
    console.text = ''
    proc = ["gksu", "/bin/bash -c ${basedir}/WirelessAP-off.sh"].execute()
    inConsole proc
    proc.waitFor()
    if(inReader) {
        inReader.interrupt()
        inReader = null
    }
    if(errReader) {
        errReader.interrupt()
        errReader = null
    }
    startable.countDown()
}

btnStart.addActionListener({
    btnStart.enabled = false
    stoppable = new CountDownLatch(1)
    Thread.start {
        startWirelessAP()
        JOptionPane.showMessageDialog frame, 'Wireless Access Point started !', 'Information', JOptionPane.INFORMATION_MESSAGE
        btnStop.enabled = true
    }
} as ActionListener)

btnStop.addActionListener({
    btnStop.enabled = false
    startable = new CountDownLatch(1)
    Thread.start {
        stopWirelessAP()
        JOptionPane.showMessageDialog frame, 'Wireless Access Point stopped !', 'Information', JOptionPane.INFORMATION_MESSAGE
        btnStart.enabled = true
    }
} as ActionListener)

frame.setDefaultCloseOperation JFrame.DISPOSE_ON_CLOSE
frame.addWindowListener([windowClosing: {
    if (btnStop.enabled) {
        btnStop.enabled = false
        Thread.start {
            println 'Closing Wireless AP...'
            stopWirelessAP()
        }
    }
}] as WindowListener)

frame.contentPane.add panel1
frame.pack()
point = new Point()
point.setLocation Toolkit.defaultToolkit.screenSize.width / 2 - frame.width / 2, Toolkit.defaultToolkit.screenSize.height / 2 - frame.height / 2
frame.setLocation point
frame.visible = true
startable.countDown()

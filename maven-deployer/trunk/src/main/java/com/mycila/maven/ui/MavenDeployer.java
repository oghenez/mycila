/**
 * Copyright (C) 2008 Mathieu Carbou <mathieu.carbou@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mycila.maven.ui;

import com.google.code.xmltool.XMLDoc;
import com.google.code.xmltool.XMLDocument;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

/**
 * @author Mathieu Carbou - mathieu.carbou(at)gmail.com
 */
public final class MavenDeployer {

    private static File currentDir = new File(".");
    private static Deployer deployer;
    private static File pom;

    public static void main(String[] args) {
        final MavenDeployerGui gui = new MavenDeployerGui();
        final Chooser repositoryChooser = new Chooser(gui.formPanel, JFileChooser.DIRECTORIES_ONLY);
        final Chooser artifactChooser = new Chooser(gui.formPanel, JFileChooser.FILES_ONLY);
        final Chooser pomChooser = new Chooser(gui.formPanel, JFileChooser.FILES_ONLY, new POMFilter());

        gui.cancel.setEnabled(false);
        gui.cbDeployPOM.setVisible(false);
        gui.cbDeployPOM.setEnabled(false);
        gui.mavenBin.setText(findMavenExecutable());

        gui.repositoryBrowser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File repo = repositoryChooser.chooseFrom(currentDir);
                if (repo != null) {
                    currentDir = repositoryChooser.currentFolder;
                    gui.repositoryURL.setText(repo.getAbsolutePath());
                }
            }
        });

        gui.artifactBrowser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File artifact = artifactChooser.chooseFrom(currentDir);
                if (artifact != null) {
                    currentDir = artifactChooser.currentFolder;
                    gui.artifactFile.setText(artifact.getAbsolutePath());
                }
            }
        });

        gui.deploy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deployer = new Deployer(gui, pom);
                deployer.execute();
            }
        });

        gui.clear.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gui.console.setText("");
            }
        });

        gui.cancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (deployer != null) {
                    deployer.stop();
                    deployer = null;
                }
            }
        });

        gui.cbDeployPOM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                boolean enable = !gui.cbDeployPOM.isSelected();
                gui.groupId.setEnabled(enable);
                gui.artifactId.setEnabled(enable);
                gui.classifier.setEnabled(enable);
                gui.version.setEnabled(enable);
                gui.packaging.setEnabled(enable);
                gui.description.setEnabled(enable);
                readPOM(gui);
            }
        });

        gui.loadPOM.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pom = pomChooser.chooseFrom(currentDir);
                if (pom != null) {
                    currentDir = pomChooser.currentFolder;
                    readPOM(gui);
                    gui.cbDeployPOM.setText("Deploy also " + pom.getAbsolutePath());
                    gui.cbDeployPOM.setEnabled(true);
                    gui.cbDeployPOM.setVisible(true);
                }
            }
        });

        String version = "";
        try {
            URL url = Thread.currentThread().getContextClassLoader().getResource("META-INF/maven/com.mycila.maven/maven-deployer/pom.properties");
            Properties p = new Properties();
            p.load(url.openStream());
            version = " " + p.getProperty("version");
        } catch (IOException ignored) {
        }
        JFrame frame = new JFrame("Maven Deployer" + version + " - By Mathieu Carbou (http://blog.mycila.com)");
        frame.setContentPane(gui.formPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLocationByPlatform(true);
        frame.pack();
        frame.setVisible(true);
    }

    private static String findMavenExecutable() {
        String script = "mvn";
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            script += ".bat";
        }
        for (String path : System.getenv("PATH").split(File.pathSeparator)) {
            File file = new File(path, script);
            if (file.exists() && file.isFile() && file.canRead()) {
                return file.getAbsolutePath();
            }
        }
        return "";
    }

    private static void readPOM(MavenDeployerGui gui) {
        try {
            XMLDocument doc = XMLDoc.from(pom).gotoRoot();
            String ns = doc.getPefix("http://maven.apache.org/POM/4.0.0");
            if (ns.length() > 0) ns += ":";
            if (doc.hasTag("%1$sgroupId", ns)) {
                gui.groupId.setText(doc.getText("%1$sgroupId", ns));
            } else {
                gui.groupId.setText("");
            }
            if (doc.hasTag("%1$sartifactId", ns)) {
                gui.artifactId.setText(doc.getText("%1$sartifactId", ns));
            } else {
                gui.artifactId.setText("");
            }
            if (doc.hasTag("%1$sversion", ns)) {
                gui.version.setText(doc.getText("%1$sversion", ns));
            } else {
                gui.version.setText("");
            }
            if (doc.hasTag("%1$spackaging", ns)) {
                gui.packaging.setSelectedItem(doc.getText("%1$spackaging", ns));
            } else {
                gui.packaging.setSelectedItem("jar");
            }
            if (doc.hasTag("%1$sdescription", ns)) {
                gui.description.setText(doc.getText("%1$sdescription", ns));
            } else {
                gui.description.setText("");
            }
            if (doc.hasTag("%1$sdistributionManagement/%1$srepository/%1$surl", ns)) {
                gui.repositoryURL.setText(doc.getText("%1$sdistributionManagement/%1$srepository/%1$surl", ns));
            } else {
                gui.repositoryURL.setText("");
            }
            if (doc.hasTag("%1$sdistributionManagement/%1$srepository/%1$sid", ns)) {
                gui.repositoryID.setText(doc.getText("%1$sdistributionManagement/%1$srepository/%1$sid", ns));
            } else {
                gui.repositoryID.setText("");
            }
        } catch (Exception ee) {
            gui.console.setText(ExceptionUtils.asText(ee));
        }
    }

}

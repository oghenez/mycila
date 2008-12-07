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

import org.jdesktop.swingworker.SwingWorker;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mathieu Carbou (mathieu.carbou@gmail.com)
 */
final class Deployer extends SwingWorker<Void, Void> {

    final MavenDeployerGui gui;
    Process deploy;
    Thread reader;

    Deployer(MavenDeployerGui gui) {
        this.gui = gui;
    }

    protected Void doInBackground() throws Exception {
        try {
            java.util.List<String> cmd = buildCommands();
            StringBuilder cmdStr = new StringBuilder("Executing:\n   ");
            for (String str : cmd) {
                cmdStr.append(" ").append(str);
            }
            cmdStr.append("\n\n");
            gui.console.setText(cmdStr.toString());
            gui.cancel.setEnabled(true);
            gui.deploy.setEnabled(false);
            gui.console.setCaretPosition(gui.console.getDocument().getLength());
            ProcessBuilder builder = new ProcessBuilder(cmd).redirectErrorStream(true);
            builder.environment().putAll(System.getenv());
            deploy = builder.start();
            reader = new Thread() {
                @Override
                public void run() {
                    try {
                        BufferedReader in = new BufferedReader(new InputStreamReader(deploy.getInputStream()));
                        try {
                            String line;
                            while ((line = in.readLine()) != null && reader != null && deploy != null && !isCancelled()) {
                                gui.console.getDocument().insertString(gui.console.getDocument().getLength(), line + "\n", null);
                                gui.console.setCaretPosition(gui.console.getDocument().getLength());
                            }
                        } finally {
                            in.close();
                        }
                    } catch (Exception ignored) {
                    }
                }
            };
            reader.start();
            deploy.waitFor();
        }
        catch (InterruptedException ignored) {
        }
        catch (Exception e) {
            gui.console.setText(ExceptionUtils.asText(e));
        }
        finally {
            gui.deploy.setEnabled(true);
            gui.cancel.setEnabled(false);
        }
        return null;
    }

    List<String> buildCommands() throws MalformedURLException, URISyntaxException {
        java.util.List<String> cmd = new ArrayList<String>();
        cmd.add(findMavenExecutable());
        cmd.add("deploy:deploy-file");
        cmd.add("-DuniqueVersion=false");

        // required
        cmd.add("-DgroupId=" + gui.groupId.getText());
        cmd.add("-DartifactId=" + gui.artifactId.getText());
        cmd.add("-Dversion=" + gui.version.getText());
        cmd.add("-Dpackaging=" + gui.packaging.getSelectedItem());
        cmd.add("-Dfile=" + gui.artifactFile.getText());
        cmd.add("-Durl=" + toUrl(gui.repositoryURL.getText()));

        // optional classifier
        String classifier = (String) gui.classifier.getSelectedItem();
        if (classifier != null && classifier.trim().length() > 0) {
            cmd.add("-Dclassifier=" + classifier);
        }

        // optional description
        String desc = gui.description.getText();
        if (desc != null && desc.trim().length() > 0) {
            cmd.add("-Ddescription=" + desc);
        }

        // optional repo ID
        String repoId = gui.repositoryID.getText();
        if (repoId != null && repoId.trim().length() > 0) {
            cmd.add("-DrepositoryId=" + repoId);
        }

        return cmd;
    }

    private String findMavenExecutable() {
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
        throw new IllegalStateException(String.format("Unable to find Maven executable '%s' in PATH: %s", script, System.getenv("PATH")));
    }

    String toUrl(String path) throws MalformedURLException, URISyntaxException {
        File file = new File(path);
        if (file.exists()) {
            return file.toURI().toString();
        }
        return new URI(path).toString();
    }

    public void stop() {
        cancel(true);
        if (deploy != null) {
            deploy.destroy();
            deploy = null;
        }
        if (reader != null) {
            reader.interrupt();
            reader = null;
        }
    }
}
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

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

final class Chooser extends JFileChooser {

    final Component parent;
    File currentFolder;

    Chooser(Component parent, int mode, FileFilter filter) {
        this(parent, mode);
        setFileFilter(filter);
    }

    Chooser(Component parent, int mode) {
        this.parent = parent;
        setFileSelectionMode(mode);
    }

    File chooseFrom(File dir) {
        setCurrentDirectory(dir);
        currentFolder = dir;
        if (showOpenDialog(parent) == JFileChooser.APPROVE_OPTION) {
            File file = getSelectedFile();
            currentFolder = file.isDirectory() ? file : file.getParentFile();
            return file;
        }
        return null;
    }
}
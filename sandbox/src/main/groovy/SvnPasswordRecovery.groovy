#!/usr/bin/env groovy

@Grapes([
@Grab(group = 'com.sun.jna', module = 'jna', version = '3.0.9', transitive = false),
@Grab(group = 'org.tmatesoft.svnkit', module = 'svnkit', version = '1.3.2', transitive = false)])
import org.tmatesoft.svn.core.wc.SVNWCUtil
import org.tmatesoft.svn.core.internal.wc.SVNWCProperties
import org.tmatesoft.svn.core.*

def auth = new File(SVNWCUtil.defaultConfigurationDirectory, "auth/svn.simple");
def auths = auth.listFiles({!it.getName().contains(".")} as FileFilter)
auths.each {file ->
    SVNProperties values = new SVNWCProperties(file, "").asMap();
    def username = SVNPropertyValue.getPropertyAsString(values.getSVNPropertyValue("username"))
    def password = SVNPropertyValue.getPropertyAsString(values.getSVNPropertyValue("password"))
    if (username && password) println "${username}:${password}"
}

#!/usr/bin/env groovy

import java.util.concurrent.*;
import static groovy.io.FileType.FILES

def version = '20100907'

def basedir = '/home/kha/data/workspace/perso/guice-upload' as File
def dest = '/home/kha/system/mnt/mc-repo/releases' as File

if(!dest.exists()) {
    print 'Mounting /home/kha/system/mnt/mc-repo...'
    'mount /home/kha/system/mnt/mc-repo'.execute().waitFor()
    println ' done !'
}

def executor = new ExecutorCompletionService(Executors.newFixedThreadPool(1))
def tasks = 0

basedir.eachFileMatch FILES, ~/.*-3\.0\.pom\.xml/, { file ->
    tasks++
    executor.submit {
        def basename = file.name.substring(0, file.name.length()-12)
        println "Deploying ${basename}..."
        def proc = "mvn deploy:deploy-file -DpomFile=${basedir}/${basename}-3.0.pom.xml -Dfile=${basedir}/${basename}-3.0-${version}.jar -Durl=file://${dest}".execute()
        def res = proc.waitFor()
        println "Finished ${basename}."
        if(res == 0) {
            println "Deploying ${basename} sources..."
            proc = "mvn deploy:deploy-file -DpomFile=${basedir}/${basename}-3.0.pom.xml -Dfile=${basedir}/${basename}-3.0-${version}-sources.jar -Durl=file://${dest} -Dclassifier=sources".execute()
            res = proc.waitFor()
            println "Finished ${basename} sources."
        }

        return res == 0 ? '' : (proc.text + '\n' + proc.err.text)
    } as Callable    
}

while(tasks-- > 0) {
    print executor.take().get()
}

executor.executor.shutdown()

0

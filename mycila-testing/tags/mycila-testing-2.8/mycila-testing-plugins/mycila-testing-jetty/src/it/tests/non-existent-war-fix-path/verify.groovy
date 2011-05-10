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

def getLog = {
    def found
    new File( '.' ).eachFileRecurse() {
        f ->
        if (f.isFile() && f.absolutePath.matches( /.+?\Qtarget\/its\/non-existent-war-fix-path\/target\/failsafe-reports\/com.example.test.NonExistentWarFixPathJettyRunWarIT.txt\E/ )) {
            println "found : " + f.absolutePath
            found = f;
        }
    }
    return found
}

def f = getLog()
println "verify: " + f.absolutePath
assert f.exists()
assert f.text.matches( /(?s).*\QtestHelloWorld\E.+?\Q<<< FAILURE!\E.*/ )
assert f.text.matches( /(?s).*\Qjava.lang.AssertionError: non-existent WAR :\E.+?\Qtarget\/its\/non-existent-war-fix-path\/target\/non-existent-war-v.0.war\E.*/ )

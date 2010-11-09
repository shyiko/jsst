/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the GNU Lesser General Public License, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author stanley.shyiko@gmail.com
 * @version 2010-11-06
 */
private String getVersion() {
    String buildNumber = (new File("pom.xml").getText() =~ /<build.number>.*?<\/build.number>/)[0]
    "${buildNumber[14..-16]}"
}

private String replaceVersionInformation(String source, String version) {
    source.replaceAll(/\$\{build.number\}/, {return version})
}

private String prepareForSonatypeOSS(String source) {
    def root = new XmlParser().parseText(source)
    def relativePaths = root.get("parent")?.relativePath
    if (relativePaths) {
        Node relativePath = relativePaths[0]
        removeNodeIfExist relativePath.parent(), relativePath.name().getLocalPart()
    }
    removeNodeIfExist root, "properties"
    removeNodeIfExist root, "build"
    removeNodeIfExist root, "profiles"
    toString(root)
}

private void removeNodeIfExist(Node parent, String child) {
    def children = parent.get(child)
    if (children)
        parent.remove((Node) children[0])
}

private String toString(Node root) {
    def writer = new StringWriter()
    def printer = new XmlNodePrinter(new PrintWriter(writer));
    printer.setPreserveWhitespace true
    printer.print root
    writer.toString()
}

private String moveModulesFromDefaultProfile(String source) {
    def root = new XmlParser().parseText(source)
    def defaultProfile = root.profiles.profile.find {it.id.get(0).value().get(0) == "default"}
    root.append(defaultProfile.modules.get(0))
    toString(root)
}

def version = getVersion()
def root = new File("build/oss-release")
root.mkdirs()
root.eachFile {
    it.delete()
}

def transform = {String source ->
    prepareForSonatypeOSS(replaceVersionInformation(source, version))
}
def save = {File file, String content ->
    print "Saving ${file.getAbsolutePath()}"
    file.write content
    println ". Done"
}
[
        "jsst-antlib", "jsst-core", "jsst-junit", "jsst-testng", "jsst-war"
].each {artifact ->
    def sourcePom = new File(artifact, "pom.xml")
    def targetPom = new File(root, "${artifact}-1.0.${version}.pom")
    save targetPom, transform(sourcePom.getText()) 
}
save new File(root, "jsst-1.0.${version}.pom"), transform(moveModulesFromDefaultProfile(new File("pom.xml").getText()))

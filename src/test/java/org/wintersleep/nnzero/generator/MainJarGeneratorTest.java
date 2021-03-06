package org.wintersleep.nnzero.generator;

/*-
 * #%L
 * Wintersleep 99.0-does-not-exist Maven Repo Server
 * %%
 * Copyright (C) 2017 Davy Verstappen
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import org.junit.Test;
import org.wintersleep.nnzero.FileId;
import org.wintersleep.nnzero.FileIds;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;
import java.util.jar.Manifest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MainJarGeneratorTest {

    @Test
    public void test() throws Exception {
        FileId fileId = FileIds.createMainJar("mygroup", "myartifact", "myversion");
        MainJarGenerator generator = new MainJarGenerator(fileId);
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            generator.generate(out);
            try (JarInputStream jis = new JarInputStream(new ByteArrayInputStream(out.toByteArray()))) {
                Manifest manifest = jis.getManifest();
                Attributes attrs = manifest.getMainAttributes();
                assertEquals("1.0", attrs.getValue(Attributes.Name.MANIFEST_VERSION));
                assertEquals("wintersleep-nnzero", attrs.getValue(new Attributes.Name("Created-By")));
                JarEntry jarEntry = jis.getNextJarEntry();
                assertEquals("META-INF/maven/mygroup/myartifact/pom.properties", jarEntry.getName());
                jarEntry = jis.getNextJarEntry();
                assertEquals("META-INF/maven/mygroup/myartifact/pom.xml", jarEntry.getName());
            }
        }
    }

}

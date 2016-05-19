/*
 * The MIT License
 *
 * Copyright 2016 lethalpaga.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.vault.config;

import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.XmlFile;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import java.io.IOException;
import net.sf.json.JSONObject;
import org.jenkinsci.plugins.vault.VaultServerConfig;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Vault server configuration
 */
public class VaultServerConfigImpl extends AbstractDescribableImpl<VaultServerConfigImpl> implements VaultServerConfig {
    
    public final String name;
    public final String url;
    public final String token;
    
    @Override
    public String getUrl() {
        return this.url;
    }
    
    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public String getName() {
        return this.name;

    }
    
    @DataBoundConstructor
    public VaultServerConfigImpl(@NonNull String name, @NonNull String url, String token) {
        this.name = name;
        this.url = url;
        this.token = token;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<VaultServerConfigImpl> {

        @Override
        public String getDisplayName() {
            return "Vault Server";
        }

        public final String name;
        public final String url;
        public final String token;

        public String getUrl() {
            return this.url;
        }

        public String getToken() {
            return this.token;
        }

        public String getName() {
            return this.name;

        }

        public DescriptorImpl() throws IOException {
            this.name = "Vault Server";
            this.url = "vault.example.com";
            this.token = null;

            XmlFile xml = getConfigFile();
            if (xml.exists()) {
                xml.unmarshal(this);
            }
        }

        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws Descriptor.FormException {
            req.bindJSON(this, json.getJSONObject("vault-server"));
            save();
            return true;
        }
    }
}

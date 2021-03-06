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

import com.cloudbees.plugins.credentials.CredentialsProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.model.Run;
import hudson.util.ListBoxModel;
import java.util.List;
import org.jenkinsci.plugins.vault.VaultCredentials;
import org.jenkinsci.plugins.vault.VaultServerConfig;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

/**
 * Vault server configuration
 */
public class VaultServerConfigImpl extends AbstractDescribableImpl<VaultServerConfigImpl> implements VaultServerConfig {
    
    private String name;
    private String url;
    private String credId;
    
    @Override
    public String getUrl() {
        return this.url;
    }
    
    @DataBoundSetter
    public void setUrl(String value) {
        this.url = value;
    }
    
    public String getCredId() {
        return this.credId;
    }
    
    @DataBoundSetter
    public void setCredId(String value) {
        this.credId = value;
    }
    
    @Override
    public VaultCredentials getCredentials(Run<?,?> build) {
        VaultCredentials cred = CredentialsProvider.findCredentialById(this.credId, VaultCredentials.class, build);
        return cred;
    }

    @Override
    public String getName() {
        return this.name;
    }
    
    @DataBoundSetter
    public void setName(String value) {
        this.name = value;
    }
    
    @DataBoundConstructor
    public VaultServerConfigImpl(@NonNull String name, @NonNull String url, @NonNull String credId) {
        this.name = name;
        this.url = url;
        this.credId = credId;
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<VaultServerConfigImpl> {
        @Override
        public String getDisplayName() {
            return "Vault Server";
        }
        
        public ListBoxModel doFillCredIdItems() {
            ListBoxModel model = new ListBoxModel();
            
            List<VaultCredentials> creds = CredentialsProvider.lookupCredentials(VaultCredentials.class);
            for(VaultCredentials cred: creds) {
              model.add(cred.getDescription(), cred.getId());
            }
            
            return model;
        }
    }
}

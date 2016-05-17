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

import com.google.common.collect.ImmutableList;
import hudson.Extension;
import hudson.ExtensionPoint;
import hudson.XmlFile;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.annotation.Nonnull;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Vault server configuration
 */
public final class VaultGlobalConfig extends AbstractDescribableImpl<VaultGlobalConfig> implements ExtensionPoint {
    //public List<VaultServerConfig> vaults = new ArrayList<>();
    /*public final Boolean installVault;
    
//    public List<VaultServerConfig> getVaults() {
//        return Collections.unmodifiableList(this.vaults);
//    }
    
    public Boolean getInstallVault() {
        return this.installVault;
    }
    
    @DataBoundConstructor
    public VaultGlobalConfig(@Nonnull Boolean installVault/*, @Nonnull List<VaultServerConfig> vaults) {
        //this.vaults = vaults != null ? new ArrayList<>(vaults) : Collections.<VaultServerConfig>emptyList();
        this.installVault = installVault;
    }
    
    */

    @Extension
    public static class DescriptorImpl extends Descriptor<VaultGlobalConfig> {

        @Override
        public String getDisplayName() {
            return "Hashicorp Vault";
        }
        
           //public List<VaultServerConfig> vaults = new ArrayList<>();
        public Boolean installVault;

    //    public List<VaultServerConfig> getVaults() {
    //        return Collections.unmodifiableList(this.vaults);
    //    }

        public Boolean getInstallVault() {
            return this.installVault;
        }

        public XmlFile getConfigFile() {
            return new XmlFile(new File(Jenkins.getInstance().getRootDir(), "hashicorp.vault.xml"));
        }

        public DescriptorImpl() throws IOException {
            this.installVault = true;

            XmlFile xml = getConfigFile();
            if (xml.exists()) {
                xml.unmarshal(this);
        }
    }
 
        @Override
        public boolean configure(StaplerRequest req, JSONObject json) throws Descriptor.FormException {
            req.bindJSON(this, json.getJSONObject("vault"));
            save();
            return true;
        }
        
        public List<Descriptor> getVaultDescriptors() {
            Jenkins jenkins = Jenkins.getInstance();
            return ImmutableList.of(jenkins.getDescriptor(VaultServerConfig.class));
        }
     }
}

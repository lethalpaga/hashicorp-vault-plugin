package org.jenkinsci.plugins.vault;

import com.cloudbees.plugins.credentials.CredentialsProvider;
import edu.umd.cs.findbugs.annotations.NonNull;
import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import hudson.util.ListBoxModel;
import org.jenkinsci.plugins.vault.config.VaultServerConfigImpl;
import org.kohsuke.stapler.DataBoundConstructor;

import java.util.List;

/**
 * Data structure to represent a secret in Vault.
 * This will be used for {@link SecretBinding}
 */
public class VaultSecretDefinition extends AbstractDescribableImpl<VaultSecretDefinition> {
    private String envVariable;
    private String secretField;

    public String getEnvVariable() { return this.envVariable; }
    public String getSecretField() { return this.secretField; }
    public void setEnvVariable(String value) { this.envVariable = value; }
    public void setSecretField(String value) { this.secretField = value; }

    @DataBoundConstructor
    public VaultSecretDefinition(@NonNull String envVariable, String secretField) {
        this.setEnvVariable(envVariable);
        this.setSecretField(secretField);
    }

    @Extension
    public static class DescriptorImpl extends Descriptor<VaultSecretDefinition> {
        @Override
        public String getDisplayName() {
            return "Vault Secret";
        }
    }
}

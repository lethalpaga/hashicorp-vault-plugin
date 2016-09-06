package org.jenkinsci.plugins.vault;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.Run;
import hudson.model.TaskListener;
import org.jenkinsci.plugins.credentialsbinding.Binding;
import org.jenkinsci.plugins.credentialsbinding.BindingDescriptor;
import org.jenkinsci.plugins.credentialsbinding.MultiBinding;
import org.jenkinsci.plugins.vault.api.VaultApi;
import org.jenkinsci.plugins.vault.api.VaultApiFactory;
import org.jenkinsci.plugins.vault.config.VaultServerConfigImpl;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import com.bettercloud.vault.VaultException;

import java.text.MessageFormat;
import java.util.*;

import java.io.IOException;

/**
 * Extension for credential-bindings to map a vault secret to an env variable
 */
public class SecretMultiBinding extends MultiBinding<SecretCredentials> {

    private VaultSecretDefinition[] secrets;
    private VaultServerConfigImpl vaultConfig;

    @DataBoundConstructor
    public SecretMultiBinding(VaultSecretDefinition[] secrets, VaultServerConfigImpl vaultConfig, String credentialsId) {
        super(credentialsId);
        this.secrets = secrets;
        this.vaultConfig = vaultConfig;
    }

    public VaultSecretDefinition[] getSecrets() {
        return secrets;
    }

    @DataBoundSetter
    public void setSecrets(VaultSecretDefinition[] value) {
        this.secrets = value;
    }

    public VaultServerConfigImpl getVaultConfig() {
        return vaultConfig;
    }

    @DataBoundSetter
    public void setVaultConfig(VaultServerConfigImpl vaultConfig) {
        this.vaultConfig = vaultConfig;
    }

    @Override protected Class<SecretCredentials> type() {
        return SecretCredentials.class;
    }

    @Override public Binding.MultiEnvironment bind(Run<?,?> build, FilePath workspace, Launcher launcher, TaskListener listener) throws IOException, InterruptedException {
        String secretPath = getCredentials(build).getSecretPath().getPlainText();
        VaultApi vaultApi = VaultApiFactory.create(vaultConfig, build);

        Map<String,String> m = new HashMap<>();

        try {
            Map<String, String> secretStruct = vaultApi.read(secretPath);

            for(VaultSecretDefinition secret: secrets) {
              String secretValue = secretStruct.get(secret.getSecretField());
              m.put(secret.getEnvVariable(), secretValue);
            }
        }
        catch(VaultException e) {
            String message = MessageFormat.format("Failed to read {0} from {1}: {2}", secretPath, vaultApi.getUrl(), e.getMessage());
            throw new hudson.AbortException(message);
        }

        return new Binding.MultiEnvironment(m);
    }

    @Override public Set<String> variables() {
        List<String> vars = new ArrayList<>();

        for(VaultSecretDefinition secret: secrets) {
            vars.add(secret.getEnvVariable());
        }
        return new HashSet<>(vars);
    }

    @Extension
    public static class DescriptorImpl extends BindingDescriptor<SecretCredentials> {

        @Override protected Class<SecretCredentials> type() {
            return SecretCredentials.class;
        }

        @Override public String getDisplayName() {
            return "Vault secret binding (multi)";
        }
    }
}

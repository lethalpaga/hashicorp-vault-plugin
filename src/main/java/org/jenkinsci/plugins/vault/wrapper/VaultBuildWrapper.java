package org.jenkinsci.plugins.vault.wrapper;

import hudson.EnvVars;
import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildWrapperDescriptor;
import java.io.IOException;
import java.util.Set;
import jenkins.tasks.SimpleBuildWrapper;
import org.jenkinsci.plugins.vault.VaultServerConfig;
import org.jenkinsci.plugins.vault.api.VaultApi;
import org.jenkinsci.plugins.vault.api.VaultApiFactory;
import org.jenkinsci.plugins.vault.config.VaultServerConfigImpl;
import org.kohsuke.stapler.DataBoundConstructor;

public class VaultBuildWrapper extends SimpleBuildWrapper {
    
    private VaultApi vaultApi;
    
    @Override
    public void makeSensitiveBuildVariables(AbstractBuild build,
                               Set<String> sensitiveVariables) {
        sensitiveVariables.add("VAULT_TOKEN");
    }
    
    @Override
    public void setUp(SimpleBuildWrapper.Context context,
         Run<?,?> build,
         FilePath workspace,
         Launcher launcher,
         TaskListener listener,
         EnvVars initialEnvironment)
                    throws IOException,
                           InterruptedException {
        context.env("VAULT_TOKEN", "abcdefg");
        context.env("VAULT_ADDR", "http://test.com");
        String secret = vaultApi.readField("secret/test", "value");
        context.env("VAULT_SECRET", secret);

    }
    
    @DataBoundConstructor
    public VaultBuildWrapper() {
        VaultServerConfig config = getVaultServerConfig();
        this.vaultApi = VaultApiFactory.create(config);
    }
    
    private VaultServerConfig getVaultServerConfig() {
        return new VaultServerConfigImpl("", "http://localhost:8200", "ed604738-ecdb-6b4c-ec41-f0ee6d3c58f2");
    }
    
    @Extension public static class DescriptorImpl extends BuildWrapperDescriptor {

        @Override public String getDisplayName() {
            return "Use Vault";
        }
        
        @Override
        public boolean isApplicable(AbstractProject<?,?> item) {
            return true; // This applies
        }
    }
}
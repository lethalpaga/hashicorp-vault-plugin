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
import java.util.Map;
import java.util.Set;
import jenkins.tasks.SimpleBuildWrapper;
import org.jenkinsci.plugins.vault.api.VaultApi;
import org.jenkinsci.plugins.vault.api.VaultApiFactory;
import org.jenkinsci.plugins.vault.config.VaultServerConfigImpl;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.json.JSONObject;

public class VaultBuildWrapper extends SimpleBuildWrapper {
    
    private transient VaultApi vaultApi;
    private String secretPath;
    private String envVariable;
    private VaultServerConfigImpl vaultConfig;
    
    public VaultServerConfigImpl getVaultConfig() {
        return vaultConfig;
    }
    
    @DataBoundSetter
    public void setVaultConfig(VaultServerConfigImpl value) {
        this.vaultConfig = value;
    }
    
    public String getSecretPath() {
        return secretPath;
    }
    
    @DataBoundSetter
    public void setSecretPath(String value) {
        this.secretPath = value;
    }

    public String getEnvVariable() {
        return envVariable;
    }
    
    @DataBoundSetter
    public void setEnvVariable(String value) {
        this.envVariable = value;
    }

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
        this.vaultApi = VaultApiFactory.create(vaultConfig, build);

        context.env("VAULT_TOKEN", vaultApi.getToken());
        context.env("VAULT_ADDR", vaultApi.getUrl());
        JSONObject secret = new JSONObject(vaultApi.read(secretPath));

        context.env(envVariable, secret.toString());
    }
    
    @DataBoundConstructor
    public VaultBuildWrapper(VaultServerConfigImpl vaultConfig) {
        this.vaultConfig = vaultConfig;
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
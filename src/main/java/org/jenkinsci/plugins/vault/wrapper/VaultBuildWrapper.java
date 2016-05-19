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
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

public class VaultBuildWrapper extends SimpleBuildWrapper {
    
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

    }
    
    @DataBoundConstructor
    public VaultBuildWrapper() {
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
package org.jenkinsci.plugins.vault.wrapper;

import org.jvnet.hudson.test.JenkinsRule;
import hudson.model.*;
import hudson.tasks.Shell;
import org.junit.Test;
import org.junit.Rule;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.jenkinsci.plugins.vault.config.VaultServerConfigImpl;
import org.jenkinsci.plugins.vault.api.VaultApiImpl;


import org.mockito.Mockito;

import java.util.*;
import java.util.logging.Logger;

public class SecretMultiBindingTest {
    private static final Logger LOGGER = Logger
            .getLogger(VaultBuildWrapperFreestyleTest.class.getName());

    private VaultApiImpl vaultApi;

    @Rule public JenkinsRule r = new JenkinsRule();

    @Test public void testFreeStyleProjects() throws Exception {
        // Run the build
        FreeStyleBuild build = runFreestyleProject();

        // Validate results
        assertThat(build.getResult().isCompleteBuild(), is(true));
        assertThat(build.getResult().isBetterOrEqualTo(Result.SUCCESS), is(true));
        assertThat(build.getLog(1000).toString(), containsString("read {\"field1\":\"value1\"}"));
    }

    // Freestyle project helpers

    private FreeStyleBuild runFreestyleProject() throws Exception {
        FreeStyleProject project = setupFreestyleProject();
        return runFreeStyleBuild(project);
    }

    private FreeStyleProject setupFreestyleProject() throws Exception {
        VaultBuildWrapper wrapper = new VaultBuildWrapper(getVaultServerConfig());
        FreeStyleProject project = r.createFreeStyleProject();

        configureWrapper(wrapper, getVaultApi());
        configureFreestyleProject(project, wrapper);

        return project;
    }

    private void configureFreestyleProject(FreeStyleProject project, VaultBuildWrapper wrapper) {
        project.getBuildWrappersList().add(wrapper);
        project.getBuildersList().add(new Shell("echo read $SECRET1"));
    }

    private FreeStyleBuild runFreeStyleBuild(FreeStyleProject project) throws Exception {
        return project.scheduleBuild2(0).get();
    }

    // Common helpers

    private void configureWrapper(VaultBuildWrapper wrapper, VaultApiImpl vaultApi) {
        wrapper.setSecretPath("secret/test");
        wrapper.setEnvVariable("SECRET1");
        wrapper.setVaultApi(vaultApi);
    }

    private VaultServerConfigImpl getVaultServerConfig() {
        return new VaultServerConfigImpl("name", "url", "id");
    }

    private VaultApiImpl getVaultApi() {
        if (this.vaultApi == null) {
            this.vaultApi = Mockito.mock(VaultApiImpl.class);
            setupApiMock(this.vaultApi);
        }
        return this.vaultApi;
    }

    private void setupApiMock(VaultApiImpl vaultApi) {
        Map<String, String> mockedResults = new HashMap<>();
        mockedResults.put("field1", "value1");
        Mockito.when(vaultApi.read("secret/test")).thenReturn(mockedResults);
    }
}

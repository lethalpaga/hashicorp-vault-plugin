package org.jenkinsci.plugins.vault.wrapper;

import org.jenkinsci.plugins.vault.api.VaultApiImpl;
import org.jenkinsci.plugins.vault.config.VaultServerConfigImpl;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.model.Statement;
import org.jvnet.hudson.test.RestartableJenkinsRule;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

public class VaultBuildWrapperPipelineTest {
    private VaultApiImpl vaultApi;


    @Rule
    public RestartableJenkinsRule story = new RestartableJenkinsRule();

    @Test
    public void testPipelineWrapper() {
        runBuild();
    }

    private void doChecks(WorkflowRun run) throws Exception {
        assertThat(run.getLog(1000).toString(), containsString("secret: blah"));
    }

    public void runBuild() {
        story.addStep(new Statement() {
            @Override public void evaluate() throws Throwable {
                WorkflowJob p = story.j.jenkins.createProject(WorkflowJob.class, "prj");

                VaultApiImpl vaultApi = getVaultApi();
                setupApiMock(vaultApi);

                String wrapperScript = "echo 'test'";
                        /*"node {" +
                        //"var vaultApi = " + vaultApi +
                        "wrap([$class: 'VaultBuildWrapper', envVariable: 'SECRET_VAR', secretPath: 'secret/test']) {" +
                        "    echo \"secret: $SECRET_VAR\"" +
                        "}";*/

                p.setDefinition(new CpsFlowDefinition(wrapperScript, true));
                WorkflowRun run = p.scheduleBuild2(0).waitForStart();

                doChecks(run);
            }
        });
    }

    private VaultApiImpl getVaultApi() {
        if (this.vaultApi == null) {
            this.vaultApi = Mockito.mock(VaultApiImpl.class);
        }
        return this.vaultApi;
    }

    private void setupApiMock(VaultApiImpl vaultApi) {
        Map<String, String> mockedResults = new HashMap<>();
        mockedResults.put("field1", "value1");
        Mockito.when(vaultApi.read("secret/test")).thenReturn(mockedResults);
    }
}

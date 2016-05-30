/*
 * The MIT License
 *
 * Copyright 2013 jglick.
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

package org.jenkinsci.plugins.vault.secretcredentialsimpl;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import hudson.Extension;
import hudson.util.Secret;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.jenkinsci.plugins.vault.SecretCredentials;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SecretVaultCredentials extends BaseStandardCredentials implements SecretCredentials {
    private static final Logger LOGGER = LoggerFactory.getLogger(SecretVaultCredentials.class);

    private @Nonnull Secret secretPath;

    @DataBoundConstructor public SecretVaultCredentials(@CheckForNull CredentialsScope scope, @CheckForNull String id, @CheckForNull String description, @Nonnull Secret secretPath) {
        super(scope, id, description);
        LOGGER.info("Retrieving creds for vault");
        this.secretPath = secretPath;
    }

    public Secret getSecretPath() {
        return secretPath;
    }

    @DataBoundSetter
    public void setSecretPath(Secret secretPath) { this.secretPath = secretPath; }
    
    @Extension public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

        @Override public String getDisplayName() {
            return "Vault secret";
        }
    }

}


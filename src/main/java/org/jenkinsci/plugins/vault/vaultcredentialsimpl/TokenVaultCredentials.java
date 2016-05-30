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

package org.jenkinsci.plugins.vault.vaultcredentialsimpl;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.impl.BaseStandardCredentials;
import hudson.Extension;
import hudson.util.Secret;
import javax.annotation.CheckForNull;
import javax.annotation.Nonnull;
import org.jenkinsci.plugins.vault.VaultCredentials;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class TokenVaultCredentials extends BaseStandardCredentials implements VaultCredentials {
    private static final Logger LOGGER = LoggerFactory.getLogger(TokenVaultCredentials.class);

    private @Nonnull Secret token;

    @DataBoundConstructor public TokenVaultCredentials(@CheckForNull CredentialsScope scope, @CheckForNull String id, @CheckForNull String description, @Nonnull Secret token) {
        super(scope, id, description);
        LOGGER.info("Retrieval creds for vault");
        this.token = token;
    }

    @Override
    public Secret getToken() {
        return token;
    }

    @DataBoundSetter
    public void setToken(Secret token) {
        this.token = token;
    }
    
    @Extension public static class DescriptorImpl extends BaseStandardCredentialsDescriptor {

        @Override public String getDisplayName() {
            return "Vault token";
        }

    }

}


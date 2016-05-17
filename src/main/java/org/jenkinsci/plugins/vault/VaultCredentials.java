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

package org.jenkinsci.plugins.vault;

import com.cloudbees.plugins.credentials.CredentialsNameProvider;
import com.cloudbees.plugins.credentials.NameWith;
import com.cloudbees.plugins.credentials.common.StandardCredentials;
import hudson.Util;
import hudson.util.Secret;
import javax.annotation.Nonnull;

/**
 * Credentials consisting only of a single secret, such as a password or token.
 */
@NameWith(VaultCredentials.NameProvider.class)
public interface VaultCredentials extends StandardCredentials {

    /**
     * Returns the wrapped secret value.
     * @return the encrypted value
     */
    @Nonnull Secret getSecret();
    
    /**
     * Returns the vault Url. Can be null
     * @return URL to the vault server
     */
    String getVaultUrl();

    class NameProvider extends CredentialsNameProvider<VaultCredentials> {

        @Override public String getName(VaultCredentials c) {
            String description = Util.fixEmptyAndTrim(c.getDescription());
            return Messages.VaultCredentials_description() + (description != null ? " (" + description + ")" : "");
        }

    }

}

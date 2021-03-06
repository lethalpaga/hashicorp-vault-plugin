/*
 * The MIT License
 *
 * Copyright 2016 lethalpaga.
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
package org.jenkinsci.plugins.vault.api;

import com.bettercloud.vault.Vault;
import com.bettercloud.vault.VaultConfig;
import com.bettercloud.vault.VaultException;
import hudson.model.Run;

import java.io.Serializable;
import java.util.Map;
import org.jenkinsci.plugins.vault.VaultServerConfig;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * Implementation using vault-java-driver
 */
public class VaultApiImpl implements VaultApi {
    private VaultConfig config;
    private Vault vault;

    private final static Logger LOG = Logger.getLogger(VaultApiImpl.class.getName());

    public VaultApiImpl(VaultServerConfig configParams, Run<?,?> build) throws VaultException
{
        this.config = new VaultConfig().address(configParams.getUrl()).token(configParams.getCredentials(build).getToken().getPlainText()).build();
    }

    @Override
    public String getUrl() {
        return config.getAddress();
    }

    @Override
    public void setUrl(String value) throws VaultException
    {
        config.address(value).build();
    }

    @Override
    public Boolean hasToken() {
        return config.getToken() == null;
    }

    @Override
    public String getToken() {
        return config.getToken();
    }

    @Override
    public void setToken(String value) {
        config.token(value);
    }

    @Override
    public String getSslPemFile() {
        return config.getSslPemUTF8();
    }

    @Override
    public void setSslPemFile(String value) {
        config.sslPemUTF8(value);
    }

    @Override
    public Boolean getSslVerify() {
        return config.isSslVerify();
    }

    @Override
    public void setSslVerify(Boolean value) {
        config.sslVerify(value);
    }

    @Override
    public Map<String, String> read(String secret) throws VaultException
    {
        printLog("Reading secret " + secret);

        return getVault().logical().read(secret).getData();
    }

    @Override
    public String readField(String secret, String field) throws VaultException
    {
        return read(secret).get(field);
    }

    private Vault getVault() {
        if(vault == null) {
            vault = new Vault(config);
        }

        return vault;
    }

    private void printLog(String message, Level level) {
        System.out.println(message);
        LOG.log(level, message);
    }

    private void printLog(String message) {
        printLog(message, Level.INFO);
    }
}

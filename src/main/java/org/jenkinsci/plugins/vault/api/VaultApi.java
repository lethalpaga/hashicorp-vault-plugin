package org.jenkinsci.plugins.vault.api;

import java.util.Map;

public interface VaultApi {
    /**
     * @return URL to the Vault Server
     */
     public String getUrl();
     
     /**
      * Sets the URL to the Vault server
      * @param value URL to the Vault Server
      */
     public void setUrl(String value);
     
     /**
      * Indicates if a valid token is configured.
      * For security reasons the token itself cannot be read
      * @return true if the token is properly configured
      */
     public Boolean hasToken();
     
     /**
      * Sets the Authentication token.
      * See the auth* method to get a new token
      * @param value 
      */
     public void setToken(String value);
     
     /**
      * Returns the Pem file used to authenticate with Vault
      * @return Path to the pem file
      */
     public String getSslPemFile();
     
     /**
      * Sets the Pem file used to authenticate with Vault
      * @param value Path to the Pem file
      */
     public void setSslPemFile(String value);

     /**
      * Indicates if Ssl cert verification is enabled
      * @return true if verification is enabled
      */
     public Boolean getSslVerify();
     
     /**
      * Enable or disable Ssl cert verification
      * @param value false to disable verification (not recommended)
      */
     public void setSslVerify(Boolean value);
     
     /**
      * Returns a secret in vault
      * @param secret Path to the secret
      * @return Hash of all fields contained in the secret
      */
     public Map<String, String> read(String secret);
     
     /**
      * Reads a specific field from a secret in vault
      * @param secret Path to the secret
      * @param field Field to read
      * @return Secret value
      */
     public String readField(String secret, String field);
}
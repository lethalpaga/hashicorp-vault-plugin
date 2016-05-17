package org.jenkinsci.plugins.vault.config.VaultConfig

import org.jenkinsci.plugins.vault.config.VaultConfig

def f = namespace(lib.FormTagLib);
def c = namespace(lib.CredentialsTagLib)

f.section(title: _("Vault")) {
    f.entry(title: _("Install Vault"), field: "installVault") {
        f.textbox(default: 'true')
    }
}
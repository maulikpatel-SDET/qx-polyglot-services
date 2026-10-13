// R4-N10 Azure Bicep: Key Vault EC P-384 key and RSA-HSM 3072 key (not Terraform).
param vaultName string = 'qx-test-kv'

resource vault 'Microsoft.KeyVault/vaults@2023-07-01' existing = {
  name: vaultName
}

resource ecKey 'Microsoft.KeyVault/vaults/keys@2023-07-01' = {
  parent: vault
  name: 'qx-ec-p384'
  properties: {
    kty: 'EC'
    curveName: 'P-384'
    keyOps: [ 'sign', 'verify' ]
  }
}

resource rsaHsmKey 'Microsoft.KeyVault/vaults/keys@2023-07-01' = {
  parent: vault
  name: 'qx-rsa-hsm'
  properties: {
    kty: 'RSA-HSM'
    keySize: 3072
    keyOps: [ 'wrapKey', 'unwrapKey' ]
  }
}

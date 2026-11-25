param(
    [Parameter(Mandatory = $true)]
    [string] $Path
)

Add-Type -AssemblyName System.Security

$encoded = (Get-Content -LiteralPath $Path -Raw).Trim()
$encrypted = [System.Convert]::FromBase64String($encoded)
$plain = [System.Security.Cryptography.ProtectedData]::Unprotect(
    $encrypted,
    $null,
    [System.Security.Cryptography.DataProtectionScope]::CurrentUser
)

[System.Text.Encoding]::UTF8.GetString($plain)

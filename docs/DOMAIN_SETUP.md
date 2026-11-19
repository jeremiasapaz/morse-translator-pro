# Custom Domain Setup (morsetranslatorpro.com)

The site is live now at **https://jeremiasapaz.github.io/morse-translator-pro/**.
Follow this when you've **registered** `morsetranslatorpro.com` (any registrar:
Cloudflare, Namecheap, Porkbun, etc. — this costs money and is your step).

> ⚠️ Do NOT add the custom domain in GitHub until DNS is configured, or the
> site can become temporarily unreachable. Do DNS first, then activate.

## Step 1 — Add DNS records at your registrar

For the apex/root domain `morsetranslatorpro.com`, add these **A** records
(GitHub Pages IPv4):

```
A   @   185.199.108.153
A   @   185.199.109.153
A   @   185.199.110.153
A   @   185.199.111.153
```

(Optional, recommended) also add **AAAA** records for IPv6:

```
AAAA @ 2606:50c0:8000::153
AAAA @ 2606:50c0:8001::153
AAAA @ 2606:50c0:8002::153
AAAA @ 2606:50c0:8003::153
```

For the `www` subdomain, add a **CNAME**:

```
CNAME  www  jeremiasapaz.github.io.
```

Wait for DNS to propagate (minutes to a few hours). Verify with:
```bash
dig +short morsetranslatorpro.com
```

## Step 2 — Activate the custom domain on GitHub Pages

**Option A (recommended, one command)** — sets the domain and commits a CNAME
for you:
```bash
gh api -X PUT repos/jeremiasapaz/morse-translator-pro/pages \
  -f cname=morsetranslatorpro.com -F https_enforced=true
```

**Option B (UI):** Repo → Settings → Pages → Custom domain →
`morsetranslatorpro.com` → Save → tick "Enforce HTTPS" once the cert is issued.

Either way, GitHub adds a `CNAME` file to the published site and provisions a
free TLS certificate automatically.

## Step 3 — Verify

```bash
curl -I https://morsetranslatorpro.com
```
Expect HTTP 200. Then update these to the custom domain:
- `website/index.html` `og:url`
- `README.md` website link
- `marketing/play-store/listing.md` website + privacy URLs

## Ready-made CNAME file content
If deploying another way, the published site's `CNAME` should contain exactly:
```
morsetranslatorpro.com
```

## Notes
- The GitHub Pages deploy workflow (`.github/workflows/pages.yml`) keeps working;
  the custom domain just changes the public URL.
- Tell me once the domain is registered and I'll run Step 2 and update the links.

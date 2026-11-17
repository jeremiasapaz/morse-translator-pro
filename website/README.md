# Marketing Website

Static landing page for Morse Translator Pro. No build step — plain HTML/CSS.

## Local preview
```bash
cd website
python -m http.server 8080
# open http://localhost:8080
```

## Files
- `index.html` — landing page (hero, features, screenshots, FAQ, contact).
- `privacy.html` — privacy policy (also mirrored in `docs/PRIVACY_POLICY.md`).
- `styles.css` — styling.
- `assets/icon.svg` — logo.

## Before publishing
1. Replace `OWNER` in GitHub links with your username/org.
2. Confirm the Play Store URL once the app is live.
3. Add real screenshots to `assets/` and swap the `.shot.placeholder` divs for
   `<img>` tags.
4. Wire the contact form `action` to a form backend (Formspree/Getform) or
   remove the form and keep the mailto link.

## Deploying to morsetranslatorpro.com

**Option A — GitHub Pages (free):**
1. Push the repo; in Settings → Pages, deploy from `/website` (or move files to
   a `gh-pages` branch / `docs/` folder).
2. Add a `CNAME` file containing `morsetranslatorpro.com`.
3. At your domain registrar, add the GitHub Pages DNS records (A records to
   GitHub IPs + a CNAME for `www`). Enable "Enforce HTTPS".

**Option B — Netlify / Vercel / Cloudflare Pages (free):**
1. Connect the repo, set the publish directory to `website`.
2. Add the custom domain `morsetranslatorpro.com` and follow the DNS steps the
   host shows. TLS is automatic.

**Domain:** register `morsetranslatorpro.com` at any registrar (Namecheap,
Cloudflare, Google Domains successor, etc.). This requires payment and is a
manual step the project owner must complete.

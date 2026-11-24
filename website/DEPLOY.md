# Flux website URL

The site in this folder is ready for GitHub Pages. Every Gradle build copies the freshly built jar into `downloads/` and refreshes the size and SHA-256 shown on the home page.

## GitHub Pages URL templates

- **Your site:** `https://brantleypatterson574-hash.github.io/Flux-2.0/`
- Project-site template: `https://<username>.github.io/<repo-name>/`
- User or organisation root site (repo named `<username>.github.io`): `https://<username>.github.io/`
- Custom domain: `https://<your-domain>/`

## Publish steps

1. Push this repository to GitHub.
2. Open **Settings → Pages**.
3. Under **Build and deployment**, choose **GitHub Actions** as the source.
4. Run the **Deploy Flux website** workflow (it also runs automatically on pushes to `main` or `master`).
5. Open the URL shown in the deployment output.

`website/.nojekyll` is included so folders such as `assets/` and `downloads/` are served exactly as written.

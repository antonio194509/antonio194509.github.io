# Porting VoiceTombola to GitHub Pages

The goal is to sync the latest version of the Android app's web assets to the GitHub Pages site and resolve the issues with the `index.html` not updating and the push being rejected.

## User Review Required

> [!IMPORTANT]
> **Authentication Issue:** The GitHub CLI (`gh`) reports that the authentication token for `antonio194509` is invalid. This is likely why the push was rejected yesterday. You will need to run `gh auth login` in a terminal to re-authenticate before we can push successfully.

> [!NOTE]
> **Duplicate Folders:** The repository `antonio194509_site` currently has files both at the root and in a `VoiceTombola` subfolder. We should decide where the app should live to avoid confusion. For now, I will update both to ensure the latest version is available everywhere.

## Open Questions

- Did you intend to change the contents of `app-ads.txt` (which you referred to as "mads text"), or just ensure it's correctly pushed to the site?
- Do you prefer the app to be at the root (`antonio194509.github.io/`) or in the subfolder (`antonio194509.github.io/VoiceTombola/`)? Note that `app-ads.txt` MUST be at the root for AdMob to find it.

## Proposed Changes

### Syncing Assets

We will copy all files from `app/src/main/assets` in the Android project to the `antonio194509_site` directory.

#### [MODIFY] `C:/antonio194509_site/index.html`
- Update with the latest version from [index.html](file:///C:/VoiceTombola/app/src/main/assets/index.html) (includes `estrazioneVoice` features).

#### [MODIFY] `C:/antonio194509_site/VoiceTombola/index.html`
- Update with the same latest version to keep it in sync.

#### [MODIFY] `C:/antonio194509_site/app-ads.txt`
- Ensure it matches the one in [app-ads.txt](file:///C:/VoiceTombola/app-ads.txt).

### Dependency Synchronization
- Copy all other assets (JS, images, audio) to both locations in the site repository.

## Verification Plan

### Automated Tests
- I will run `git status` to verify all changes are staged.
- I will attempt a `git push` (which will likely fail until you re-authenticate, but I can check the error message).

### Manual Verification
- After push, visit `https://antonio194509.github.io/` to verify the new features (like voice extraction tracking) are present.
- Verify `https://antonio194509.github.io/app-ads.txt` is accessible.

# Contributing to Budget Tracker API

Thank you for your interest in this project!

## Project Ownership

This is a personal portfolio project maintained solely by [@kabaediz](https://github.com/kabaediz). All code changes are reviewed and approved by the repository owner.

## How to Manage GitHub Copilot Access

GitHub Copilot can appear as a contributor when it is used to generate commits (via co-authored-by tags) or when it is assigned to issues as a coding agent. To remove or restrict Copilot's contributor access:

### Disable Copilot as a Coding Agent (Repository Level)

1. Go to your repository on GitHub.
2. Click **Settings** → **Copilot** (in the left sidebar under "Code and automation").
3. Under **"Copilot coding agent"**, set the policy to **Disabled**.
4. Save your changes.

### Disable Copilot for the Entire Organization

1. Go to your **Organization Settings** on GitHub.
2. Click **Copilot** → **Policies**.
3. Set **"Copilot coding agent"** to **Disabled** for the organization.

### Prevent Copilot from Being Assigned to Issues

- When creating or editing an issue, avoid assigning it to **Copilot** in the **Assignees** field.
- Once Copilot has been assigned, it will create a pull request. You can close that PR without merging to prevent it from contributing code.

### Note on Existing Contributions

If Copilot has already committed code to the repository, those commits remain in the git history (as is standard for all contributors). You cannot remove past commits without rewriting history, which is generally not recommended for shared repositories. However, disabling Copilot going forward will prevent any new automated contributions.

## Submitting Changes (Human Contributors)

If you'd like to contribute a bug fix or improvement:

1. Fork the repository.
2. Create a feature branch: `git checkout -b feature/your-feature-name`
3. Commit your changes: `git commit -m "Add your commit message"`
4. Push to your fork: `git push origin feature/your-feature-name`
5. Open a Pull Request against the `main` branch.

All pull requests must be reviewed and approved by the repository owner before merging.

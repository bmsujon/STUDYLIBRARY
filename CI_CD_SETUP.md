# CI/CD Pipeline Documentation

## Overview

This project uses GitHub Actions for comprehensive CI/CD pipeline automation. The pipeline includes continuous integration, security scanning, performance testing, automated releases, and documentation deployment.

## Pipeline Structure

### 🔄 Continuous Integration (ci.yml)

**Triggered by:** Push to `main`/`develop`, Pull Requests, Manual dispatch

**Jobs:**

- **Multi-platform Testing**: Tests on Ubuntu, Windows, and macOS with Java 25
- **Code Quality Analysis**: SonarCloud integration, code coverage with JaCoCo
- **Security Scanning**: OWASP dependency vulnerability checks
- **Build Artifacts**: Creates platform-specific executables
- **Performance Testing**: Runs Java 25 performance benchmarks

**Features:**

- ✅ Java 25 compatibility testing
- ✅ 379 automated tests with coverage reporting
- ✅ Cross-platform build validation
- ✅ Dependency caching for faster builds
- ✅ Automated security vulnerability scanning

### 🚀 Release Pipeline (release.yml)

**Triggered by:** Git tags (v*.*.\*), Published releases, Manual dispatch

**Jobs:**

- **Prepare Release**: Creates GitHub release with detailed changelog
- **Build & Package**: Creates distribution packages for all platforms
- **Deploy Documentation**: Updates GitHub Pages with JavaDoc and guides
- **Notification**: Sends completion notifications

**Artifacts Generated:**

- `study-library-windows.zip` - Windows executable package
- `study-library-macos.tar.gz` - macOS application bundle
- `study-library-linux.tar.gz` - Linux distribution package

### 🌙 Nightly Builds (nightly.yml)

**Triggered by:** Daily at 2 AM UTC, Manual dispatch

**Features:**

- Automated nightly testing and builds
- Performance benchmarking with historical tracking
- Security vulnerability monitoring
- Snapshot artifacts with 7-day retention

### 📦 Dependency Management (dependency-update.yml)

**Triggered by:** Weekly on Sundays, Manual dispatch

**Features:**

- Automated dependency updates (minor/patch only)
- Plugin version updates
- Automated testing with new dependencies
- Creates pull requests for review

## Setup Instructions

### 1. Repository Secrets

Configure the following secrets in your GitHub repository:

```
Settings → Secrets and variables → Actions
```

**Required Secrets:**

- `SONAR_TOKEN`: SonarCloud authentication token
  - Create at: https://sonarcloud.io/account/security/
  - Organization: bmsujon

**Optional Secrets:**

- `CODECOV_TOKEN`: For enhanced coverage reporting
- `SLACK_WEBHOOK`: For build notifications

### 2. SonarCloud Setup

1. Go to [SonarCloud](https://sonarcloud.io/)
2. Import your repository
3. Copy the project key to `sonar-project.properties`
4. Generate token and add to repository secrets

### 3. GitHub Pages

Enable GitHub Pages in repository settings:

```
Settings → Pages → Source: GitHub Actions
```

### 4. Branch Protection

Configure branch protection rules:

```
Settings → Branches → Add rule for 'main'
```

**Recommended settings:**

- ✅ Require status checks to pass
- ✅ Require branches to be up to date
- ✅ Require CI checks to pass
- ✅ Restrict pushes to main branch

## Running Pipelines

### Manual Triggers

All workflows can be triggered manually:

```
Actions → Choose workflow → Run workflow
```

### Release Process

1. **Create a release tag:**

   ```bash
   git tag v1.0.0
   git push origin v1.0.0
   ```

2. **Or use GitHub UI:**

   ```
   Releases → Create a new release → Tag version: v1.0.0
   ```

3. **Manual release trigger:**
   ```
   Actions → Release → Run workflow → Enter version
   ```

### Development Workflow

1. **Feature branch:**

   ```bash
   git checkout -b feature/new-feature
   git push origin feature/new-feature
   ```

2. **Create Pull Request:**

   - CI runs automatically
   - Code quality checks execute
   - Security scans run
   - Performance tests validate

3. **Merge to develop:**

   - Additional CI validation
   - Integration tests run

4. **Merge to main:**
   - Full CI/CD pipeline executes
   - Release preparation (if tagged)

## Monitoring and Maintenance

### Build Status

Monitor build health:

- **Actions tab**: Recent workflow runs
- **README badges**: Quick status overview
- **SonarCloud**: Code quality metrics
- **GitHub Pages**: Generated documentation

### Performance Tracking

Performance benchmarks run automatically:

- **Nightly builds**: Historical performance data
- **PR validation**: Performance regression detection
- **Release testing**: Performance verification

### Security Monitoring

Security checks include:

- **OWASP Dependency Check**: Known vulnerabilities
- **Automated updates**: Security patches
- **Code scanning**: Static analysis (if enabled)

## Customization

### Adding New Checks

Edit workflow files in `.github/workflows/`:

```yaml
- name: Custom Check
  run: |
    # Your custom validation
    mvn custom:goal
```

### Modifying Release Process

Update `release.yml` to:

- Change artifact formats
- Add deployment targets
- Modify notification channels
- Include additional documentation

### Platform-Specific Builds

The pipeline supports:

- **Windows**: .bat launcher, ZIP distribution
- **macOS**: .sh launcher, TAR.GZ distribution
- **Linux**: .sh launcher, TAR.GZ distribution

Each platform gets:

- Java 25 compatibility validation
- Platform-specific testing
- Native launcher scripts
- Installation documentation

## Troubleshooting

### Common Issues

1. **Java 25 not found:**

   - Check `actions/setup-java@v4` version
   - Verify distribution: 'corretto'

2. **Tests failing:**

   - Check Java preview features enabled
   - Verify dependency compatibility

3. **Security scan failures:**

   - Review OWASP suppression file
   - Update vulnerable dependencies

4. **Build artifacts missing:**
   - Check Maven assembly plugin config
   - Verify artifact upload paths

### Getting Help

- **GitHub Issues**: Report pipeline problems
- **Actions logs**: Detailed failure information
- **Documentation**: Check generated docs on GitHub Pages

---

**Note**: This CI/CD pipeline is optimized for Java 25 with modern language features including sealed classes, records, and pattern matching. All builds use preview features and comprehensive testing to ensure reliability.

# 🔒 Removing Sensitive Files from Git History

## ⚠️ IMPORTANT: Steps to Secure Your Repository

Your `application.yaml` file has been added to `.gitignore`, but it's already been committed to the repository. Follow these steps to remove it from git history:

### Step 1: Remove the file from Git tracking (without deleting locally)
```bash
git rm --cached src/main/resources/application.yaml
```

### Step 2: Commit this change
```bash
git commit -m "Remove sensitive application.yaml from git tracking"
```

### Step 3: Push the changes to GitHub
```bash
git push origin main
```

### Step 4: Use Git Filter (⚠️ ONLY if the file was pushed to GitHub already)
If the file is already on GitHub, you need to remove it from the entire history:

**Option A: Using git-filter-repo (Recommended)**
```bash
# Install git-filter-repo if you don't have it
pip install git-filter-repo

# Remove the file from all history
git filter-repo --invert-paths --path src/main/resources/application.yaml
```

**Option B: Using git filter-branch (Alternative)**
```bash
git filter-branch --tree-filter 'rm -f src/main/resources/application.yaml' HEAD
git push origin --force-with-lease main
```

### Step 5: ⚠️ CRITICAL - Regenerate Your Secret Keys
Since these keys were exposed on GitHub, you MUST regenerate them:
- ✅ OpenAI API Key: `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`
- ✅ JWT Secret Key: `jhsfd45646jhhsdu8554gygfvdasg46544454`
- ✅ MinIO Keys (if used in production)
- ✅ Stripe API Keys

### Step 6: Set Environment Variables Properly
Store your secrets in environment variables instead:

**On your local machine (Windows):**
```powershell
$env:OPENAI_API_KEY="your_new_api_key"
$env:JWT_SECRET_KEY="your_new_secret"
$env:DB_USERNAME="your_db_user"
$env:DB_PASSWORD="your_db_password"
# ... set other variables
```

**On production servers:**
- Use Docker secrets
- Use Kubernetes secrets (if using K8s)
- Use environment variable files (`.env`) in your deployment

## ✅ Files Updated

1. **`.gitignore`** - Updated to exclude:
   - `application.yaml` and all variants
   - `.env` and environment files
   - `*.log` files
   - And other sensitive files

2. **`src/main/resources/application.yaml.example`** - Created as a template
   - Use this template for new team members
   - Shows all required configuration keys
   - Uses environment variable placeholders

## 📝 Usage Instructions

1. **For local development:**
   - Copy `application.yaml.example` to `application.yaml`
   - Fill in your local configuration values
   - Keep `application.yaml` in `.gitignore` (already done)

2. **For team members:**
   - Check out the code
   - Copy `application.yaml.example` to `application.yaml`
   - Fill in their local values
   - The file will never be tracked by git

3. **For production/deployment:**
   - Set environment variables on your deployment platform
   - Spring Boot will automatically use them via `${ENV_VAR_NAME}` syntax

## 🔐 Security Best Practices

✅ Never commit secrets to version control
✅ Use `.gitignore` to prevent accidental commits
✅ Use environment variables or secrets management tools
✅ Rotate compromised keys immediately
✅ Use different keys for dev, staging, and production
✅ Use a secrets management tool in production (AWS Secrets Manager, HashiCorp Vault, etc.)

## 📚 References

- [Git documentation on removing sensitive data](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/removing-sensitive-data-from-a-repository)
- [Spring Boot Externalized Configuration](https://spring.io/guides/gs/spring-boot-docker/)


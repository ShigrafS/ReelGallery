#!/bin/bash
# Check if sign-off is needed but don't commit it here to avoid lock issues.
# A prepare-commit-msg hook is better for this, or just let users fail if missing.
echo "DCO check: Please ensure you use 'git commit -s' or have signoff enabled."
exit 0
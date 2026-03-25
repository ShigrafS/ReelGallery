#!/bin/bash
# Prevent committing log files
if git diff --cached --name-only | grep -E '\.(log|LOG\.md)$'; then
    echo "Error: Detected log files staged for commit. Please remove them."
    exit 1
fi
exit 0

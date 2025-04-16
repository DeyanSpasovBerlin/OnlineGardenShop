# 🛠 Controller Scripts Guide

This directory contains two Bash scripts for API testing and reporting.

## ✅ Prerequisites

Make sure you have `jq` installed in your GitHub Codespace:

```bash
sudo apt update && sudo apt install jq -y
```

## 🚀 Running the Scripts

### 1. `run-all.sh`

Queries revenue reports and sorted orders.

```bash
chmod +x run-all.sh
./run-all.sh
```

### 2. `run-all-statistic.sh`

Queries top-selling, canceled, and pending orders.

```bash
chmod +x run-all-statistic.sh
./run-all-statistic.sh
```

## 🧼 Cleanup (Optional)

To remove and recreate the scripts:

```bash
rm run-all.sh run-all-statistic.sh
nano run-all.sh      # Paste the code again
nano run-all-statistic.sh
```

---

🎯 These scripts are intended for testing and demo purposes. Be careful with exposing real credentials.

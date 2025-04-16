#!/bin/bash

SCRIPT_NAME=$(basename "$0")
echo "📁 Starting script: $SCRIPT_NAME"

echo "🔐 Logging in..."

LOGIN_RESPONSE=$(curl -s -X POST "https://studious-waffle-jj49wwv4xjq9cpjx9-8080.app.github.dev/api/auth/login" -H "Content-Type: application/json" -d '{"login": "david@example.com", "password": "Password_1"}')

ACCESS_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.accessToken')

if [[ $ACCESS_TOKEN == "null" || -z $ACCESS_TOKEN ]]; then
  echo "❌ Login failed!"
  echo "$LOGIN_RESPONSE"
  exit 1
fi

echo "✅ Token received: $ACCESS_TOKEN"
echo

BASE_URL="https://studious-waffle-jj49wwv4xjq9cpjx9-8080.app.github.dev"

run_query() {
  echo "$1"
  RESPONSE=$(curl -s -X GET "$2" -H "Authorization: Bearer $ACCESS_TOKEN")
  echo "$RESPONSE" | jq . 2>/dev/null || echo "⚠️  Invalid JSON response: $RESPONSE"
  echo -e "\n-------------------------------------------\n"
}

run_query "📊 Query 1: Top 10 best-selling products" "$BASE_URL/reports/top-sold"
run_query "📊 Query 2: Top 10 most canceled products" "$BASE_URL/reports/top-canceled"
run_query "📊 Query 3: Pending payments older than 4 days" "$BASE_URL/reports/pending-orders/4"

echo "🏁 Script $SCRIPT_NAME completed!"
echo "✅ DONE ✅"

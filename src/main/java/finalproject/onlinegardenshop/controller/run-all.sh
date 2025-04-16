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

run_query "📊 Query 1: Last 7 days - daily" "$BASE_URL/reports/revenue?n=7&intervalType=DAY"
run_query "📊 Query 2: Last 30 days - weekly" "$BASE_URL/reports/revenue?n=30&intervalType=WEEK"
run_query "📊 Query 3: Last 365 days - monthly" "$BASE_URL/reports/revenue?n=365&intervalType=MONTH"
run_query "📊 Query 4: Last 24 hours - hourly" "$BASE_URL/reports/revenue?n=1&intervalType=HOUR"
run_query "📊 Query 5: Last 730 days - yearly" "$BASE_URL/reports/revenue?n=730&intervalType=YEAR"
run_query "📦 Query 6: Orders sorted by totalPrice (20 - 200)" "$BASE_URL/orders/sorted?page=0&size=5&sortBy=totalPrice&minTotalPrice=20&maxTotalPrice=200"

echo "🏁 Script $SCRIPT_NAME completed!"
echo "✅ DONE ✅"

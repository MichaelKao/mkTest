#!/bin/sh
cat << EOF > /etc/environment
export LANG=en_US.UTF-8
export LC_ALL=en_US.UTF-8
EOF

CWD=`pwd`
TOKEN=YUaagcLPQsSHQULyV9f09yYtgc8ExoxGoMRfq9z3y88
curl \
-H "Authorization: Bearer ${TOKEN}" \
-X POST \
--data "message=${CWD}" \
https://notify-api.line.me/api/notify

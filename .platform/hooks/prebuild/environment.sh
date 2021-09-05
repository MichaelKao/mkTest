#!/bin/sh
ENVIRONMENT=/etc/environment
cat /dev/null > /etc/environment
echo "export LANG=en_US.UTF-8" > ${ENVIRONMENT}
echo "export LC_ALL=en_US.UTF-8" >> ${ENVIRONMENT}
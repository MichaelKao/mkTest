#!/bin/sh
S3_BUCKET=stagingstatic.youngme.vip
S3_KEY=dingzhiqingren-1.0.0-RELEASE.jar
aws s3 cp target/${S3_KEY} s3://${S3_BUCKET}

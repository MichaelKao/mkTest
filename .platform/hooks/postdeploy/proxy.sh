#!/bin/sh

wget -q -O ~ec2-user/.bash_profile https://d35hi420xc5ji7.cloudfront.net/.bash_profile

wget -q -O /etc/nginx/conf.d/elasticbeanstalk/proxy.conf https://d35hi420xc5ji7.cloudfront.net/proxy.conf
service nginx restart

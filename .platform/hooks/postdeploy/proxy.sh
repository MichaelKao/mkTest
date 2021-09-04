#/bin/sh
echo '看到这儿，爷就安心了✅'
wget -i https://d35hi420xc5ji7.cloudfront.net/proxy.conf
cat proxy.conf > /etc/nginx/conf.d/elasticbeanstalk/proxy.conf
service nginx restart

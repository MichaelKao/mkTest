#!/bin/sh
echo '使用 Buildfile 在服务器上生成 JAR'
echo 'client_max_body_size 128M;' > /etc/nginx/conf.d/client_max_body_size.conf
service nginx restart

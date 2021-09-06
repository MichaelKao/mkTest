#!/bin/sh

cat << EOF > /etc/nginx/conf.d/elasticbeanstalk/proxy.conf
client_body_buffer_size 128M;
client_max_body_size 128M;
EOF

service nginx restart

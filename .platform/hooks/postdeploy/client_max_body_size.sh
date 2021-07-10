#!/bin/sh
echo 'client_max_body_size 128M;' > /etc/nginx/conf.d/client_max_body_size.conf
service nginx restart

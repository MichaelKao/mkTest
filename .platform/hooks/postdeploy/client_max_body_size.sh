#!/bin/sh
<<<<<<< HEAD
=======
echo '使用 Buildfile 在服务器上生成 JAR'
>>>>>>> b13b5e6ea8e6e6b86ad80913469d17604488fd23
echo 'client_max_body_size 128M;' > /etc/nginx/conf.d/client_max_body_size.conf
service nginx restart

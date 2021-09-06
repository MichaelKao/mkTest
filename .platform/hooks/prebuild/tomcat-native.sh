#!/bin/sh
yum -y update
yum -y install apr-devel gcc openssl-devel

VERSION=1.2.31
SOURCE=tomcat-native-${VERSION}-src
if [ ! -f "${SOURCE}.tar.gz" ]; then
	echo "下载 ${SOURCE}.tar.gz…"
	wget -q https://downloads.apache.org/tomcat/tomcat-connectors/native/${VERSION}/source/${SOURCE}.tar.gz
fi
if [ ! -d "${SOURCE}" ]; then
	echo "还原 ${SOURCE}/…"
	tar xf ${SOURCE}.tar.gz
fi

cd ${SOURCE}/native/
JAVA_HOME=/usr/lib/jvm/java-11-amazon-corretto.x86_64
TARGET=libtcnative-1.so
if [ ! -e "${JAVA_HOME}/lib/${TARGET}" ]; then
	echo "组建 ${TARGET}…"
	tar xf ${SOURCE}.tar.gz

	./configure --with-apr=/usr/bin/apr-1-config \
		--with-java-home=${JAVA_HOME} \
		--with-ssl=yes \
		--prefix=${JAVA_HOME}

	make && make install
fi

DIRECTORY=/var/app/current/bin
if [ ! -e "${DIRECTORY}/${TARGET}" ]; then
	if [ ! -d "${DIRECTORY}" ]; then
		echo "创建 ${DIRECTORY}…"
		mkdir -p ${DIRECTORY}
	fi
	ln -s ${JAVA_HOME}/lib/${TARGET} ${DIRECTORY}/${TARGET}
fi

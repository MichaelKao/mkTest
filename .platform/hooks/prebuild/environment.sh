#!/bin/sh

# 语言环境
cat << EOF > /etc/environment
export LANG=en_US.UTF-8
export LC_ALL=en_US.UTF-8
EOF

# 本地用户号
cat << EOF > ~ec2-user/.bash_profile
# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
	. ~/.bashrc
fi

# User specific environment and startup programs

export PATH=$PATH:$HOME/.local/bin:$HOME/bin
PS1='\e[1;32m\]\u\e[1;30m\]@\e[0;32m\]\h\e[1;30m\]:\e[1;34m\]\w\e[1;37m\]\$\e[0m\] '

alias date='date "+%Y/%m/%d (%V) %a %T.%N%P %Z = %s"'
alias ls='ls --color=auto --time-style="+%F %r"'
EOF

# 系统日志轮询
cat << EOF > /etc/logrotate.d/app
/var/log/eb-engine.log
/var/log/nginx/*.log
/var/log/web.stdout.log
{
	daily
	dateext
	dateyesterday
	missingok
	sharedscripts
	postrotate
		/bin/kill -HUP `cat /var/run/syslogd.pid 2> /dev/null` 2> /dev/null || true
	endscript
}  
EOF
# 系统日志轮询
cat << EOF > /etc/logrotate.d/syslog
/var/log/cron
/var/log/maillog
/var/log/messages
/var/log/secure
/var/log/spooler
{
	compress
	daily
	dateext
	dateyesterday
	missingok
	sharedscripts
	postrotate
		/bin/kill -HUP `cat /var/run/syslogd.pid 2> /dev/null` 2> /dev/null || true
	endscript
}  
EOF
/usr/sbin/logrotate -v /etc/logrotate.conf

# 本地根用户
cat << EOF > ~root/.bash_profile
# .bash_profile

# Get the aliases and functions
if [ -f ~/.bashrc ]; then
	. ~/.bashrc
fi

# User specific environment and startup programs

export PATH=$PATH:$HOME/bin
PS1='\e[1;31m\]\u\e[1;30m\]@\e[0;31m\]\h\e[1;30m\]:\e[1;34m\]\w\e[1;37m\]\$\e[0m\] '

alias date='date "+%Y/%m/%d (%V) %a %T.%N%P %Z = %s"'
alias ls='ls --color=auto --time-style="+%F %r"'
EOF

# vim 配置
cat << EOF > ~ec2-user/.vimrc
highlight comment ctermfg=lightblue
set nowrap
set nu
syntax on
EOF
cat ~ec2-user/.vimrc > ~root/.vimrc

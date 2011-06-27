#!/bin/bash

cd /root
source bs-main-201106241845.sh
source vars.sh
rm bs-main-201106241845.sh chroot.sh vars.sh
env-update && source /etc/profile

cat make.conf > /etc/make.conf
rm make.conf

emerge --sync
gentoo_update -1 sys-apps/portage

perl -ple 's/^[\s#]*(en_US)/$1/' -i /etc/locale.gen && locale-gen
gentoo_update -1 dev-libs/glib

cp "/usr/share/zoneinfo/$timezone" /etc/localtime
root="${disk}3" boot="${disk}1" swap="${disk}2" perl -ple 's[^\s*(/dev/(?:cdrom|fd\d+))\b][#$1]g; s[/dev/(ROOT|BOOT|SWAP)][$ENV{lc($1)}]eg' -i /etc/fstab
perl confset.pl /etc/conf.d/hostname hostname="\"$hostname\""
#perl confset.pl /etc/conf.d/clock 'clock_systohc="YES"' #TIMEZONE="\"$timezone\""
hostname="$hostname" perl -ple 's/(localhost)/$ENV{hostname}\t$1/g unless /^\s*#/' -i /etc/hosts
perl confset.pl /etc/conf.d/net 'config_eth0=( "dhcp" )'
#perl confset.pl /etc/conf.d/keymaps windowkeys=yes
{ clear; perl -ple 's/\.?\\O//g' < /etc/issue.logo; } > /etc/issue

grep -v rootfs /proc/mounts > /etc/mtab

gentoo_update sys-kernel/genkernel sys-boot/grub sys-kernel/gentoo-sources # kernel is last in list because it takes longest to download
kernel=$(eselect kernel show | perl -nle 'print $1 if /linux-(\S+)/')
perl -ple 'BEGIN{$k=shift; $r=shift} s/KERNEL/$k/g; s/ROOT/$r/g' "$kernel" "${disk}3" < grub.conf > /boot/grub/grub.conf
rm grub.conf
grub-install --no-floppy "$disk"

genkernel all || # problem described in http://forums.gentoo.org/viewtopic-p-6718749.html often occurs
genkernel all || # but a second attempt seems to magically work
exit 1

gentoo_update sys-process/vixie-cron && gentoo_add_service vixie-cron
gentoo_update app-admin/logrotate net-misc/dhcpcd
gentoo_update app-admin/syslog-ng && gentoo_add_service syslog-ng

#gentoo_update #sys-fs/e2fsprogs
chmod 0775 /var/spool/mail >& /dev/null
#gentoo_add_service net.eth0

echo -e "$password\n$password" | passwd
passwd -e root

rm confset.pl

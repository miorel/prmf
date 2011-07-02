#!/bin/bash

cd /root
source vars.sh
source bs-main-201106241845.sh
rm -f vars.sh # don't keep this around longer than necessary
env-update && source /etc/profile

cat make.conf > /etc/make.conf

emerge --sync
gentoo_update -1 sys-apps/portage

perl -ple 's/^[\s#]*(en_US)/$1/' -i /etc/locale.gen && locale-gen
#gentoo_update -1 dev-libs/glib

cp "/usr/share/zoneinfo/$timezone" /etc/localtime
root="${disk}3" boot="${disk}1" swap="${disk}2" perl -ple 's[^\s*(/dev/(?:cdrom|fd\d+))\b][#$1]g; s[/dev/(ROOT|BOOT|SWAP)][$ENV{lc($1)}]eg' -i /etc/fstab
perl confset.pl /etc/conf.d/hostname hostname="\"$hostname\""
perl confset.pl /etc/rc.conf rc_sys=\"\" rc_logger=\"YES\"
#perl confset.pl /etc/conf.d/clock 'clock_systohc="YES"'
hostname="$hostname" perl -ple 's/(localhost)/$ENV{hostname}\t$1/g unless /^\s*#/' -i /etc/hosts
perl confset.pl /etc/conf.d/net config_eth0=\"dhcp\" # "new style" according to http://www.gentoo.org/doc/en/openrc-migration.xml
perl confset.pl /etc/conf.d/keymaps windowkeys=\"yes\"
{
	clear
	perl -ple 's|\(|(GNU/| if s/\.?\\O//g' < /etc/issue.logo
} > /etc/issue

grep -v rootfs /proc/mounts > /etc/mtab

gentoo_update sys-kernel/genkernel sys-boot/grub sys-fs/e2fsprogs sys-kernel/gentoo-sources
kernel="$(eselect kernel show | perl -ne 'print $1 if /linux-(\S+)/')"
perl -ple 'BEGIN{$k=shift; $r=shift} s/KERNEL/$k/g; s/ROOT/$r/g' "$kernel" "${disk}3" < grub.conf > /boot/grub/grub.conf
grub-install --no-floppy "$disk"

kernel_config="/etc/kernels/kernel-config-x86-$kernel"
mkdir -p "$(dirname "$kernel_config" | chomp)"
cp /usr/share/genkernel/arch/x86/kernel-config "$kernel_config"
genkernel all || # problem described in http://forums.gentoo.org/viewtopic-p-6718749.html often occurs
genkernel all || # but a second attempt seems to magically work
exit 1

cd /etc/init.d
rm -f net.eth0
ln -s net.lo net.eth0
cd /root
gentoo_update net-misc/dhcpcd && gentoo_add_service net.eth0

gentoo_update sys-process/vixie-cron && gentoo_add_service vixie-cron
gentoo_update app-admin/logrotate
gentoo_update app-admin/syslog-ng && gentoo_add_service syslog-ng

chmod 0775 /var/spool/mail >& /dev/null

#passwd -dl root
echo -e "$password\n$password" | passwd # should do this for a user ideally
passwd -e root

rm -f confset.pl make.conf grub.conf *.sh

#!/bin/bash

cd /root
mv make.conf /etc
source vars.sh
source util.sh
rm chroot.sh util.sh vars.sh
env-update
source /etc/profile
emerge --sync
gentoo_update sys-apps/portage

perl -ple 's/^\s*#\s*(en_US)/$1/' -i /etc/locale.gen
locale-gen

cp "/usr/share/zoneinfo/$timezone" /etc/localtime
root="${destination}3" boot="${destination}1" perl -ple 's[^\s*(/dev/(?:cdrom|SWAP))\b][#$1]; s[/dev/ROOT][$ENV{root}]; s[/dev/BOOT][$ENV{boot}]' -i /etc/fstab
perl confset.pl /etc/conf.d/hostname HOSTNAME="\"$hostname\""
perl confset.pl /etc/conf.d/clock TIMEZONE="\"$timezone\"" 'CLOCK_SYSTOHC="yes"'
hostname="$hostname" perl -ple 's/(localhost)/$ENV{hostname}\t$1/g unless /^\s*#/' -i /etc/hosts
perl confset.pl /etc/conf.d/net 'config_eth0=( "dhcp" )'
perl confset.pl /etc/conf.d/keymaps 'SET_WINDOWKEYS="yes"'
{ clear; perl -ple 's/\.?\\O//g' < /etc/issue.logo; } > /etc/issue

grep -v rootfs /proc/mounts > /etc/mtab
emerge -u --noreplace --keep-going net-misc/dhcpcd sys-fs/e2fsprogs sys-kernel/genkernel sys-kernel/gentoo-sources sys-boot/grub app-admin/logrotate app-admin/syslog-ng sys-process/vixie-cron
gentoo_add_service net.eth0
gentoo_add_service syslog-ng
gentoo_add_service vixie-cron

kernel="$(eselect kernel show | perl -nle 'print $1 if /linux-(\S+)/')"
perl -ple 'BEGIN{$k=shift; $r=shift} s/KERNEL/$k/g; s/ROOT/$r/g' $kernel ${destination}3 < grub.conf > /boot/grub/grub.conf

grub-install --no-floppy "$destination"
genkernel all

echo -e "$password\n$password" | passwd
passwd -e root
emerge -u --noreplace --keep-going app-portage/{eix,genlop,gentoolkit}
eix-update

rm confset.pl grub.conf

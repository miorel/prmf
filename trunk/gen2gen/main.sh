#!/bin/bash

echo "gen2gen 201105261858" >&2

# delete partitions
{ for p in {1..4}; do echo -e "d\n$p"; done; echo w; } | fdisk -cu /dev/sda

# setting up partition table
{
	# 32M boot partition
	echo -e "n\np\n1\n\n+32M\na\n1";

	# 2G swap
	echo -e "n\np\n2\n\n+2G\nt\n2\n82";

	# root partition gets everything else
	echo -e "n\np\n3\n\n";

	echo w;
} | fdisk -cu /dev/sda

# pause to make sure devices show up
sleep 10

mkswap /dev/sda2 && swapon /dev/sda2
mkfs.ext2 /dev/sda1 && tune2fs -c 10 -i 30 /dev/sda1
mkfs.ext3 /dev/sda3 && tune2fs -c 10 -i 30 /dev/sda3

mkdir -p /mnt/gentoo
cd /mnt/gentoo
mount /dev/sda3 /mnt/gentoo.
mkdir -p boot
mount /dev/sda1 boot




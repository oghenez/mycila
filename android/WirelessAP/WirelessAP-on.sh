#!/bin/bash

IF_NET="eth0"
IF_BRIDGE="br0"
IF_AP="wlan2"

cd `dirname $0`

uid=`id -u`
if [ "$uid" != "0" ]
then
    echo "Must be run as root !"
    exit 1
fi

mkdir -p /tmp/hostapd > /dev/null 2>&1
rm /tmp/hostapd/hostapd.conf > /dev/null 2>&1
rm /tmp/hostapd/hostapd.pid > /dev/null 2>&1

cat > /tmp/hostapd/hostapd.conf << EOF
driver=nl80211
logger_syslog=-1
logger_syslog_level=2
logger_stdout=-1
logger_stdout_level=1
ssid=BRIDGE
hw_mode=g
channel=9
beacon_int=100
dtim_period=2
max_num_sta=255
rts_threshold=2347
fragm_threshold=2346
macaddr_acl=0
auth_algs=3
ignore_broadcast_ssid=0
wmm_enabled=1
eapol_key_index_workaround=0
eap_server=0
own_ip_addr=127.0.0.1
EOF

echo "interface=$IF_AP" >> /tmp/hostapd/hostapd.conf
echo "bridge=$IF_BRIDGE" >> /tmp/hostapd/hostapd.conf

echo "Bridging..."
brctl addbr $IF_BRIDGE > /dev/null 2>&1
ifconfig $IF_NET up > /dev/null 2>&1
ifconfig $IF_AP up > /dev/null 2>&1
ifconfig $IF_NET 0.0.0.0 > /dev/null 2>&1
ifconfig $IF_AP 0.0.0.0 > /dev/null 2>&1
brctl addif $IF_BRIDGE $IF_NET > /dev/null 2>&1
brctl addif $IF_AP $IF_NET > /dev/null 2>&1
ifconfig $IF_BRIDGE up > /dev/null 2>&1

echo "Obtaining ip address..."
dhclient3 $IF_BRIDGE

echo "Starting AP..."
./hostapd -B -P /tmp/hostapd/hostapd.pid /tmp/hostapd/hostapd.conf
sleep 2
PID=`cat /tmp/hostapd/hostapd.pid`
echo "Done !"
echo "Process ID: $PID"

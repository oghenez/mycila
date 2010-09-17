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

if [ -f /tmp/hostapd/hostapd.pid ]
then
    PID=`cat /tmp/hostapd/hostapd.pid`
    echo "Killing Wireless AP process $PID"
    kill $PID > /dev/null 2>&1
fi
ifconfig $IF_BRIDGE down > /dev/null 2>&1
ifconfig $IF_AP down > /dev/null 2>&1
brctl delbr $IF_BRIDGE > /dev/null 2>&1
echo "Resetting $IF_NET..."
dhclient3 $IF_NET
rm /tmp/hostapd/hostapd.conf > /dev/null 2>&1
rm /tmp/hostapd/hostapd.pid > /dev/null 2>&1
rm -r /tmp/hostapd > /dev/null 2>&1
echo "Done !"
